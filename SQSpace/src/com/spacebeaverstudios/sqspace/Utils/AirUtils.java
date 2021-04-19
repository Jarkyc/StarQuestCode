package com.spacebeaverstudios.sqspace.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AirUtils {

    private static ArrayList<String> noAirPlanets = new ArrayList<>();
    public static ArrayList<Player> suffocatingPlayers = new ArrayList<>();

    static{
        noAirPlanets.add("space");
        noAirPlanets.add("slipspace");
    }

    public static boolean hasSpaceSuit(Player player){
        ItemStack[] armor = player.getInventory().getArmorContents();
        if(armor == null) return false;
        for(ItemStack i : armor){
            if(i == null) return false;
            Material m = i.getType();
            if (m == Material.CHAINMAIL_HELMET ||  m == Material.CHAINMAIL_CHESTPLATE ||  m == Material.CHAINMAIL_LEGGINGS || m ==  Material.CHAINMAIL_BOOTS) return true;
        }
        return false;
    }

    public static boolean hasNoAir(String world){
        if(noAirPlanets.contains(world.toLowerCase())) return true;
        else return false;
    }

}
