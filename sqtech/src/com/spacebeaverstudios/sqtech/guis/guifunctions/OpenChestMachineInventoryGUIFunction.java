package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.machines.ChestMachine;
import org.bukkit.entity.Player;

public class OpenChestMachineInventoryGUIFunction extends GUIFunction {
    private final ChestMachine machine;

    public OpenChestMachineInventoryGUIFunction(ChestMachine machine) {
        this.machine = machine;
    }

    public void run(Player player) {
        player.openInventory(machine.getChestInventory());
    }
}
