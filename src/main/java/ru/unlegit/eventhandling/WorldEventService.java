package ru.unlegit.eventhandling;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;

import java.util.function.Function;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class WorldEventService implements EventListener {

    EventService parentContext;
    World world;

    public <E extends Event> void registerHandler(
            @NonNull EventHandlerDescription<E> handlerDescription, @NonNull Function<E, World> worldDetector
    ) {
        parentContext.registerHandler(this, new EventHandlerDescription<>(
                handlerDescription.eventType(),
                handlerDescription.priority(),
                handlerDescription.ignoreCancelled(),
                event -> (worldDetector.apply(event) == world) && handlerDescription.filter().test(event),
                handlerDescription.handler()
        ));
    }

    public <E extends Event>EventHandlerDescription.Builder<E> newHandler(
            @NonNull Class<E> eventType, @NonNull Function<E, World> worldDetector
    ) {
        return new EventHandlerDescription.Builder<>(
                handlerDescription -> registerHandler(handlerDescription, worldDetector),
                eventType
        );
    }

    public <E extends Event & Cancellable> void cancelIf(
            @NonNull Class<E> eventType, @NonNull Function<E, World> worldDetector, @NonNull EventFilter<E> filter
    ) {
        newHandler(eventType, worldDetector)
                .priority(EventPriority.LOWEST)
                .filter(filter)
                .handler(EventHandler.canceller())
                .register();
    }


    public <E extends Event & Cancellable> void cancel(
            @NonNull Class<E> eventType, @NonNull Function<E, World> worldDetector
    ) {
        cancelIf(eventType, worldDetector, EventFilter.none());
    }

    public <E extends WorldEvent> void registerWorldHandler(@NonNull EventHandlerDescription<E> handlerDescription) {
        registerHandler(handlerDescription, WorldEvent::getWorld);
    }

    public <E extends WorldEvent> EventHandlerDescription.Builder<E> newWorldHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerWorldHandler, eventType);
    }

    public <E extends WorldEvent & Cancellable> void cancelWorldEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, WorldEvent::getWorld, filter);
    }

    public <E extends WorldEvent & Cancellable> void cancelWorldEvent(@NonNull Class<E> eventType) {
        cancelWorldEventIf(eventType, EventFilter.none());
    }

    public <E extends EntityEvent> void registerEntityHandler(@NonNull EventHandlerDescription<E> handlerDescription) {
        registerHandler(handlerDescription, event -> event.getEntity().getWorld());
    }

    public <E extends EntityEvent> EventHandlerDescription.Builder<E> newEntityHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerEntityHandler, eventType);
    }

    public <E extends EntityEvent & Cancellable> void cancelEntityEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, event -> event.getEntity().getWorld(), filter);
    }

    public <E extends EntityEvent & Cancellable> void cancelEntityEvent(@NonNull Class<E> eventType) {
        cancelEntityEventIf(eventType, EventFilter.none());
    }

    public <E extends WeatherEvent> void registerWeatherHandler(
            @NonNull EventHandlerDescription<E> handlerDescription
    ) {
        registerHandler(handlerDescription, WeatherEvent::getWorld);
    }

    public <E extends WeatherEvent> EventHandlerDescription.Builder<E> newWeatherHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerWeatherHandler, eventType);
    }

    public <E extends WeatherEvent & Cancellable> void cancelWeatherEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, WeatherEvent::getWorld, filter);
    }

    public <E extends WeatherEvent & Cancellable> void cancelWeatherEvent(@NonNull Class<E> eventType) {
        cancelWeatherEventIf(eventType, EventFilter.none());
    }

    public <E extends PlayerEvent> void registerPlayerHandler(@NonNull EventHandlerDescription<E> handlerDescription) {
        registerHandler(handlerDescription, event -> event.getPlayer().getWorld());
    }

    public <E extends PlayerEvent> EventHandlerDescription.Builder<E> newPlayerHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerPlayerHandler, eventType);
    }

    public <E extends PlayerEvent & Cancellable> void cancelPlayerEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, event -> event.getPlayer().getWorld(), filter);
    }

    public <E extends PlayerEvent & Cancellable> void cancelPlayerEvent(@NonNull Class<E> eventType) {
        cancelPlayerEventIf(eventType, EventFilter.none());
    }

    public <E extends BlockEvent> void registerBlockHandler(@NonNull EventHandlerDescription<E> handlerDescription) {
        registerHandler(handlerDescription, event -> event.getBlock().getWorld());
    }

    public <E extends BlockEvent> EventHandlerDescription.Builder<E> newBlockHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerBlockHandler, eventType);
    }

    public <E extends BlockEvent & Cancellable> void cancelBlockEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, event -> event.getBlock().getWorld(), filter);
    }

    public <E extends BlockEvent & Cancellable> void cancelBlockEvent(@NonNull Class<E> eventType) {
        cancelBlockEventIf(eventType, EventFilter.none());
    }

    public <E extends VehicleEvent> void registerVehicleHandler(
            @NonNull EventHandlerDescription<E> handlerDescription
    ) {
        registerHandler(handlerDescription, event -> event.getVehicle().getWorld());
    }

    public <E extends VehicleEvent> EventHandlerDescription.Builder<E> newVehicleHandler(@NonNull Class<E> eventType) {
        return new EventHandlerDescription.Builder<>(this::registerVehicleHandler, eventType);
    }

    public <E extends VehicleEvent & Cancellable> void cancelVehicleEventIf(
            @NonNull Class<E> eventType, @NonNull EventFilter<E> filter
    ) {
        cancelIf(eventType, event -> event.getVehicle().getWorld(), filter);
    }

    public <E extends VehicleEvent & Cancellable> void cancelVehicleEvent(@NonNull Class<E> eventType) {
        cancelVehicleEventIf(eventType, EventFilter.none());
    }
}