package com.spacebeaverstudios.sqcore.listeners;

import com.spacebeaverstudios.sqcore.utils.MapUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class PlayerListeners implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public static void onCraft(PrepareItemCraftEvent e){
        if(e.getRecipe() == null || e.getRecipe().getResult() == null) return;
        if(e.getRecipe().getResult().getType() == Material.ENDER_CHEST || e.getRecipe().getResult().getType() == Material.ENCHANTING_TABLE){
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // prevents placing a map with an attached SQMapRenderer where SQMapRenderer#canItemFrame == false into an item frame
        if (event.getRightClicked() instanceof ItemFrame) {
            ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
            ItemStack offHand = event.getPlayer().getInventory().getItemInOffHand();
            if (mainHand.getType() == Material.FILLED_MAP) {
                int id = ((MapMeta) mainHand.getItemMeta()).getMapId();
                if (MapUtils.idsByName.containsValue(id) && !MapUtils.renderersByName.get(MapUtils.idsByName.inverse().get(id)).canItemFrame) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You can't place this map on an item frame!");
                    event.setCancelled(true);
                }
            } else if (mainHand.getType() == Material.AIR && offHand.getType() == Material.FILLED_MAP) {
                int id = ((MapMeta) offHand.getItemMeta()).getMapId();
                if (MapUtils.idsByName.containsValue(id) && !MapUtils.renderersByName.get(MapUtils.idsByName.inverse().get(id)).canItemFrame) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You can't place this map on an item frame!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
