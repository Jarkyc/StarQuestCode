package com.spacebeaverstudios.sqcorporations.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcorporations.objects.Corporation;
import com.spacebeaverstudios.sqcorporations.objects.facility.Outpost;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CorporationCmd extends SQCmd {
    public CorporationCmd() {
        super("corporation", "Command for all things corporation", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        new Outpost(Corporation.getCorporations().get(0), ((Player) sender).getLocation());
        // TODO
    }
}
