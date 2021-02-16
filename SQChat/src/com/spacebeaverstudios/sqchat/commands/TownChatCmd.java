package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TownChatCmd extends SQCmd {
    public TownChatCmd() {
        super("tc", "Sets player's chat channel to town.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        sender.sendMessage(ChatColor.RED + "This hasn't been implemented yet! " +
                "If towns are actually in the game, then please let a staff member know because this should be implemented.");
    }
}
