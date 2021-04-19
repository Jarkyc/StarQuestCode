package com.spacebeaverstudios.sqcore.utils;

import org.bukkit.block.BlockFace;

@SuppressWarnings("unused")
public class RotationUtils {
    public static BlockFace rotateLeft(BlockFace blockFace) {
        switch (blockFace) {
            case EAST: return BlockFace.NORTH;
            case EAST_NORTH_EAST: return BlockFace.NORTH_NORTH_WEST;
            case EAST_SOUTH_EAST: return BlockFace.NORTH_NORTH_EAST;

            case NORTH: return BlockFace.WEST;
            case NORTH_EAST: return BlockFace.NORTH_WEST;
            case NORTH_NORTH_EAST: return BlockFace.WEST_NORTH_WEST;
            case NORTH_NORTH_WEST: return BlockFace.WEST_SOUTH_WEST;
            case NORTH_WEST: return BlockFace.SOUTH_WEST;

            case SOUTH: return BlockFace.EAST;
            case SOUTH_EAST: return BlockFace.NORTH_EAST;
            case SOUTH_SOUTH_EAST: return BlockFace.EAST_NORTH_EAST;
            case SOUTH_SOUTH_WEST: return BlockFace.EAST_SOUTH_EAST;
            case SOUTH_WEST: return BlockFace.SOUTH_EAST;

            case WEST: return BlockFace.SOUTH;
            case WEST_NORTH_WEST: return BlockFace.SOUTH_SOUTH_WEST;
            case WEST_SOUTH_WEST: return BlockFace.SOUTH_SOUTH_EAST;
        }
        return blockFace;
    }

    public static BlockFace rotateRight(BlockFace blockFace) {
        switch (blockFace) {
            case EAST: return BlockFace.SOUTH;
            case EAST_NORTH_EAST: return BlockFace.SOUTH_SOUTH_EAST;
            case EAST_SOUTH_EAST: return BlockFace.SOUTH_SOUTH_WEST;

            case NORTH: return BlockFace.EAST;
            case NORTH_EAST: return BlockFace.SOUTH_EAST;
            case NORTH_NORTH_EAST: return BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_WEST: return BlockFace.EAST_NORTH_EAST;
            case NORTH_WEST: return BlockFace.NORTH_EAST;

            case SOUTH: return BlockFace.WEST;
            case SOUTH_EAST: return BlockFace.SOUTH_WEST;
            case SOUTH_SOUTH_EAST: return BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST: return BlockFace.WEST_NORTH_WEST;
            case SOUTH_WEST: return BlockFace.NORTH_WEST;

            case WEST: return BlockFace.NORTH;
            case WEST_NORTH_WEST: return BlockFace.NORTH_NORTH_EAST;
            case WEST_SOUTH_WEST: return BlockFace.NORTH_NORTH_WEST;
        }
        return blockFace;
    }
}
