package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.objects.StaticLocationMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddMarkerCmd extends SQCmd {
    public AddMarkerCmd() {
        super("addmarker", "Adds a custom marker to the compass display. " +
                "If you don't provide coordinates, the marker will be at your location.", true);

        this.addArgument(new StringArgument("name"));
        this.addArgument(new CharArgument("marker (one character)"));
        this.addArgument(new ChatColorArgument("color"));

        this.addOptionalArgument(new WorldArgument("world"));
        this.addOptionalArgument(new IntegerArgument("x"));
        this.addOptionalArgument(new IntegerArgument("z"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        StaticLocationMarker marker;

        if (CompassUtils.getCustomMarkers().get(player.getUniqueId()).size() == 5) {
            player.sendMessage(ChatColor.RED + "You can't have more than 5 custom markers!");
            return;
        }

        if (CompassUtils.getCustomMarkers().get(player.getUniqueId()).containsKey(args[0].toString())) {
            player.sendMessage(ChatColor.RED + "You already have a marker of that name!");
            return;
        }

        if (args.length == 6) marker = new StaticLocationMarker(new Location((World) args[3], (int) args[4], 0, (int) args[5]),
                    (char) args[1], (ChatColor) args[2], 10);
        else marker = new StaticLocationMarker(player.getLocation(), (char) args[1], (ChatColor) args[2], 10);

        CompassUtils.getCompassMarkers().get(player).add(marker);
        CompassUtils.getCustomMarkers().get(player.getUniqueId()).put(args[0].toString(), marker);
        CompassUtils.render(player);

        player.sendMessage(ChatColor.GREEN + "Marker added!");
    }
}
