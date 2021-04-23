package com.spacebeaverstudios.sqcompass.objects;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public enum CardinalDirectionMarker implements CompassMarker {
    NORTH(180, 'N'), EAST(270, 'E'), SOUTH(0, 'S'), WEST(90, 'W');

    private final int yaw;
    private final char marker;

    CardinalDirectionMarker(int yaw, char marker) {
        this.yaw = yaw;
        this.marker = marker;
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
