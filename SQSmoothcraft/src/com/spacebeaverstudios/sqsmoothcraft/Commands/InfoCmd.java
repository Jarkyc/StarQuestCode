package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCmd extends SQCmd {

    public InfoCmd() {
        super("info", "Tells you information regarding the ship you are currently piloting", true);
    }


    @Override
    public void onExecute(CommandSender commandSender, String s, Object[] objects) {
        Player player = (Player) commandSender;
        if (ShipUtils.getShipByPlayer(player) != null) {
            player.openInventory(ShipUtils.getShipByPlayer(player).infoWindow);
            System.out.println(ShipUtils.getShipByPlayer(player).getShipClass());
        } else {
            player.sendMessage(ChatColor.RED + "You must be flying a ship to use this command!");
        }


    }
}
