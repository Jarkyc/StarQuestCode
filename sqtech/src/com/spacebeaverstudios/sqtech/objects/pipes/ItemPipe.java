package com.spacebeaverstudios.sqtech.objects.pipes;

import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class ItemPipe implements Pipe {
    // static
    // TODO: store in a HashMap by Location
    private static final ArrayList<ItemPipe> allPipes = new ArrayList<>();

    public static ArrayList<ItemPipe> getAllPipes() {
        return allPipes;
    }

    // instance
    private final ArrayList<Machine> inputMachines = new ArrayList<>();
    private final Material pipeMaterial;
    private final Location starterBlock; // must always be connected to outputMachine
    private final ArrayList<Location> blocks = new ArrayList<>();
    private Machine outputMachine;

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

    public ItemPipe(Location starterBlock) {
        this.pipeMaterial = starterBlock.getBlock().getType();
        this.starterBlock = starterBlock;
        blocks.add(starterBlock);

        calculate();
        allPipes.add(this);
    }

    public void calculate() {
        // TODO: if it tries to connect to other pipes (including PowerPipes)
        ArrayList<Location> blocksToCheck = new ArrayList<>();
        blocksToCheck.add(starterBlock);
        while (blocksToCheck.size() != 0) {
            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST)) {
                Block block = blocksToCheck.get(0).getBlock().getRelative(face);
                if (blocks.contains(block.getLocation())) {
                    continue;
                }
                if (block.getType().equals(pipeMaterial)) {
                    blocks.add(block.getLocation());
                    blocksToCheck.add(block.getLocation());
                } else if (block.getType().equals(Material.LAPIS_BLOCK) && outputMachine == null) {
                    if (Machine.getMachinesByBlock().containsKey(block.getLocation())) {
                        Machine machine = Machine.getMachinesByBlock().get(block.getLocation());
                        if (machine.getInputPipeMaterials().contains(pipeMaterial)
                                && machine.getInputTypes().contains(Machine.TransferType.ITEMS)) {
                            machine.getItemInputPipes().add(this);
                            outputMachine = machine;
                        }
                    }
                }
            }
            blocksToCheck.remove(0);
        }
    }

    public void breakBlock() {
        // TODO: doesn't work
        allPipes.remove(this);
        outputMachine.getItemInputPipes().remove(this);
        outputMachine = null;
        for (Machine machine : inputMachines) {
            machine.setItemOutputPipe(null);
        }
        inputMachines.clear();

        ArrayList<Location> blocksToCheck = new ArrayList<>(blocks);
        while (blocksToCheck.size() != 0) {
            if (blocksToCheck.get(0).getBlock().getType().equals(pipeMaterial)) {
                ItemPipe newPipe = new ItemPipe(blocksToCheck.get(0));
                for (Location loc : newPipe.getBlocks()) {
                    blocksToCheck.remove(loc);
                }
                if (newPipe.getOutputMachine() == null && newPipe.getInputMachines().size() == 0) {
                    allPipes.remove(newPipe);
                }
            } else {
                blocksToCheck.remove(0);
            }
        }
    }

    public boolean connects(Location loc) {
        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                BlockFace.EAST, BlockFace.WEST)) {
            if (blocks.contains(loc.getBlock().getRelative(face).getLocation())) {
                return true;
            }
        }
        return false;
    }

    public void setOutputMachine(Machine outputMachine) {
        this.outputMachine = outputMachine;
        if (outputMachine == null && inputMachines.size() == 0) {
            allPipes.remove(this);
        }
    }

    public void checkIntact() {
        if (outputMachine == null && inputMachines.size() == 0) {
            allPipes.remove(this);
        }
        for (Location loc : blocks) {
            if (!loc.getBlock().getType().equals(pipeMaterial)) {
                breakBlock();
                return;
            }
        }
    }
}
