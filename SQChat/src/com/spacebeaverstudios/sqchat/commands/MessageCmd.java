package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OnlinePlayerArgument;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCmd extends SQCmd {
    public MessageCmd() {
        super("message", "Message a player", true);
        this.addArgument(new OnlinePlayerArgument("player"));

        this.addAliase("msg");
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        Player target = (Player) args[0];

        if (ChatUtils.getMutedPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are muted!");
            return;
        }
        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "You can't message yourself!");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please input a message to send!");
            return;
        }

        if (ChatUtils.getMutedPlayers().containsKey(target.getUniqueId()))
            player.sendMessage(ChatColor.RED + "The person you are messaging is muted, and won't be able to respond.");

        ChatUtils.getReplies().put(player, target);

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) message.append(args[i]);

        player.sendMessage(ChatColor.BLUE + "[" + ChatColor.AQUA + "You -> " + target.getDisplayName()
                + ChatColor.BLUE + "] " + ChatColor.WHITE + message);
        target.sendMessage(ChatColor.BLUE + "[" + ChatColor.AQUA + player.getDisplayName() + " -> You"
                + ChatColor.BLUE + "] " + ChatColor.WHITE + message);

        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
