package com.spacebeaverstudios.sqcorporations;

import com.spacebeaverstudios.sqcorporations.commands.CorporationCmd;
import com.spacebeaverstudios.sqcorporations.listeners.PlayerListener;
import com.spacebeaverstudios.sqcorporations.objects.Corporation;
import com.spacebeaverstudios.sqcorporations.objects.ReputationLevel;
import com.spacebeaverstudios.sqcorporations.objects.facility.Facility;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SQCorporations extends JavaPlugin {
    private static SQCorporations instance;

    public static SQCorporations getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) {
            this.saveDefaultConfig();
        }

        ReputationLevel.initializeMinimums();
        Corporation.loadCorporations();

        CorporationCmd corporationCmd = new CorporationCmd();
        getCommand("corporation").setExecutor(corporationCmd);
        getCommand("corporation").setTabCompleter(corporationCmd);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, Facility::checkForRemoval, 1200, 1200);
    }
}
