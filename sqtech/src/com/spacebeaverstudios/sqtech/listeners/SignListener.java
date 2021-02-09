package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (block.getType().toString().endsWith("_WALL_SIGN")) {
            if (Machine.getMachinesSignText().contains(((Sign) block.getState()).getLine(0))) Machine.createFromSign(block);
            else {
                for (Machine machine : Machine.getMachines()) {
                    if (machine.getSign().equals(block.getLocation())) {
                        machine.openGUI(event.getPlayer());
                        break;
                    }
                }
            }
        }
    }
}
