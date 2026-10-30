package br.edu.urnacidada.model;

public final class Partido {
    private final String numero;
    private final String nome;

    public Partido(String numero, String nome) {
        if (numero == null || !numero.matches("\\d{2}")) {
            throw new IllegalArgumentException("A legenda deve ter dois dígitos.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da legenda é obrigatório.");
        }
        this.numero = numero;
        this.nome = nome;
    }

    public String getNumero() { return numero; }
    public String getNome() { return nome; }
}

