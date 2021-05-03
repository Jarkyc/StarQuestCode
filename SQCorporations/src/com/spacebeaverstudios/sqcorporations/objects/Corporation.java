package com.spacebeaverstudios.sqcorporations.objects;

import com.spacebeaverstudios.sqcorporations.SQCorporations;
import com.spacebeaverstudios.sqcorporations.objects.facility.Facility;
import com.spacebeaverstudios.sqcorporations.objects.targetselectors.CorporationEnemiesTargetSelector;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class Corporation {
    // static
    private static final ArrayList<Corporation> corporations = new ArrayList<>();

    public static ArrayList<Corporation> getCorporations() {
        return corporations;
    }

    public static void loadCorporations() {
        File folder = new File(SQCorporations.getInstance().getDataFolder().getAbsolutePath());
        if (!folder.mkdirs()) {
            for (File file : folder.listFiles()) {
                if (file.getName().startsWith("corp-")) {
                    new Corporation(file);
                }
            }
        }
    }

    // instance
    private final YamlConfiguration config;
    private final CorporationData data;
    private final ArrayList<Facility> facilities = new ArrayList<>();
    private final CorporationEnemiesTargetSelector targetSelector = new CorporationEnemiesTargetSelector(this);

    public YamlConfiguration getConfig() {
        return config;
    }
    public CorporationData getData() {
        return data;
    }
    public ArrayList<Facility> getFacilities() {
        return facilities;
    }
    public CorporationEnemiesTargetSelector getTargetSelector() {
        return targetSelector;
    }

    public Corporation(File configFile) {
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.data = new CorporationData(new File(SQCorporations.getInstance().getDataFolder().getAbsolutePath() + "/"
                + config.getString("data-file")));
        this.facilities.addAll(data.getFacilities());
        corporations.add(this);
    }
}
