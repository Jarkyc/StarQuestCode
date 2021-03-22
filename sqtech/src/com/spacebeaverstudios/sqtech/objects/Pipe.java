package com.spacebeaverstudios.sqtech.objects;

import com.spacebeaverstudios.sqtech.objects.machines.BatteryMachine;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Pipe implements CanCheckIntact {
    // static
    private static final ArrayList<Pipe> allPipes = new ArrayList<>();
    private static final HashMap<Location, Pipe> pipesByBlock = new HashMap<>();

    public static ArrayList<Pipe> getAllPipes() {
        return allPipes;
    }
    public static HashMap<Location, Pipe> getPipesByBlock() {
        return pipesByBlock;
    }

    // instance
    private final ArrayList<Machine> itemInputMachines = new ArrayList<>();
    private final ArrayList<Machine> powerInputMachines = new ArrayList<>();
    private final ArrayList<Machine> itemOutputMachines = new ArrayList<>();
    private final ArrayList<Machine> powerOutputMachines = new ArrayList<>();
    private final Material pipeMaterial;
    private final Location starterBlock;
    private final ArrayList<Location> blocks = new ArrayList<>();
    private Integer itemOutputIndex = 0;

    public ArrayList<Location> getBlocks() {
        return blocks;
    }
    public ArrayList<Machine> getItemInputMachines() {
        return itemInputMachines;
    }
    public ArrayList<Machine> getPowerInputMachines() {
        return powerInputMachines;
    }
    public ArrayList<Machine> getItemOutputMachines() {
        return itemOutputMachines;
    }
    public ArrayList<Machine> getPowerOutputMachines() {
        return powerOutputMachines;
    }

    public Pipe(Location starterBlock) {
        this.pipeMaterial = starterBlock.getBlock().getType();
        this.starterBlock = starterBlock;

        calculate();
        allPipes.add(this);
    }

    public void calculate() {
        // TODO: if it tries to connect to other pipes
        for (Location loc : blocks) {
            pipesByBlock.remove(loc);
        }
        blocks.clear();

        ArrayList<Location> blocksToCheck = new ArrayList<>();
        blocksToCheck.add(starterBlock);
        blocks.add(starterBlock);
        pipesByBlock.put(starterBlock, this);

        while (blocksToCheck.size() != 0) {
            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST)) {
                Block block = blocksToCheck.get(0).getBlock().getRelative(face);
                if (blocks.contains(block.getLocation())) {
                    continue;
                }
                if (block.getType() == pipeMaterial) {
                    blocks.add(block.getLocation());
                    pipesByBlock.put(block.getLocation(), this);
                    blocksToCheck.add(block.getLocation());
                } else if (block.getType() == Material.LAPIS_BLOCK) {
                    if (Machine.getMachinesByBlock().containsKey(block.getLocation())) {
                        Machine machine = Machine.getMachinesByBlock().get(block.getLocation());
                        if (machine.getItemInputPipeMaterials().contains(pipeMaterial) && !machine.getItemInputPipes().contains(this)) {
                            machine.getItemInputPipes().add(this);
                            itemOutputMachines.add(machine);
                        } else if (machine.getPowerInputPipeMaterials().contains(pipeMaterial)
                                && !machine.getPowerInputPipes().contains(this)) {
                            machine.getPowerInputPipes().add(this);
                            powerOutputMachines.add(machine);
                        } else if (pipeMaterial == machine.getOutputPipeMaterial()) {
                            if (machine.getOutputType() == Machine.TransferType.ITEMS) {
                                machine.setItemOutputPipe(this);
                                itemInputMachines.add(machine);
                            } else {
                                machine.setPowerOutputPipe(this);
                                powerInputMachines.add(machine);
                            }
                        }
                    }
                }
            }
            blocksToCheck.remove(0);
        }
    }

    public void breakBlock() {
        for (Location loc : blocks) {
            pipesByBlock.remove(loc);
        }
        allPipes.remove(this);

        for (Machine machine : itemInputMachines) {
            machine.setItemOutputPipe(null);
        }
        itemInputMachines.clear();
        for (Machine machine : powerInputMachines) {
            machine.setPowerOutputPipe(null);
        }
        powerInputMachines.clear();
        for (Machine machine : itemOutputMachines) {
            machine.getItemInputPipes().remove(this);
        }
        itemInputMachines.clear();
        for (Machine machine : powerInputMachines) {
            machine.getPowerInputPipes().remove(this);
        }
        powerInputMachines.clear();

        ArrayList<Location> blocksToCheck = new ArrayList<>(blocks);
        while (blocksToCheck.size() != 0) {
            if (blocksToCheck.get(0).getBlock().getType() == pipeMaterial) {
                Pipe newPipe = new Pipe(blocksToCheck.get(0));
                for (Location loc : newPipe.getBlocks()) {
                    blocksToCheck.remove(loc);
                }
                if (newPipe.getItemInputMachines().size() == 0 && newPipe.getPowerInputMachines().size() == 0
                        && newPipe.getItemOutputMachines().size() == 0 && newPipe.getPowerOutputMachines().size() == 0) {
                    for (Location loc : newPipe.getBlocks()) {
                        pipesByBlock.remove(loc);
                    }
                    allPipes.remove(newPipe);
                }
            } else {
                blocksToCheck.remove(0);
            }
        }
    }

    public void checkIntact() {
        if (itemInputMachines.size() == 0 && powerInputMachines.size() == 0
                && itemOutputMachines.size() == 0 && powerOutputMachines.size() == 0) {
            for (Location loc : blocks) {
                pipesByBlock.remove(loc);
            }
            allPipes.remove(this);
            return;
        }
        for (Location loc : blocks) {
            if (loc.getBlock().getType() != pipeMaterial) {
                breakBlock();
                return;
            }
        }
    }

    public boolean canUsePower(Integer amount) {
        for (Machine machine : powerInputMachines) {
            if (machine instanceof BatteryMachine) {
                BatteryMachine batteryMachine = (BatteryMachine) machine;
                if (batteryMachine.getPower() >= amount) {
                    return true;
                } else if (batteryMachine.getPower() != 0) {
                    amount -= batteryMachine.getPower();
                }
            }
        }
        return false;
    }
    public boolean tryUsePower(Integer amount) {
        HashMap<BatteryMachine, Integer> powerDeducted = new HashMap<>();
        for (Machine machine : powerInputMachines) {
            if (machine instanceof BatteryMachine) {
                BatteryMachine batteryMachine = (BatteryMachine) machine;
                if (batteryMachine.getPower() >= amount) {
                    batteryMachine.setPower(batteryMachine.getPower() - amount);
                    return true;
                } else if (batteryMachine.getPower() != 0) {
                    powerDeducted.put(batteryMachine, batteryMachine.getPower());
                    amount -= batteryMachine.getPower();
                    batteryMachine.setPower(0);
                }
            }
        }

        // if we get here, there wasn't enough
        for (BatteryMachine machine : powerDeducted.keySet()) {
            machine.setPower(machine.getPower() + powerDeducted.get(machine));
        }
        return false;
    }

    public boolean connectedToBattery() {
        for (Machine machine : powerOutputMachines) {
            if (machine instanceof BatteryMachine) {
                return true;
            }
        }
        return false;
    }
    public void powerToBattery(Integer amount) {
        for (Machine machine : powerOutputMachines) {
            if (machine instanceof BatteryMachine) {
                BatteryMachine batteryMachine = (BatteryMachine) machine;
                batteryMachine.setPower(batteryMachine.getPower()+amount);
                return;
            }
        }
    }

    public ItemStack tryOutputItemStack(ItemStack itemStack) {
        int newItemOutputIndex = itemOutputIndex + 1;
        ItemStack newStack = new ItemStack(itemStack.getType(), itemStack.getAmount());

        if (itemOutputMachines.size() == 0) {
            return newStack;
        }

        while (newItemOutputIndex != itemOutputIndex) {
            if (newItemOutputIndex >= itemOutputMachines.size()) {
                newItemOutputIndex = 0;
            }
            newStack = itemOutputMachines.get(newItemOutputIndex).tryAddItemStack(newStack);
            if (newStack.getAmount() == 0) {
                return new ItemStack(newStack.getType(), 0);
            }
        }
        itemOutputIndex = newItemOutputIndex;

        return newStack;
    }

    public void mergeWith(Pipe pipe) {
        for (Location loc : pipe.getBlocks()) {
            blocks.add(loc);
            pipesByBlock.put(loc, this);
        }
        for (Machine machine : pipe.getItemInputMachines()) {
            if (!itemInputMachines.contains(machine)) {
                itemInputMachines.add(machine);
            }
        }
        for (Machine machine : pipe.getPowerInputMachines()) {
            if (!powerInputMachines.contains(machine)) {
                powerInputMachines.add(machine);
            }
        }
        for (Machine machine : pipe.getItemOutputMachines()) {
            if (!itemOutputMachines.contains(machine)) {
                itemOutputMachines.add(machine);
            }
        }
        for (Machine machine : pipe.getPowerOutputMachines()) {
            if (!powerOutputMachines.contains(machine)) {
                powerOutputMachines.add(machine);
            }
        }
        allPipes.remove(pipe);
    }
}
