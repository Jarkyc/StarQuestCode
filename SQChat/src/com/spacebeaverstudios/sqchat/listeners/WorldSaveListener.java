package com.spacebeaverstudios.sqchat.listeners;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldSaveListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase("space")) ChatUtils.saveMutedPlayers();
    }
}
