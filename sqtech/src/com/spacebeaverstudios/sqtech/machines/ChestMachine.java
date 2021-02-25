package com.spacebeaverstudios.sqtech.machines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ChestMachine extends Machine {
    private Inventory chestInventory; // TODO: make it work with double chests

    public ChestMachine(Block sign) {
        super(sign, "Chest Input/Output");
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.CHEST);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Machine.getMachines().add(this);
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, "");
        sign.setLine(1, ChatColor.BLUE + "Chest Input/Output");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();

        chestInventory = ((Chest) sign.getBlock().getRelative(((Directional) sign.getBlock().getBlockData())
                .getFacing().getOppositeFace()).getState()).getBlockInventory();
    }

    public void tick() {
        if (chestInventory.getViewers().size() == 0 && getOutputPipe() != null) {
            ItemStack[] contents = chestInventory.getContents();
            for (int i = chestInventory.getSize()-1; i >= 0; i--) {
                if (contents[i] != null) {
                    chestInventory.setItem(i, null);
                    tryOutput(contents[i]);
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack tryAddItemStack(ItemStack itemStack) {
        HashMap<Integer, ItemStack> left = chestInventory.addItem(itemStack);
        if (left.size() != 0)
            for (ItemStack stack : left.values())
                return stack;
        return new ItemStack(itemStack.getType(), 0);
    }

    public String getMachineInfo() {
        return "Allows pipe input/output to and from the attached chest.";
    }

    public Inventory getChestInventory() {
        return chestInventory;
    }
}
