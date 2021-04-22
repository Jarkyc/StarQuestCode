package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.entity.Entity;

public abstract class EntityCombatNPC extends CombatNPC {
    protected final Entity entity;

    public EntityCombatNPC(Entity entity, TargetSelector enemies) {
        super(enemies);
        this.entity = entity;
    }
}
