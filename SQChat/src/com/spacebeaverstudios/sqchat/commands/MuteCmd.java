package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.IntegerArgument;
import com.spacebeaverstudios.sqcore.commands.arguments.OnlinePlayerArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCmd extends SQCmd {
    public MuteCmd() {
        super("mute", "Mutes a player.", false);
        addArgument(new OnlinePlayerArgument("player"));
        addArgument(new IntegerArgument("minutes"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player target = (Player) args[0];

        if (ChatUtils.getMutedPlayers().containsKey(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + target.getDisplayName() + " is already muted!");
            return;
        }

        ChatUtils.getMutedPlayers().put(target.getUniqueId(), (int) args[1]);
        sender.sendMessage(ChatColor.GREEN + target.getDisplayName() + " has been muted for " + args[1] + " minutes!");
    }
}
