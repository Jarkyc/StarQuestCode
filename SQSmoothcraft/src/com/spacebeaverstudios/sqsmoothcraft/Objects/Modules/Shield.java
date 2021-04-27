package com.spacebeaverstudios.sqsmoothcraft.Objects.Modules;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.ChatColor;

public class Shield extends Module {

    public int addedHealth = 100;

    public Shield() {
        super(1, false);
    }

    @Override
    public void activate(Ship ship, Pilot pilot) {
        if(this.active){
            this.active = false;
            pilot.getEntity().sendMessage(ChatColor.RED + "Shield Generator Deactivated");
            regen();
        } else {
         this.active = true;
         pilot.getEntity().sendMessage(ChatColor.GREEN + "Shield Generator Activated" );
        }
    }

    private void regen(){
        // Regenerate ship's shield health += to max health
    }
}
