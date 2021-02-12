package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ShipCmd extends SQCmd {
    public ShipCmd() {
        super("ship", "Commands regarding smoothcraft ships", true);

        this.addSubcommand(new InfoCmd());
        this.addSubcommand(new UnpilotCmd());

    }

    @Override
    public void onExecute(CommandSender commandSender, String s, Object[] objects) {
         for(SQCmd cmd : this.getSubcommands()){
             commandSender.sendMessage(ChatColor.DARK_AQUA + cmd.getLabel() + ": " + ChatColor.AQUA + cmd.getDescription());
         }
    }
}
