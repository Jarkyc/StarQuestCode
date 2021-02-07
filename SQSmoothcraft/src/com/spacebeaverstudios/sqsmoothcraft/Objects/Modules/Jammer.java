package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Jammer extends Module {
    public Jammer(int slotCost, boolean passive) {
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
