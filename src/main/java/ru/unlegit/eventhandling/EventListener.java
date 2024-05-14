package ru.unlegit.eventhandling;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface EventListener extends Listener {

    default void unregisterAll() {
        HandlerList.unregisterAll(this);
    }
}