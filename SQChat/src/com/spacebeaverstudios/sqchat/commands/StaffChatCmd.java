package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCmd extends SQCmd {
    public StaffChatCmd() {
        super("s", "Sets player's chat channel to staff.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        ChatUtils.getPlayerChannels().put((Player) sender, ChatUtils.Channel.STAFF);
        sender.sendMessage("You have been put into" + ChatColor.AQUA + " Staff " + ChatColor.WHITE + "chat!");
    }
}
