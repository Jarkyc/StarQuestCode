package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.ChatColor;

public class Jammer extends Module {


    public Jammer() {
        super(1, false);
    }

    @Override
    public void activate(Ship ship, Pilot player) {
        if(this.active){
            this.active = false;
            player.getEntity().sendMessage(ChatColor.GREEN + "Jammer Activated");
        } else {
            this.active = false;
            player.getEntity().sendMessage(ChatColor.RED + "Jammer Deactivated");
        }
    }


}
