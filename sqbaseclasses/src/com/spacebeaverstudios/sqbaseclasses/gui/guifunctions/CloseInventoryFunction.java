package com.spacebeaverstudios.sqbaseclasses.gui.guifunctions;

import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class CloseInventoryFunction extends GUIFunction {
    @Override
    public void run(Player player){
        player.closeInventory();
    }
}
