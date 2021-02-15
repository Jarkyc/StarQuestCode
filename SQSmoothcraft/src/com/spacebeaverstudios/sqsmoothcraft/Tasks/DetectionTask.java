package com.spacebeaverstudios.sqsmoothcraft.Tasks;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Data.SolidShipData;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipBlock;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipLocation;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import org.bukkit.*;
import org.bukkit.block.Block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class DetectionTask {

    HashSet<ShipBlock> blocks = new HashSet<>();
    ShipBlock core;

    public DetectionTask(Location location, Player player){

        Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        Location originalVector = player.getLocation();
        World world = location.getWorld();

        Stack<Vector> jumpBlocks = new Stack<>();
        jumpBlocks.push(vector);

        ArrayList<ShipBlock> pistons = new ArrayList<>();

        ArrayList<Vector> analyed = new ArrayList<>();

        while(!jumpBlocks.empty()){

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

            for(Vector checkBlock : checkBlocks){
                if(!analyed.contains(checkBlock)){
                    Block b = world.getBlockAt(checkBlock.getBlockX(), checkBlock.getBlockY(), checkBlock.getBlockZ());

                    if(SQSmoothcraft.instance.shipBlocks.contains(b.getType()) || b.getType() == Material.NOTE_BLOCK){
                        ShipLocation shipLoc = new ShipLocation(checkBlock.clone().getX() - location.clone().getX(), checkBlock.clone().getY() - location.clone().getY(), checkBlock.clone().getZ() - location.clone().getZ());


                        if(player.getFacing() == BlockFace.NORTH){
                            shipLoc.x *= -1;
                            shipLoc.z *= -1;
                        } else if(player.getFacing() == BlockFace.EAST){
                            double temp = shipLoc.x;
                            shipLoc.x = shipLoc.z * -1;
                            shipLoc.z = temp;
                        } else if(player.getFacing() == BlockFace.WEST){
                            double temp = shipLoc.x;
                            shipLoc.x = shipLoc.z;
                            shipLoc.z = temp * -1;
                        }


                        double yOffset = 0;
                        BlockData data = b.getBlockData();
                        if(data instanceof Slab){
                            Slab slab = (Slab) data;
                            if(slab.getType() == Slab.Type.TOP){
                                yOffset = 0.5;
                            }
                        }

                        ShipBlock shipBlock = new ShipBlock(shipLoc, new Location(b.getWorld(), checkBlock.getX(), checkBlock.getY(), checkBlock.getZ()), b.getType(), yOffset, data);
                        blocks.add(shipBlock);

                        if(b.getType() == Material.PISTON){
                            pistons.add(shipBlock);
                        }

                        if(b.getType() == Material.NOTE_BLOCK){
                            this.core = shipBlock;
                        }
                        analyed.add(checkBlock);
                        jumpBlocks.push(checkBlock);

                    }
                }
            }

        }

        // 25 blocks counting the core
        if(blocks.size() < 26){
            player.sendMessage(ChatColor.RED + "This ship does not meet the minimum size requirements!");
            return;
        }

        Ship ship = new Ship(this.blocks, player, location, this.core, originalVector, pistons);

        SolidShipData data = null;

        for(SolidShipData solid : SQSmoothcraft.instance.solidShips){
            if(location.getBlockX() == solid.x && location.getBlockY() == solid.y && location.getBlockZ() == solid.z && location.getWorld().getName().equalsIgnoreCase(solid.world)){
                data = solid;
                ship.modules = solid.modules;
            }
        }

        if(data != null){
            SQSmoothcraft.instance.solidShips.remove(data);
        }

    }

    public HashSet<ShipBlock> getBlocks() {
        return blocks;
    }
}
