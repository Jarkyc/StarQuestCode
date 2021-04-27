package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DeleteArmorStandsCmd extends SQCmd {
    public DeleteArmorStandsCmd() {
        super("deletearmorstands", "Deletes all armor stands in a 20 block radius.", true, "SQSmoothcraft.deletearmorstands");
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        for (Entity armorStand : ((Player) sender).getLocation().getNearbyEntitiesByType(ArmorStand.class, 20)) {
            armorStand.remove();
        }
    }
}
