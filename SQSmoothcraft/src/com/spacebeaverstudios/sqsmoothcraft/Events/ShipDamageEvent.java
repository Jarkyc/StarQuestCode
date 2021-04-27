package com.spacebeaverstudios.sqsmoothcraft.Events;

import com.spacebeaverstudios.sqsmoothcraft.Objects.DamageReason;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ShipDamageEvent extends Event implements Cancellable {

    private static final HandlerList handles = new HandlerList();

    private Pilot pilot;
    private Ship ship;
    private int damage;
    private DamageReason reason;

    private boolean isCancelled = false;

    public ShipDamageEvent(Pilot pilot, Ship ship, int damage, DamageReason reason){
        this.pilot = pilot;
        this.ship = ship;
        this.damage = damage;
        this.reason = reason;
    }

    public Pilot getPilot(){
        return this.pilot;
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
