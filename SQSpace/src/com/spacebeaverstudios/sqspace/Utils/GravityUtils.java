package com.spacebeaverstudios.sqspace.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GravityUtils {

    private static ArrayList<String> noGravityWorld = new ArrayList<>();

    static{
        noGravityWorld.add("space");
        noGravityWorld.add("slipspace");
    }

    public static boolean hasNoGravity(String world){
        if(noGravityWorld.contains(world.toLowerCase())) return true;
        else return false;
    }

    public static boolean isInsideStructure(Player player){
        Location loc = player.getLocation();
        World w = loc.getWorld();
        boolean air1 = false;
        boolean air2 = false;
        int height = 40;
        for(int i = 0; i < height; i++){
            Material one = w.getBlockAt(loc.clone().subtract(0, i, 0)).getType();
            Material two = w.getBlockAt(loc.clone().add(0, i + 1, 0)).getType();
            if(one.isSolid()){
                air1 = true;
            }
            if(two.isSolid()){
                air2 = true;
            }
            if(air1 && air2) return true;
        }
        return false;
    }

}
