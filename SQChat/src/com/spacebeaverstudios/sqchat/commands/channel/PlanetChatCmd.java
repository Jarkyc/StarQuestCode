package com.spacebeaverstudios.sqchat.commands.channel;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlanetChatCmd extends SQCmd {
    public PlanetChatCmd() {
        super("p", "Sets player's chat channel to planet.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        ChatUtils.getPlayerChannels().put((Player) sender, ChatUtils.Channel.PLANET);
        sender.sendMessage("You have been put into" + ChatColor.BLUE + " Planet " + ChatColor.WHITE + "chat!");
    }
}
