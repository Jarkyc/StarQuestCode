package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TakeFromMachineInventoryGUIFunction extends GUIFunction {
    private final Machine machine;
    private final int slot;
    private final MachineInventoryGUI gui;

    public TakeFromMachineInventoryGUIFunction(Machine machine, int slot, MachineInventoryGUI gui) {
        this.machine = machine;
        this.slot = slot;
        this.gui = gui;
    }

    public void run(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            return;
        }
        inventory.addItem(machine.takeItem(slot, machine.getInventory().get(slot).getAmount()));
        gui.refresh();
    }
}
