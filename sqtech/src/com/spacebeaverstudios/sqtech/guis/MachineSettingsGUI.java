package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MachineSettingsGUI extends GUI {
    private final Machine machine;

    public MachineSettingsGUI(Machine machine) {
        super(machine.getMachineName());
        this.machine = machine;
    }

    public Inventory createInventory() {
        // TODO
        return Bukkit.createInventory(null, 27);
    }

    @Override
    public void onClose() {
        super.onClose();
        MachineGUI machineGUI = new MachineGUI(machine);
        machine.setGUI(machineGUI);
        machineGUI.open(this.getPlayer());
    }
}
