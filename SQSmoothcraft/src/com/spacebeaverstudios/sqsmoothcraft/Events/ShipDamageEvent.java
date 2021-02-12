package com.spacebeaverstudios.sqsmoothcraft.Events;

import com.spacebeaverstudios.sqsmoothcraft.Objects.DamageReason;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.craftbukkit.libs.org.objectweb.asm.Handle;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;


public class ShipDamageEvent extends Event implements Cancellable {

    private static final HandlerList handles = new HandlerList();

    private Player player;
    private Ship ship;
    private int damage;
    private DamageReason reason;

    private boolean isCancelled = false;

    public ShipDamageEvent(Player player, Ship ship, int damage, DamageReason reason){
        this.player = player;
        this.ship = ship;
        this.damage = damage;
        this.reason = reason;
    }

    public Player getPlayer(){
        return this.player;
    }

    public Ship getShip(){
        return this.ship;
    }

    public int getDamage(){
        return this.damage;
    }

    public DamageReason getDamageReason(){
        return this.reason;
    }

    @Override
    public boolean isCancelled(){
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean canclled){
        this.isCancelled = canclled;
    }

    @Override
    public HandlerList getHandlers(){
        return this.getHandlers();
    }

    public static HandlerList getHandlerList() {
        return handles;
    }


}
