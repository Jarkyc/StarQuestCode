package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ChooseInputColorsGUI extends GUI {
    private final Machine machine;

    public ChooseInputColorsGUI(Machine machine) {
        super("Choose " + machine.getMachineName() + " Input Pipe Colors");
        this.machine = machine;
    }

    public Inventory createInventory() {
        return Bukkit.createInventory(null, 27);
        // TODO
    }
}
