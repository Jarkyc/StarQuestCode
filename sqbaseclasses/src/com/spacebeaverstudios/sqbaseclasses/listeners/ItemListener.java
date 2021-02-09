package com.spacebeaverstudios.sqbaseclasses.listeners;

import com.spacebeaverstudios.sqbaseclasses.gui.GUIUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (GUIUtils.isWanted(event.getEntity().getItemStack())) event.setCancelled(true);
    }
}
