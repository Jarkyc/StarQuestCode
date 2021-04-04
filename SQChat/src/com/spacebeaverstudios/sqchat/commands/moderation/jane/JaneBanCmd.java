package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OnlinePlayerArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JaneBanCmd extends SQCmd {
    public JaneBanCmd() {
        super("ban", "Bans someone from speaking to Jane.", false);
        this.addArgument(new OnlinePlayerArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        UUID uuid = ((Player) args[0]).getUniqueId();
        if (JaneUtils.getBannedPlayers().contains(uuid)) {
            sender.sendMessage(ChatColor.RED + "That player is already banned from using Jane!");
        } else {
            JaneUtils.getBannedPlayers().add(uuid);
            sender.sendMessage(ChatColor.GREEN + "That player has been banned from using Jane.");
        }
    }
}
