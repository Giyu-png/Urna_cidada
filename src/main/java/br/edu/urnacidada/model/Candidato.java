package br.edu.urnacidada.model;

import java.util.Objects;

public abstract class Candidato {
    private final Cargo cargo;
    private final String numero;
    private final String nome;
    private final Partido partido;

    protected Candidato(Cargo cargo, String numero, String nome, Partido partido) {
        this.cargo = Objects.requireNonNull(cargo, "cargo");
        this.partido = Objects.requireNonNull(partido, "partido");
        if (numero == null || !numero.matches("\\d{" + cargo.getQuantidadeDigitos() + "}")) {
            throw new IllegalArgumentException("Número inválido para o cargo.");
        }
        if (!numero.startsWith(partido.getNumero())) {
            throw new IllegalArgumentException("O número deve começar com a legenda fictícia.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do candidato é obrigatório.");
        }
        this.numero = numero;
        this.nome = nome;
    }

    public Cargo getCargo() { return cargo; }
    public String getNumero() { return numero; }
    public String getNome() { return nome; }
    public Partido getPartido() { return partido; }

    public abstract String getDescricao();
}

