package com.spacebeaverstudios.sqhelpgui.listeners;

import com.spacebeaverstudios.sqhelpgui.objects.GUIItem;
import com.spacebeaverstudios.sqhelpgui.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class onClickEvent implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();


        if(e.getCurrentItem() != null && utils.isButton(e.getCurrentItem())){

            GUIItem item = utils.getGUIItem(e.getCurrentItem());

            e.setCancelled(true);

            if(item.hasFunction()){

                item.runFunction(player);

            }


        }

    }

}
