package com.spacebeaverstudios.sqcombatnpcs.listeners;

import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.CombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.turret.Turret;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EntityListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent event) {
        if (CombatNPC.getNPCsByEntity().containsKey(event.getEntity())) {
            if (event.getDamager() instanceof LivingEntity
                    && !CombatNPC.getNPCsByEntity().get(event.getEntity())
                    .getEnemies().isSelected((LivingEntity) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (CombatNPC.getNPCsByEntity().containsKey(event.getEntity())) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            CombatNPC.getNPCsByEntity().get(event.getEntity()).die();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        // prevent shulker turrets from teleporting
        if (CombatNPC.getNPCsByEntity().get(event.getEntity()) instanceof Turret) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        // prevent shulker turrets from firing during their AI = true phases
        if (event.getEntity().getShooter() instanceof LivingEntity
                && CombatNPC.getNPCsByEntity().get((LivingEntity) event.getEntity().getShooter()) instanceof Turret) {
            event.setCancelled(true);
        }
    }
}
