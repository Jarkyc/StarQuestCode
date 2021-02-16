package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalChatCmd extends SQCmd {
    public GlobalChatCmd() {
        super("g", "Sets player's chat channel to global.", true);

        this.addAliase("global");
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        ChatUtils.getPlayerChannels().put((Player) sender, ChatUtils.Channel.GLOBAL);
        sender.sendMessage("You have been put into" + ChatColor.DARK_GREEN + " Global " + ChatColor.WHITE + "chat!");
    }
}
