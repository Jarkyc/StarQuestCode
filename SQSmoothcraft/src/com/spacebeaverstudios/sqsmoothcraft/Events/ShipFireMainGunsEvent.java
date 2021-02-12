package com.spacebeaverstudios.sqsmoothcraft.Events;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ShipFireMainGunsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Ship ship;
    private boolean isCancelled = false;

    public ShipFireMainGunsEvent(Player player, Ship ship){
        this.player = player;
        this.ship = ship;
    }

    public Player getPlayer(){
        return this.player;
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
