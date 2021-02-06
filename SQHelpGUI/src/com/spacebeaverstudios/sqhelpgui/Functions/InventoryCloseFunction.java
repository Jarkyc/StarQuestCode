package com.spacebeaverstudios.sqhelpgui.Functions;

import org.bukkit.entity.Player;

public class InventoryCloseFunction extends Function{

    @Override
    public void run(Player player){

        player.closeInventory();

    }


}
