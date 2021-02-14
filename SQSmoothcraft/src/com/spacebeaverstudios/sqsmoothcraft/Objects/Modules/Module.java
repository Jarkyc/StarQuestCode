package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Module {

    public int slotCost;
    public boolean active = false;
    public boolean passive;

    private ItemStack item;

    public Module(int slotCost, boolean passive) {
        this.slotCost = slotCost;
        this.passive = passive;
    }

    public abstract void activate(Ship ship, Player player);

    public ItemStack getItem(){
        return this.item;
    }
}
