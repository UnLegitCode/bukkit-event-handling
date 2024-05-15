package ru.unlegit.eventhandling;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class EventService implements EventListener {

    Plugin plugin;
    Map<World, WorldEventService> worldEventServiceMap = new HashMap<>();

    public EventService(@NonNull Plugin plugin) {
        this.plugin = plugin;

        newHandler(WorldUnloadEvent.class)
                .priority(EventPriority.MONITOR)
                .handler(event -> Optional.ofNullable(worldEventServiceMap.remove(event.getWorld()))
                        .ifPresent(EventListener::unregisterAll)
                ).register();
    }

    <E extends Event> void registerHandler(Listener listener, EventHandlerDescription<E> handlerDescription) {
        plugin.getServer().getPluginManager().registerEvent(
                handlerDescription.getEventType(), listener, handlerDescription.getPriority(),
                handlerDescription.getHandler().withFilter(handlerDescription.getFilter()),
                plugin, handlerDescription.isIgnoreCancelled()
        );
    }

    public <E extends Event> void registerHandler(@NonNull EventHandlerDescription<E> handlerDescription) {
        registerHandler(this, handlerDescription);
    }

    public <E extends Event> EventHandlerDescription.Builder<E> newHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerHandler, eventType);
    }

    public <E extends Event & Cancellable> void cancelIf(@NonNull Class<E> eventType, @NonNull EventFilter<E> filter) {
        newHandler(eventType)
                .priority(EventPriority.LOWEST)
                .filter(filter)
                .handler(EventHandler.canceller())
                .register();
    }

    public <E extends Event & Cancellable> void cancel(@NonNull Class<E> eventType) {
        cancelIf(eventType, EventFilter.none());
    }

    public WorldEventService getWorldService(@NonNull World world) {
        return worldEventServiceMap.computeIfAbsent(world, __ -> new WorldEventService(this, world));
    }
}