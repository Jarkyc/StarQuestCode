package com.spacebeaverstudios.sqspace.Listeners;

import com.spacebeaverstudios.sqspace.SQSpace;
import com.spacebeaverstudios.sqspace.SuffocationTask;
import com.spacebeaverstudios.sqspace.Utils.AirUtils;
import com.spacebeaverstudios.sqspace.Utils.GravityUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        String world = player.getWorld().getName().toLowerCase();

        if(AirUtils.hasNoAir(world)){
            if((player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) && (!AirUtils.hasSpaceSuit(player) && !GravityUtils.isInsideStructure(player)) && !AirUtils.suffocatingPlayers.contains(player)){
                player.sendMessage(ChatColor.RED + "You are now suffocating!" );
                new SuffocationTask(SQSpace.instance, player);
            }
        }

        if(GravityUtils.hasNoGravity(world)){
            if(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL){
                if(!GravityUtils.isInsideStructure(player) && !player.isFlying()){
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.setFlySpeed(0.02F);
                } else if(player.isFlying() && GravityUtils.isInsideStructure(player)){
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setFlySpeed(0.1F);
                    player.setFallDistance(0.0F);
                }
                if(player.isFlying() && player.isSprinting()){
                    player.setSprinting(false);
                }
                if(player.getLocation().getY() > 256){
                    player.teleport(player.getLocation().clone().subtract(0, 1, 0));
                    player.setVelocity(new Vector(0, 0 , 0));
                }
                if(player.getLocation().getY() < 0){
                    player.teleport(player.getLocation().clone().add(0, 1, 0));
                    player.setVelocity(new Vector(0, 0 , 0));
                }
            }
        }

    }

    @EventHandler
    public void fallDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.VOID && GravityUtils.hasNoGravity(e.getEntity().getWorld().getName().toLowerCase())){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemThrow(ItemSpawnEvent e){
        Entity ent = e.getEntity();
        String world = ent.getWorld().getName().toLowerCase();
        if(GravityUtils.hasNoGravity(world)){
            ent.setGravity(false);
            Vector vec = ent.getVelocity();
            vec.setY(0);
            vec.multiply(.4);
            ent.setVelocity(vec);
        }
    }

}
