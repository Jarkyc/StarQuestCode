package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.objects.CompassMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.SelectionArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class HideCommonMarkerCmd extends SQCmd {
    public HideCommonMarkerCmd() {
        super("hidecommonmarker", "Hides one of the common markers created by the server", true);

        this.addArgument(new SelectionArgument("marker", new ArrayList<>(CompassUtils.getCommonMarkers().keySet())));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        CompassMarker marker = CompassUtils.getCommonMarkers().get(args[0].toString());
        Player player = (Player) sender;

        if (CompassUtils.getMarkers().get(player).contains(marker)) {
            CompassUtils.getMarkers().get(player).remove(marker);
            CompassUtils.getCommonMarkersEnabled().get(player.getUniqueId()).remove(args[0].toString());
            CompassUtils.render(player);
            player.sendMessage(ChatColor.GREEN + "Common marker disabled!");
        } else {
            player.sendMessage(ChatColor.RED + "You already have this common marker disabled!");
        }
    }
}