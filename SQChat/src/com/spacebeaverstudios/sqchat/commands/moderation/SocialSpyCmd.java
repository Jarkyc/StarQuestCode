package com.spacebeaverstudios.sqchat.commands.moderation;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.SelectionArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SocialSpyCmd extends SQCmd {
    public SocialSpyCmd() {
        super("socialspy", "Sets a player in or out of social spy mode.", true);
        this.addArgument(new SelectionArgument("type", Arrays.asList("channels", "messages")));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        if (args[0].toString().equals("channels")) {
            if (ChatUtils.getChannelSpies().contains(player)) {
                ChatUtils.getChannelSpies().remove(player);
                player.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.WHITE + " You have disabled channel spying.");
            } else {
                ChatUtils.getChannelSpies().add(player);
                player.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.WHITE + " You have enabled channel spying.");
            }
        } else {
            if (ChatUtils.getMsgSpies().contains(player)) {
                ChatUtils.getMsgSpies().remove(player);
                player.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.WHITE + " You have disabled message spying.");
            } else {
                ChatUtils.getMsgSpies().add(player);
                player.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.WHITE + " You have enabled message spying.");
            }
        }
    }
}
