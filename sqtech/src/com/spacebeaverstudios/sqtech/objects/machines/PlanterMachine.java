package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.utils.RotationUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlanterMachine extends Machine {
    // static
    private static final HashMap<Material, Material> itemsToBlocks = new HashMap<>();
    private static final HashMap<Material, ArrayList<Material>> canPlant = new HashMap<>();
    private static final ArrayList<Material> dontHarvest = new ArrayList<>();
    private static final ArrayList<Material> extraHarvest = new ArrayList<>();

    public static void initializePlants() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("planter");
        for (String key : configSection.getConfigurationSection("items-to-blocks").getKeys(false)) {
            itemsToBlocks.put(Material.getMaterial(key), Material.getMaterial(configSection.getString("items-to-blocks." + key)));
        }
        for (String key : configSection.getConfigurationSection("can-plant").getKeys(false)) {
            ArrayList<Material> list = new ArrayList<>();
            for (String option : configSection.getStringList("can-plant." + key)) {
                list.add(Material.getMaterial(option));
            }
            canPlant.put(Material.getMaterial(key), list);
        }
        for (String material : configSection.getStringList("dont-harvest")) {
            dontHarvest.add(Material.getMaterial(material));
        }
        for (String material : configSection.getStringList("extra-harvest")) {
            extraHarvest.add(Material.getMaterial(material));
        }
    }

    public static void sweepersToDefaultPosition() {
        for (Machine machine : getMachines()) {
            if (machine instanceof PlanterMachine) {
                ((PlanterMachine) machine).sweeperToDefaultPosition();
            }
        }
    }

    // instance
    private ArrayList<Location> movingBlocks;
    private int direction = 1;
    private boolean movesX;
    private int currMove;
    private int originalMove;
    private int startMove;
    private int endMove;
    private int startWidth;
    private int endWidth;
    private Material fenceType;

    public PlanterMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        // To make things easier, only these count as the "core". Everything else is calculated in PlanterMachine#init
        ArrayList<HashMap<Vector, Material>> schemas = new ArrayList<>();
        for (Material fence : Arrays.asList(Material.OAK_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE,
                Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.SPRUCE_FENCE, Material.NETHER_BRICK_FENCE)) {
            HashMap<Vector, Material> schema = new HashMap<>();
            schema.put(new Vector(1, 0, 0), Material.LAPIS_BLOCK);
            schema.put(new Vector(1, 1, 0), fence);
            schema.put(new Vector(2, 1, 0), fence);
            schema.put(new Vector(1, 1, -1), fence);
            schemas.add(schema);
        }
        return schemas;
    }

    public void init() {
        // this has to be defined here because https://stackoverflow.com/a/14806340/9070242
        movingBlocks = new ArrayList<>();

        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Planter");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();

        // calculate values
        BlockFace dir = ((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace();
        movesX = (dir == BlockFace.NORTH || dir == BlockFace.SOUTH);
        Block currBlock = getSign().getBlock().getRelative(dir, 2).getRelative(BlockFace.UP);

        // calculate width
        startWidth = (movesX ? currBlock.getLocation().getBlockZ() : currBlock.getLocation().getBlockX());
        fenceType = currBlock.getType();
        while (currBlock.getType() == fenceType && (getMachinesByBlock().get(currBlock.getLocation()) == this
                || getMachinesByBlock().get(currBlock.getLocation()) == null) && endWidth - startWidth < 10) {
            endWidth = (movesX ? currBlock.getLocation().getBlockZ() : currBlock.getLocation().getBlockX());
            movingBlocks.add(currBlock.getLocation());
            if (!getMachinesByBlock().containsKey(currBlock.getLocation())) {
                getBlocks().put(currBlock.getLocation(), fenceType);
                getMachinesByBlock().put(currBlock.getLocation(), this);
            }
            currBlock = currBlock.getRelative(dir);
        }
        if (startWidth > endWidth) {
            int temp = endWidth;
            endWidth = startWidth;
            startWidth = temp;
        }

        // calculate span of movement
        BlockFace rotated = RotationUtils.rotateLeft(dir);
        currBlock = getSign().getBlock().getRelative(dir).getRelative(BlockFace.UP);
        startMove = (movesX ? currBlock.getLocation().getBlockX() : currBlock.getLocation().getBlockZ());
        while (currBlock.getType() == fenceType && (getMachinesByBlock().get(currBlock.getLocation()) == this
                || getMachinesByBlock().get(currBlock.getLocation()) == null) && endMove - startMove < 10) {
            endMove = (movesX ? currBlock.getLocation().getBlockX() : currBlock.getLocation().getBlockZ());
            if (!getMachinesByBlock().containsKey(currBlock.getLocation())) {
                getBlocks().put(currBlock.getLocation(), fenceType);
                getMachinesByBlock().put(currBlock.getLocation(), this);
            }
            currBlock = currBlock.getRelative(rotated);
        }
        currMove = startMove;
        originalMove = startMove;
        if (startMove > endMove) {
            int temp = endMove;
            endMove = startMove;
            startMove = temp;
        }

        if (endMove - startMove == 1 || endWidth - startWidth == 1) {
            destroy();
            sign.setLine(1, ChatColor.RED + "Too Small");
            sign.update();
        }
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        if (getAvailablePower() == 0) {
            sign.setLine(1, ChatColor.RED + "No Power");
        } else {
            sign.setLine(1, ChatColor.GREEN + "Active");
            // swap if the sweeper is at the end of the track
            if (currMove == startMove) {
                direction = 1;
            } else if (currMove == endMove) {
                direction = -1;
            }
            // check if can move
            boolean canMove = true;
            int sugarCanes = 0;
            for (Location location : movingBlocks) {
                Block block = location.clone().add((movesX ? direction : 0), 0, (movesX ? 0 : direction)).getBlock();
                if (block.getType() != Material.AIR && block.getType() != Material.SUGAR_CANE) {
                    canMove = false;
                    break;
                }
                while (block.getType() == Material.SUGAR_CANE) {
                    sugarCanes++;
                    block.setType(Material.AIR);
                    block = block.getRelative(BlockFace.UP);
                }
            }
            if (sugarCanes * 20 > getAvailablePower()) {
                canMove = false;
            }
            // if can't move, try to turn around
            if (!canMove) {
                if (currMove != startMove && currMove != endMove) {
                    // turn around, will move in that direction next tick because I'm lazy
                    direction = -1 * direction;
                } else {
                    sign.setLine(1, ChatColor.RED + "Can't Move");
                }
                sign.update();
                return;
            }
            // move
            tryUsePower(sugarCanes * 20);
            tryOutput(new ItemStack(Material.SUGAR_CANE, sugarCanes));
            currMove += direction;
            for (Location location : movingBlocks) {
                getBlocks().remove(location);
                getMachinesByBlock().remove(location);
                location.getBlock().setType(Material.AIR);
                if (movesX) {
                    location.setX(currMove);
                } else {
                    location.setZ(currMove);
                }
                getBlocks().put(location, fenceType);
                getMachinesByBlock().put(location, this);
                location.getBlock().setType(fenceType);
            }
            // harvest/plant
            for (Location location : movingBlocks) {
                Block below = location.getBlock().getRelative(BlockFace.DOWN);
                if (itemsToBlocks.containsValue(below.getType()) && !dontHarvest.contains(below.getType())) {
                    Ageable ageable = (Ageable) below.getBlockData();
                    if (ageable.getAge() == ageable.getMaximumAge() && tryUsePower(20)) {
                        if (below.getType() == Material.SWEET_BERRY_BUSH) {
                            tryOutput(new ItemStack(Material.SWEET_BERRIES, 1));
                            ageable.setAge(1); // because it cycles between ages 1-3 when producing berries
                            below.setBlockData(ageable);
                        } else {
                            for (ItemStack itemStack : below.getDrops()) {
                                tryOutput(itemStack);
                            }
                            below.setType(Material.AIR);
                        }
                    }
                } else if (extraHarvest.contains(below.getType()) && tryUsePower(20)) {
                    for (ItemStack itemStack : below.getDrops()) {
                        tryOutput(itemStack);
                    }
                    below.setType(Material.AIR);
                } else if (below.getType() == Material.AIR) {
                    ItemStack toRemove = null;
                    for (ItemStack itemStack : getInventory()) {
                        if (itemsToBlocks.containsKey(itemStack.getType())
                                && canPlant.get(itemStack.getType()).contains(below.getRelative(BlockFace.DOWN).getType())
                                && getAvailablePower() >= 20) {
                            if (itemStack.getType() == Material.SUGAR_CANE) {
                                boolean canPlace = false;
                                for (BlockFace face : Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)) {
                                    if (below.getRelative(BlockFace.DOWN).getRelative(face).getType() == Material.WATER) {
                                        canPlace = true;
                                        break;
                                    }
                                }
                                if (!canPlace) {
                                    continue;
                                }
                            }

                            tryUsePower(20);
                            below.setType(itemsToBlocks.get(itemStack.getType()));
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            if (itemStack.getAmount() == 0) {
                                toRemove = itemStack;
                            }
                            break;
                        }
                    }
                    if (toRemove != null) {
                        getInventory().remove(toRemove);
                    }
                }
            }
        }
        sign.update();
    }

    private void sweeperToDefaultPosition() {
        for (Location location : movingBlocks) {
            getBlocks().remove(location);
            getMachinesByBlock().remove(location);
            location.getBlock().setType(Material.AIR);
            if (movesX) {
                location.setX(originalMove);
            } else {
                location.setZ(originalMove);
            }
            getBlocks().put(location, fenceType);
            getMachinesByBlock().put(location, this);
            location.getBlock().setType(fenceType);
        }
    }

    public List<TransferType> getInputTypes() {
        return new ArrayList<>(Arrays.asList(TransferType.POWER, TransferType.ITEMS));
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Planter";
    }
    public String getMachineInfo() {
        return "Automatically plants and harvests crops.\n " + ChatColor.GOLD + "Cost per Crop: " + ChatColor.AQUA + "20 BV";
    }

    public String getSignText() {
        return "[planter]";
    }
}
