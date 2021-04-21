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

        // Jarkyc here, it does this to make sure the server is still up
        // and receiving things properly
        if (event.getCommand().equals("list")) {
            event.setCancelled(true);
        }
    }
}
