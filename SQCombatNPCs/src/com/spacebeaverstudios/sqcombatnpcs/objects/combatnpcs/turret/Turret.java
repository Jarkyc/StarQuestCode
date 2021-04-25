package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.turret;

import com.spacebeaverstudios.sqcombatnpcs.SQCombatNPCs;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.EntityCombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Shulker;

public abstract class Turret extends EntityCombatNPC {
    protected boolean open = false;
    protected final Location fireFrom;

    public Turret(Location location, DyeColor color, TargetSelector enemies, int health) {
        super(location.getWorld().spawn(location, Shulker.class), enemies);
        Shulker shulker = (Shulker) entity;
        shulker.setColor(color);
        shulker.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        shulker.setHealth(health);
        shulker.setAI(false);
        this.fireFrom = shulker.getLocation().clone().add(0.5, 0.5, 0.5); // getEyeLocation() doesn't work right
    }

    protected void open() {
        if (!open) {
            open = true;
            entity.setAI(true);
            ((Shulker) entity).setTarget(entity);
            SQCombatNPCs.getInstance().getServer().getScheduler()
                    .scheduleSyncDelayedTask(SQCombatNPCs.getInstance(), () -> entity.setAI(false), 12);
        }
    }

    // TODO: target setting doesn't work properly if a player is nearby
    // TODO: doesn't always close fully
    protected void close() {
        if (open) {
            open = false;
            entity.setAI(true);
            ((Shulker) entity).setTarget(null);
            SQCombatNPCs.getInstance().getServer().getScheduler()
                    .scheduleSyncDelayedTask(SQCombatNPCs.getInstance(), () -> entity.setAI(false), 12);
        }
    }
}
