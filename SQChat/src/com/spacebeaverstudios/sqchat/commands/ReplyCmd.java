package com.spacebeaverstudios.sqchat.commands;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCmd extends SQCmd {
    public ReplyCmd() {
        super("reply", "Reply to a message.", true);

        this.addAliase("r");
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;

        if (ChatUtils.getMutedPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are muted!");
            return;
        }
        if (!ChatUtils.getReplies().containsKey(player) || !ChatUtils.getReplies().get(player).isOnline()) {
            player.sendMessage(ChatColor.RED + "You have no message to reply to!");
            return;
        }

        Player target = ChatUtils.getReplies().get(player);

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Please input a message to send!");
            return;
        }

        if (ChatUtils.getMutedPlayers().containsKey(target.getUniqueId()))
            player.sendMessage(ChatColor.RED + "The person you are replying to is muted, and won't be able to respond.");

        ChatUtils.getReplies().put(player, target);

        StringBuilder message = new StringBuilder();
        for (Object arg : args) message.append(arg);

        player.sendMessage(ChatColor.BLUE + "[" + ChatColor.AQUA + "You -> " + target.getDisplayName()
                + ChatColor.BLUE + "] " + ChatColor.WHITE + message);
        target.sendMessage(ChatColor.BLUE + "[" + ChatColor.AQUA + player.getDisplayName() + " -> You"
                + ChatColor.BLUE + "] " + ChatColor.WHITE + message);

        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
}
