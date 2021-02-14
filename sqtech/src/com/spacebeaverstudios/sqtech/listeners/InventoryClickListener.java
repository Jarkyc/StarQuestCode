package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null && GUI.getGuis().get(player) instanceof MachineInventoryGUI) {
            if (!player.getInventory().equals(event.getClickedInventory())) return;
            event.setCancelled(true);
            MachineInventoryGUI gui = (MachineInventoryGUI) GUI.getGuis().get(player);
            ItemStack tried = gui.getMachine().tryAddItemStack(event.getCurrentItem());
            if (tried.getAmount() > 0) event.getClickedInventory().setItem(event.getSlot(), tried);
            else event.getClickedInventory().setItem(event.getSlot(), null);
            gui.refresh();
        }
    }
}
