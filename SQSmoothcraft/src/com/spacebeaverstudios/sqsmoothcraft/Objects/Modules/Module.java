package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public abstract class Module implements Serializable {

    public int slotCost;
    public boolean active = false;
    public boolean passive;

    private ItemStack item;

    public Module(int slotCost, boolean passive) {
        this.slotCost = slotCost;
        this.passive = passive;
    }

    public abstract void activate(Ship ship, Pilot pilot);

    public ItemStack getItem(){
        return this.item;
    }
}
