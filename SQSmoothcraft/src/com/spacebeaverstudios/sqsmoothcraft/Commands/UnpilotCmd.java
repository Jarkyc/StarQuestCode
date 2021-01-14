package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnpilotCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if(ShipUtils.isAPilot(player)){
            player.leaveVehicle();
            ShipUtils.getShipByPlayer(player).buildSolid();
        } else {
            player.sendMessage(ChatColor.RED + "You are not piloting a ship!");
        }
        return true;


    }
}
