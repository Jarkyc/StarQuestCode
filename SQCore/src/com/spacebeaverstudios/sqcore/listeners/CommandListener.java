package com.spacebeaverstudios.sqcore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        // for some reason the server runs /list every 50 seconds
        // so we cancel it to prevent console spam
        if (event.getCommand().equals("list")) {
            event.setCancelled(true);
        }
    }
}
