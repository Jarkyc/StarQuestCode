package com.spacebeaverstudios.sqstaffutils.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OfflinePlayerArgument;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Warning;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCmd extends SQCmd {
    public WarnCmd() {
        super("warn", "Warn a player.", false, "sqstaffutils.warn");
        this.addArgument(new OfflinePlayerArgument("player"));
        this.addArgument(new StringArgument("message"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        Warning warning = new Warning(new InfractionSender(sender instanceof Player ? ((Player) sender).getUniqueId() : null),
                ((OfflinePlayer) args[0]).getUniqueId(), message.toString());

        if (warning.delivered) {
            sender.sendMessage(ChatColor.GREEN + "Your warning has been delivered!");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Your warning has been logged. It will be delivered when the warned player logs in.");
        }
    }
}
