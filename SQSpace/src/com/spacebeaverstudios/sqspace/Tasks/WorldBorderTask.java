package com.spacebeaverstudios.sqspace.Tasks;
import java.util.HashMap;

import com.spacebeaverstudios.sqspace.Utils.WorldBorderUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.ChatColor;

public class WorldBorderTask implements Runnable {

    public static HashMap<Player, Location> lastLocation = new HashMap<Player, Location>();

    @Override
    public void run () {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!lastLocation.containsKey(player)) {

                lastLocation.put(player, player.getLocation());

            }

            if (!lastLocation.get(player).equals(player.getLocation())) {

                Location location = player.getLocation();

                if (!WorldBorderUtils.isWithinBorder(location)) {

                    player.sendMessage(ChatColor.RED + "You cannot move beyond the worldborder!");

                    Location closestLocation = WorldBorderUtils.getClosestWithinBorder(location);

                    double y = -1;

                    for (int i = 255; i >= 0; i --) {

                        if (!closestLocation.getWorld().getBlockAt(closestLocation.getBlockX(), i, closestLocation.getBlockZ()).getType().equals(Material.AIR)) {

                            y = i;
                            break;

                        }

                    }

                    if (y == -1) {

                        y = closestLocation.getY();

                    }

                    closestLocation.setY(y);
                    closestLocation.setYaw(player.getLocation().getYaw());
                    closestLocation.setPitch(player.getLocation().getPitch());

                    player.teleport(closestLocation);

                }

            }

            lastLocation.put(player, player.getLocation());

        }

    }


}