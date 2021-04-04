package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OnlinePlayerArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JaneUnbanCmd extends SQCmd {
    public JaneUnbanCmd() {
        super("unbban", "Unbans someone from speaking to Jane.", false);
        this.addArgument(new OnlinePlayerArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        UUID uuid = ((Player) args[0]).getUniqueId();
        if (JaneUtils.getBannedPlayers().contains(uuid)) {
            JaneUtils.getBannedPlayers().remove(uuid);
            sender.sendMessage(ChatColor.GREEN + "That player has been unbanned from using Jane.");
        } else {
            sender.sendMessage(ChatColor.RED + "That player isn't already banned from using Jane!");
        }
    }
}
