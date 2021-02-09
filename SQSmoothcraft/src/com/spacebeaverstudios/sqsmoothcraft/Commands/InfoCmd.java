package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (ShipUtils.getShipByPlayer(player) != null) {
            player.openInventory(ShipUtils.getShipByPlayer(player).infoWindow);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You must be flying a ship to use this command!");
            return true;
        }
    }
}
