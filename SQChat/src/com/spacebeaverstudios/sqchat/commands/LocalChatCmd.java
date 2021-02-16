package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocalChatCmd extends SQCmd {
    public LocalChatCmd() {
        super("l", "Sets player's chat channel to local.", true);

        this.addAliase("l");
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        ChatUtils.getPlayerChannels().put((Player) sender, ChatUtils.Channel.LOCAL);
        sender.sendMessage("You have been put into" + ChatColor.YELLOW + " Local " + ChatColor.WHITE + "chat!");
    }
}
