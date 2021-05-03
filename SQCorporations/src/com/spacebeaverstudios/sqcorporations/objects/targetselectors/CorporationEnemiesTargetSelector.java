package com.spacebeaverstudios.sqcorporations.objects.targetselectors;

import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.CombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import com.spacebeaverstudios.sqcorporations.objects.Corporation;
import com.spacebeaverstudios.sqcorporations.objects.ReputationLevel;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CorporationEnemiesTargetSelector extends TargetSelector {
    private final Corporation corporation;

    public CorporationEnemiesTargetSelector(Corporation corporation) {
        super(true);
        this.corporation = corporation;
    }

    public boolean isSelected(LivingEntity entity) {
        if (entity instanceof Player) {
            // players below NEUTRAL
            return ReputationLevel.getLevel(corporation.getData().getReputation((Player) entity)).ordinal() > 3;
        } else {
            // only target CombatNPCs that have a CorporationEnemiesTargetSelector of an enemy corporation
            return CombatNPC.getNPCsByEntity().containsKey(entity)
                    && CombatNPC.getNPCsByEntity().get(entity).getEnemies() instanceof CorporationEnemiesTargetSelector
                    && CombatNPC.getNPCsByEntity().get(entity).getEnemies() != this;
        }
    }
}
