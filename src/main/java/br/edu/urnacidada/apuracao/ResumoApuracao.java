package br.edu.urnacidada.apuracao;

import br.edu.urnacidada.model.Candidato;
import br.edu.urnacidada.model.Cargo;
import br.edu.urnacidada.service.CatalogoFicticio;
import br.edu.urnacidada.service.VotacaoService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Transforma contagens agregadas (sem dados de eleitores) em resultados por cargo.
 * Não chama a rede e pode ser testada sem credenciais.
 */
public final class ResumoApuracao {
    public record Linha(String opcao, String tipo, String numero, long votos, double percentual) {}
    public record PorCargo(Cargo cargo, List<Linha> linhas, long total, long brancos, long nulos) {
        public PorCargo {
            linhas = List.copyOf(linhas);
        }
    }

    private ResumoApuracao() {}

    /** Mantém a mesma lista de candidatos fictícios usada na urna. */
    private static final Map<Cargo, List<String>> CHAVES_CONHECIDAS = Map.of(
        Cargo.DEPUTADO_FEDERAL, List.of(
            "DEPUTADO_FEDERAL_NOMINAL_8101",
            "DEPUTADO_FEDERAL_NOMINAL_8202",
            "DEPUTADO_FEDERAL_LEGENDA_81",
            "DEPUTADO_FEDERAL_LEGENDA_82",
            "DEPUTADO_FEDERAL_BRANCO",
            "DEPUTADO_FEDERAL_NULO"),
        Cargo.PRESIDENTE, List.of(
            "PRESIDENTE_NOMINAL_81",
            "PRESIDENTE_NOMINAL_82",
            "PRESIDENTE_BRANCO",
            "PRESIDENTE_NULO")
    );

    public static Map<Cargo, PorCargo> analisar(Map<String, Long> quantidades) {
        Objects.requireNonNull(quantidades, "quantidades");
        VotacaoService catalogo = CatalogoFicticio.criarServico();
        Map<Cargo, PorCargo> resultado = new LinkedHashMap<>();

        for (Cargo cargo : Cargo.values()) {
            Set<String> chaves = new LinkedHashSet<>(CHAVES_CONHECIDAS.get(cargo));
            // Não perder votos de categorias ou candidatos adicionados posteriormente.
            quantidades.keySet().stream()
                .filter(chave -> chave.startsWith(cargo.name() + "_"))
                .sorted()
                .forEach(chaves::add);

            long total = 0;
            for (String chave : chaves) {
                Long quantidade = quantidades.getOrDefault(chave, 0L);
                if (quantidade == null || quantidade < 0) {
                    throw new IllegalArgumentException("Contagem inválida para " + chave);
                }
                total = Math.addExact(total, quantidade);
            }

            List<Linha> linhas = new ArrayList<>();
            for (String chave : chaves) {
                long quantidade = quantidades.getOrDefault(chave, 0L);
                String sufixo = chave.substring(cargo.name().length() + 1);
                String tipo;
                String numero = "—";
                String opcao;

                if (sufixo.startsWith("NOMINAL_")) {
                    tipo = "Nominal";
                    numero = sufixo.substring("NOMINAL_".length());
                    String referencia = numero;
                    opcao = catalogo.buscarCandidato(cargo, referencia)
                        .map(Candidato::getNome)
                        .orElse("Candidato " + referencia);
                } else if (sufixo.startsWith("LEGENDA_")) {
                    tipo = "Legenda";
                    numero = sufixo.substring("LEGENDA_".length());
                    String referencia = numero;
                    opcao = catalogo.buscarCandidato(Cargo.PRESIDENTE, referencia)
                        .map(c -> "Legenda — " + c.getPartido().getNome())
                        .orElse("Legenda " + referencia);
                } else if ("BRANCO".equals(sufixo)) {
                    tipo = "Branco";
                    opcao = "Voto em branco";
                } else if ("NULO".equals(sufixo)) {
                    tipo = "Nulo";
                    opcao = "Voto nulo";
                } else {
                    tipo = "Outro";
                    opcao = "Categoria adicional: " + sufixo;
                }
                double percentual = total == 0 ? 0.0 : (quantidade * 100.0) / total;
                linhas.add(new Linha(opcao, tipo, numero, quantidade, percentual));
            }

            resultado.put(cargo, new PorCargo(
                cargo, linhas, total,
                quantidades.getOrDefault(cargo.name() + "_BRANCO", 0L),
                quantidades.getOrDefault(cargo.name() + "_NULO", 0L)));
        }
        return Map.copyOf(resultado);
    }
}
