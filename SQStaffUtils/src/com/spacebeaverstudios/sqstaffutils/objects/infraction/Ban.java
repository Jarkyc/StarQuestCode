package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Ban extends Infraction {
    private final long duration;
    private final String reason;

    public Ban(BanEntry banEntry) {
        super(senderFromString(banEntry.getSource()), Bukkit.getPlayerUniqueId(banEntry.getTarget()), Instant.now().getEpochSecond());

        if (banEntry.getExpiration() == null) {
            this.duration = -1;
        } else {
            this.duration = banEntry.getExpiration().toInstant().getEpochSecond() - date;
        }

        this.reason = (banEntry.getReason() == null ? "No reason given." : banEntry.getReason());
    }
    public Ban(String[] saveString) {
        super (new InfractionSender(saveString[0].equals("null") ? null : (UUID.fromString(saveString[0]))), UUID.fromString(saveString[1]),
                Long.parseLong(saveString[2]), Integer.parseInt(saveString[3]));
        this.duration = Long.parseLong(saveString[4]);
        StringBuilder reason = new StringBuilder();
        for (int i = 5; i < saveString.length; i++) {
            reason.append(saveString[i].replace("\\n", "\n"));
        }
        this.reason = reason.toString();
    }

    public InfractionLogEvent getInfractionLogEvent() {
        String name = sender.getName() + " banned " + Bukkit.getOfflinePlayer(target).getName();
        if (duration != -1) {
            name += " for " + durationString(duration);
        }
        return new InfractionLogEvent(name, reason, sender);
    }

    public GUIItem getGUIItem() {
        String name = (duration == -1 ? "" : "Temp ") + "Ban (";
        long expiry = (duration == -1 ? Long.MAX_VALUE : date + duration);

        // find if ban is active, expired, or cancelled
        Infraction unbanned = null;
        ArrayList<Infraction> infractions = Infraction.infractionsToPlayer(target);
        for (int i = infractions.indexOf(this) + 1; i < infractions.size(); i++) {
            if (infractions.get(i) instanceof Unban) {
                if (infractions.get(i).date < expiry) {
                    name += ChatColor.GREEN + "Cancelled";
                    unbanned = infractions.get(i);
                }
                break;
            }
        }
        if (unbanned == null) {
            if (expiry < Instant.now().getEpochSecond()) {
                name += ChatColor.GREEN + "Expired";
            } else {
                name += ChatColor.RED + "Active";
            }
        }
        name += ChatColor.WHITE + ")";

        // lore
        String lore = ChatColor.WHITE + reason + ChatColor.GOLD
                + "\n Target: " + ChatColor.AQUA + Bukkit.getOfflinePlayer(target).getName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD
                + "\n Date: " + ChatColor.AQUA + new Date(date * 1000) + ChatColor.GOLD
                + (duration == -1 ? "" : "\n Duration: " + ChatColor.AQUA + durationString(duration) + ChatColor.GOLD) + "\n ";
        if (unbanned != null) {
            lore += "Unbanned: " + ChatColor.AQUA + new Date(unbanned.date * 1000) + " by " + unbanned.sender.getName();
        } else if (duration != -1) {
            if (expiry > Instant.now().getEpochSecond()) {
                lore += "Expires: " + ChatColor.AQUA + new Date(expiry * 1000);
            } else {
                lore += "Expired: " + ChatColor.AQUA + new Date(expiry * 1000);
            }
        }

        return new GUIItem(name, lore, (duration == -1 ? Material.RED_CONCRETE : Material.YELLOW_CONCRETE), null);
    }

    public String getSaveString() {
        return sender.uuid + "," + target + "," + date + "," + id + "," + duration + "," + reason.replace("\n", "\\n");
    }
}
