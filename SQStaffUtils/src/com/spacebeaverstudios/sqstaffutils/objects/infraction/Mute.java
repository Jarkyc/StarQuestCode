package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Mute extends Infraction {
    private final long duration;

    @SuppressWarnings("unused")
    public Mute(InfractionSender sender, UUID target, long duration) {
        super(sender, target, Instant.now().getEpochSecond());
        this.duration = duration;
    }
    public Mute(String[] saveString) {
        super(new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
        this.duration = Long.parseLong(saveString[4]);
    }

    public InfractionLogEvent getInfractionLogEvent() {
        return new InfractionLogEvent(sender.getName() + " muted " + Bukkit.getOfflinePlayer(target).getName() + " for "
                + durationString(duration), "", sender);
    }

    public GUIItem getGUIItem() {
        String name = "Mute (";
        long expiry = (duration == -1 ? Long.MAX_VALUE : date + duration);

        // find if mute is active, expired, or cancelled
        Infraction unmuted = null;
        ArrayList<Infraction> infractions = Infraction.infractionsToPlayer(target);
        for (int i = infractions.indexOf(this) + 1; i < infractions.size(); i++) {
            if (infractions.get(i) instanceof Unmute) {
                if (infractions.get(i).date < expiry) {
                    name += ChatColor.GREEN + "Cancelled";
                    unmuted = infractions.get(i);
                }
                break;
            }
        }
        if (unmuted == null) {
            if (expiry < Instant.now().getEpochSecond()) {
                name += ChatColor.GREEN + "Expired";
            } else {
                name += ChatColor.RED + "Active";
            }
        }
        name += ChatColor.WHITE + ")";

        // lore
        String lore = ChatColor.GOLD
                + "Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000) + ChatColor.GOLD
                + "\n Duration: " + ChatColor.AQUA + durationString(duration) + ChatColor.GOLD + "\n ";
        if (unmuted != null) {
            lore += "Unmuted: " + ChatColor.AQUA + new Date(unmuted.date * 1000) + " by " + unmuted.sender.getName();
        } else if (duration != -1) {
            if (expiry > Instant.now().getEpochSecond()) {
                lore += "Expires: " + ChatColor.AQUA + new Date(expiry * 1000);
            } else {
                lore += "Expired: " + ChatColor.AQUA + new Date(expiry * 1000);
            }
        }

        return new GUIItem(name, lore, Material.BARRIER, null);
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id + "," + duration;
    }
}
