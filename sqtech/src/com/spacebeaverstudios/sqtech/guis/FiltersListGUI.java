package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ToggleWhitelistGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.SortingMachine;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class FiltersListGUI extends GUI {
    private final SortingMachine machine;

    public FiltersListGUI(SortingMachine machine) {
        super("Sorting Filters");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 18, getInventoryName());
        for (int i = 0; i < machine.getHoppers().size(); i++) {
            StringBuilder itemsInFilter = new StringBuilder(ChatColor.GRAY + "");
            for (Material material : machine.filters[i].items) {
                itemsInFilter.append(WordUtils.capitalizeFully(String.valueOf(material).replace("_", " "))).append(", ");
            }
            if (itemsInFilter.toString().endsWith(", ")) {
                itemsInFilter.delete(itemsInFilter.length() - 2, itemsInFilter.length());
            } else {
                itemsInFilter.append("None");
            }
            GUIItem filterItem = new GUIItem("Choose Filtered Items For Hopper " + (i + 1), itemsInFilter.toString(), Material.HOPPER,
                    new OpenMachineGUIFunction(new FilterItemsGUI(machine, machine.filters[i]), machine));
            inventory.setItem(i, filterItem.getItemStack());
            getGuiItems().add(filterItem);

            boolean whitelist = machine.filters[i].whitelist;
            GUIItem whitelistItem = new GUIItem("Use " + (whitelist ? "Whitelist" : "Blacklist") + " For Hopper " + (i + 1),
                    ChatColor.GRAY + "Click to change to " + (whitelist ? "blacklist" : "whitelist"),
                    (whitelist ? Material.WHITE_CONCRETE : Material.BLACK_CONCRETE),
                    new ToggleWhitelistGUIFunction(this, machine.filters[i]));
            inventory.setItem(9 + i, whitelistItem.getItemStack());
            getGuiItems().add(whitelistItem);
        }
        return inventory;
    }
}
