package com.spacebeaverstudios.sqsmoothcraft.Events;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ShipFireMainGunsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Pilot pilot;
    private Ship ship;
    private boolean isCancelled = false;

    public ShipFireMainGunsEvent(Pilot pilot, Ship ship){
        this.pilot = pilot;
        this.ship = ship;
    }

    public Pilot getPilot(){
        return this.pilot;
    }

    public Ship getShip(){
        return this.ship;
    }

    @Override
    public void setCancelled(boolean cancelled){
        this.isCancelled = cancelled;
    }

    @Override
    public boolean isCancelled(){
        return this.isCancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

}
