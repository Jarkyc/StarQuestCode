package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.objects.CompassMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.SelectionArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ShowCommonMarkerCmd extends SQCmd {
    public ShowCommonMarkerCmd() {
        super("showcommonmarker", "Shows one of the preset markers created by the server", true);

        this.addArgument(new SelectionArgument("marker", new ArrayList<>(CompassUtils.getCommonMarkers().keySet())));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        CompassMarker marker = CompassUtils.getCommonMarkers().get(args[0].toString());
        Player player = (Player) sender;

        if (CompassUtils.getCompassMarkers().get(player).contains(marker)) {
            player.sendMessage(ChatColor.RED + "You already have this marker enabled!");
        } else {
            CompassUtils.getCompassMarkers().get(player).add(marker);
            CompassUtils.render(player);
            player.sendMessage(ChatColor.GREEN + "Marker enabled!");
        }
    }
}
