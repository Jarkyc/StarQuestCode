package com.spacebeaverstudios.sqcompass.objects;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface CompassMarker {
    int getYaw(Player player);
    char getMarker();
    ChatColor getColor();
    boolean isWorldSpecific();
    World getWorld();
    int getImportance(); // 0-10, 10 is most important
}
