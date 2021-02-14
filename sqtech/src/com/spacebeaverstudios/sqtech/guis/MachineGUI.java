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

        GUIItem inventoryButton = new GUIItem("Machine Inventory", "", Material.CHEST,
                new OpenMachineGUIFunction(new MachineInventoryGUI(machine), machine));
        inventory.setItem(12, inventoryButton.getItemStack());
        this.getGuiItems().add(inventoryButton);

        GUIItem chooseOutput = new GUIItem("Choose Output Pipe Color", null,
                (machine.getOutputPipeMaterial() == null ? Material.GLASS : machine.getOutputPipeMaterial()),
                new OpenMachineGUIFunction(new ChooseOutputColorGUI(machine), machine));
        inventory.setItem(14, chooseOutput.getItemStack());
        this.getGuiItems().add(chooseOutput);

        GUIItem chooseInputs = new GUIItem("Choose Input Pipe Colors", null, Material.COMPASS,
                new OpenMachineGUIFunction(new ChooseInputColorsGUI(machine), machine));
        inventory.setItem(15, chooseInputs.getItemStack());
        this.getGuiItems().add(chooseInputs);

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUI(null);
    }
}
