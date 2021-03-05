package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import com.spacebeaverstudios.sqtech.objects.pipes.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.ArrayList;

public class BlockListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            // TODO: check if not adding to pipe but touching node block
            ArrayList<Pipe> pipes = new ArrayList<>();
            for (ItemPipe pipe : ItemPipe.getAllPipes())
                if (pipe.connects(block.getLocation()) && pipe.getPipeMaterial().equals(block.getType()))
                    pipes.add(pipe);
            for (PowerPipe pipe : PowerPipe.getAllPipes())
                if (pipe.connects(block.getLocation()) && pipe.getPipeMaterial().equals(block.getType()))
                    pipes.add(pipe);

            if (pipes.size() == 1) pipes.get(0).calculate();
            else if (pipes.size() != 0) {
                // TODO: try to "merge" pipes
            }
        }
    }

    private void blockBreak(Block block) {
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            for (ItemPipe pipe : ItemPipe.getAllPipes()) {
                if (pipe.getBlocks().contains(block.getLocation())) {
                    pipe.breakBlock();
                    return;
                }
            }
            for (PowerPipe pipe : PowerPipe.getAllPipes()) {
                if (pipe.getBlocks().contains(block.getLocation())) {
                    pipe.breakBlock();
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
