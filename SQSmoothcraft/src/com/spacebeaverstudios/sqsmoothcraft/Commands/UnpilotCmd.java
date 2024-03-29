package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnpilotCmd extends SQCmd {
    public UnpilotCmd() {
        super("unpilot", "Unpilots the ship you are currently flying", true);
    }

    @Override
    public void onExecute(CommandSender commandSender, String s, Object[] objects) {
        Player player = (Player) commandSender;

        if(ShipUtils.isAPilot(player)){
            ShipUtils.getShipByPlayer(player).buildSolid();
        } else {
            player.sendMessage(ChatColor.RED + "You are not piloting a ship!");
        }
    }
}
