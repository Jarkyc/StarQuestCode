package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import com.spacebeaverstudios.sqtech.objects.pipes.ItemPipe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            ArrayList<ItemPipe> pipes = new ArrayList<>();
            for (ItemPipe pipe : ItemPipe.getAllPipes())
                if (pipe.connects(block.getLocation()) && pipe.getPipeMaterial().equals(block.getType()))
                    pipes.add(pipe);

            if (pipes.size() == 1) {
                pipes.get(0).getBlocks().add(block.getLocation());
                for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)) {
                    if (block.getRelative(face).getType().equals(Material.LAPIS_BLOCK)) {
                        for (Machine machine : Machine.getMachines()) {
                            if (block.getRelative(face).getLocation().equals(machine.getNode()) && machine.getOutputPipe() == null
                                    && machine.getOutputPipeMaterial().equals(block.getRelative(face).getType())) {
                                pipes.get(0).getInputMachines().add(machine);
                            }
                        }
                    }
                }
            } else if (pipes.size() != 0) {
                // TODO: try to "merge" pipes
            }
        }
    }

    private void blockBreak(Block block) {
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            for (ItemPipe pipe : ItemPipe.getAllPipes()) {
                if (pipe.getBlocks().contains(block.getLocation())) {
                    pipe.breakBlock(block.getLocation());
                    return;
                }
            }
        } else {
            for (Machine machine : Machine.getMachines()) {
                if (machine.getBlocks().containsKey(block.getLocation())) {
                    machine.destroy();
                    return;
                }
            }
        }
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
        for (Block block : event.getBlocks()) blockBreak(block.getRelative(event.getDirection().getOppositeFace()));
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) blockBreak(block.getRelative(event.getDirection().getOppositeFace()));
    }

    // TODO: on ship packing/unpacking
}
