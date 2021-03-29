package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlocksToReplicateGUI extends ListGUI {
    private final ReplicatorMachine machine;
    private ArrayList<Inventory> chestInventories;

    public BlocksToReplicateGUI(ReplicatorMachine machine) {
        super("Blocks Needed to Replicate", 4);
        this.machine = machine;
    }

    public List<Object> getObjectList() {
        chestInventories = machine.getChestInventories(); // done here so I don't call it a bunch
        return new ArrayList<>(machine.getBlocksToReplicate());
    }

    public GUIItem getObjectItem(Object obj) {
        ItemStack itemStack = (ItemStack) obj;
        int count = 0;
        for (Inventory inventory : chestInventories) {
            for (ItemStack stack : inventory.all(itemStack.getType()).values()) {
                count += stack.getAmount();
            }
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((count >= itemStack.getAmount() ? ChatColor.GREEN : ChatColor.RED) + itemMeta.getDisplayName());
        itemMeta.setLore(Arrays.asList(ChatColor.GOLD + "Needed: " + ChatColor.AQUA + itemStack.getAmount(),
                ChatColor.GOLD + "Amount in Chests: " + count));
        itemStack.setItemMeta(itemMeta);
        if (itemStack.getAmount() > itemStack.getMaxStackSize()) {
            itemStack.setAmount(itemStack.getMaxStackSize());
        } else if (itemStack.getAmount() == 0) {
            itemStack.setAmount(1);
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
