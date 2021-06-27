package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Ban extends Infraction {
    private final long duration;

    public Ban(BanEntry banEntry) {
        super((banEntry.getSource().equals("Console") ? null : Bukkit.getPlayerUniqueId(banEntry.getSource())),
                Bukkit.getPlayerUniqueId(banEntry.getTarget()), banEntry.getCreated().toInstant().getEpochSecond());

        if (banEntry.getExpiration() == null) {
            duration = -1;
        } else {
            duration = banEntry.getExpiration().toInstant().getEpochSecond() - date;
        }
    }

    public GUIItem getEntry() {
        String name = ChatColor.WHITE + "Ban (";
        long expiry = (duration == -1 ? Long.MAX_VALUE : date + duration);

        // find if ban is active, expired, or cancelled
        boolean found = false;
        Date unbanned = null;
        ArrayList<Infraction> infractions = Infraction.infractionsToPlayer(target);
        Collections.reverse(infractions);
        for (int i = infractions.indexOf(this) + 1; i < infractions.size(); i++) {
            if (infractions.get(i) instanceof Unban) {
                if (infractions.get(i).date < expiry) {
                    name += ChatColor.GREEN + "Cancelled" + ChatColor.WHITE + ")";
                    unbanned = new Date(infractions.get(i).date * 1000);
                    found = true;
                }
                break;
            }
        }
        if (!found) {
            if (expiry < Instant.now().getEpochSecond()) {
                name += ChatColor.GREEN + "Expired" + ChatColor.WHITE + ")";
            } else {
                name += ChatColor.RED + "Active" + ChatColor.WHITE + ")";
            }
        }

        // lore
        String lore = ChatColor.GOLD + "Target: " + ChatColor.AQUA + Bukkit.getPlayer(target).getDisplayName() + ChatColor.GOLD
                + "\n Sender: " + ChatColor.AQUA + (sender == null ? "Console" : Bukkit.getPlayer(sender).getDisplayName())
                + ChatColor.GOLD + "\n Date: " + ChatColor.AQUA + new Date(date * 1000) + ChatColor.GOLD
                + "\n Duration: " + ChatColor.AQUA + durationString(duration) + ChatColor.GOLD + "\n ";
        if (unbanned != null) {
            lore += "Unbanned: " + unbanned;
        } else if (duration != -1) {
            if (expiry > Instant.now().getEpochSecond()) {
                lore += "Expired: " + new Date(expiry * 1000);
            } else {
                lore += "Expires: " + new Date(expiry * 1000);
            }
        }

        return new GUIItem(name, lore, (duration == -1 ? Material.RED_CONCRETE : Material.YELLOW_CONCRETE), null);
    }

    public String saveString() {
        // TODO
    }
}
