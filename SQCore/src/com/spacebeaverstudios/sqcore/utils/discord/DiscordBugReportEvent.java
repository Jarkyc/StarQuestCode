package com.spacebeaverstudios.sqcore.utils.discord;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DiscordBugReportEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    private final String message;

    public DiscordBugReportEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
