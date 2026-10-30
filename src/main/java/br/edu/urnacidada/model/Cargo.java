package br.edu.urnacidada.model;

public enum Cargo {
    DEPUTADO_FEDERAL("Deputado Federal", 4),
    PRESIDENTE("Presidente", 2);

    private final String nome;
    private final int quantidadeDigitos;

    Cargo(String nome, int quantidadeDigitos) {
        this.nome = nome;
        this.quantidadeDigitos = quantidadeDigitos;
    }

    public String getNome() { return nome; }
    public int getQuantidadeDigitos() { return quantidadeDigitos; }
}

