package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseCrafterMachineRecipeGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.CrafterMachine;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;

public class InventoryListener implements Listener {
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
