package com.spacebeaverstudios.sqstaffutils.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OfflinePlayerArgument;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Note;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoteCmd extends SQCmd {
    public NoteCmd() {
        super("note", "Attach a note to a player.", false, "sqstaffutils.note");
        this.addArgument(new OfflinePlayerArgument("target"));
        this.addArgument(new StringArgument("message"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        new Note(new InfractionSender(sender instanceof Player ? ((Player) sender).getUniqueId() : null),
                ((OfflinePlayer) args[0]).getUniqueId(), message.toString());

        sender.sendMessage(ChatColor.GREEN + "Your note has been made!");
    }
}
