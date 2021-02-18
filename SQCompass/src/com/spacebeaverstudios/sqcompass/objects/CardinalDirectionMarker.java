package com.spacebeaverstudios.sqcompass.objects;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CardinalDirectionMarker implements CompassMarker {
    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private int yaw;
    private char marker;

    public CardinalDirectionMarker(Direction direction) {
        switch(direction) {
            case NORTH:
                this.yaw = 180;
                this.marker = 'N';
                break;
            case EAST:
                this.yaw = 270;
                this.marker = 'E';
                break;
            case SOUTH:
                this.yaw = 0;
                this.marker = 'S';
                break;
            case WEST:
                this.yaw = 90;
                this.marker = 'W';
                break;
        }
    }

    public int getYaw(Player player) {
        return yaw;
    }

    public char getMarker() {
        return marker;
    }

    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    public boolean isWorldSpecific() {
        return false;
    }

    public World getWorld() {
        return null;
    }

    public int getImportance() {
        return 0;
    }
}
