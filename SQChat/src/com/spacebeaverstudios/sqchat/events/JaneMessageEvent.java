package com.spacebeaverstudios.sqchat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JaneMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    private final String message;

    public JaneMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
