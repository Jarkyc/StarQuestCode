package com.spacebeaverstudios.sqtech.machines;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqtech.guis.MachineGUI;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Machine {
    // static
    private static final ArrayList<String> machinesSignText = new ArrayList<>(Arrays.asList("[test machine]"));
    private static final ArrayList<Machine> machines = new ArrayList<>();

    public static ArrayList<String> getMachinesSignText() {
        return machinesSignText;
    }
    public static ArrayList<Machine> getMachines() {
        return machines;
    }

    public static void createFromSign(Block sign) {
        switch(((Sign) sign.getState()).getLine(0)) {
            case "[test machine]":
                new TestMachine(sign);
                break;
        }
    }

    public static void tickMachines() {
        for (Machine machine : machines) machine.tick();
        MachineInventoryGUI.refreshAll();
    }

    // instance
    private final Location sign;
    private final ArrayList<ItemStack> inventory = new ArrayList<>(); // TODO: drop inventory when machine broken
    private GUI gui = null;
    private final String machineName;

    public Machine(Block sign, String machineName) {
        if (detect(sign)) {
            this.sign = sign.getLocation();
            this.machineName = machineName;
            init();
        } else {
            this.sign = null;
            this.machineName = null;
        }
    }

    public Location getSign() {
        return sign;
    }
    public String getMachineName() {
        return machineName;
    }
    public ArrayList<ItemStack> getInventory() {
        return inventory;
    }

    public abstract boolean detect(Block sign); // TODO: change to a standardized script with data checking
    public abstract void init(); // MUST include MachineBase.getMachines().add(this);

    public abstract void tick(); // run all the functions that occur once a second

    public abstract String getMachineInfo();

    public ItemStack tryAddItemStack(ItemStack itemStack) {
        // returns whatever items weren't able to be added
        for (ItemStack stack : inventory) {
            if (stack.getType().equals(itemStack.getType()) && stack.getAmount() < stack.getMaxStackSize()) {
                if (stack.getMaxStackSize()-stack.getAmount() >= itemStack.getAmount()) {
                    stack.setAmount(stack.getAmount() + itemStack.getAmount());
                    return new ItemStack(itemStack.getType(), 0);
                } else {
                    itemStack.setAmount(itemStack.getAmount()-(stack.getMaxStackSize()-stack.getAmount()));
                    stack.setAmount(stack.getMaxStackSize());
                }
            }
        }
        if (itemStack.getAmount() > 0 && inventory.size() < 9) {
            inventory.add(itemStack);
            return new ItemStack(itemStack.getType(), 0);
        } else return itemStack;
    }
    public ItemStack takeItem(int slot, int amount) {
        if (inventory.size() <= slot) return null;
        else if (inventory.get(slot).getAmount() <= amount) {
            ItemStack itemStack = inventory.get(slot);
            inventory.remove(slot);
            return itemStack;
        } else {
            inventory.get(slot).setAmount(inventory.get(slot).getAmount() - amount);
            return new ItemStack(inventory.get(slot).getType(), amount);
        }
    }

    public void openGUI(Player player) {
        if (gui == null) {
            gui = new MachineGUI(this);
            gui.open(player);
        } else player.sendMessage(ChatColor.RED + "Someone is already accessing that machine!");
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
