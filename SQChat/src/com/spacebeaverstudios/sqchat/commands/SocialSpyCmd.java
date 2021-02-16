package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SocialSpyCmd extends SQCmd {
    public SocialSpyCmd() {
        super("socialspy", "Sets a player in or out of social spy mode.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        if (ChatUtils.getSpies().contains(player)) {
            if (ChatUtils.getSuperSpies().contains(player)) {
                ChatUtils.getSuperSpies().remove(player);
                player.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "SocialSpy" + ChatColor.DARK_BLUE + "]"
                        + ChatColor.WHITE + " You have disabled super social spy.");
            }
            ChatUtils.getSpies().remove(player);
            player.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "SocialSpy" + ChatColor.DARK_BLUE + "]"
                    + ChatColor.WHITE + " You have disabled social spy.");
        } else {
            ChatUtils.getSpies().add(player);
            player.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "SocialSpy" + ChatColor.DARK_BLUE + "]"
                    + ChatColor.WHITE + " You have enabled social spy.");
        }
    }
}
