package com.spacebeaverstudios.sqhelpgui.Functions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryOpenFunction extends Function{

    private Inventory inv;


    public InventoryOpenFunction(Inventory inventory) {

        this.inv = inventory;

    }

    @Override
    public void run(Player player){

        player.openInventory(this.inv);

    }


}
