package com.spacebeaverstudios.sqtech.objects.pipes;

import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class ItemPipe implements CanCheckIntact {
    private static final ArrayList<ItemPipe> allPipes = new ArrayList<>();

    public static ArrayList<ItemPipe> getAllPipes() {
        return allPipes;
    }

    private final ArrayList<Machine> inputMachines = new ArrayList<>();
    private final Material pipeMaterial;
    private final Location starterBlock; // must always be connected to outputMachine
    private final ArrayList<Location> blocks = new ArrayList<>();
    private Machine outputMachine;

    public ItemPipe(Location starterBlock) {
        this.pipeMaterial = starterBlock.getBlock().getType();
        this.starterBlock = starterBlock;
        blocks.add(starterBlock);

        calculate();
        allPipes.add(this);
    }

    public void calculate() {
        // TODO: calculate output machines
        ArrayList<Location> blocksToCheck = new ArrayList<>();
        blocksToCheck.add(starterBlock);
        while (blocksToCheck.size() != 0) {
            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)) {
                Block block = blocksToCheck.get(0).getBlock().getRelative(face);
                if (block.getType().equals(pipeMaterial) && !blocks.contains(block.getLocation())) {
                    blocks.add(block.getLocation());
                    blocksToCheck.add(block.getLocation());
                }
            }
            blocksToCheck.remove(0);
        }
    }

    public void breakBlock(Location broken) {
        // TODO: test this
        outputMachine.getInputPipes().remove(this);
        outputMachine = null;
        for (Machine machine : inputMachines) machine.setOutputPipe(null);
        inputMachines.clear();

        if (broken.equals(starterBlock)) {
            allPipes.remove(this);
            return;
        }

        ArrayList<Location> oldBlocks = new ArrayList<>(blocks);
        calculate();
        for (Location loc : oldBlocks) {
            if (!blocks.contains(loc) && !loc.equals(broken)) {
                ItemPipe newPipe = new ItemPipe(loc);
                if (newPipe.getOutputMachine() == null && newPipe.getInputMachines().size() == 0) allPipes.remove(newPipe);
            }
        }
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

    public void setOutputMachine(Machine outputMachine) {
        this.outputMachine = outputMachine;
        if (outputMachine == null && inputMachines.size() == 0) allPipes.remove(this);
    }

    public void checkIntact() {
        for (Location loc : blocks) {
            if (!loc.getBlock().getType().equals(pipeMaterial)) {
                breakBlock(loc);
                return;
            }
        }
    }
}
