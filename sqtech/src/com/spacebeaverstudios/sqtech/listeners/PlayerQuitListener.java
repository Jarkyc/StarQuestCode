package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseCrafterMachineRecipeGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Machine machine : Machine.getMachines()) {
            if (machine.getGUIPlayer() != null) {
                if (machine.getGUIPlayer().equals(event.getPlayer())) {
                    machine.setGUIPlayer(null);
                    ChooseCrafterMachineRecipeGUIFunction.getPlayersCrafting().remove(event.getPlayer());
                    return;
                }
            }
        }
    }
}
