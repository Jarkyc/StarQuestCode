package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.commands.arguments.MutedPlayerArgument;
import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCmd extends SQCmd {
    public UnmuteCmd() {
        super("unmute", "Unmutes a player.", false);
        addArgument(new MutedPlayerArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player target = (Player) args[0];

        if (!ChatUtils.getMutedPlayers().containsKey(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + target.getDisplayName() + " isn't muted!");
            return;
        }

        ChatUtils.getMutedPlayers().remove(target.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + target.getDisplayName() + " has been unmuted!");
    }
}
