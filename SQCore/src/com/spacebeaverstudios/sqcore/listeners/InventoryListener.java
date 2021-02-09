package com.spacebeaverstudios.sqcore.listeners;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIUtils;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && GUIUtils.isButton(event.getCurrentItem())) {
            GUIItem item = GUIUtils.getGUIItem(event.getCurrentItem());
            event.setCancelled(true);
            if (item.hasFunction()) item.runFunction((Player) event.getWhoClicked());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GUI.closePlayer((Player) event.getPlayer());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPLayerLeave(PlayerQuitEvent event) {
        GUI.closePlayer(event.getPlayer());
    }
}
