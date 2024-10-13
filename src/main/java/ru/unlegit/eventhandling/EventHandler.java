package ru.unlegit.eventhandling;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface EventHandler<E extends Event> extends EventExecutor {

    EventHandler<?> CANCELLER = event -> ((Cancellable) event).setCancelled(true);

    void handle(E event) throws EventException;

    @Override
    @SuppressWarnings("unchecked")
    default void execute(Listener listener, Event event) throws EventException {
        try {
            handle((E) event);
        } catch (ClassCastException ignored) {}
    }

    default EventHandler<E> withFilter(@NonNull EventFilter<E> filter) {
        return event -> {
            if (filter.test(event)) {
                handle(event);
            }
        };
    }

    @SafeVarargs
    static <E extends Event> EventHandler<E> compose(EventHandler<E>... handlers) {
        for (EventHandler<E> handler : handlers) {
            Objects.requireNonNull(handler, "can't compose null handler");
        }

        return event -> {
            for (EventHandler<E> handler : handlers) {
                handler.handle(event);
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <E extends Event & Cancellable> EventHandler<E> canceller() {
        return (EventHandler<E>) CANCELLER;
    }

    static <E extends Event> EventHandler<E> caller(@NonNull Function<E, Event> eventFunction) {
        return event -> Bukkit.getPluginManager().callEvent(eventFunction.apply(event));
    }
}