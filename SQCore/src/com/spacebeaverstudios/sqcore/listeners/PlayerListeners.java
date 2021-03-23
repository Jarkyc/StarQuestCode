package com.spacebeaverstudios.sqcore.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public static void onCraft(PrepareItemCraftEvent e){
        if(e.getRecipe() == null || e.getRecipe().getResult() == null) return;
        if(e.getRecipe().getResult().getType() == Material.ENDER_CHEST || e.getRecipe().getResult().getType() == Material.ENCHANTING_TABLE){
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
