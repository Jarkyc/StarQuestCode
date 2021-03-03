package com.spacebeaverstudios.sqtech.objects.pipes;

import com.spacebeaverstudios.sqtech.objects.machines.BatteryMachine;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PowerPipe implements Pipe {
    // static
    private static final ArrayList<PowerPipe> allPipes = new ArrayList<>();

    public static ArrayList<PowerPipe> getAllPipes() {
        return allPipes;
    }

    // instance
    private final Location starterBlock;
    private final ArrayList<Location> blocks = new ArrayList<>();
    private final Material pipeMaterial;
    private final ArrayList<Machine> inputMachines = new ArrayList<>(); // should be instanceof BatteryMachine
    private final ArrayList<Machine> outputMachines = new ArrayList<>(); // should allow input of TransferType.POWER

    public ArrayList<Location> getBlocks() {
        return blocks;
    }
    public Material getPipeMaterial() {
        return pipeMaterial;
    }
    public ArrayList<Machine> getInputMachines() {
        return inputMachines;
    }
    public ArrayList<Machine> getOutputMachines() {
        return outputMachines;
    }

    public PowerPipe(Location starterBlock) {
        this.starterBlock = starterBlock;
        this.pipeMaterial = starterBlock.getBlock().getType();
        blocks.add(starterBlock);

        calculate();
        allPipes.add(this);
    }

    public void calculate() {
        // TODO: if it tries to connect to other pipes
        // TODO: calculate connected machines
        ArrayList<Location> blocksToCheck = new ArrayList<>();
        blocksToCheck.add(starterBlock);
        while (blocksToCheck.size() != 0) {
            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST)) {
                Block block = blocksToCheck.get(0).getBlock().getRelative(face);
                if (block.getType().equals(pipeMaterial) && !blocks.contains(block.getLocation())) {
                    blocks.add(block.getLocation());
                    blocksToCheck.add(block.getLocation());
                }
            }
            blocksToCheck.remove(0);
        }
    }

    public boolean connects(Location loc) {
        for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST))
            if (blocks.contains(loc.getBlock().getRelative(face).getLocation()))
                return true;
        return false;
    }

    public void breakBlock(Location broken) {
        // TODO
    }

    public void checkIntact() {
        for (Location loc : blocks) {
            if (!loc.getBlock().getType().equals(pipeMaterial)) {
                breakBlock(loc);
                return;
            }
        }
    }

    public boolean tryUsePower(Integer amount) {
        HashMap<BatteryMachine, Integer> powerDeducted = new HashMap<>();
        for (Machine machine : inputMachines) {
            BatteryMachine batteryMachine = (BatteryMachine) machine;
            if (batteryMachine.getPower() >= amount) {
                batteryMachine.setPower(batteryMachine.getPower()-amount);
                return true;
            } else if (batteryMachine.getPower() != 0) {
                powerDeducted.put(batteryMachine, batteryMachine.getPower());
                amount -= batteryMachine.getPower();
                batteryMachine.setPower(0);
            }
        }

        // means that there wasn't enough
        for (BatteryMachine machine : powerDeducted.keySet())
            machine.setPower(machine.getPower() + powerDeducted.get(machine));
        return false;
    }

    public boolean connectedToBattery() {
        for (Machine machine : outputMachines)
            if (machine instanceof BatteryMachine)
                return true;
        return false;
    }
    public void powerToBattery(Integer amount) {
        for (Machine machine : outputMachines) {
            if (machine instanceof BatteryMachine) {
                BatteryMachine batteryMachine = (BatteryMachine) machine;
                batteryMachine.setPower(batteryMachine.getPower()+amount);
                return;
            }
        }
    }
}
