package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqtech.guis.guifunctions.RemoveFromFilterGUIFunction;
import com.spacebeaverstudios.sqtech.objects.SortingFilter;
import com.spacebeaverstudios.sqtech.objects.machines.SortingMachine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterItemsGUI extends GUI {
    private final SortingMachine machine;
    private final SortingFilter filter;
    private final ArrayList<GUIItem> inventoryGUIItems = new ArrayList<>();

    public FilterItemsGUI(SortingMachine machine, SortingFilter filter) {
        super("Choose Filtered Items");
        this.machine = machine;
        this.filter = filter;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryName());

        List<Integer> ironBarSlots = Arrays.asList(0, 1, 2, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 24, 25);
        for (Integer slot : ironBarSlots) {
            GUIItem guiItem = new GUIItem(" ", null, Material.IRON_BARS, null);
            this.getGuiItems().add(guiItem);
            inventory.setItem(slot, guiItem.getItemStack());
        }

        List<Integer> inventorySlots = Arrays.asList(3, 4, 5, 12, 13, 14, 21, 22, 23);
        for (int i = 0; i < filter.items.size(); i++) {
            GUIItem guiItem = new GUIItem(new ItemStack(filter.items.get(i), 1),
                    new RemoveFromFilterGUIFunction(this, filter, i));
            this.getGuiItems().add(guiItem);
            inventoryGUIItems.add(guiItem);
            inventory.setItem(inventorySlots.get(i), guiItem.getItemStack());
        }

        GUIItem closeItem = new GUIItem("Close Inventory", null, Material.RED_TERRACOTTA, new CloseInventoryFunction());
        this.getGuiItems().add(closeItem);
        inventory.setItem(26, closeItem.getItemStack());

        return inventory;
    }

    @Override
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }
        if (!filter.items.contains(event.getCurrentItem().getType()) && filter.items.size() < 9) {
            filter.items.add(event.getCurrentItem().getType());
        }
        refresh();
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
        for (int i = 0; i < filter.items.size(); i++) {
            GUIItem guiItem = new GUIItem(new ItemStack(filter.items.get(i), 1), new RemoveFromFilterGUIFunction(this, filter, i));
            this.getGuiItems().add(guiItem);
            inventoryGUIItems.add(guiItem);
            getInventory().setItem(inventorySlots.get(i), guiItem.getItemStack());
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
