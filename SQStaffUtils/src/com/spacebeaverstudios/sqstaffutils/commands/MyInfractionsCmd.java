package com.spacebeaverstudios.sqstaffutils.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqstaffutils.gui.InfractionsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyInfractionsCmd extends SQCmd {
    public MyInfractionsCmd() {
        super("myinfractions", "See your own infractions.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        (new InfractionsGUI(((Player) sender).getUniqueId(), false)).open((Player) sender);
    }
}
