package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ConfirmInputColorsGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ToggleInputColorGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseInputColorsGUI extends GUI {
    private final Machine machine;
    private final ArrayList<Material> enabledColors = new ArrayList<>();

    public ChooseInputColorsGUI(Machine machine) {
        super("Choose Input Pipe Colors");
        this.machine = machine;
        enabledColors.addAll(machine.getInputPipeMaterials());
    }

    private List<Integer> getWoolSlots() {
        return Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 27, 28, 29, 30, 32, 33, 34, 35);
    }
    private List<Integer> getGlassSlots() {
        return Arrays.asList(9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26);
    }
    private List<Material> getGlassColors() {
        return Arrays.asList(Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS,
                Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.BLACK_STAINED_GLASS);
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, getInventoryName());

        for (int i = 0; i < getWoolSlots().size(); i++) {
            Material woolMaterial = Material.GREEN_WOOL;
            ChatColor color = ChatColor.GREEN;
            String enabledDisabled = ": Enabled";
            if (!machine.getInputPipeMaterials().contains(getGlassColors().get(i))) {
                woolMaterial = Material.RED_WOOL;
                color = ChatColor.RED;
                enabledDisabled = ": Disabled";
            }

            GUIItem woolItem = new GUIItem(color + getGlassColors().get(i).toString() + enabledDisabled, "", woolMaterial,
                    new ToggleInputColorGUIFunction(getGlassColors().get(i)));
            getGuiItems().add(woolItem);
            inventory.setItem(getWoolSlots().get(i), woolItem.getItemStack());

            GUIItem glassItem = new GUIItem(color + getGlassColors().get(i).toString() + enabledDisabled, "",
                    getGlassColors().get(i), new ToggleInputColorGUIFunction(getGlassColors().get(i)));
            getGuiItems().add(glassItem);
            inventory.setItem(getGlassSlots().get(i), glassItem.getItemStack());
        }

        GUIItem cancelItem = new GUIItem("Cancel", "", Material.RED_TERRACOTTA, new CloseInventoryFunction());
        getGuiItems().add(cancelItem);
        inventory.setItem(52, cancelItem.getItemStack());

        GUIItem confirmItem = new GUIItem("Confirm", "", Material.GREEN_TERRACOTTA,
                new ConfirmInputColorsGUIFunction(machine, enabledColors));
        getGuiItems().add(confirmItem);
        inventory.setItem(53, confirmItem.getItemStack());

        return inventory;
    }

    public void toggle(Material glassMaterial) {
        if (enabledColors.contains(glassMaterial)) {
            enabledColors.remove(glassMaterial);

            GUIItem woolItem = new GUIItem(ChatColor.RED + glassMaterial.toString() + ": Disabled", "", Material.RED_WOOL,
                    new ToggleInputColorGUIFunction(glassMaterial));
            getGuiItems().add(woolItem);
            getInventory().setItem(getWoolSlots().get(getGlassColors().indexOf(glassMaterial)), woolItem.getItemStack());

            GUIItem glassItem = new GUIItem(ChatColor.RED + glassMaterial.toString() + ": Disabled", "",
                    glassMaterial, new ToggleInputColorGUIFunction(glassMaterial));
            getGuiItems().add(glassItem);
            getInventory().setItem(getGlassSlots().get(getGlassColors().indexOf(glassMaterial)), glassItem.getItemStack());
        } else {
            enabledColors.add(glassMaterial);

            GUIItem woolItem = new GUIItem(ChatColor.GREEN + glassMaterial.toString() + ": Enabled", "", Material.GREEN_WOOL,
                    new ToggleInputColorGUIFunction(glassMaterial));
            getGuiItems().add(woolItem);
            getInventory().setItem(getWoolSlots().get(getGlassColors().indexOf(glassMaterial)), woolItem.getItemStack());

            GUIItem glassItem = new GUIItem(ChatColor.GREEN + glassMaterial.toString() + ": Enabled", "",
                    glassMaterial, new ToggleInputColorGUIFunction(glassMaterial));
            getGuiItems().add(glassItem);
            getInventory().setItem(getGlassSlots().get(getGlassColors().indexOf(glassMaterial)), glassItem.getItemStack());
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
