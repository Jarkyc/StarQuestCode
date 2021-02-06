package com.spacebeaverstudios.sqhelpgui.utils;

import com.mysql.fabric.xmlrpc.base.Array;
import com.spacebeaverstudios.sqhelpgui.objects.GUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;

public class utils {

    public static ArrayList<GUIItem> buttons = new ArrayList<>();


    public static boolean isButton(ItemStack itemClicked){

        boolean is = false;

        if(itemClicked == null || itemClicked.getType() == Material.AIR) {

            return is;

        }

        for(GUIItem item : buttons){

            ItemStack stack = item.getItemStack();


            if(stack.getItemMeta().getDisplayName().equals(itemClicked.getItemMeta().getDisplayName()) && stack.getType() == itemClicked.getType()){

                is = true;

                break;

            } else continue;


        }

        return is;

    }


    public static ArrayList<GUIItem> getButtons(){

        return buttons;

    }

    public static GUIItem getGUIItem(ItemStack stack){

        for(GUIItem item : getButtons()){


            if(stack.getType() == item.getItemStack().getType() && stack.getItemMeta().getDisplayName().equals(item.getName())){

                return item;

            } else continue;


        }

        return null;

    }



}
