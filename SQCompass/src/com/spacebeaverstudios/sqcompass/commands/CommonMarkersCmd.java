package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.gui.CommonMarkersGUI;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommonMarkersCmd extends SQCmd {
    public CommonMarkersCmd() {
        super("commonmarkers", "Lists all common markers and allows them to be toggled.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        (new CommonMarkersGUI()).open((Player) sender);
    }
}
