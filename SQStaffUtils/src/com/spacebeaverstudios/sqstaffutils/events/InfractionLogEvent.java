package com.spacebeaverstudios.sqstaffutils.events;

import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class InfractionLogEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    private final String name;
    private final String message;
    private final String imageUrl;

    public String getName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public InfractionLogEvent(String name, String message, InfractionSender sender) {
        this.name = name;
        this.message = message;
        this.imageUrl = "https://cravatar.eu/helmavatar/" + sender.getName() + "/16.png";
    }
}
