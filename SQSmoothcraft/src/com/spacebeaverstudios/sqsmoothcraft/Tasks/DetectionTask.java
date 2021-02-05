package com.spacebeaverstudios.sqsmoothcraft.Tasks;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipBlock;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipLocation;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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

                        ShipBlock shipBlock = new ShipBlock(shipLoc, new Location(b.getWorld(), checkBlock.getX(), checkBlock.getY(), checkBlock.getZ()), b.getType());
                        blocks.add(shipBlock);
                        if(b.getType() == Material.NOTE_BLOCK){
                            this.core = shipBlock;
                        }
                        analyed.add(checkBlock);
                        jumpBlocks.push(checkBlock);

                    }
                }
            }

        }

        System.out.println(blocks.size() + " blocks detected");
        new Ship(this.blocks, player, location, this.core, originalVector);

    }

    public HashSet<ShipBlock> getBlocks() {
        return blocks;
    }
}
