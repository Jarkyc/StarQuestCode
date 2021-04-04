package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class JaneBansCmd extends SQCmd {
    public JaneBansCmd() {
        super("bans", "See the list of players banned from speaking to Jane.", false);
    }

    public void onExecute(CommandSender sender, String previousLables, Object[] args) {
        if (JaneUtils.getBannedPlayers().size() == 0) {
            sender.sendMessage(ChatColor.RED + "No one has been banned from speaking to Jane!");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Here's a list of everyone who has been banned from speaking to Jane:");
            for (UUID uuid : JaneUtils.getBannedPlayers()) {
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                if (name == null) {
                    sender.sendMessage(ChatColor.AQUA + "Unknown player with UUID " + uuid.toString());
                } else {
                    sender.sendMessage(ChatColor.AQUA + name);
                }
            }
        }
    }
}
