package com.spacebeaverstudios.sqtech.pipes;

import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class ItemPipe {
    /*
    TODO
    Also, doing it once a second just causes a once a second small lagspike
    The list of ways to change blocks is pretty much just placing/breaking, pistons, explosions, and ship packing/unpacking.
    And then you could do a physical check much more irregularly
    If you do want to do it every second, spread out the different networks onto different ticks

    - Ginger
     */
    private static final ArrayList<ItemPipe> allPipes = new ArrayList<>();

    public static ArrayList<ItemPipe> getAllPipes() {
        return allPipes;
    }

    private final ArrayList<Machine> inputMachines = new ArrayList<>();
    private final Material pipeMaterial;
    private final Location starterBlock; // must always be connected to outputMachine
    private final ArrayList<Location> blocks = new ArrayList<>();
    private final Machine outputMachine;

    public ItemPipe(Material pipeMaterial, Location starterBlock, Machine outputMachine) {
        this.pipeMaterial = pipeMaterial;
        this.starterBlock = starterBlock;
        this.outputMachine = outputMachine;
        blocks.add(starterBlock);

        calculate();
    }

    public void calculate() {
        List<BlockFace> facesToCheck = Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);
        ArrayList<Location> blocksToCheck = (ArrayList<Location>) Arrays.asList(starterBlock);
        while (blocksToCheck.size() != 0) {
            for (BlockFace face : facesToCheck) {
                Block block = blocksToCheck.get(0).getBlock().getRelative(face);
                if (block.getType().equals(pipeMaterial)) {
                    blocks.add(block.getLocation());
                    blocksToCheck.add(block.getLocation());
                }
            }
            blocksToCheck.remove(0);
        }
    }

    public boolean intact() {
        for (Location loc : blocks)
            if (!loc.getBlock().getType().equals(pipeMaterial))
                return false;
        return true;
    }

    public boolean connects(Location loc) {
        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST))
            if (blocks.contains(loc.getBlock().getRelative(face).getLocation()))
                return true;
        return false;
    }

    public ArrayList<Machine> getInputMachines() {
        return inputMachines;
    }
    public ArrayList<Location> getBlocks() {
        return blocks;
    }
    public Machine getOutputMachine() {
        return outputMachine;
    }
    public Material getPipeMaterial() {
        return pipeMaterial;
    }
}
