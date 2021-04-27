package com.spacebeaverstudios.sqsmoothcraft.Events;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class ShipAutopilotEvent extends Event implements Cancellable {

    private static final HandlerList handles = new HandlerList();

    private Pilot pilot;
    private Ship ship;
    private Vector vector;

    private boolean isCancelled = false;

    public ShipAutopilotEvent(Pilot pilot, Ship ship, Vector vector){
        this.pilot = pilot;
        this.ship = ship;
        this.vector = vector;
    }

    public Pilot getPilot(){
        return this.pilot;
    }

    public Ship getShip(){
        return this.ship;
    }

    public Vector getVector(){
        return this.vector;
    }

    @Override
    public HandlerList getHandlers(){
        return handles;
    }

    public static HandlerList getHandlerList() {
        return handles;
    }

    @Override
    public void setCancelled(boolean cancelled){
        this.isCancelled = cancelled;
    }

    @Override
    public boolean isCancelled(){
        return this.isCancelled;
    }
}
