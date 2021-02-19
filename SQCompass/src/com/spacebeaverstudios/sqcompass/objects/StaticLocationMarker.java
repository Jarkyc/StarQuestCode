package com.spacebeaverstudios.sqcompass.objects;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class StaticLocationMarker implements CompassMarker {
    private final Location location;
    private final char marker;
    private final ChatColor color;
    private final int importance;

    public StaticLocationMarker(Location location, char marker, ChatColor color, int importance) {
        this.location = location;
        this.marker = marker;
        this.color = color;
        this.importance = importance;
    }

    public Location getLocation() {
        return location;
    }

    public int getYaw(Player player) {
        return (int) Math.round(Math.toDegrees(
                Math.atan2(player.getLocation().getX()-location.getX(), location.getZ()-player.getLocation().getZ())));
    }

    public char getMarker() {
        return marker;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isWorldSpecific() {
        return true;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public int getImportance() {
        return importance;
    }
}
