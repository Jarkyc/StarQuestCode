package com.spacebeaverstudios.sqcorporations.objects;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqcorporations.SQCorporations;
import com.spacebeaverstudios.sqcorporations.objects.facility.Facility;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CorporationData {
    private final File file;
    private final YamlConfiguration config;

    public CorporationData(File file) {
        this.file = file;
        if (!file.exists()) {
            // initialize default from src/default-corp-data.yml
            try {
                FileUtils.copyFile(
                        new File(SQCorporations.getInstance().getClass().getClassLoader()
                                .getResource("default-corp-data.yml").getPath()),
                        file);
            } catch (IOException e) {
                SQCorporations.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                        + " Couldn't initialize copy of default-corp-data.yml");
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            SQCorporations.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Couldn't save CorporationData");
            e.printStackTrace();
        }
    }

    public int getReputation(Player player) {
        if (config.getConfigurationSection("reputation").contains(player.getUniqueId().toString())) {
            return config.getInt("reputation." + player.getUniqueId());
        } else {
            int newRep = SQCorporations.getInstance().getConfig().getInt("initial-reputation");
            setReputation(player, newRep);
            return 0;
        }
    }

    public void setReputation(Player player, int reputation) {
        config.set("reputation." + player.getUniqueId(), reputation);
        save();
    }

    public int getScrip(Player player) {
        if (config.getConfigurationSection("scrip").contains(player.getUniqueId().toString())) {
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
        // TODO
        return new ArrayList<>();
    }

    public void saveFacility(Facility facility) {
        // TODO
    }
}
