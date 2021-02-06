package com.spacebeaverstudios.sqhelpgui.objects;

import com.spacebeaverstudios.sqcore.utils.StringUtils;
import com.spacebeaverstudios.sqhelpgui.Functions.Function;
import com.spacebeaverstudios.sqhelpgui.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GUIItem {

    private final String name;

    private ItemStack item;

    private final Material itemType;

    private final boolean hasFunction;

    private final Function func;

    private final String lore;


    public GUIItem(String name, String lore, Material itemType, Function func) {

        this.name = name;
        this.itemType = itemType;

        this.func = func;

        if (func != null) {
            this.hasFunction = true;
        } else {
            this.hasFunction = false;
        }

        this.lore = lore;

        ItemStack stack = new ItemStack(this.itemType);

        ItemMeta stackMeta = stack.getItemMeta();

        stackMeta.setDisplayName(this.name);

        List<String> loreList = StringUtils.splitStringOverLines(this.lore, "", 20);

        stackMeta.setLore(loreList);

        stack.setItemMeta(stackMeta);

        this.item = stack;

        utils.buttons.add(this);


    }


    public ItemStack getItemStack() {

        return this.item;

    }

    public String getName() {

        return this.name;

    }

    public boolean hasFunction() {

        return this.hasFunction;

    }

    public void runFunction(Player player) {

        this.func.run(player);

    }


}
