package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.objects.Pipe;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
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
            ArrayList<Machine> machines = new ArrayList<>();

            for (BlockFace face : Arrays.asList(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST,
                    BlockFace.NORTH, BlockFace.SOUTH)) {
                Block relative = block.getRelative(face);
                if (relative.getType() == block.getType() && Pipe.getPipesByBlock().containsKey(relative.getLocation())) {
                    Pipe.getPipesByBlock().get(relative.getLocation()).calculate();
                    return;
                } else if (relative.getType() == Material.LAPIS_BLOCK
                        && Machine.getMachinesByBlock().containsKey(relative.getLocation())) {
                    machines.add(Machine.getMachinesByBlock().get(relative.getLocation()));
                }
            }

            if (machines.size() != 0) {
                for (Machine machine : machines) {
                    if (block.getType() == machine.getOutputPipeMaterial()) {
                        if (machine.getOutputType() != null) {
                            new Pipe(block.getLocation());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void blockBreak(Block block) {
        if (block.getType().toString().endsWith("_STAINED_GLASS")) {
            if (Pipe.getPipesByBlock().containsKey(block.getLocation())) {
                Pipe.getPipesByBlock().get(block.getLocation()).breakBlock();
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
