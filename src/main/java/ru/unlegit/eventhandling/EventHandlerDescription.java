package ru.unlegit.eventhandling;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.Objects;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class EventHandlerDescription<E extends Event> {

    Class<E> eventType;
    EventPriority priority;
    boolean ignoreCancelled;
    EventFilter<E> filter;
    EventHandler<E> handler;

    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static final class Builder<E extends Event> {

        final Consumer<EventHandlerDescription<E>> registerer;
        final Class<E> eventType;
        EventPriority priority = EventPriority.NORMAL;
        boolean ignoreCancelled = false;
        EventFilter<E> filter = EventFilter.none();
        EventHandler<E> handler;

        public Builder<E> priority(@NonNull EventPriority priority) {
            this.priority = priority;

            return this;
        }

        public Builder<E> ignoreCancelled() {
            ignoreCancelled = true;

            return this;
        }

        public Builder<E> filter(@NonNull EventFilter<E> filter) {
            this.filter = filter;

            return this;
        }

        public Builder<E> handler(@NonNull EventHandler<E> executor) {
            this.handler = executor;

            return this;
        }

        public EventHandlerDescription<E> build() {
            return new EventHandlerDescription<>(
                    eventType, priority, ignoreCancelled, filter,
                    Objects.requireNonNull(handler, "handler can't be null")
            );
        }

        public void register() {
            registerer.accept(build());
        }
    }
}