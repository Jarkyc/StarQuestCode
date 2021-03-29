package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ReplicateGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.SwapReplicationSideGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReplicatorGUI extends GUI {
    private final ReplicatorMachine machine;

    public ReplicatorGUI(ReplicatorMachine machine) {
        super("Replicate");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, getInventoryName());

        int blocks = 0;
        for (ItemStack itemStack : machine.getBlocksToReplicate()) {
            blocks += itemStack.getAmount();
        }
        GUIItem copyItem = new GUIItem("Replicate", ChatColor.GRAY + "Replicate from "
                + (machine.getCopyFromLeft() ? "left to right.\n " : "right to left.\n ") + ChatColor.GOLD + "Power Needed: "
                + ChatColor.AQUA + blocks * 5 + "\n " + ChatColor.GOLD + "Power Available: " + ChatColor.AQUA + machine.getAvailablePower(),
                Material.WRITABLE_BOOK, new ReplicateGUIFunction(machine));
        getGuiItems().add(copyItem);
        inventory.setItem(1, copyItem.getItemStack());

        GUIItem switchItem = new GUIItem("Switch Sides", (machine.getCopyFromLeft() ? "<-- From\n To -->" : "From -->\n <-- To"),
                Material.OAK_SIGN, new SwapReplicationSideGUIFunction(machine, this));
        getGuiItems().add(switchItem);
        inventory.setItem(4, switchItem.getItemStack());

        GUIItem listItem = new GUIItem("Blocks Needed", "See a list of the blocks you need in order to replicate.",
                Material.CHEST, new OpenMachineGUIFunction(new BlocksToReplicateGUI(machine), machine));
        getGuiItems().add(listItem);
        inventory.setItem(7, listItem.getItemStack());

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
