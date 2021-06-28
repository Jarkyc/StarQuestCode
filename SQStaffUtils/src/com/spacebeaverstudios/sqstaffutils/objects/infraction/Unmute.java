package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Unmute extends Infraction {
    @SuppressWarnings("unused")
    public Unmute(InfractionSender sender, UUID target) {
        super(sender, target, Instant.now().getEpochSecond());
    }
    public Unmute(String[] saveString) {
        super(new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
    }

    public InfractionLogEvent getInfractionLogEvent() {
        return new InfractionLogEvent(sender.getName() + " unmuted " + Bukkit.getOfflinePlayer(target).getName(), "", sender);
    }

    public GUIItem getGUIItem() {
        return new GUIItem("Unmute", ChatColor.GOLD
                + "Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000), Material.GREEN_CONCRETE, null); // TODO: material
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id;
    }
}
