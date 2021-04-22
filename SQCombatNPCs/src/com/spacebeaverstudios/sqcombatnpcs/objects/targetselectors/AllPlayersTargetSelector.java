package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AllPlayersTargetSelector extends TargetSelector {
    public boolean isSelected(Entity entity) {
        return entity instanceof Player;
    }
    public boolean canTargetMobs() {
        return false;
    }
}
