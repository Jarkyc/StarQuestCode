package com.spacebeaverstudios.sqtech.objects.machines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CoalGeneratorMachine extends Machine {
    public CoalGeneratorMachine(Block sign) {
        super(sign, "Coal Generator");
    }

    public HashMap<Vector, Material> getSchema() {
        // TODO: better design
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Machine.getMachines().add(this);
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, "");
        sign.setLine(1, ChatColor.BLUE + "Coal Generator");
        sign.setLine(2, "0 POWER_UNITs/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        if (getPowerOutputPipe().connectedToBattery()) {
            for (ItemStack itemStack : getInventory()) {
                if (itemStack.getType().equals(Material.COAL) || itemStack.getType().equals(Material.CHARCOAL)) {
                    getPowerOutputPipe().powerToBattery(400);
                    sign.setLine(2, "400 POWER_UNITs/second");
                    sign.update();
                    return;
                }
            }
            sign.setLine(2, "0 POWER_UNITs/second");
        } else sign.setLine(2, ChatColor.RED + "Not Connected to Battery");
        sign.update();
    }

    public String getMachineInfo() {
        return "Generates POWER_UNITS from coal and charcoal.";
    }
    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.ITEMS);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
}
