package com.spacebeaverstudios.sqtech.machines;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.MachineGUI;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.pipes.ItemPipe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Machine {
    // static
    private static final ArrayList<String> machinesSignText = new ArrayList<>(Arrays.asList("[smelter]"));
    private static final ArrayList<Machine> machines = new ArrayList<>();

    public static ArrayList<String> getMachinesSignText() {
        return machinesSignText;
    }
    public static ArrayList<Machine> getMachines() {
        return machines;
    }

    public static void createFromSign(Block sign) {
        switch(((Sign) sign.getState()).getLine(0)) {
            case "[smelter]":
                new SmelterMachine(sign);
                break;
        }
    }

    // instance
    private final Location sign;
    private final ArrayList<ItemStack> inventory = new ArrayList<>(); // TODO: drop inventory when machine broken
    private GUI gui = null;
    private final String machineName;
    private final ArrayList<Material> inputPipeMaterials = new ArrayList<>();
    private Material outputPipeMaterial;
    private Location node = null;
    private ItemPipe outputPipe;

    public Machine(Block sign, String machineName) {
        if (detect(sign)) {
            this.sign = sign.getLocation();
            this.machineName = machineName;

            // get node from schema
            HashMap<Vector, Material> schema = getSchema();
            for (Vector vec : schema.keySet()) {
                if (schema.get(vec).equals(Material.LAPIS_BLOCK)) {
                    Block block = sign;
                    Directional dir = (Directional) sign.getBlockData();
                    block = block.getRelative(dir.getFacing().getOppositeFace(), vec.getBlockX());
                    block = block.getRelative(BlockFace.UP, vec.getBlockY());

                    // have to do it like this cause there's no 90 degree rotate
                    switch (dir.getFacing().getOppositeFace()) {
                        case NORTH:
                            block = block.getRelative(BlockFace.EAST, vec.getBlockZ());
                            break;
                        case EAST:
                            block = block.getRelative(BlockFace.SOUTH, vec.getBlockZ());
                            break;
                        case SOUTH:
                            block = block.getRelative(BlockFace.WEST, vec.getBlockZ());
                            break;
                        case WEST:
                            block = block.getRelative(BlockFace.NORTH, vec.getBlockZ());
                            break;
                    }

                    this.node = block.getLocation();
                    break;
                }
            }

            if (node == null) {
                // This SHOULDN'T happen. DON'T let it.
                SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                        + " Machine has no node block! Sign text: " + ((Sign) sign.getState()).getLine(0));
                for (Player player : sign.getLocation().getNearbyPlayers(5))
                    player.sendMessage(ChatColor.RED + "An error occurred when attempting to create the machine. Staff have been notified!");
                return;
            }

            init();
        } else {
            this.sign = null;
            this.machineName = null;
            this.node = null;
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
    public ArrayList<Material> getInputPipeMaterials() {
        return inputPipeMaterials;
    }
    public Location getNode() {
        return node;
    }
    public ItemPipe getOutputPipe() {
        return outputPipe;
    }
    public Material getOutputPipeMaterial() {
        return outputPipeMaterial;
    }

    public void setOutputPipeMaterial(Material material) {
        outputPipeMaterial = material;
        // TODO: detect nearby pipes of that color
    }

    // schema is oriented so that positive x points to behind the sign, and positive z to the right
    // schemas MUST include a node block (Material.LAPIS_BLOCK)
    public abstract HashMap<Vector, Material> getSchema();

    public boolean detect(Block sign) {
        HashMap<Vector, Material> schema = getSchema();
        for (Vector vec : schema.keySet()) {
            if (schema.get(vec).equals(Material.LAPIS_BLOCK)) {
                Block block = sign;
                Directional dir = (Directional) sign.getBlockData();
                block = block.getRelative(dir.getFacing().getOppositeFace(), vec.getBlockX());
                block = block.getRelative(BlockFace.UP, vec.getBlockY());

                // have to do it like this cause there's no 90 degree rotate
                switch (dir.getFacing().getOppositeFace()) {
                    case NORTH:
                        block = block.getRelative(BlockFace.EAST, vec.getBlockZ());
                        break;
                    case EAST:
                        block = block.getRelative(BlockFace.SOUTH, vec.getBlockZ());
                        break;
                    case SOUTH:
                        block = block.getRelative(BlockFace.WEST, vec.getBlockZ());
                        break;
                    case WEST:
                        block = block.getRelative(BlockFace.NORTH, vec.getBlockZ());
                        break;
                }

                if (!block.getType().equals(schema.get(vec))) return false;
            }
        }
        return true;
    }

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

    public void tryOutput(ItemStack stack) {
        if (outputPipe == null) {
            ItemStack left = tryAddItemStack(stack);
            if (left.getAmount() != 0) sign.getWorld().dropItem(sign, left);
        } else {
            ItemStack left = outputPipe.getOutputMachine().tryAddItemStack(stack);
            if (left.getAmount() != 0) {
                left = tryAddItemStack(stack);
                if (left.getAmount() != 0) sign.getWorld().dropItem(sign, left);
            }
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
