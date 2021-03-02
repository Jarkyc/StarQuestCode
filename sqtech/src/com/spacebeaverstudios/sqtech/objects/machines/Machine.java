package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.MachineGUI;
import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.pipes.ItemPipe;
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

public abstract class Machine implements CanCheckIntact {
    // static
    private static final ArrayList<Machine> machines = new ArrayList<>();

    public static ArrayList<Machine> getMachines() {
        return machines;
    }

    public static boolean createFromSign(Block sign) {
        switch(((Sign) sign.getState()).getLine(0)) {
            case "[smelter]":
                new SmelterMachine(sign);
                return true;
            case "[hopper]":
                new HopperMachine(sign);
                return true;
            default:
                return false;
        }
    }

    // instance
    private final Location sign;
    private final ArrayList<ItemStack> inventory = new ArrayList<>();
    private GUI gui = null;
    private final String machineName;
    private final ArrayList<Material> inputPipeMaterials = new ArrayList<>();
    private final ArrayList<ItemPipe> inputPipes = new ArrayList<>();
    private Material outputPipeMaterial;
    private ItemPipe outputPipe;
    private Location node = null;
    private final HashMap<Location, Material> blocks = new HashMap<>();

    public Machine(Block sign, String machineName) {
        blocks.put(sign.getLocation(), sign.getType());
        if (detect(sign)) {
            this.sign = sign.getLocation();
            this.machineName = machineName;

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
    public ArrayList<ItemPipe> getInputPipes() {
        return inputPipes;
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
    public HashMap<Location, Material> getBlocks() {
        return blocks;
    }

    public void setOutputPipe(ItemPipe outputPipe) {
        this.outputPipe = outputPipe;
    }

    public void setOutputPipeMaterial(Material material, Player player) {
        if (inputPipeMaterials.contains(material)) {
            player.sendMessage(ChatColor.RED + material.toString() + " is already one of the input colors for this machine!");
            return;
        }

        if (material.equals(outputPipeMaterial)) return;
        outputPipeMaterial = material;
        if (outputPipe != null) {
            outputPipe.getInputMachines().remove(this);
            outputPipe = null;
        }

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (glass.getType().equals(material)) {
                for (ItemPipe pipe : ItemPipe.getAllPipes()) {
                    if (pipe.getBlocks().contains(glass.getLocation())) {
                        pipe.getInputMachines().add(this);
                        outputPipe = pipe;
                        return;
                    }
                }
                outputPipe = new ItemPipe(glass.getLocation()); // no pipes found matching it, so create a new one
                return;
            }
        }
    }
    public void setInputPipeMaterials(ArrayList<Material> enabledColors, Player player) {
        if (enabledColors.contains(outputPipeMaterial)) {
            player.sendMessage(ChatColor.RED + outputPipeMaterial.toString() + " is already the output color for this machine!");
            return;
        }

        inputPipeMaterials.clear();
        inputPipeMaterials.addAll(enabledColors);
        for (ItemPipe pipe : inputPipes) pipe.setOutputMachine(null);
        inputPipes.clear();

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (enabledColors.contains(glass.getType())) {
                for (ItemPipe pipe : ItemPipe.getAllPipes()) {
                    if (pipe.getBlocks().contains(glass.getLocation())) {
                        inputPipes.add(pipe);
                        pipe.setOutputMachine(this);
                        break;
                    }
                }
            }
        }
    }

    // schema is oriented so that positive x points to behind the sign, and positive z to the right
    // schemas MUST include a node block (Material.LAPIS_BLOCK)
    public abstract HashMap<Vector, Material> getSchema();

    public boolean detect(Block sign) {
        HashMap<Vector, Material> schema = getSchema();
        Directional dir = (Directional) sign.getBlockData();
        for (Vector vec : schema.keySet()) {
            if (schema.get(vec).equals(Material.LAPIS_BLOCK)) {
                Block block = sign;
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
                blocks.put(block.getLocation(), block.getType());
                if (block.getType().equals(Material.LAPIS_BLOCK)) node = block.getLocation();
            }
        }

        if (node == null) {
            // This SHOULDN'T happen. DON'T let it.
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Machine has no node block! Sign text: " + ((Sign) sign.getState()).getLine(0));
            for (Player player : sign.getLocation().getNearbyPlayers(5))
                player.sendMessage(ChatColor.RED + "An error occurred when attempting to create the machine. " +
                        "Staff have been notified!");
            return false;
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
        if (outputPipe == null || outputPipe.getOutputMachine() == null) {
            ItemStack left = tryAddItemStack(stack);
            if (left.getAmount() != 0) sign.getWorld().dropItem(sign, left);
        } else {
            ItemStack left = outputPipe.getOutputMachine().tryAddItemStack(stack);
            if (left.getAmount() != 0) {
                left = tryAddItemStack(stack);
                if (left.getAmount() != 0) sign.getWorld().dropItemNaturally(sign, left);
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

    public void destroy() {
        if (outputPipe != null) outputPipe.getInputMachines().remove(this);
        for (ItemPipe pipe : inputPipes) pipe.setOutputMachine(null);
        if (gui != null) {
            gui.getPlayer().sendMessage(ChatColor.RED + "The machine which you were accessing has been broken!");
            gui.getPlayer().closeInventory();
        }
        if (sign.getBlock().getType().toString().endsWith("_SIGN")) {
            Sign signBlock = (Sign) sign.getWorld().getBlockAt(this.getSign()).getState();
            signBlock.setLine(2, ChatColor.RED + "Broken");
            signBlock.update();
        }
        for (ItemStack itemStack : inventory) sign.getWorld().dropItemNaturally(sign, itemStack);
        machines.remove(this);
    }

    public void checkIntact() {
        for (Location loc : blocks.keySet())
            if (!loc.getBlock().getType().equals(blocks.get(loc)))
                destroy();
    }
}
