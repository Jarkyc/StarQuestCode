package com.spacebeaverstudios.sqsmoothcraft.Tasks;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Data.SolidShipData;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipBlock;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipClass;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipLocation;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import org.bukkit.*;
import org.bukkit.block.Block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class DetectionTask {

    HashSet<ShipBlock> blocks = new HashSet<>();
    ShipBlock core;

    public DetectionTask(Location location, Player player, ShipClass clazz) {

        Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        Location originalVector = player.getLocation();
        World world = location.getWorld();

        Stack<Vector> jumpBlocks = new Stack<>();
        jumpBlocks.push(vector);

        ArrayList<ShipBlock> pistons = new ArrayList<>();
        ArrayList<ShipBlock> droppers = new ArrayList<>();

        ArrayList<Vector> analyed = new ArrayList<>();


        while (!jumpBlocks.empty()) {

            Vector block = jumpBlocks.pop();

            ArrayList<Vector> checkBlocks = new ArrayList<>();

            checkBlocks.add(new Vector(block.getX() + 1, block.getY(), block.getZ()));
            checkBlocks.add(new Vector(block.getX() + 1, block.getY() + 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX() + 1, block.getY(), block.getZ() + 1));
            checkBlocks.add(new Vector(block.getX() + 1, block.getY() - 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX() + 1, block.getY(), block.getZ() - 1));

            checkBlocks.add(new Vector(block.getX() - 1, block.getY(), block.getZ()));
            checkBlocks.add(new Vector(block.getX() - 1, block.getY() + 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX() - 1, block.getY(), block.getZ() + 1));
            checkBlocks.add(new Vector(block.getX() - 1, block.getY() - 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX() - 1, block.getY(), block.getZ() - 1));

            checkBlocks.add(new Vector(block.getX(), block.getY() + 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX(), block.getY() + 1, block.getZ() + 1));
            checkBlocks.add(new Vector(block.getX(), block.getY() + 1, block.getZ() - 1));

            checkBlocks.add(new Vector(block.getX(), block.getY() - 1, block.getZ()));
            checkBlocks.add(new Vector(block.getX(), block.getY() - 1, block.getZ() + 1));
            checkBlocks.add(new Vector(block.getX(), block.getY() - 1, block.getZ() - 1));

            checkBlocks.add(new Vector(block.getX(), block.getY(), block.getZ() + 1));
            checkBlocks.add(new Vector(block.getX(), block.getY(), block.getZ() - 1));

            for (Vector checkBlock : checkBlocks) {
                if (!analyed.contains(checkBlock)) {
                    Block b = world.getBlockAt(checkBlock.getBlockX(), checkBlock.getBlockY(), checkBlock.getBlockZ());

                    if (SQSmoothcraft.instance.shipBlocks.contains(b.getType()) || b.getType() == Material.NOTE_BLOCK) {
                        ShipLocation shipLoc = new ShipLocation(checkBlock.clone().getX() - location.clone().getX(), checkBlock.clone().getY() - location.clone().getY(), checkBlock.clone().getZ() - location.clone().getZ());


                        if (player.getFacing() == BlockFace.NORTH) {
                            shipLoc.x *= -1;
                            shipLoc.z *= -1;
                        } else if (player.getFacing() == BlockFace.EAST) {
                            double temp = shipLoc.x;
                            shipLoc.x = shipLoc.z * -1;
                            shipLoc.z = temp;
                        } else if (player.getFacing() == BlockFace.WEST) {
                            double temp = shipLoc.x;
                            shipLoc.x = shipLoc.z;
                            shipLoc.z = temp * -1;
                        }


                        double yOffset = 0;
                        BlockData data = b.getBlockData();
                        if (data instanceof Slab) {
                            Slab slab = (Slab) data;
                            if (slab.getType() == Slab.Type.TOP) {
                                yOffset = 0.5;
                            }
                        }

                        Location checkLoc = new Location(world, checkBlock.getX(), checkBlock.getY(), checkBlock.getZ());

                        Block neighbor1 = checkLoc.clone().add(1, 0, 0).getBlock();
                        Block neighbor2 = checkLoc.clone().add(0, 1, 0).getBlock();
                        Block neighbor3 = checkLoc.clone().add(0, 0, 1).getBlock();
                        Block neighbor4 = checkLoc.clone().subtract(1, 0, 0).getBlock();
                        Block neighbor5 = checkLoc.clone().subtract(0, 1, 0).getBlock();
                        Block neighbor6 = checkLoc.clone().subtract(0, 0, 1).getBlock();

                        Boolean visible = (neighbor1.getType() == Material.AIR || neighbor2.getType() == Material.AIR || neighbor3.getType() == Material.AIR || neighbor4.getType() == Material.AIR || neighbor5.getType() == Material.AIR || neighbor6.getType() == Material.AIR);


                        Inventory inv = null;
                        if (b.getState() instanceof Container) {
                            inv = ((Container) b.getState()).getSnapshotInventory();
                        }

                        ShipBlock shipBlock = new ShipBlock(shipLoc, new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()), b.getType(), yOffset, data, inv, visible);
                        blocks.add(shipBlock);

                        if (b.getType() == Material.PISTON) {
                            pistons.add(shipBlock);
                        }

                        if (b.getType() == Material.DROPPER) {
                            droppers.add(shipBlock);
                        }

                        if (b.getType() == Material.NOTE_BLOCK) {
                            this.core = shipBlock;
                        }
                        analyed.add(checkBlock);
                        jumpBlocks.push(checkBlock);

                    }
                }
            }

            if (blocks.size() > clazz.getMaxSize()) {
                player.sendMessage(ChatColor.RED + "The ship exceeds maximum block size for this class!");
                return;
            }

            if (droppers.size() > clazz.getDropperCount()) {
                player.sendMessage(ChatColor.RED + "Dropper count exceeds the maximum for this class!");
                return;
            }

        }

        if (blocks.size() < clazz.getMinSize()) {
            player.sendMessage(ChatColor.RED + "Ship does not meet minimum size requirements for this class!");
            return;
        }


        Ship ship = new Ship(this.blocks, player, location, this.core, originalVector, pistons, droppers, clazz);

        SolidShipData data = null;

        for (SolidShipData solid : SQSmoothcraft.instance.solidShips) {
            if (location.getBlockX() == solid.x && location.getBlockY() == solid.y && location.getBlockZ() == solid.z && location.getWorld().getName().equalsIgnoreCase(solid.world)) {
                data = solid;

                ship.modules = solid.modules;
            }
        }

        if (data != null) {
            SQSmoothcraft.instance.solidShips.remove(data);
        }

    }

    public HashSet<ShipBlock> getBlocks() {
        return blocks;
    }
}
