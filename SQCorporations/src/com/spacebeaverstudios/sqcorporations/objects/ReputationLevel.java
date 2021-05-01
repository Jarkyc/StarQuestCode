package com.spacebeaverstudios.sqcorporations.objects;

import com.spacebeaverstudios.sqcorporations.SQCorporations;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public enum ReputationLevel {
    EXECUTIVE,  // ordinal: 0
    MANAGER,    // ordinal: 1
    PARTNER,    // ordinal: 2
    CONTRACTOR, // ordinal: 3
    NEUTRAL,    // ordinal: 4
    LIABILITY,  // ordinal: 5
    COMPETITOR, // ordinal: 6
    RIVAL;      // ordinal: 7

    private static final HashMap<ReputationLevel, Integer> minimums = new HashMap<>();

    public static void initializeMinimums() {
        ConfigurationSection configSection = SQCorporations.getInstance().getConfig().getConfigurationSection("rank-minimums");
        for (ReputationLevel level : ReputationLevel.values()) {
            minimums.put(level, configSection.getInt(level.toString()));
        }
    }

    public static ReputationLevel getLevel(int reputation) {
        // should be in order because of how they were initialized
        for (ReputationLevel level : minimums.keySet()) {
            if (reputation >= minimums.get(level)) {
                return level;
            }
        }
        return RIVAL;
    }
}
