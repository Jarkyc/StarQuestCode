package com.spacebeaverstudios.sqcore.listeners;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
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
        if (GUI.getGuis().containsKey((Player) event.getWhoClicked())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && GUIUtils.isButton(event.getCurrentItem())
                    && GUI.getGuis().get((Player) event.getWhoClicked()).getInventory().equals(event.getClickedInventory())) {
                GUIItem item = GUIUtils.getGUIItem(event.getCurrentItem());
                if (item.hasFunction()) item.runFunction((Player) event.getWhoClicked());
            }
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
