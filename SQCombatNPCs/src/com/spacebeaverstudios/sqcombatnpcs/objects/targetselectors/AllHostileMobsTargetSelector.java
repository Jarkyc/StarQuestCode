package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

public class AllHostileMobsTargetSelector extends TargetSelector {
    public boolean isSelected(Entity entity) {
        return entity instanceof Monster;
    }
    public boolean canTargetMobs() {
        return true;
    }
}
