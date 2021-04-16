package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqtech.guis.ChooseBrewingRecipeGUI;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseCrafterMachineRecipeGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.CrafterMachine;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null && GUI.getGuis().get(player) instanceof MachineInventoryGUI) {
            if (!player.getInventory().equals(event.getClickedInventory())) {
                return;
            }
            event.setCancelled(true);
            MachineInventoryGUI gui = (MachineInventoryGUI) GUI.getGuis().get(player);
            ItemStack tried = gui.getMachine().tryAddItemStack(event.getCurrentItem());
            if (tried.getAmount() > 0) {
                event.getClickedInventory().setItem(event.getSlot(), tried);
            } else {
                event.getClickedInventory().setItem(event.getSlot(), null);
            }
            gui.refresh();
        } else if (event.getCurrentItem() != null && GUI.getGuis().get(player) instanceof ChooseBrewingRecipeGUI) {
            if (!player.getInventory().equals(event.getClickedInventory())) {
                return;
            }
            event.setCancelled(true);
            ((ChooseBrewingRecipeGUI) GUI.getGuis().get(player)).selectItem(event.getCurrentItem());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        HashMap<Player, CrafterMachine> playersCrafting = ChooseCrafterMachineRecipeGUIFunction.getPlayersCrafting();

        if (playersCrafting.containsKey(player)) {
            playersCrafting.remove(player);
            for (Machine machine : Machine.getMachines()) {
                if (player.equals(machine.getGUIPlayer())) {
                    machine.setGUIPlayer(null);
                    machine.openGUI(player);
                    return;
                }
            }
        }
    }
}
