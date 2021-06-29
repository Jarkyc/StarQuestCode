package com.spacebeaverstudios.sqstaffutils.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.OfflinePlayerArgument;
import com.spacebeaverstudios.sqstaffutils.gui.InfractionsGUI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfractionsCmd extends SQCmd {
    public InfractionsCmd() {
        super("infractions", "See a player's infractions.", true, "sqstaffutils.infractions");
        this.addArgument(new OfflinePlayerArgument("player"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        (new InfractionsGUI(((OfflinePlayer) args[0]).getUniqueId(), true)).open((Player) sender);
    }
}
