package com.spacebeaverstudios.sqcore.listeners;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.MapUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.meta.MapMeta;

public class InventoryListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (GUI.getGuis().containsKey((Player) event.getWhoClicked())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && GUIUtils.isButton(event.getCurrentItem())
                    && GUI.getGuis().get((Player) event.getWhoClicked()).getInventory().equals(event.getClickedInventory())) {
                GUIItem item = GUIUtils.getGUIItem(event.getCurrentItem());
                item.runFunction((Player) event.getWhoClicked());
            }
        } else if (event.getClickedInventory() instanceof CartographyInventory && event.getRawSlot() == 0) {
            if (event.getCursor().getType() == Material.FILLED_MAP
                    && MapUtils.idsByName.containsValue(((MapMeta) event.getCursor().getItemMeta()).getMapId())) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "You aren't allowed to modify that map!");
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GUI.closePlayer((Player) event.getPlayer(), event.getInventory());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPLayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (GUI.getGuis().containsKey(player)) {
            GUI.closePlayer(player, GUI.getGuis().get(player).getInventory());
        }
    }
}
