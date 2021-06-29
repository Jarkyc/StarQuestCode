package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Warning extends Infraction {
    public final String message;
    public boolean delivered;

    public Warning(InfractionSender sender, UUID target, String message) {
        super(sender, target, Instant.now().getEpochSecond());
        this.message = message;

        Player targetPlayer = Bukkit.getPlayer(this.target);
        if (targetPlayer == null) {
            delivered = false;
        } else {
            delivered = true;
            targetPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + this.sender.getName() + " warned you:"
                    + ChatColor.WHITE + " " + message);
        }
    }
    public Warning(String[] saveString) {
        super (new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
        this.delivered = saveString[4].equals("true");
        StringBuilder message = new StringBuilder();
        for (int i = 5; i < saveString.length; i++) {
            message.append(saveString[i].replace("\\n", "\n"));
        }
        this.message = message.toString();
    }

    public InfractionLogEvent getInfractionLogEvent() {
        return new InfractionLogEvent(sender.getName() + " warned " + Bukkit.getOfflinePlayer(target).getName(), message, sender);
    }

    public GUIItem getGUIItem() {
        return new GUIItem("Warning", ChatColor.WHITE + message + ChatColor.GOLD
                + "\n Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000)
                + (delivered ? "" : ChatColor.RED + "\n Not yet delivered! (Player hasn't logged in)"), Material.BELL, null);
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id + "," + delivered + "," + message.replace("\n", "\\n");
    }
}
