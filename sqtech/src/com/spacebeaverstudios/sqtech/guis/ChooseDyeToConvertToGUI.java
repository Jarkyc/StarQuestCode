package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseDyeToConvertToGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.DyeMixerMachine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChooseDyeToConvertToGUI extends GUI {
    private final DyeMixerMachine machine;

    public ChooseDyeToConvertToGUI(DyeMixerMachine machine) {
        super("Choose Dye to Convert To");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryName());

        for (Material material : DyeMixerMachine.getDyes()) {
            GUIItem guiItem = new GUIItem(new ItemStack(material, 1), new ChooseDyeToConvertToGUIFunction(machine, material));
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
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
