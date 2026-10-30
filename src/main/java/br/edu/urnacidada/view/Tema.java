package br.edu.urnacidada.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/** Identidade visual neutra para a experiência educativa. */
public final class Tema {
    public static final Color FUNDO = new Color(0xF5F4F0);
    public static final Color TEXTO = new Color(0x19212C);
    public static final Color CINZA = new Color(0x576575);
    public static final Color AZUL = new Color(0x235A7A);
    public static final Color BRANCO = Color.WHITE;

    private Tema() {}

    public static JLabel texto(String valor, int tamanho, boolean negrito) {
        JLabel label = new JLabel(valor);
        label.setForeground(TEXTO);
        label.setFont(new Font("SansSerif", negrito ? Font.BOLD : Font.PLAIN, tamanho));
        return label;
    }

    public static JButton botao(String texto, Color fundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setBackground(fundo);
        botao.setForeground(corTexto);
        botao.setOpaque(true);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setBorder(new EmptyBorder(13, 18, 13, 18));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }
}

