package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.HopperMachine;
import org.bukkit.entity.Player;

public class OpenHopperMachineInventoryGUIFunction extends GUIFunction {
    private final HopperMachine machine;

    public OpenHopperMachineInventoryGUIFunction(HopperMachine machine) {
        this.machine = machine;
    }

    public void run(Player player) {
        player.openInventory(machine.getHopperInventory());
    }
}
