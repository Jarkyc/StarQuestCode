package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.MachineGUI;
import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.Pipe;
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
    private final ArrayList<Material> itemInputPipeMaterials = new ArrayList<>();
    private final ArrayList<Material> powerInputPipeMaterials = new ArrayList<>();
    private final ArrayList<Pipe> itemInputPipes = new ArrayList<>();
    private final ArrayList<Pipe> powerInputPipes = new ArrayList<>();
    private Material outputPipeMaterial;
    private Pipe itemOutputPipe;
    private Pipe powerOutputPipe;
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
    public ArrayList<Material> getItemInputPipeMaterials() {
        return itemInputPipeMaterials;
    }
    public ArrayList<Material> getPowerInputPipeMaterials() {
        return powerInputPipeMaterials;
    }
    public ArrayList<Pipe> getItemInputPipes() {
        return itemInputPipes;
    }
    public ArrayList<Pipe> getPowerInputPipes() {
        return powerInputPipes;
    }
    public Pipe getItemOutputPipe() {
        return itemOutputPipe;
    }
    public Pipe getPowerOutputPipe() {
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
    public void setItemOutputPipe(Pipe itemOutputPipe) {
        this.itemOutputPipe = itemOutputPipe;
    }
    public void setPowerOutputPipe(Pipe powerOutputPipe) {
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

            if (block.getType() != schema.get(vec)) {
                return false;
            }
            blocks.put(block.getLocation(), block.getType());
            if (block.getType() == Material.LAPIS_BLOCK) {
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
        if (itemInputPipeMaterials.contains(material) || powerInputPipeMaterials.contains(material)) {
            player.sendMessage(ChatColor.RED + material.toString() + " is already one of the input colors for this machine!");
            return;
        }

        if (material == outputPipeMaterial) {
            return;
        }
        outputPipeMaterial = material;

        if (itemOutputPipe != null) {
            itemOutputPipe.getItemInputMachines().remove(this);
            itemOutputPipe = null;
        } else if (powerOutputPipe != null) {
            powerOutputPipe.getItemInputMachines().remove(this);
            powerOutputPipe = null;
        }

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (glass.getType() == material) {
                if (getOutputType() == TransferType.ITEMS) {
                    if (Pipe.getPipesByBlock().containsKey(glass.getLocation())) {
                        itemOutputPipe = Pipe.getPipesByBlock().get(glass.getLocation());
                        itemOutputPipe.getItemInputMachines().add(this);
                    } else {
                        itemOutputPipe = new Pipe(glass.getLocation());
                    }
                } else {
                    if (Pipe.getPipesByBlock().containsKey(glass.getLocation())) {
                        powerOutputPipe = Pipe.getPipesByBlock().get(glass.getLocation());
                        powerOutputPipe.getPowerInputMachines().add(this);
                    } else {
                        powerOutputPipe = new Pipe(glass.getLocation());
                    }
                }
                return;
            }
        }
    }
    public void setItemInputPipeMaterials(ArrayList<Material> enabledColors, Player player) {
        if (!getInputTypes().contains(TransferType.ITEMS)) {
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Tried to add setItemInputPipeMaterials() to machine that doesn't accept item input!"
                    + " Sign location of accepting machine: "
                    + sign.getWorld().getName() + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
            return;
        }

        if (enabledColors.contains(outputPipeMaterial)) {
            player.sendMessage(ChatColor.RED + outputPipeMaterial.toString() + " is already the output color for this machine!");
            return;
        }
        for (Material color : enabledColors) {
            if (powerInputPipeMaterials.contains(color)) {
                player.sendMessage(ChatColor.RED + color.toString() + " is already one of the power input colors for this machine!");
                return;
            }
        }

        itemInputPipeMaterials.clear();
        itemInputPipeMaterials.addAll(enabledColors);
        for (Pipe pipe : itemInputPipes) {
            pipe.getItemOutputMachines().remove(this);
        }
        itemInputPipes.clear();

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (enabledColors.contains(glass.getType()) && Pipe.getPipesByBlock().containsKey(glass.getLocation())) {
                Pipe pipe = Pipe.getPipesByBlock().get(glass.getLocation());
                if (!itemInputPipes.contains(pipe)) {
                    itemInputPipes.add(pipe);
                    pipe.getItemOutputMachines().add(this);
                }
            }
        }
    }
    public void setPowerInputPipeMaterials(ArrayList<Material> enabledColors, Player player) {
        if (!getInputTypes().contains(TransferType.POWER)) {
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Tried to setPowerInputPipeMaterials() to machine that doesn't accept power input!"
                    + " Sign location of accepting machine: "
                    + sign.getWorld().getName() + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
            return;
        }

        if (enabledColors.contains(outputPipeMaterial)) {
            player.sendMessage(ChatColor.RED + outputPipeMaterial.toString() + " is already the output color for this machine!");
            return;
        }
        for (Material color : enabledColors) {
            if (itemInputPipeMaterials.contains(color)) {
                player.sendMessage(ChatColor.RED + color.toString() + " is already one of the item input colors for this machine!");
                return;
            }
        }

        powerInputPipeMaterials.clear();
        powerInputPipeMaterials.addAll(enabledColors);
        for (Pipe pipe : powerInputPipes) {
            pipe.getPowerOutputMachines().remove(this);
        }
        powerInputPipes.clear();

        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH)) {
            Block glass = node.getBlock().getRelative(face);
            if (enabledColors.contains(glass.getType()) && Pipe.getPipesByBlock().containsKey(glass.getLocation())) {
                Pipe pipe = Pipe.getPipesByBlock().get(glass.getLocation());
                if (!powerInputPipes.contains(pipe)) {
                    powerInputPipes.add(pipe);
                    pipe.getPowerOutputMachines().add(this);
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
            if (stack.getType() == itemStack.getType() && stack.getAmount() < stack.getMaxStackSize()) {
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
        ItemStack left;
        if (itemOutputPipe == null) {
            left = new ItemStack(stack.getType(), stack.getAmount());
        } else {
            left = itemOutputPipe.tryOutputItemStack(stack);
        }
        if (left.getAmount() != 0) {
            left = tryAddItemStack(stack);
            if (left.getAmount() != 0) sign.getWorld().dropItemNaturally(sign, left);
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
            itemOutputPipe.getItemInputMachines().remove(this);
        }
        if (powerOutputPipe != null) {
            powerOutputPipe.getPowerInputMachines().remove(this);
        }
        for (Pipe pipe : itemInputPipes) {
            pipe.getItemOutputMachines().remove(this);
        }
        for (Pipe pipe : powerInputPipes) {
            pipe.getPowerOutputMachines().remove(this);
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
            if (loc.getBlock().getType() != blocks.get(loc)) {
                destroy();
            }
        }
    }

    public boolean canUsePower(Integer amount) {
        for (Pipe pipe : powerInputPipes) {
            if (pipe.canUsePower(amount)) {
                return true;
            }
        }
        return false;
    }
    public boolean tryUsePower(Integer amount) {
        for (Pipe pipe : powerInputPipes) {
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
