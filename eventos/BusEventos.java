package eventos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Canal central de eventos. Quien publica un evento no sabe quién lo escucha:
 * así la ventana y la lógica nunca se llaman directamente entre sí.
 */
public class BusEventos {

    private final Map<Class<?>, List<Consumer<Object>>> oyentes = new HashMap<>();

    /** Registra una función (callback) que se ejecutará cada vez que ocurra un evento de ese tipo. */
    public <E> void suscribir(Class<E> tipo, Consumer<E> oyente) {
        oyentes.computeIfAbsent(tipo, clave -> new ArrayList<>())
                .add(evento -> oyente.accept(tipo.cast(evento)));
    }

    /** Avisa a todos los oyentes registrados para el tipo de este evento. */
    public void publicar(Object evento) {
        for (Consumer<Object> oyente : oyentes.getOrDefault(evento.getClass(), List.of())) {
            oyente.accept(evento);
        }
    }
}
