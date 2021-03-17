package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.MachineGUI;
import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.pipes.ItemPipe;
import com.spacebeaverstudios.sqtech.objects.pipes.PowerPipe;
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
import java.util.List;

public abstract class Machine implements CanCheckIntact {
    public enum TransferType {
        ITEMS,
        POWER
    }

    // static
    private static final ArrayList<Machine> machines = new ArrayList<>();
    private static final HashMap<Location, Machine> machinesByBlock = new HashMap<>();

    public static ArrayList<Machine> getMachines() {
        return machines;
    }
    public static HashMap<Location, Machine> getMachinesByBlock() {
        return machinesByBlock;
    }

    public static boolean createFromSign(Block sign) {
        switch(((Sign) sign.getState()).getLine(0).toLowerCase()) {
            case "[smelter]":
                new SmelterMachine(sign);
                return true;
            case "[hopper]":
                new HopperMachine(sign);
                return true;
            case "[coal generator]":
            case "[coalgenerator]":
                new CoalGeneratorMachine(sign);
                return true;
            case "[solar panel]":
            case "[solarpanel]":
                new SolarPanelMachine(sign);
                return true;
            case "[battery]":
                new BatteryMachine(sign);
                return true;
            case "[crafter]":
            case "[auto crafter]":
            case "[autocrafter]":
                new CrafterMachine(sign);
                return true;
            default:
                return false;
        }
    }

    // instance
    private final Location sign;
    private final ArrayList<ItemStack> inventory = new ArrayList<>();
    private Player guiPlayer = null;
    private final String machineName;
    private final String machineInfo;
    private final ArrayList<Material> inputPipeMaterials = new ArrayList<>();
    private final ArrayList<ItemPipe> itemInputPipes = new ArrayList<>();
    private final ArrayList<PowerPipe> powerInputPipes = new ArrayList<>();
    private Material outputPipeMaterial;
    private ItemPipe itemOutputPipe;
    private PowerPipe powerOutputPipe;
    private Location node = null;
    private final HashMap<Location, Material> blocks = new HashMap<>();

    public Machine(Block sign, String machineName, String machineInfo) {
        blocks.put(sign.getLocation(), sign.getType());
        this.sign = sign.getLocation();
        this.machineName = machineName;
        this.machineInfo = machineInfo;
        if (detect(sign)) {
            machines.add(this);
            for (Location loc : blocks.keySet()) {
                machinesByBlock.put(loc, this);
            }
            init();
        }
    }

    public Location getSign() {
        return sign;
    }
    public String getMachineName() {
        return machineName;
    }
    public String getMachineInfo() {
        return machineInfo;
    }
    public ArrayList<ItemStack> getInventory() {
        return inventory;
    }
    public Player getGUIPlayer() {
        return guiPlayer;
    }
    public ArrayList<Material> getInputPipeMaterials() {
        return inputPipeMaterials;
    }
    public ArrayList<ItemPipe> getItemInputPipes() {
        return itemInputPipes;
    }
    public ArrayList<PowerPipe> getPowerInputPipes() {
        return powerInputPipes;
    }
    public ItemPipe getItemOutputPipe() {
        return itemOutputPipe;
    }
    public PowerPipe getPowerOutputPipe() {
        return powerOutputPipe;
    }
    public Material getOutputPipeMaterial() {
        return outputPipeMaterial;
    }
    public Location getNode() {
        return node;
    }

    public void setGUIPlayer(Player guiPlayer) {
        this.guiPlayer = guiPlayer;
    }
    public void setItemOutputPipe(ItemPipe itemOutputPipe) {
        this.itemOutputPipe = itemOutputPipe;
    }
    public void setPowerOutputPipe(PowerPipe powerOutputPipe) {
        this.powerOutputPipe = powerOutputPipe;
    }

    // schema is oriented so that positive x points to behind the sign, and positive z to the right
    // schemas MUST include a single node block (Material.LAPIS_BLOCK)
    public abstract HashMap<Vector, Material> getSchema();

    public boolean detect(Block sign) {
        HashMap<Vector, Material> schema = getSchema();
        Directional dir = (Directional) sign.getBlockData();
        for (Vector vec : schema.keySet()) {
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

            if (!block.getType().equals(schema.get(vec))) {
                return false;
            }
            blocks.put(block.getLocation(), block.getType());
            if (block.getType().equals(Material.LAPIS_BLOCK)) {
                node = block.getLocation();
                if (machinesByBlock.containsKey(node)) {
                    if (machinesByBlock.get(node) != this && machinesByBlock.get(node).getNode().equals(node)) {
                        return false;
                    }
                }
            }
        }

        if (node == null) {
            // This SHOULDN'T happen. DON'T let it.
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Machine has no node block! Sign text: " + ((Sign) sign.getState()).getLine(0));
            for (Player player : sign.getLocation().getNearbyPlayers(5)) {
                player.sendMessage(ChatColor.RED + "An error occurred when attempting to create the machine. " +
                        "Staff have been notified!");
            }
            return false;
        }

        return true;
    }

    public abstract void init();
    public abstract void tick(); // run all the functions that occur once a second

    public void setOutputPipeMaterial(Material material, Player player) {
        if (inputPipeMaterials.contains(material)) {
            player.sendMessage(ChatColor.RED + material.toString() + " is already one of the input colors for this machine!");
            return;
        }

        if (material.equals(outputPipeMaterial)) {
            return;
        }
        outputPipeMaterial = material;

        if (itemOutputPipe != null) {
            itemOutputPipe.getInputMachines().remove(this);
            itemOutputPipe = null;
        } else if (powerOutputPipe != null) {
            powerOutputPipe.getInputMachines().remove(this);
            powerOutputPipe = null;
        }

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (glass.getType().equals(material)) {
                if (getOutputType().equals(TransferType.ITEMS)) {
                    if (ItemPipe.getPipesByBlock().containsKey(glass.getLocation())) {
                        ItemPipe pipe = ItemPipe.getPipesByBlock().get(glass.getLocation());
                        pipe.getInputMachines().add(this);
                        itemOutputPipe = pipe;
                    } else {
                        itemOutputPipe = new ItemPipe(glass.getLocation()); // no pipes found matching it, so create a new one
                        itemOutputPipe.getInputMachines().add(this);
                    }
                } else {
                    if (PowerPipe.getPipesByBlock().containsKey(glass.getLocation())) {
                        PowerPipe pipe = PowerPipe.getPipesByBlock().get(glass.getLocation());
                        pipe.getInputMachines().add(this);
                        powerOutputPipe = pipe;
                    } else {
                        powerOutputPipe = new PowerPipe(glass.getLocation()); // no pipes found matching it, so create a new one
                        powerOutputPipe.getInputMachines().add(this);
                    }
                }
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
        for (ItemPipe pipe : itemInputPipes) {
            pipe.setOutputMachine(null);
        }
        itemInputPipes.clear();
        for (PowerPipe pipe : powerInputPipes) {
            pipe.getOutputMachines().remove(this);
        }
        powerInputPipes.clear();

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (enabledColors.contains(glass.getType())) {
                if (getInputTypes().contains(TransferType.ITEMS) && ItemPipe.getPipesByBlock().containsKey(glass.getLocation())) {
                    ItemPipe pipe = ItemPipe.getPipesByBlock().get(glass.getLocation());
                    if (!itemInputPipes.contains(pipe)) {
                        itemInputPipes.add(pipe);
                        pipe.setOutputMachine(this);
                    }
                }
                if (getInputTypes().contains(TransferType.POWER) && PowerPipe.getPipesByBlock().containsKey(glass.getLocation())) {
                    PowerPipe pipe = PowerPipe.getPipesByBlock().get(glass.getLocation());
                    if (!powerInputPipes.contains(pipe)) {
                        powerInputPipes.add(pipe);
                        pipe.getOutputMachines().add(this);
                    }
                }
            }
        }
    }

    public ItemStack tryAddItemStack(ItemStack itemStack) {
        // returns whatever items weren't able to be added
        if (!getInputTypes().contains(TransferType.ITEMS)) {
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Tried to add items to machine that doesn't accept item input! Sign location of accepting machine: "
                    + sign.getWorld().getName() + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
            return new ItemStack(itemStack.getType(), itemStack.getAmount());
        }

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
        } else {
            return itemStack;
        }
    }
    public ItemStack takeItem(int slot, int amount) {
        if (inventory.size() <= slot) {
            return null;
        } else if (inventory.get(slot).getAmount() <= amount) {
            ItemStack itemStack = inventory.get(slot);
            inventory.remove(slot);
            return itemStack;
        } else {
            inventory.get(slot).setAmount(inventory.get(slot).getAmount() - amount);
            return new ItemStack(inventory.get(slot).getType(), amount);
        }
    }

    public void tryOutput(ItemStack stack) {
        if (itemOutputPipe == null || itemOutputPipe.getOutputMachine() == null) {
            ItemStack left = tryAddItemStack(stack);
            if (left.getAmount() != 0) {
                sign.getWorld().dropItem(sign, left);
            }
        } else {
            ItemStack left = itemOutputPipe.getOutputMachine().tryAddItemStack(stack);
            if (left.getAmount() != 0) {
                left = tryAddItemStack(stack);
                if (left.getAmount() != 0) sign.getWorld().dropItemNaturally(sign, left);
            }
        }
    }

    public void openGUI(Player player) {
        if (guiPlayer == null) {
            guiPlayer = player;
            (new MachineGUI(this)).open(player);
        } else {
            player.sendMessage(ChatColor.RED + "Someone is already accessing that machine!");
        }
    }

    public void destroy() {
        // clear pipes
        if (itemOutputPipe != null) {
            itemOutputPipe.getInputMachines().remove(this);
        }
        if (powerOutputPipe != null) {
            powerOutputPipe.getInputMachines().remove(this);
        }
        for (ItemPipe pipe : itemInputPipes) {
            pipe.setOutputMachine(null);
        }
        for (PowerPipe pipe : powerInputPipes) {
            pipe.getOutputMachines().remove(this);
        }

        if (guiPlayer != null) {
            guiPlayer.sendMessage(ChatColor.RED + "The machine which you were accessing has been broken!");
            guiPlayer.closeInventory();
        }
        if (sign.getBlock().getType().toString().endsWith("_SIGN")) {
            Sign signBlock = (Sign) sign.getWorld().getBlockAt(this.getSign()).getState();
            signBlock.setLine(2, ChatColor.RED + "Broken");
            signBlock.update();
        }
        for (ItemStack itemStack : inventory) {
            sign.getWorld().dropItemNaturally(sign, itemStack);
        }

        // remove this machine from the lists
        for (Location loc : blocks.keySet()) {
            machinesByBlock.remove(loc);
        }
        machines.remove(this);
    }

    public void checkIntact() {
        for (Location loc : blocks.keySet()) {
            if (!loc.getBlock().getType().equals(blocks.get(loc))) {
                destroy();
            }
        }
    }

    public boolean canUsePower(Integer amount) {
        for (PowerPipe pipe : powerInputPipes) {
            if (pipe.canUsePower(amount)) {
                return true;
            }
        }
        return false;
    }
    public boolean tryUsePower(Integer amount) {
        for (PowerPipe pipe : powerInputPipes) {
            if (pipe.tryUsePower(amount)) {
                return true;
            }
        }
        return false;
    }

    public abstract List<TransferType> getInputTypes();
    public abstract TransferType getOutputType();

    public GUIItem getCustomOptionsGUIItem() {
        return null;
    }
}
