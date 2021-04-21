package com.spacebeaverstudios.sqtech.utils;

import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ReplicatorUtils {
    private static int POWER_COST;
    private static final HashMap<Material, Material> blocksToItems = new HashMap<>();
    private static final ArrayList<Material> halveCost = new ArrayList<>();
    private static final ArrayList<Material> ignore = new ArrayList<>();

    public static void initializeConfig(ConfigurationSection configSection) {
        POWER_COST = configSection.getInt("power-cost");
        for (String key : configSection.getConfigurationSection("blocks-to-items").getKeys(false)) {
            blocksToItems.put(Material.getMaterial(key.toUpperCase()),
                    Material.getMaterial(configSection.getString("blocks-to-items." + key).toUpperCase()));
        }
        for (String item : configSection.getStringList("halve-cost")) {
            halveCost.add(Material.getMaterial(item.toUpperCase()));
        }
        for (String item : configSection.getStringList("ignore")) {
            ignore.add(Material.getMaterial(item.toUpperCase()));
        }
    }

    public static HashMap<Material, Integer> getBlocksToReplicate(ReplicatorMachine machine) {
        HashMap<Material, Integer> blocksToReplicate = new HashMap<>();

        // add all blocks to hashmap
        Location scanFromPointOne = machine.getCopyFromBox().getSortedPointOne();
        Location scanFromPointTwo = machine.getCopyFromBox().getSortedPointTwo();
        int relativeX = 0, relativeY = 0, relativeZ = 0;

        while (relativeY <= scanFromPointTwo.getBlockY() - scanFromPointOne.getBlockY()) {
            while (relativeX <= scanFromPointTwo.getBlockX() - scanFromPointOne.getBlockX()) {
                while (relativeZ <= scanFromPointTwo.getBlockZ() - scanFromPointOne.getBlockZ()) {
                    Block block = scanFromPointOne.clone().add(relativeX, relativeY, relativeZ).getBlock();
                    Material item = blocksToItems.getOrDefault(block.getType(), block.getType());

                    if (!ignore.contains(item)) {
                        if (block.getBlockData() instanceof Slab && ((Slab) block.getBlockData()).getType() == Slab.Type.DOUBLE) {
                            blocksToReplicate.put(item, blocksToReplicate.getOrDefault(item, 0) + 2);
                        } else {
                            blocksToReplicate.put(item, blocksToReplicate.getOrDefault(item, 0) + 1);
                        }
                    }
                    relativeZ++;
                }
                relativeZ = 0;
                relativeX++;
            }
            relativeX = 0;
            relativeY++;
        }

        // apply halveCost
        for (Material key : blocksToReplicate.keySet()) {
            if (halveCost.contains(key)) {
                // fancy math to halve and round up
                blocksToReplicate.put(key, (blocksToReplicate.get(key) + 1) / 2);
            }
        }

        // log this just in case it's causing lag
        Location sign = machine.getSign();
        SQTech.getInstance().getLogger().info("called ReplicatorUtils#getBlocksToReplicate() from sign at "
                + sign.getWorld().getName() + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
        return blocksToReplicate;
    }

    public static void replicate(ReplicatorMachine machine, Player player) {
        Location scanFromPointOne = machine.getCopyFromBox().getSortedPointOne();
        Location scanFromPointTwo = machine.getCopyFromBox().getSortedPointTwo();
        Location scanToPointOne = machine.getCopyToBox().getSortedPointOne();
        ArrayList<Material> paidForDouble = new ArrayList<>();
        ArrayList<Inventory> inventories = machine.getChestInventories();
        int relativeX = 0, relativeY = 0, relativeZ = 0;

        outerloop:
        while (relativeY <= scanFromPointTwo.getBlockY() - scanFromPointOne.getBlockY()) {
            while (relativeX <= scanFromPointTwo.getBlockX() - scanFromPointOne.getBlockX()) {
                while (relativeZ <= scanFromPointTwo.getBlockZ() - scanFromPointOne.getBlockZ()) {
                    Block block = scanFromPointOne.clone().add(relativeX, relativeY, relativeZ).getBlock();
                    Block newBlock = scanToPointOne.clone().add(relativeX, relativeY, relativeZ).getBlock();

                    if (!ignore.contains(block.getType()) && newBlock.getType() == Material.AIR) {
                        boolean paid = false;

                        // try to deduct power
                        if (!machine.tryUsePower(5)) {
                            player.sendMessage(ChatColor.RED + "Your replicator has run out of power!");
                            break outerloop;
                        }

                        // determine if enough in chest
                        Material item = blocksToItems.getOrDefault(block.getType(), block.getType());
                        if (paidForDouble.contains(block.getType())) {
                            paidForDouble.remove(block.getType());
                            paid = true;
                        } else {
                            boolean isSlab = block.getBlockData() instanceof Slab
                                    && ((Slab) block.getBlockData()).getType() == Slab.Type.DOUBLE;

                            for (Inventory inventory : inventories) {
                                int first = inventory.first(item);
                                if (first != -1) {
                                    ItemStack itemStack = inventory.getItem(first);
                                    itemStack.setAmount(itemStack.getAmount() - 1);

                                    // annoyingly complicated script for deducting the proper number of items
                                    if (isSlab) {
                                        if (itemStack.getAmount() == 0) {
                                            inventory.setItem(first, null);
                                            first = inventory.first(item);
                                            if (first == -1) {
                                                itemStack.setAmount(1);
                                                inventory.addItem(itemStack);
                                            } else {
                                                inventory.getItem(first).setAmount(itemStack.getAmount() - 1);
                                                paid = true;
                                            }
                                        } else {
                                            itemStack.setAmount(itemStack.getAmount() - 1);
                                            paid = true;
                                        }
                                    } else {
                                        if (halveCost.contains(item)) {
                                            paidForDouble.add(item);
                                        }
                                        paid = true;
                                    }
                                }
                            }
                        }

                        // copy the block
                        if (paid) {
                            BlockData newBlockData = block.getBlockData().clone();
                            // prevent exploits
                            if (newBlockData instanceof Ageable) {
                                ((Ageable) newBlockData).setAge(0);
                            }
                            newBlock.setBlockData(newBlockData, false);

                            // copy sign text
                            if (block.getState() instanceof Sign) {
                                // manual copy so I don't accidentally copy over other block state things
                                Sign sign = (Sign) block.getState();
                                Sign newSign = (Sign) newBlock.getState();
                                for (int i = 0; i < 4; i++) {
                                    newSign.setLine(i, sign.getLine(i));
                                }
                                newSign.update();
                            }
                        }
                    }
                    relativeZ++;
                }
                relativeZ = 0;
                relativeX++;
            }
            relativeX = 0;
            relativeY++;
        }

        Location sign = machine.getSign();
        sign.getWorld().playSound(sign, Sound.BLOCK_PISTON_EXTEND, 2f, 2f);
        SQTech.getInstance().getLogger().info("Replicated ship from sign at " + sign.getWorld().getName()
                + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
    }
}
