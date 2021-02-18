package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Jammer extends Module {


    public Jammer() {
        super(1, false);
    }

    @Override
    public void activate(Ship ship, Player player) {
        if(this.active){
            this.active = false;
            player.sendMessage(ChatColor.GREEN + "Jammer Activated");
        } else {
            this.active = false;
            player.sendMessage(ChatColor.RED + "Jammer Deactivated");
        }
    }


}
