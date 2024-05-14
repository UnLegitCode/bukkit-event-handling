package ru.unlegit.eventhandling;

import lombok.NonNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

@FunctionalInterface
public interface EventHandler<E extends Event> extends EventExecutor {

    EventHandler<?> CANCELLER = event -> ((Cancellable) event).setCancelled(true);

    void handle(E event) throws EventException;

    @Override
    @SuppressWarnings("unchecked")
    default void execute(Listener listener, Event event) throws EventException {
        handle((E) event);
    }

    default EventHandler<E> withFilter(@NonNull EventFilter<E> filter) {
        return event -> {
            if (filter.test(event)) {
                handle(event);
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <E extends Event & Cancellable> EventHandler<E> canceller() {
        return (EventHandler<E>) CANCELLER;
    }
}