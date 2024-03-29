package com.spacebeaverstudios.sqchat.commands.moderation;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OnlinePlayerArgument;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Unmute;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCmd extends SQCmd {
    public UnmuteCmd() {
        super("unmute", "Unmutes a player.", false);
        addArgument(new OnlinePlayerArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player target = (Player) args[0];

        if (!ChatUtils.getMutedPlayers().containsKey(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + target.getDisplayName() + " isn't muted!");
            return;
        }

        ChatUtils.getMutedPlayers().remove(target.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + target.getDisplayName() + " has been unmuted!");

        new Unmute(new InfractionSender(sender instanceof Player ? ((Player) sender).getUniqueId() : null), target.getUniqueId());
    }
}
