package com.spacebeaverstudios.sqsmoothcraft.Listeners;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipBlock;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.CannonTask;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.DetectionTask;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerListeners implements Listener {


    @EventHandler
    public static void onInteract(PlayerInteractEvent e){

        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CLOCK && ShipUtils.isAPilot(e.getPlayer())){
            Ship ship = ShipUtils.getShipByPlayer(e.getPlayer());

            if(ship.isAutopilot){
                ship.isAutopilot = false;
                ship.autoPilotDirection = null;
                e.getPlayer().sendMessage(ChatColor.GREEN + "Stopping auto-pilot");
            } else {
                ship.isAutopilot = true;
                ship.autoPilotDirection = e.getPlayer().getLocation().getDirection();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Initiating auto-pilot");
            }
        }

        if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CLOCK && ShipUtils.isAPilot(e.getPlayer())){
            for(ShipBlock block : ShipUtils.getShipByPlayer(e.getPlayer()).pistons){
                new CannonTask(block.getArmorStand().getLocation(), ShipUtils.getShipByPlayer(e.getPlayer()).getOwner().getLocation().getDirection(), ShipUtils.getShipByPlayer(e.getPlayer()));
            }
        }
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

    @EventHandler
    public static void entityInteract(PlayerInteractAtEntityEvent e){
        Player player = e.getPlayer();
        Entity ent = e.getRightClicked();

        if(ent.getType() == EntityType.ARMOR_STAND){

            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CLOCK && ShipUtils.isAPilot(e.getPlayer())){
                Ship ship = ShipUtils.getShipByPlayer(e.getPlayer());

                if(ship.isAutopilot){
                    ship.isAutopilot = false;
                    ship.autoPilotDirection = null;
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Stopping auto-pilot");
                } else {
                    ship.isAutopilot = true;
                    ship.autoPilotDirection = e.getPlayer().getLocation().getDirection();
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Initiating auto-pilot");
                }
            }
        }
    }

}
