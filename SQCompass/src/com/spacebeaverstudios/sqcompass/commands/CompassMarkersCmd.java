package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.objects.StaticLocationMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CompassMarkersCmd extends SQCmd {
    public CompassMarkersCmd() {
        super("markers", "See a list of all your custom compass markers.", true);
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        HashMap<UUID, HashMap<String, StaticLocationMarker>> customMarkers = CompassUtils.getCustomMarkers();

        if (customMarkers.get(player.getUniqueId()).size() != 0) {
            player.sendMessage(ChatColor.GOLD + "Here are your custom compass markers:");
            for (String name : customMarkers.get(player.getUniqueId()).keySet()) {
                StaticLocationMarker marker = customMarkers.get(player.getUniqueId()).get(name);
                player.sendMessage(ChatColor.AQUA + name + ": " + marker.getColor() + marker.getMarker() + ChatColor.AQUA + " at x: "
                        + marker.getLocation().getBlockX() + ", z: " + marker.getLocation().getBlockZ() + " in " + marker.getWorld().getName());
            }
        } else player.sendMessage(ChatColor.RED + "You have no custom compass markers!");
    }
}
