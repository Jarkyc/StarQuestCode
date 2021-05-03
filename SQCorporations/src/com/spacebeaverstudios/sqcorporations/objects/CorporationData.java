package com.spacebeaverstudios.sqcorporations.objects;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqcorporations.SQCorporations;
import com.spacebeaverstudios.sqcorporations.objects.facility.Facility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CorporationData {
    private final File file;
    private final YamlConfiguration config;
    private int mostRecentSaveTick = 0;

    public CorporationData(File file) {
        this.file = file;
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config = new YamlConfiguration();
        }
    }

    private void save() {
        // only bother saving once per tick
        if (Bukkit.getCurrentTick() != mostRecentSaveTick) {
            mostRecentSaveTick = Bukkit.getCurrentTick();
            SQCorporations.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SQCorporations.getInstance(), () -> {
                try {
                    config.save(file);
                } catch (IOException e) {
                    SQCorporations.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                            + " Couldn't save CorporationData to " + file.getName());
                    e.printStackTrace();
                    SQCorporations.getInstance().getLogger().info("Full YAML data:");
                    SQCorporations.getInstance().getLogger().info(config.saveToString());
                }
            }, 1);
        }
    }

    public int getReputation(Player player) {
        if (!config.contains("reputation")) {
            int newRep = SQCorporations.getInstance().getConfig().getInt("initial-reputation");
            setReputation(player, newRep);
            return newRep;
        } else if (config.getConfigurationSection("reputation").contains(player.getUniqueId().toString())) {
            return config.getInt("reputation." + player.getUniqueId());
        } else {
            int newRep = SQCorporations.getInstance().getConfig().getInt("initial-reputation");
            setReputation(player, newRep);
            return newRep;
        }
    }

    public void setReputation(Player player, int reputation) {
        config.set("reputation." + player.getUniqueId(), reputation);
        save();
    }

    public int getScrip(Player player) {
        if (!config.contains("scrip")) {
            setScrip(player, 0);
            return 0;
        } else if (config.getConfigurationSection("scrip").contains(player.getUniqueId().toString())) {
            return config.getInt("scrip." + player.getUniqueId());
        } else {
            setScrip(player, 0);
            return 0;
        }
    }

    public void setScrip(Player player, int scrip) {
        config.set("scrip." + player.getUniqueId(), scrip);
        save();
    }

    public ArrayList<Facility> getFacilities() {
        if (!config.contains("facilities")) {
            return new ArrayList<>();
        }
        // TODO
        return new ArrayList<>();
    }

    public void saveFacilityList(ArrayList<Facility> facilities) {
        // TODO
        save();
    }
}
