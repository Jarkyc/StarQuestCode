package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class AllPlayersTargetSelector extends TargetSelector {
    public AllPlayersTargetSelector() {
        super(false);
    }

    public boolean isSelected(LivingEntity entity) {
        return entity instanceof Player;
    }
}
