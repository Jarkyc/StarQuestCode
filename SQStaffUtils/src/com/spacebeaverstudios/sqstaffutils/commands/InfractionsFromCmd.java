package com.spacebeaverstudios.sqstaffutils.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqstaffutils.commands.arguments.InfractionSenderArgument;
import com.spacebeaverstudios.sqstaffutils.gui.InfractionsGUI;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfractionsFromCmd extends SQCmd {
    public InfractionsFromCmd() {
        super("infractionsfrom", "See infractions made by a player.", true, "sqstaffutils.infractionsfrom");
        this.addArgument(new InfractionSenderArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        (new InfractionsGUI((InfractionSender) args[0])).open((Player) sender);
    }
}
