package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MachineGUI extends GUI {
    private final Machine machine;

    public MachineGUI(Machine machine) {
        super(machine.getMachineName());
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, super.getInventoryName());

        GUIItem machineInfo = new GUIItem("Machine Info", machine.getMachineInfo(), Material.FURNACE, null);
        inventory.setItem(11, machineInfo.getItemStack());
        this.getGuiItems().add(machineInfo);

        GUIItem settingsButton = new GUIItem("Machine Settings", "", Material.COMPASS,
                new OpenMachineGUIFunction(new MachineSettingsGUI(machine), machine));
        inventory.setItem(13, settingsButton.getItemStack());
        this.getGuiItems().add(settingsButton);

        GUIItem inventoryButton = new GUIItem("Machine Inventory", "", Material.CHEST,
                new OpenMachineGUIFunction(new MachineInventoryGUI(machine), machine));
        inventory.setItem(15, inventoryButton.getItemStack());
        this.getGuiItems().add(inventoryButton);

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUI(null);
    }
}
