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

public class Note extends Infraction {
    private final String message;

    public Note(InfractionSender sender, UUID target, String message) {
        super(sender, target, Instant.now().getEpochSecond());
        this.message = message;
    }
    public Note(String[] saveString) {
        super (new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
        StringBuilder message = new StringBuilder();
        for (int i = 4; i < saveString.length; i++) {
            message.append(saveString[i].replace("\\n", "\n"));
        }
        this.message = message.toString();
    }

    public InfractionLogEvent getInfractionLogEvent() {
        return new InfractionLogEvent(sender.getName() + " made a note about " + Bukkit.getOfflinePlayer(target).getName(), message, sender);
    }

    public GUIItem getGUIItem() {
        return new GUIItem("Note", ChatColor.WHITE + message + ChatColor.GOLD
                + "\n Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000), Material.WRITABLE_BOOK, null);
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id + "," + message.replace("\n", "\\n");
    }
}
