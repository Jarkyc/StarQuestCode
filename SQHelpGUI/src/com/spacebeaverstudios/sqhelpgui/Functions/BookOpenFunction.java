package com.spacebeaverstudios.sqhelpgui.Functions;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookOpenFunction extends Function{

    private ItemStack book;

    public BookOpenFunction(ItemStack item){
        this.book = item;
    }

    @Override
    public void run(Player player){

        if(this.book.getType() != Material.WRITTEN_BOOK){
            player.sendMessage(ChatColor.RED + "Attempted to open an item that isn't a valid book! let a dev know!");
        } else {
            player.openBook(this.book);
        }

    }


}
