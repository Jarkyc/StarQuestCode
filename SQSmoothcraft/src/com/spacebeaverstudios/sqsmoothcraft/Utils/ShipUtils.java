package com.spacebeaverstudios.sqsmoothcraft.Utils;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipBlock;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class ShipUtils {

    public static Ship getShipByPlayer(Player player){
        for(Ship ship : SQSmoothcraft.instance.allShips){
            if(ship.getOwner() == player) return ship;
        }
        return null;
    }

    public static Ship getShipByStand(ArmorStand stand){
        for(Ship ship :SQSmoothcraft.instance.allShips){
            for(ShipBlock block : ship.getBlocks()){
                if(block.armorStand == stand){
                    return ship;
                }
            }
        }
        return null;
    }

    public static boolean isAPilot(Player player){
        return getShipByPlayer(player) != null;
    }


}
