package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

@SuppressWarnings("unused")
public class AllHostileMobsTargetSelector extends TargetSelector {
    public AllHostileMobsTargetSelector() {
        super(true);
    }

    public boolean isSelected(LivingEntity entity) {
        return entity instanceof Monster;
    }
}
