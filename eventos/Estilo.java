package eventos;

import eventos.Gasto.Categoria;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.text.NumberFormat;
import java.util.Locale;

/** Colores, tipografía y componentes visuales personalizados de la aplicación. */
public final class Estilo {

    static final Color FONDO = new Color(0xF6F7F9);
    static final Color BLANCO = Color.WHITE;
    static final Color BORDE = new Color(0xE2E5EC);
    static final Color DIVISOR = new Color(0xF1F2F5);
    static final Color TEXTO = new Color(0x1F2330);
    static final Color TEXTO_MEDIO = new Color(0x5B6172);
    static final Color TEXTO_SUAVE = new Color(0x8A90A2);
    static final Color ACENTO = new Color(0x4F46E5);
    static final Color ACENTO_OSCURO = new Color(0x4338CA);
    static final Color ACENTO_SUAVE = new Color(0xEEF0FF);
    static final Color ACENTO_CLARO = new Color(0xC7C4FA);
    static final Color EXITO = new Color(0x047857);
    static final Color ERROR = new Color(0xDC2626);

    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));

    static {
        MONEDA.setMaximumFractionDigits(0);
    }

    private Estilo() {
    }

    static Font fuente(int estilo, float tamano) {
        return new Font("Segoe UI", estilo, 12).deriveFont(tamano);
    }

    static String dinero(double monto) {
        return MONEDA.format(monto).replace((char) 160, ' ');
    }

    static Color color(Categoria categoria) {
        return switch (categoria) {
            case ALIMENTACION -> new Color(0xC2410C);
            case TRANSPORTE -> new Color(0x1D4ED8);
            case SERVICIOS -> new Color(0x6D28D9);
            case ENTRETENIMIENTO -> new Color(0xBE185D);
        };
    }

    static Color fondo(Categoria categoria) {
        return switch (categoria) {
            case ALIMENTACION -> new Color(0xFFF1E6);
            case TRANSPORTE -> new Color(0xE8F1FF);
            case SERVICIOS -> new Color(0xF3EEFF);
            case ENTRETENIMIENTO -> new Color(0xFDECF3);
        };
    }

    private static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    /** Panel blanco con esquinas redondeadas. */
    static class Tarjeta extends JPanel {

        private final Color relleno;

        Tarjeta(LayoutManager layout, Color relleno) {
            super(layout);
            this.relleno = relleno;
            setOpaque(false);
            setBorder(new EmptyBorder(20, 22, 20, 22));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setColor(relleno);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            if (relleno == BLANCO) {
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
            g2.dispose();
        }
    }

    /** Botón plano: principal (índigo), píldora (redondo) o secundario (contorno). */
    static class Boton extends JButton {

        enum Tipo { PRINCIPAL, PILDORA, SECUNDARIO }

        private final Tipo tipo;
        private boolean encima;

        Boton(String texto, Tipo tipo) {
            super(texto);
            this.tipo = tipo;
            setFont(fuente(tipo == Tipo.PRINCIPAL ? Font.BOLD : Font.PLAIN, 13f));
            setForeground(tipo == Tipo.PRINCIPAL ? BLANCO : TEXTO_MEDIO);
            setBorder(tipo == Tipo.PILDORA ? new EmptyBorder(7, 14, 7, 14) : new EmptyBorder(10, 14, 10, 14));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evento) {
                    encima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent evento) {
                    encima = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            int ancho = getWidth() - 1;
            int alto = getHeight() - 1;
            int radio = tipo == Tipo.PILDORA ? alto : 10;
            if (tipo == Tipo.PRINCIPAL) {
                g2.setColor(encima ? ACENTO_OSCURO : ACENTO);
                g2.fillRoundRect(0, 0, ancho, alto, radio, radio);
            } else {
                g2.setColor(encima ? ACENTO_SUAVE : BLANCO);
                g2.fillRoundRect(0, 0, ancho, alto, radio, radio);
                g2.setColor(encima ? ACENTO_CLARO : BORDE);
                g2.drawRoundRect(0, 0, ancho, alto, radio, radio);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Campo de texto redondeado con texto de ayuda; el borde se ilumina al enfocarlo. */
    static class Campo extends JTextField {

        private final String ayuda;

        Campo(String ayuda) {
            this.ayuda = ayuda;
            setOpaque(false);
            setFont(fuente(Font.PLAIN, 14f));
            setForeground(TEXTO);
            setCaretColor(ACENTO);
            setBorder(new EmptyBorder(10, 12, 10, 12));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent evento) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent evento) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setColor(BLANCO);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.setStroke(new BasicStroke(hasFocus() ? 1.6f : 1f));
            g2.setColor(hasFocus() ? ACENTO : BORDE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            if (getText().isEmpty()) {
                FontMetrics metricas = g2.getFontMetrics(getFont());
                g2.setFont(getFont());
                g2.setColor(TEXTO_SUAVE);
                g2.drawString(ayuda, getInsets().left, (getHeight() - metricas.getHeight()) / 2 + metricas.getAscent());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Opción de categoría con forma de píldora y un punto de color. */
    static class Pildora extends JToggleButton {

        final Categoria categoria;

        Pildora(Categoria categoria) {
            super(categoria.toString());
            this.categoria = categoria;
            setFont(fuente(Font.PLAIN, 12.5f));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(130, 32));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            int alto = getHeight() - 1;
            g2.setColor(isSelected() ? ACENTO_SUAVE : BLANCO);
            g2.fillRoundRect(0, 0, getWidth() - 1, alto, alto, alto);
            g2.setColor(isSelected() ? ACENTO_CLARO : BORDE);
            g2.drawRoundRect(0, 0, getWidth() - 1, alto, alto, alto);

            FontMetrics metricas = g2.getFontMetrics(getFont());
            int x = (getWidth() - metricas.stringWidth(getText()) - 14) / 2;
            g2.setColor(color(categoria));
            g2.fillOval(x, getHeight() / 2 - 3, 7, 7);
            g2.setFont(getFont());
            g2.setColor(isSelected() ? ACENTO_OSCURO : TEXTO_MEDIO);
            g2.drawString(getText(), x + 14, (getHeight() - metricas.getHeight()) / 2 + metricas.getAscent());
            g2.dispose();
        }
    }

    /** Dibuja cada gasto de la lista: ícono de categoría, concepto, categoría y monto. */
    static class FilaGasto extends JComponent implements ListCellRenderer<Gasto> {

        private Gasto gasto;
        private boolean ultima;

        FilaGasto() {
            setPreferredSize(new Dimension(100, 62));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Gasto> lista, Gasto valor, int indice,
                                                      boolean seleccionado, boolean foco) {
            gasto = valor;
            ultima = indice == lista.getModel().getSize() - 1;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            int medio = getHeight() / 2;
            dibujarIcono(g2, gasto.categoria(), 0, medio - 19);

            g2.setFont(fuente(Font.PLAIN, 14f));
            g2.setColor(TEXTO);
            g2.drawString(gasto.concepto(), 52, medio - 2);
            g2.setFont(fuente(Font.PLAIN, 12f));
            g2.setColor(TEXTO_SUAVE);
            g2.drawString(gasto.categoria().toString(), 52, medio + 15);

            String monto = dinero(gasto.monto());
            g2.setFont(fuente(Font.BOLD, 14f));
            g2.setColor(TEXTO);
            g2.drawString(monto, getWidth() - g2.getFontMetrics().stringWidth(monto) - 6, medio + 5);

            if (!ultima) {
                g2.setColor(DIVISOR);
                g2.fillRect(52, getHeight() - 1, getWidth() - 52, 1);
            }
            g2.dispose();
        }

        private static void dibujarIcono(Graphics2D g, Categoria categoria, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(fondo(categoria));
            g2.fillRoundRect(x, y, 38, 38, 12, 12);
            g2.translate(x + 19, y + 19);
            g2.setColor(color(categoria));
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (categoria) {
                case ALIMENTACION -> {
                    g2.draw(new Arc2D.Double(-8, -6, 16, 14, 180, 180, Arc2D.CHORD));
                    g2.drawLine(-3, -7, -3, -4);
                    g2.drawLine(2, -8, 2, -4);
                }
                case TRANSPORTE -> {
                    g2.drawRoundRect(-7, -8, 14, 13, 4, 4);
                    g2.drawLine(-7, -2, 7, -2);
                    g2.fillOval(-6, 6, 3, 3);
                    g2.fillOval(3, 6, 3, 3);
                }
                case SERVICIOS -> g2.draw(new Polygon(new int[]{2, -6, 0, -2, 6, 0}, new int[]{-9, 1, 1, 9, -1, -1}, 6));
                case ENTRETENIMIENTO -> {
                    g2.drawOval(-9, -9, 18, 18);
                    g2.fill(new Polygon(new int[]{-2, -2, 4}, new int[]{-4, 4, 0}, 3));
                }
            }
            g2.dispose();
        }
    }
}
