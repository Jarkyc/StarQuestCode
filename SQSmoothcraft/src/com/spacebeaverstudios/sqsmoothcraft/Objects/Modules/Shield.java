package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Shield extends Module {

    public int addedHealth = 100;

    public Shield() {
        super(1, false);
    }

    @Override
    public void activate(Ship ship, Player player) {
        if(this.active){
            this.active = false;
            player.sendMessage(ChatColor.RED + "Shield Generator Deactivated");
            regen();
        } else {
         this.active = true;
         player.sendMessage(ChatColor.GREEN + "Shield Generator Activated" );
        }
    }

    private void regen(){
        // Regenerate ship's shield health += to max health
    }
}
