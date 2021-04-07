package com.spacebeaverstudios.sqspace;

import com.spacebeaverstudios.sqspace.Utils.AirUtils;
import com.spacebeaverstudios.sqspace.Utils.GravityUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SuffocationTask extends BukkitRunnable {

    Player player;
    SQSpace plugin;
    boolean cancelled = false;

    public SuffocationTask(SQSpace plugin, Player player){
        this.player = player;
        this.plugin = plugin;
        long time = 20L;
        AirUtils.suffocatingPlayers.add(player);
        this.runTaskTimer(plugin, time, time);
    }

    public void cancel(Player player){
        this.cancel();
        this.cancelled = true;
        AirUtils.suffocatingPlayers.remove(player);
        player.sendMessage(ChatColor.GREEN + "You are no longer suffocating");
    }

    @Override
    public void run() {
        if(AirUtils.hasSpaceSuit(this.player) || !AirUtils.hasNoAir(this.player.getWorld().getName().toLowerCase()) || this.player.isDead() || GravityUtils.isInsideStructure(player)){
            this.cancel(player);
        }
        if(!this.cancelled){
            if(player.getGameMode().equals(GameMode.ADVENTURE) || player.getGameMode().equals(GameMode.SURVIVAL)){
                double health = player.getHealth();
                player.damage(1.0D);
                double newHealth = health - 1.0;
                if (newHealth < 0) {
                    newHealth = 0;
                }
                this.player.setHealth(newHealth);
            }
        }
    }
}
