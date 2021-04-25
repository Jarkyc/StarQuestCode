package com.spacebeaverstudios.sqcombatnpcs.listeners;

import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.EntityCombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.turret.ParticleTurret;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.turret.Turret;
import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.AllHostileMobsTargetSelector;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EntityListener implements Listener {
    // TODO: remember to remove this
    @SuppressWarnings("unused")
    @EventHandler
    public void spawnCombatNPC(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            event.getBlock().setType(Material.AIR);
            new ParticleTurret(event.getBlock().getLocation(), DyeColor.CYAN, new AllHostileMobsTargetSelector(), 20,
                    20, 4, 10, 15);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent event) {
        if (EntityCombatNPC.getEntityCombatNPCs().containsKey(event.getEntity())) {
            if (event.getDamager() instanceof LivingEntity
                    && !EntityCombatNPC.getEntityCombatNPCs().get(event.getEntity())
                    .getEnemies().isSelected((LivingEntity) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (EntityCombatNPC.getEntityCombatNPCs().containsKey(event.getEntity())) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            EntityCombatNPC.getEntityCombatNPCs().get(event.getEntity()).die();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        // prevent shulker turrets from teleporting
        if (EntityCombatNPC.getEntityCombatNPCs().get(event.getEntity()) instanceof Turret) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // prevent shulker turrets from firing during their AI = true phases
        if (event.getEntity().getShooter() instanceof LivingEntity
                && EntityCombatNPC.getEntityCombatNPCs().get((LivingEntity) event.getEntity().getShooter()) instanceof Turret) {
            event.setCancelled(true);
        }
    }
}
