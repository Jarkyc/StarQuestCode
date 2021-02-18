package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseOutputColorFunction;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ChooseOutputColorGUI extends GUI {
    private final Machine machine;

    public ChooseOutputColorGUI(Machine machine) {
        super("Choose " + machine.getMachineName() + " Output Pipe Color");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryName());

        for (Material material : Arrays.asList(Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS)) {
            GUIItem guiItem = new GUIItem(new ItemStack(material, 1), new ChooseOutputColorFunction(machine, material));
            getGuiItems().add(guiItem);
            inventory.addItem(guiItem.getItemStack());
        }

        GUIItem cancel = new GUIItem("Cancel", null, Material.RED_TERRACOTTA, new CloseInventoryFunction());
        getGuiItems().add(cancel);
        inventory.setItem(26, cancel.getItemStack());

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        MachineGUI machineGUI = new MachineGUI(machine);
        machine.setGUI(machineGUI);
        machineGUI.open(this.getPlayer());
    }
}
