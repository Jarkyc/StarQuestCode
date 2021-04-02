package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import com.spacebeaverstudios.sqtech.utils.ReplicatorUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlocksToReplicateGUI extends ListGUI {
    private final ReplicatorMachine machine;
    private ArrayList<Inventory> chestInventories;
    private HashMap<Material, Integer> blocksToReplicate;

    public BlocksToReplicateGUI(ReplicatorMachine machine) {
        super("Blocks Needed to Replicate", 4);
        this.machine = machine;
    }

    public List<Object> getObjectList() {
        chestInventories = machine.getChestInventories(); // done here so I don't call it a bunch
        blocksToReplicate = ReplicatorUtils.getBlocksToReplicate(machine);
        return new ArrayList<>(blocksToReplicate.keySet());
    }

    public GUIItem getObjectItem(Object obj) {
        Material material = (Material) obj;
        int count = 0;
        for (Inventory inventory : chestInventories) {
            for (ItemStack stack : inventory.all(material).values()) {
                count += stack.getAmount();
            }
        }

        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((count >= itemStack.getAmount() ? ChatColor.GREEN : ChatColor.RED)
                + WordUtils.capitalizeFully(material.toString().replace("_", " ")));
        itemMeta.setLore(Arrays.asList(ChatColor.GOLD + "Needed: " + ChatColor.AQUA + blocksToReplicate.get(material),
                ChatColor.GOLD + "Amount in Chests: " + ChatColor.AQUA + count));
        itemStack.setItemMeta(itemMeta);
        if (blocksToReplicate.get(material) > itemStack.getMaxStackSize()) {
            itemStack.setAmount(itemStack.getMaxStackSize());
        } else {
            itemStack.setAmount(blocksToReplicate.get(material));
        }

        return new GUIItem(itemStack, null);
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
        (new ReplicatorGUI(machine)).open(getPlayer());
    }
}
