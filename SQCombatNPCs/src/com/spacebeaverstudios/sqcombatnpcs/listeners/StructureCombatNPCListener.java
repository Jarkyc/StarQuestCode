package com.spacebeaverstudios.sqcombatnpcs.listeners;

import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.AutoTurretCombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.StructureCombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.AllHostileMobsTargetSelector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;

public class StructureCombatNPCListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location location = event.getClickedBlock().getLocation();
            if (StructureCombatNPC.getNPCsByBlock().containsKey(location)) {
                StructureCombatNPC.getNPCsByBlock().get(location).damage(1); // TODO: get damage of held item
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitBlock() != null && StructureCombatNPC.getNPCsByBlock().containsKey(event.getHitBlock().getLocation())) {
            StructureCombatNPC.getNPCsByBlock().get(event.getHitBlock().getLocation()).damage(1); // TODO: get damage of projectile
        }
        // TODO
    }

    @EventHandler
    public void test(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            new AutoTurretCombatNPC(new ArrayList<>(Collections.singletonList(event.getBlock().getLocation())),
                    event.getBlock().getLocation(), new AllHostileMobsTargetSelector(),
                    20, 20, 4, 20, 20);
        }
    }

    // TODO: cannons
}
