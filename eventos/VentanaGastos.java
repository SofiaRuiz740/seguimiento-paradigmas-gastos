package eventos;

import eventos.Estilo.Boton;
import eventos.Estilo.Campo;
import eventos.Estilo.FilaGasto;
import eventos.Estilo.Pildora;
import eventos.Estilo.Tarjeta;
import eventos.Eventos.BuscarMayor;
import eventos.Eventos.BuscarPorConcepto;
import eventos.Eventos.CalcularTotal;
import eventos.Eventos.ErrorOcurrido;
import eventos.Eventos.FiltrarPorMonto;
import eventos.Eventos.GastoRegistrado;
import eventos.Eventos.ListaActualizada;
import eventos.Eventos.MayorEncontrado;
import eventos.Eventos.RegistrarGasto;
import eventos.Eventos.TotalCalculado;
import eventos.Eventos.VerTodos;
import eventos.Gasto.Categoria;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Ventana principal. Tiene dos responsabilidades, ambas basadas en eventos:
 * 1. Traducir cada acción del usuario (clic, Enter) en una solicitud publicada en el bus.
 * 2. Escuchar los resultados que llegan por el bus y actualizar la pantalla.
 * La ventana nunca hace cálculos ni llama directamente a la lógica.
 */
public class VentanaGastos extends JFrame {

    private final BusEventos bus;

    private final Campo campoConcepto = new Campo("Ej. Almuerzo");
    private final Campo campoMonto = new Campo("Ej. 25.000");
    private final List<Pildora> categorias = new ArrayList<>();
    private final Boton botonRegistrar = new Boton("Registrar gasto", Boton.Tipo.PRINCIPAL);
    private final JLabel mensajeFormulario = etiqueta(" ", 12f, Estilo.ERROR);

    private final Boton botonVerTodos = new Boton("Ver todos", Boton.Tipo.PILDORA);
    private final Boton botonTotal = new Boton("Calcular total", Boton.Tipo.PILDORA);
    private final Boton botonMayor = new Boton("Gasto mayor", Boton.Tipo.PILDORA);
    private final Campo campoMontoMinimo = new Campo("Monto mínimo");
    private final Boton botonFiltrar = new Boton("Filtrar por monto mínimo", Boton.Tipo.SECUNDARIO);
    private final Campo campoBusqueda = new Campo("Concepto a buscar");
    private final Boton botonBuscar = new Boton("Buscar por concepto", Boton.Tipo.SECUNDARIO);

    private final DefaultListModel<Gasto> modeloLista = new DefaultListModel<>();
    private final JLabel tituloLista = etiqueta("Todos los gastos", 15f, Estilo.TEXTO);
    private final JLabel cantidadLista = etiqueta("0 gastos", 12f, Estilo.TEXTO_SUAVE);
    private final JLabel mensajeEstado = etiqueta("Registra tu primer gasto para comenzar.", 13f, Estilo.TEXTO_SUAVE);

    private final JLabel valorTotal = etiqueta("—", 22f, Estilo.TEXTO);
    private final JLabel detalleTotal = etiqueta("Pulsa Calcular total", 12f, Estilo.TEXTO_SUAVE);
    private final JLabel valorMayor = etiqueta("—", 22f, Estilo.TEXTO);
    private final JLabel detalleMayor = etiqueta("Pulsa Gasto mayor", 12f, Estilo.TEXTO_SUAVE);
    private final JLabel valorRegistros = etiqueta("0", 22f, Color.WHITE);

    public VentanaGastos(BusEventos bus) {
        super("Mis gastos");
        this.bus = bus;
        construirInterfaz();
        conectarEventosDelUsuario();
        escucharResultados();
    }

    // =====================================================================
    // 1. Eventos del usuario: cada acción publica una solicitud en el bus
    // =====================================================================

    private void conectarEventosDelUsuario() {
        botonRegistrar.addActionListener(evento -> publicarRegistro());
        campoMonto.addActionListener(evento -> publicarRegistro());

        botonVerTodos.addActionListener(evento -> bus.publicar(new VerTodos()));
        botonTotal.addActionListener(evento -> bus.publicar(new CalcularTotal()));
        botonMayor.addActionListener(evento -> bus.publicar(new BuscarMayor()));

        botonFiltrar.addActionListener(evento -> bus.publicar(new FiltrarPorMonto(campoMontoMinimo.getText())));
        campoMontoMinimo.addActionListener(evento -> bus.publicar(new FiltrarPorMonto(campoMontoMinimo.getText())));

        botonBuscar.addActionListener(evento -> bus.publicar(new BuscarPorConcepto(campoBusqueda.getText())));
        campoBusqueda.addActionListener(evento -> bus.publicar(new BuscarPorConcepto(campoBusqueda.getText())));
    }

    private void publicarRegistro() {
        bus.publicar(new RegistrarGasto(campoConcepto.getText(), campoMonto.getText(), categoriaSeleccionada()));
    }

    // =====================================================================
    // 2. Resultados: cada evento recibido actualiza una parte de la pantalla
    // =====================================================================

    private void escucharResultados() {
        bus.suscribir(GastoRegistrado.class, this::alRegistrarGasto);
        bus.suscribir(ListaActualizada.class, this::alActualizarLista);
        bus.suscribir(TotalCalculado.class, this::alCalcularTotal);
        bus.suscribir(MayorEncontrado.class, this::alEncontrarMayor);
        bus.suscribir(ErrorOcurrido.class, this::alOcurrirError);
    }

    private void alRegistrarGasto(GastoRegistrado evento) {
        limpiarFormulario();
        mostrarLista("Todos los gastos", evento.todos());
        valorRegistros.setText(String.valueOf(evento.todos().size()));
        valorTotal.setText("—");
        valorMayor.setText("—");
        detalleTotal.setText("Pulsa Calcular total");
        detalleMayor.setText("Pulsa Gasto mayor");
        mostrarEstado("Se registró \"" + evento.gasto().concepto() + "\" por " + Estilo.dinero(evento.gasto().monto()), true);
    }

    private void alActualizarLista(ListaActualizada evento) {
        mostrarLista(evento.titulo(), evento.gastos());
        mostrarEstado(evento.titulo() + ": " + evento.gastos().size() + " resultado(s)", true);
    }

    private void alCalcularTotal(TotalCalculado evento) {
        valorTotal.setText(Estilo.dinero(evento.total()));
        detalleTotal.setText("Suma de " + evento.cantidad() + " gastos");
        mostrarEstado("Total acumulado: " + Estilo.dinero(evento.total()), true);
    }

    private void alEncontrarMayor(MayorEncontrado evento) {
        Gasto mayor = evento.gasto();
        valorMayor.setText(Estilo.dinero(mayor.monto()));
        detalleMayor.setText(mayor.concepto());
        mostrarLista("Gasto de mayor valor", List.of(mayor));
        mostrarEstado("El gasto más alto es \"" + mayor.concepto() + "\"", true);
    }

    private void alOcurrirError(ErrorOcurrido evento) {
        if (evento.enFormulario()) {
            mensajeFormulario.setText(evento.mensaje());
        } else {
            mostrarEstado(evento.mensaje(), false);
        }
    }

    private void mostrarLista(String titulo, List<Gasto> gastos) {
        tituloLista.setText(titulo);
        cantidadLista.setText(gastos.size() == 1 ? "1 gasto" : gastos.size() + " gastos");
        modeloLista.clear();
        modeloLista.addAll(gastos);
    }

    private void mostrarEstado(String texto, boolean exito) {
        mensajeEstado.setText((exito ? "✓  " : "✕  ") + texto);
        mensajeEstado.setForeground(exito ? Estilo.EXITO : Estilo.ERROR);
    }

    private void limpiarFormulario() {
        campoConcepto.setText("");
        campoMonto.setText("");
        mensajeFormulario.setText(" ");
        categorias.get(0).setSelected(true);
        campoConcepto.requestFocusInWindow();
    }

    private Categoria categoriaSeleccionada() {
        for (Pildora pildora : categorias) {
            if (pildora.isSelected()) {
                return pildora.categoria;
            }
        }
        return Categoria.ALIMENTACION;
    }

    // =====================================================================
    // Construcción visual
    // =====================================================================

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1160, 740));

        JPanel cuerpo = new JPanel(new BorderLayout(18, 0));
        cuerpo.setOpaque(false);
        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearPanelGastos(), BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout(0, 22));
        raiz.setBackground(Estilo.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);

        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel titulo = new JPanel();
        titulo.setOpaque(false);
        titulo.setLayout(new BoxLayout(titulo, BoxLayout.Y_AXIS));
        titulo.add(etiqueta("Mis gastos", 28f, Estilo.TEXTO, Font.BOLD));
        titulo.add(etiqueta(mesActual(), 14f, Estilo.TEXTO_SUAVE));

        JPanel indicadores = new JPanel(new GridLayout(1, 3, 12, 0));
        indicadores.setOpaque(false);
        indicadores.add(indicador("Total acumulado", valorTotal, detalleTotal, Estilo.BLANCO));
        indicadores.add(indicador("Gasto mayor", valorMayor, detalleMayor, Estilo.BLANCO));
        indicadores.add(indicador("Registros", valorRegistros, etiqueta("gastos", 12f, Estilo.ACENTO_CLARO), Estilo.ACENTO));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(indicadores, BorderLayout.EAST);
        return encabezado;
    }

    private JPanel indicador(String titulo, JLabel valor, JLabel detalle, Color relleno) {
        Tarjeta tarjeta = new Tarjeta(null, relleno);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        tarjeta.setPreferredSize(new Dimension(190, 84));
        Color colorTitulo = relleno == Estilo.ACENTO ? Estilo.ACENTO_CLARO : Estilo.TEXTO_SUAVE;
        valor.setFont(Estilo.fuente(Font.BOLD, 22f));
        tarjeta.add(etiqueta(titulo, 12f, colorTitulo));
        tarjeta.add(valor);
        tarjeta.add(detalle);
        return tarjeta;
    }

    private JPanel crearFormulario() {
        JPanel opciones = new JPanel(new GridLayout(2, 2, 6, 6));
        opciones.setOpaque(false);
        ButtonGroup grupo = new ButtonGroup();
        for (Categoria categoria : Categoria.values()) {
            Pildora pildora = new Pildora(categoria);
            pildora.addItemListener(evento -> opciones.repaint());
            grupo.add(pildora);
            categorias.add(pildora);
            opciones.add(pildora);
        }
        categorias.get(0).setSelected(true);

        Tarjeta formulario = new Tarjeta(new GridBagLayout(), Estilo.BLANCO);
        formulario.setPreferredSize(new Dimension(320, 0));
        apilar(formulario,
                etiqueta("Nuevo gasto", 17f, Estilo.TEXTO, Font.BOLD), 2,
                etiqueta("Registra lo que gastaste hoy.", 12f, Estilo.TEXTO_SUAVE), 18,
                etiqueta("Concepto", 12f, Estilo.TEXTO_SUAVE), 4,
                campoConcepto, 12,
                etiqueta("Monto", 12f, Estilo.TEXTO_SUAVE), 4,
                campoMonto, 14,
                etiqueta("Categoría", 12f, Estilo.TEXTO_SUAVE), 8,
                opciones, 20,
                botonRegistrar, 8,
                mensajeFormulario, 0);
        return formulario;
    }

    private JPanel crearPanelGastos() {
        JPanel acciones = new JPanel();
        acciones.setOpaque(false);
        acciones.setLayout(new BoxLayout(acciones, BoxLayout.X_AXIS));
        acciones.add(botonVerTodos);
        acciones.add(Box.createHorizontalStrut(8));
        acciones.add(botonTotal);
        acciones.add(Box.createHorizontalStrut(8));
        acciones.add(botonMayor);
        acciones.add(Box.createHorizontalGlue());

        JPanel consultas = new JPanel(new GridBagLayout());
        consultas.setOpaque(false);
        agregarEnFila(consultas, campoMontoMinimo, 0, 0.45, 8);
        agregarEnFila(consultas, botonFiltrar, 1, 0, 20);
        agregarEnFila(consultas, campoBusqueda, 2, 0.55, 8);
        agregarEnFila(consultas, botonBuscar, 3, 0, 0);

        JPanel encabezadoLista = new JPanel(new BorderLayout());
        encabezadoLista.setOpaque(false);
        encabezadoLista.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.DIVISOR),
                BorderFactory.createEmptyBorder(6, 0, 10, 0)));
        tituloLista.setFont(Estilo.fuente(Font.BOLD, 15f));
        encabezadoLista.add(tituloLista, BorderLayout.WEST);
        encabezadoLista.add(cantidadLista, BorderLayout.EAST);

        JPanel superior = new JPanel();
        superior.setOpaque(false);
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));
        superior.add(alinear(acciones));
        superior.add(Box.createVerticalStrut(12));
        superior.add(alinear(consultas));
        superior.add(Box.createVerticalStrut(12));
        superior.add(alinear(encabezadoLista));

        JList<Gasto> lista = new JList<>(modeloLista);
        lista.setOpaque(false);
        lista.setFocusable(false);
        lista.setCellRenderer(new FilaGasto());
        JScrollPane desplazamiento = new JScrollPane(lista);
        desplazamiento.setOpaque(false);
        desplazamiento.getViewport().setOpaque(false);
        desplazamiento.setBorder(BorderFactory.createEmptyBorder());

        Tarjeta panel = new Tarjeta(new BorderLayout(0, 6), Estilo.BLANCO);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(desplazamiento, BorderLayout.CENTER);
        panel.add(mensajeEstado, BorderLayout.SOUTH);
        return panel;
    }

    // ----- Utilidades de construcción -----

    private static void apilar(JPanel panel, Object... componentesYEspacios) {
        for (int i = 0; i < componentesYEspacios.length; i += 2) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = i / 2;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, (int) componentesYEspacios[i + 1], 0);
            panel.add((Component) componentesYEspacios[i], c);
        }
        GridBagConstraints relleno = new GridBagConstraints();
        relleno.gridy = componentesYEspacios.length;
        relleno.weighty = 1;
        panel.add(Box.createVerticalGlue(), relleno);
    }

    private static void agregarEnFila(JPanel panel, Component componente, int columna, double peso, int espacio) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = columna;
        c.weightx = peso;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, espacio);
        panel.add(componente, c);
    }

    private static JComponent alinear(JComponent componente) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        return componente;
    }

    private static JLabel etiqueta(String texto, float tamano, Color color) {
        return etiqueta(texto, tamano, color, Font.PLAIN);
    }

    private static JLabel etiqueta(String texto, float tamano, Color color, int estilo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Estilo.fuente(estilo, tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    private static String mesActual() {
        String texto = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.forLanguageTag("es-CO")));
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}
