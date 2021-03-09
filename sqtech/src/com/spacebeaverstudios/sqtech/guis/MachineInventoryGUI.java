package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqtech.guis.guifunctions.TakeFromMachineInventoryGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineInventoryGUI extends GUI {
    private static final ArrayList<MachineInventoryGUI> allMachineInventoryGUIs = new ArrayList<>();

    public static void refreshAll() {
        for (MachineInventoryGUI gui : allMachineInventoryGUIs) {
            gui.refresh();
        }
    }

    private final Machine machine;
    private final ArrayList<GUIItem> inventoryGUIItems = new ArrayList<>();

    public MachineInventoryGUI(Machine machine) {
        super(machine.getMachineName() + " Inventory");
        this.machine = machine;
    }

    @Override
    public void open(Player player) {
        super.open(player);
        allMachineInventoryGUIs.add(this);
    }

    public Machine getMachine() {
        return machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, this.getInventoryName());

        List<Integer> ironBarSlots = Arrays.asList(0, 1, 2, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 24, 25);
        for (Integer slot : ironBarSlots) {
            GUIItem guiItem = new GUIItem(" ", null, Material.IRON_BARS, null);
            this.getGuiItems().add(guiItem);
            inventory.setItem(slot, guiItem.getItemStack());
        }

        List<Integer> inventorySlots = Arrays.asList(3, 4, 5, 12, 13, 14, 21, 22, 23);
        for (int i = 0; i < machine.getInventory().size(); i++) {
            GUIItem guiItem = new GUIItem(machine.getInventory().get(i), new TakeFromMachineInventoryGUIFunction(machine, i, this));
            this.getGuiItems().add(guiItem);
            inventoryGUIItems.add(guiItem);
            inventory.setItem(inventorySlots.get(i), guiItem.getItemStack());
        }

        GUIItem closeItem = new GUIItem("Close Inventory", null, Material.RED_TERRACOTTA, new CloseInventoryFunction());
        this.getGuiItems().add(closeItem);
        inventory.setItem(26, closeItem.getItemStack());

        return inventory;
    }

    public void refresh() {
        List<Integer> inventorySlots = Arrays.asList(3, 4, 5, 12, 13, 14, 21, 22, 23);

        // wipe all displayed items
        for (int i = 0; i < inventoryGUIItems.size(); i++) {
            GUIUtils.getButtons().remove(inventoryGUIItems.get(i));
            getGuiItems().remove(inventoryGUIItems.get(i));
            getInventory().setItem(inventorySlots.get(i), null);
        }
        inventoryGUIItems.clear();

        // make new displayed items
        for (int i = 0; i < machine.getInventory().size(); i++) {
            GUIItem guiItem = new GUIItem(machine.getInventory().get(i), new TakeFromMachineInventoryGUIFunction(machine, i, this));
            this.getGuiItems().add(guiItem);
            inventoryGUIItems.add(guiItem);
            getInventory().setItem(inventorySlots.get(i), guiItem.getItemStack());
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        allMachineInventoryGUIs.remove(this);
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
