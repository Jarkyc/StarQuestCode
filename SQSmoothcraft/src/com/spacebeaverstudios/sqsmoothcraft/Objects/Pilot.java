package com.spacebeaverstudios.sqsmoothcraft.Objects;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Pilot {
    // the point of this class is to make ships work with NPCs
    // spaghetti is fun

    private final LivingEntity entity;
    public boolean entitySneaking = false; // will be changed in SQCombatNPCs

    public Pilot(LivingEntity entity) {
        this.entity = entity;
    }

    public ItemStack getItemInMainHand() {
        if (entity instanceof Player) {
            return ((Player) entity).getInventory().getItemInMainHand();
        } else {
            return new ItemStack(Material.CLOCK, 1);
        }
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setFlying(boolean flying) {
        // this won't matter if it's not a player
        if (entity instanceof Player) {
            ((Player) entity).setFlying(flying);
        }
    }

    public OfflinePlayer getSkullPlayer() {
        if (entity instanceof Player) {
            return (Player) entity;
        } else {
            // doesn't matter what this is, as it won't be seen
            // just use blankman's skull lol
            return Bukkit.getOfflinePlayer(UUID.fromString("3cbac417-fbea-4137-8906-666b10b57ec1"));
        }
    }

    public boolean isSneaking() {
        if (entity instanceof Player) {
            return ((Player) entity).isSneaking();
        } else {
            return entitySneaking;
        }
    }

    public void setSneaking(boolean sneaking) {
        // this won't matter if it's not a player
        if (entity instanceof Player) {
            ((Player) entity).setSneaking(sneaking);
        }
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float volume, float pitch) {
        // npcs can't hear
        if (entity instanceof Player) {
            ((Player) entity).playSound(location, sound, soundCategory, volume, pitch);
        }
    }

    public boolean isPlayer() {
        return entity instanceof Player;
    }
}
