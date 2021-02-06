package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.machines.MachineBase;
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
            if (MachineBase.getMachinesSignText().contains(((Sign) block.getState()).getLine(0))) MachineBase.createFromSign(block);
            else {
                for (MachineBase machine : MachineBase.getMachines()) {
                    if (machine.getSign().getLocation().equals(block.getLocation())) {
                        machine.openGUI(event.getPlayer());
                        break;
                    }
                }
            }
        }
    }
}
