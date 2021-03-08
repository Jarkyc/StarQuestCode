package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenHopperMachineInventoryGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.HopperMachine;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MachineGUI extends GUI {
    private final Machine machine;

    public MachineGUI(Machine machine) {
        super(machine.getMachineName());
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, super.getInventoryName());

        GUIItem machineInfoItem = new GUIItem(machine.getMachineName(), ChatColor.GRAY + machine.getMachineInfo(),
                Material.COMPASS, null);
        inventory.addItem(machineInfoItem.getItemStack());
        this.getGuiItems().add(machineInfoItem);

        if (machine.getInputTypes().contains(Machine.TransferType.ITEMS)) {
            GUIFunction inventoryGUIFunction;
            if (machine instanceof HopperMachine) {
                inventoryGUIFunction = new OpenHopperMachineInventoryGUIFunction((HopperMachine) machine);
            } else {
                inventoryGUIFunction = new OpenMachineGUIFunction(new MachineInventoryGUI(machine), machine);
            }
            GUIItem inventoryButtonItem = new GUIItem("Machine Inventory", "", Material.CHEST, inventoryGUIFunction);
            inventory.addItem(inventoryButtonItem.getItemStack());
            this.getGuiItems().add(inventoryButtonItem);
        }

        GUIItem chooseOutputItem = new GUIItem("Choose Output Pipe Color", ChatColor.GOLD + "Output Type: " + ChatColor.AQUA
                + (machine.getOutputType() == Machine.TransferType.ITEMS ? "Items" : "BV") + "\n " + ChatColor.GOLD + "Currently Selected: "
                + getColorName(machine.getOutputPipeMaterial()),
                (machine.getOutputPipeMaterial() == null ? Material.GLASS : machine.getOutputPipeMaterial()),
                new OpenMachineGUIFunction(new ChooseOutputColorGUI(machine), machine));
        inventory.addItem(chooseOutputItem.getItemStack());
        this.getGuiItems().add(chooseOutputItem);

        if (machine.getInputTypes().size() != 0) {
            StringBuilder chooseInputsLore = new StringBuilder(ChatColor.GOLD + "Input Type");

            if (machine.getInputTypes().size() == 2) {
                chooseInputsLore.append("s: ").append(ChatColor.AQUA).append("Items, BV");
            } else if (machine.getInputTypes().get(0).equals(Machine.TransferType.ITEMS)) {
                chooseInputsLore.append(ChatColor.AQUA).append("Items");
            } else {
                chooseInputsLore.append(ChatColor.AQUA).append("BV");
            }
            chooseInputsLore.append("\n ");

            chooseInputsLore.append(ChatColor.GOLD).append("Currently Selected: ");
            if (machine.getInputPipeMaterials().size() == 0) {
                chooseInputsLore.append(getColorName(null));
            } else {
                for (Material glass : machine.getInputPipeMaterials()) {
                    chooseInputsLore.append(getColorName(glass)).append(ChatColor.WHITE).append(", ");
                }
                chooseInputsLore.delete(chooseInputsLore.length()-2, chooseInputsLore.length());
            }

            GUIItem chooseInputsItem = new GUIItem("Choose Input Pipe Colors", chooseInputsLore.toString(), Material.COMPASS,
                    new OpenMachineGUIFunction(new ChooseInputColorsGUI(machine), machine));
            inventory.addItem(chooseInputsItem.getItemStack());
            this.getGuiItems().add(chooseInputsItem);
        }

        GUIItem customOptionsItem = machine.getCustomOptionsGUIItem();
        if (customOptionsItem != null) {
            inventory.addItem(customOptionsItem.getItemStack());
            getGuiItems().add(customOptionsItem);
        }

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUI(null);
    }

    public static String getColorName(Material glass) {
        if (glass == null) {
            return ChatColor.GRAY + "None";
        }
        switch(glass) {
            case WHITE_STAINED_GLASS:
                return ChatColor.WHITE + "White";
            case ORANGE_STAINED_GLASS:
                return ChatColor.GOLD + "Orange";
            case MAGENTA_STAINED_GLASS:
                return ChatColor.LIGHT_PURPLE + "Magenta";
            case LIGHT_BLUE_STAINED_GLASS:
                return ChatColor.BLUE + "Light Blue";
            case YELLOW_STAINED_GLASS:
                return ChatColor.YELLOW + "Yellow";
            case LIME_STAINED_GLASS:
                return ChatColor.GREEN + "Lime";
            case PINK_STAINED_GLASS:
                return ChatColor.LIGHT_PURPLE + "Pink";
            case GRAY_STAINED_GLASS:
                return ChatColor.DARK_GRAY + "Gray";
            case LIGHT_GRAY_STAINED_GLASS:
                return ChatColor.GRAY + "Light Gray";
            case CYAN_STAINED_GLASS:
                return ChatColor.AQUA + "Cyan";
            case PURPLE_STAINED_GLASS:
                return ChatColor.DARK_PURPLE + "Purple";
            case BLUE_STAINED_GLASS:
                return ChatColor.DARK_BLUE + "Blue";
            case BROWN_STAINED_GLASS:
                return ChatColor.DARK_GRAY + "Brown";
            case GREEN_STAINED_GLASS:
                return ChatColor.DARK_GREEN + "Green";
            case RED_STAINED_GLASS:
                return ChatColor.DARK_RED + "Red";
            case BLACK_STAINED_GLASS:
                return ChatColor.BLACK + "Black";
            default:
                return ChatColor.GRAY + "None";
        }
    }
}
