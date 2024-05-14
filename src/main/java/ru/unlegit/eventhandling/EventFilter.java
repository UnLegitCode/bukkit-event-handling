package ru.unlegit.eventhandling;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventFilter<E extends Event> {

    EventFilter<?> NONE = event -> true;

    boolean test(E event);

    @SuppressWarnings("unchecked")
    static <E extends Event> EventFilter<E> none() {
        return (EventFilter<E>) NONE;
    }
}