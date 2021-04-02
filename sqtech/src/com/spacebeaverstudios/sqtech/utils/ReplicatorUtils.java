package com.spacebeaverstudios.sqtech.utils;

import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ReplicatorUtils {
    private static final HashMap<String, String> blocksToItems = new HashMap<>();
    private static final ArrayList<String> halveCost = new ArrayList<>();
    private static final ArrayList<String> ignore = new ArrayList<>();

    public static void initializeConfig() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("replicator");
        for (String key : configSection.getConfigurationSection("blockstoitems").getKeys(false)) {
            blocksToItems.put(key, configSection.getString("blockstoitems." + key));
        }
        halveCost.addAll(configSection.getStringList("halvecost"));
        ignore.addAll(configSection.getStringList("ignore"));
    }

    public static HashMap<Material, Integer> getBlocksToReplicate(ReplicatorMachine machine) {
        HashMap<Material, Integer> blocksToReplicate = new HashMap<>();

        // add all blocks to hashmap
        Location scanToPointOne = machine.getCopyToBox().getSortedPointOne();
        Location scanToPointTwo = machine.getCopyToBox().getSortedPointTwo();
        int relativeX = 0, relativeY = 0, relativeZ = 0;
        while (relativeY <= scanToPointTwo.getBlockY() - scanToPointOne.getBlockY()) {
            while (relativeX <= scanToPointTwo.getBlockX() - scanToPointOne.getBlockX()) {
                while (relativeZ <= scanToPointTwo.getBlockZ() - scanToPointOne.getBlockZ()) {
                    Block block = scanToPointOne.clone().add(relativeX, relativeY, relativeZ).getBlock();
                    if (!ignore.contains(block.getType().toString())) {
                        if (block.getBlockData() instanceof Slab && ((Slab) block.getBlockData()).getType() == Slab.Type.DOUBLE) {
                            blocksToReplicate.put(block.getType(), blocksToReplicate.getOrDefault(block.getType(), 0) + 2);
                        } else {
                            blocksToReplicate.put(block.getType(), blocksToReplicate.getOrDefault(block.getType(), 0) + 1);
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

        // convert all blocks to their item counterparts
        ArrayList<Material> keys = new ArrayList<>(blocksToReplicate.keySet());
        for (Material key : keys) {
            if (blocksToItems.containsKey(key.toString())) {
                blocksToReplicate.put(Material.getMaterial(blocksToItems.get(key.toString())), blocksToReplicate.get(key)
                        + blocksToReplicate.getOrDefault(Material.getMaterial(blocksToItems.get(key.toString())), 0));
                blocksToReplicate.remove(key);
            }
        }

        // apply halveCost
        for (Material key : blocksToReplicate.keySet()) {
            if (halveCost.contains(key.toString())) {
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
        // TODO

        Location sign = machine.getSign();
        sign.getWorld().playSound(sign, Sound.BLOCK_PISTON_EXTEND, 2f, 2f);
        SQTech.getInstance().getLogger().info("Replicated ship from sign at " + sign.getWorld().getName()
                + ", " + sign.getBlockX() + ", " + sign.getBlockY() + ", " + sign.getBlockZ());
    }
}
