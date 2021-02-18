package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideCompassCmd extends SQCmd {
    public HideCompassCmd() {
        super("hide", "Hide the compass display", true);
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        if (CompassUtils.getShowCompassHashMap().get(player)) {
            CompassUtils.getShowCompassHashMap().put(player, false);
            CompassUtils.getBossBars().get(player).removeAll();
            player.sendMessage(ChatColor.GREEN + "The compass has been hidden from you.");
        } else player.sendMessage(ChatColor.RED + "The compass is already hidden from you!");
    }
}
