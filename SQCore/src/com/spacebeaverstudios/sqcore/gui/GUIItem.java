package com.spacebeaverstudios.sqcore.gui;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIItem {
    private final ItemStack item;
    private final GUIFunction function;

    public GUIItem(String name, String lore, Material itemType, GUIFunction function) {
        this.function = function;

        ItemStack stack = new ItemStack(itemType);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.WHITE + name); // puts in white to get rid of italics if there's no specified color
        if (lore != null) {
            List<String> loreList = GUIUtils.splitStringOverLines(ChatColor.WHITE + lore, 40);
            stackMeta.setLore(loreList);
        }
        stack.setItemMeta(stackMeta);
        GUIUtils.setRandomPersistentData(stack);

        this.item = stack;
        GUIUtils.setWanted(item, true);
        GUIUtils.getButtons().add(this);
    }
    public GUIItem(ItemStack item, GUIFunction function) {
        this.item = item;
        this.function = function;
        GUIUtils.setWanted(item, true);
        GUIUtils.getButtons().add(this);
        GUIUtils.setRandomPersistentData(item);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public boolean hasFunction() {
        return function != null;
    }

    public void runFunction(Player player) {
        if (function != null) {
            function.run(player);
        }
    }
}
