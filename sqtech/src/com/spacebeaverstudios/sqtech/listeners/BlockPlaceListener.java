package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.machines.Machine;
import com.spacebeaverstudios.sqtech.pipes.ItemPipe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockPlaceListener implements Listener {
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
}
