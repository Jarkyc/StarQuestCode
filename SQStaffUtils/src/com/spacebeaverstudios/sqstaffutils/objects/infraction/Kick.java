package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import net.essentialsx.api.v2.events.UserKickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Kick extends Infraction {
    private final String reason;

    public Kick(UserKickEvent event) {
        super(new InfractionSender(event.getKicker().getBase().getUniqueId()), event.getKicked().getBase().getUniqueId(),
                Instant.now().getEpochSecond());
        this.reason = event.getReason();
    }
    public Kick(String[] saveString) {
        super (new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
        StringBuilder reason = new StringBuilder();
        for (int i = 4; i < saveString.length; i++) {
            reason.append(saveString[i].replace("\\n", "\n"));
        }
        this.reason = reason.toString();
    }

    public InfractionLogEvent getInfractionLogEvent() {
        return new InfractionLogEvent(sender.getName() + " kicked " + Bukkit.getOfflinePlayer(target).getName(), reason, sender);
    }

    public GUIItem getGUIItem() {
        return new GUIItem("Kick", ChatColor.WHITE + reason + ChatColor.GOLD
                + "\n Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000), Material.IRON_DOOR, null);
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id + "," + reason.replace("\n", "\\n");
    }
}
