package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldSaveListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase("world")) {
            SQTech.getInstance().saveMachines();
        }
    }
}
