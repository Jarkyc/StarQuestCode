package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getType().toString().endsWith("_WALL_SIGN")) {
            if (!Machine.createFromSign(block) && Machine.getMachinesByBlock().containsKey(block.getLocation())) {
                if (Machine.getMachinesByBlock().get(block.getLocation()).getSign().equals(block.getLocation())) {
                    Machine.getMachinesByBlock().get(block.getLocation()).openGUI(event.getPlayer());
                }
            }
        }
    }
}
