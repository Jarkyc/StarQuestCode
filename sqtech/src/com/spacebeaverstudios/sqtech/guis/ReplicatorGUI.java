package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ReplicateGUIFunction;
import com.spacebeaverstudios.sqtech.guis.guifunctions.SwapReplicationSideGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import com.spacebeaverstudios.sqtech.utils.ReplicatorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class ReplicatorGUI extends GUI {
    private final ReplicatorMachine machine;

    public ReplicatorGUI(ReplicatorMachine machine) {
        super("Replicate");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, getInventoryName());

        int blocks = 0;
        // TODO: this isn't the proper amount of power due to halveCost and slabs
        for (Integer amount : ReplicatorUtils.getBlocksToReplicate(machine).values()) {
            blocks += amount;
        }
        GUIItem copyItem = new GUIItem("Replicate", ChatColor.GRAY + "Replicate from "
                + (machine.getCopyFromLeft() ? "left to right.\n " : "right to left.\n ") + ChatColor.GOLD + "Power Needed: "
                + ChatColor.AQUA + blocks * 5 + "\n " + ChatColor.GOLD + "Power Available: " + ChatColor.AQUA + machine.getAvailablePower(),
                Material.WRITABLE_BOOK, new ReplicateGUIFunction(machine));
        getGuiItems().add(copyItem);
        inventory.addItem(copyItem.getItemStack());

        GUIItem switchItem = new GUIItem("Switch Sides", ChatColor.GRAY
                + (machine.getCopyFromLeft() ? "<-- From\n To -->" : "From -->\n <-- To"),
                Material.OAK_SIGN, new SwapReplicationSideGUIFunction(machine, this));
        getGuiItems().add(switchItem);
        inventory.addItem(switchItem.getItemStack());

        GUIItem listItem = new GUIItem("Blocks Needed",
                ChatColor.GRAY + "See a list of the blocks you need in order to replicate.",
                Material.CHEST, new OpenMachineGUIFunction(new BlocksToReplicateGUI(machine), machine));
        getGuiItems().add(listItem);
        inventory.addItem(listItem.getItemStack());

        return inventory;
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
//        (new MachineGUI(machine)).open(getPlayer()); TODO
    }
}
