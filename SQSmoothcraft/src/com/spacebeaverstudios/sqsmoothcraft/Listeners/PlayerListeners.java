package com.spacebeaverstudios.sqsmoothcraft.Listeners;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.DetectionTask;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerListeners implements Listener {


    @EventHandler
    public static void onInteract(PlayerInteractEvent e){
        Block block = e.getClickedBlock();

        if(e.getClickedBlock() == null) return;

        if(e.getClickedBlock().getType() == Material.NOTE_BLOCK && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!ShipUtils.isAPilot(e.getPlayer())) {
                    e.setCancelled(true);
                    new DetectionTask(e.getClickedBlock().getLocation(), e.getPlayer());
            } else {
                e.getPlayer().sendMessage(ChatColor.RED + "You are already piloting a ship!");
            }
        }
    }

    @EventHandler
    public static void onDismount(EntityDismountEvent e){

        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();

            if(ShipUtils.isAPilot(player)){
                e.setCancelled(true);
            }

        }

    }


}
