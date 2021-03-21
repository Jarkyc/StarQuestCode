package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import com.spacebeaverstudios.sqtech.objects.pipes.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockListener implements Listener {
    private void blockPlace(Block block) {
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            ArrayList<ItemPipe> itemPipes = new ArrayList<>();
            ArrayList<PowerPipe> powerPipes = new ArrayList<>();
            ArrayList<Machine> machines = new ArrayList<>();

            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                    BlockFace.NORTH, BlockFace.SOUTH)) {
                Block relative = block.getRelative(face);
                if (ItemPipe.getPipesByBlock().containsKey(relative.getLocation())) {
                    itemPipes.add(ItemPipe.getPipesByBlock().get(relative.getLocation()));
                } else if (PowerPipe.getPipesByBlock().containsKey(relative.getLocation())) {
                    powerPipes.add(PowerPipe.getPipesByBlock().get(relative.getLocation()));
                } else if (relative.getType() == Material.LAPIS_BLOCK
                        && Machine.getMachinesByBlock().containsKey(relative.getLocation())) {
                    machines.add(Machine.getMachinesByBlock().get(relative.getLocation()));
                }
            }

            if (itemPipes.size() == 0 && powerPipes.size() == 0) {
                if (machines.size() != 0) {
                    for (Machine machine : machines) {
                        if (block.getType() == machine.getOutputPipeMaterial()) {
                            if (machine.getOutputType() == Machine.TransferType.ITEMS) {
                                new ItemPipe(block.getLocation());
                            } else if (machine.getOutputType() == Machine.TransferType.POWER) {
                                new PowerPipe(block.getLocation());
                            }
                            break;
                        }
                    }
                }
            } else if (itemPipes.size() != 0 && powerPipes.size() == 0) {
                if (itemPipes.size() == 1) {
                    itemPipes.get(0).calculate();
                } else {
                    // TODO
                }
            } else if (itemPipes.size() == 0) {
                if (powerPipes.size() == 1) {
                    powerPipes.get(0).calculate();
                } else {
                    // TODO
                }
            } else {
                // TODO
            }
        }
    }

    private void blockBreak(Block block) {
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            if (ItemPipe.getPipesByBlock().containsKey(block.getLocation())) {
                ItemPipe.getPipesByBlock().get(block.getLocation()).breakBlock();
            } else if (PowerPipe.getPipesByBlock().containsKey(block.getLocation())) {
                PowerPipe.getPipesByBlock().get(block.getLocation()).breakBlock();
            }
        } else {
            if (Machine.getMachinesByBlock().containsKey(block.getLocation())) {
                Machine.getMachinesByBlock().get(block.getLocation()).destroy();
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        blockPlace(event.getBlock());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        blockBreak(event.getBlock());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {
        blockBreak(event.getBlock());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        // do the breaks first just to prevent some errors, this shouldn't hurt performance
        for (Block block : event.getBlocks()) {
            blockBreak(block.getRelative(event.getDirection().getOppositeFace()));
        }
        for (Block block : event.getBlocks()) {
            blockPlace(block);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        // do the breaks first just to prevent some errors, this shouldn't hurt performance
        for (Block block : event.getBlocks()) {
            blockBreak(block.getRelative(event.getDirection().getOppositeFace()));
        }
        for (Block block : event.getBlocks()) {
            blockPlace(block);
        }
    }

    // TODO: on ship packing/unpacking
}
