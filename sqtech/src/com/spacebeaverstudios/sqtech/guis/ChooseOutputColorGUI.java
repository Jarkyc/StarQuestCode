package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import com.spacebeaverstudios.sqcore.gui.guifunctions.CloseInventoryFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseOutputColorFunction;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseOutputColorGUI extends ListGUI {
    private final Machine machine;

    public ChooseOutputColorGUI(Machine machine) {
        super("Choose " + machine.getMachineName() + " Output Pipe Color", 3);
        this.machine = machine;
    }

    public List<Object> getObjectList() {
        List<Material> materials = Arrays.asList(Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS);
        ArrayList<Object> list = new ArrayList<>();
        for (Material material : materials) {
            GUIItem guiItem = new GUIItem(new ItemStack(material, 1), new ChooseOutputColorFunction(machine, material));
            list.add(guiItem);
            getGuiItems().add(guiItem);
        }
        return list;
    }

    public ItemStack getObjectItem(Object obj) {
        return ((GUIItem) obj).getItemStack();
    }

    @Override
    public ArrayList<ItemStack> createBottomRow() {
        ArrayList<GUIItem> guiItems = new ArrayList<>();

        for (int i = 0; i < 8; i++) guiItems.add(new GUIItem(" ", null, Material.IRON_BARS, null));

        guiItems.add(new GUIItem("Cancel", null, Material.RED_TERRACOTTA, new CloseInventoryFunction()));

        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (GUIItem item : guiItems) {
            itemStacks.add(item.getItemStack());
            getGuiItems().add(item);
        }
        return itemStacks;
    }

    @Override
    public void onClose() {
        super.onClose();
        MachineGUI machineGUI = new MachineGUI(machine);
        machine.setGUI(machineGUI);
        machineGUI.open(this.getPlayer());
    }
}
