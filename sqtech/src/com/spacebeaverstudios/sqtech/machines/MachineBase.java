package com.spacebeaverstudios.sqtech.machines;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MachineBase {
    private static final ArrayList<String> machinesSignText = new ArrayList<>(Arrays.asList("[test machine]"));
    private static final ArrayList<MachineBase> machines = new ArrayList<>();

    public static ArrayList<String> getMachinesSignText() {
        return machinesSignText;
    }
    public static ArrayList<MachineBase> getMachines() {
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
        for (MachineBase machine : machines) machine.tick();
    }

    private Block sign;
    private final ArrayList<ItemStack> inventory = new ArrayList<>();

    public MachineBase(Block sign) {
        if (detect(sign)) {
            this.sign = sign;
            init();
        }
    }

    public Block getSign() {
        return sign;
    }

    abstract boolean detect(Block sign); // TODO: change to a standardized script with data checking
    abstract void init(); // MUST include MachineBase.getMachines().add(this);

    abstract void tick(); // run all the functions that occur once a second

    public ItemStack tryAddItemStack(ItemStack itemStack) {
        // returns whatever items weren't able to be added
        if (inventory.size() < 9) {
            inventory.add(itemStack);
            return new ItemStack(itemStack.getType(), 0);
        } else {
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
            return itemStack;
        }
    }

    public void openGUI(Player player) {
        // TODO
    }
}