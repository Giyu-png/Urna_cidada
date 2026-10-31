package br.edu.urnacidada.view;

import br.edu.urnacidada.model.Candidato;
import br.edu.urnacidada.service.CatalogoFicticio;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Fotos dos candidatos carregadas do classpath, sem dependências de rede.
 * Se o PNG ainda não existir, cria um retrato neutro sem identidade.
 */
public final class FotoCandidato {
    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();
    private static final Color FUNDO = new Color(0xE6EAED);
    private static final Color SILHUETA = new Color(0x9BA7AE);

    private FotoCandidato() {}

    public static ImageIcon icone(Candidato candidato, int largura, int altura) {
        if (largura < 1 || altura < 1) {
            throw new IllegalArgumentException("Dimensões da foto inválidas.");
        }
        String recurso = candidato == null ? "" : CatalogoFicticio
                .buscarFicha(candidato.getCargo(), candidato.getNumero())
                .map(CatalogoFicticio.Ficha::fotoRecurso)
                .orElse("");
        String chave = recurso + ":" + largura + "x" + altura;
        return CACHE.computeIfAbsent(chave, id -> criarIcone(recurso, largura, altura));
    }

    private static ImageIcon criarIcone(String recurso, int largura, int altura) {
        BufferedImage original = null;
        if (!recurso.isEmpty()) {
            try (InputStream arquivo = FotoCandidato.class.getResourceAsStream(recurso)) {
                if (arquivo != null) original = ImageIO.read(arquivo);
            } catch (IOException erro) {
                System.err.println("Foto indisponível: " + recurso);
            }
        }
        BufferedImage destino = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = destino.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(FUNDO);
            g.fillRect(0, 0, largura, altura);
            if (original != null) {
                double escala = Math.max((double) largura / original.getWidth(),
                        (double) altura / original.getHeight());
                int w = (int) Math.ceil(original.getWidth() * escala);
                int h = (int) Math.ceil(original.getHeight() * escala);
                g.drawImage(original, (largura - w) / 2, (altura - h) / 2, w, h, null);
            } else {
                // Silhueta genérica; não representa um candidato real.
                g.setColor(SILHUETA);
                int diametro = Math.min(largura * 2 / 5, altura / 3);
                int x = (largura - diametro) / 2;
                int y = altura / 5;
                g.fill(new Ellipse2D.Double(x, y, diametro, diametro));
                int ombros = largura * 3 / 4;
                int topo = altura * 3 / 5;
                g.fillRoundRect((largura - ombros) / 2, topo,
                        ombros, altura - topo + 12, largura / 3, largura / 3);
            }
        } finally {
            g.dispose();
        }
        return new ImageIcon(destino);
    }
}
