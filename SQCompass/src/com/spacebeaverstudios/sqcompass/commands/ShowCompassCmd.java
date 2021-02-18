package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowCompassCmd extends SQCmd {
    public ShowCompassCmd() {
        super("show", "Show the compass display", true);
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        if (CompassUtils.getShowCompassHashMap().get(player)) player.sendMessage(ChatColor.RED + "The compass is already shown to you!");
        else {
            // TODO: doesn't work
            CompassUtils.getShowCompassHashMap().put(player, true);
            CompassUtils.render(player);
            player.sendMessage(ChatColor.GREEN + "The compass has been shown to you.");
        }
    }
}
