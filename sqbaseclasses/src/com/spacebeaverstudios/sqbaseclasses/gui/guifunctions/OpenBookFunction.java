package com.spacebeaverstudios.sqbaseclasses.gui.guifunctions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class OpenBookFunction extends GUIFunction {
    private final ItemStack book;

    public OpenBookFunction(ItemStack item) {
        this.book = item;
    }

    @Override
    public void run(Player player) {
        if (this.book.getType() != Material.WRITTEN_BOOK) {
            player.sendMessage(ChatColor.RED + "Attempted to open an item that isn't a valid book! Please let a dev know!");
        } else {
            player.openBook(this.book);
        }
    }
}
