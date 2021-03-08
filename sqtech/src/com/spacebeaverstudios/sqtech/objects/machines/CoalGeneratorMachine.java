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
        super(sign, "Coal Generator", "Generates BV from coal and charcoal.\n " + ChatColor.GOLD + "Speed: "
                + ChatColor.GRAY + "1 item/second\n " + ChatColor.GOLD + "Power Generation: " + ChatColor.GRAY + "400 BV/item");
    }

    public HashMap<Vector, Material> getSchema() {
        // TODO: better design
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, ChatColor.BLUE + "Coal Generator");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        if (getPowerOutputPipe() != null && getPowerOutputPipe().connectedToBattery()) {
            for (ItemStack itemStack : getInventory()) {
                if (itemStack.getType().equals(Material.COAL) || itemStack.getType().equals(Material.CHARCOAL)) {
                    itemStack.setAmount(itemStack.getAmount()-1);
                    if (itemStack.getAmount() == 0) {
                        getInventory().remove(itemStack);
                    }
                    getPowerOutputPipe().powerToBattery(400);
                    sign.setLine(1, ChatColor.GREEN + "Active");
                    sign.setLine(2, "+400 BV/second");
                    sign.update();
                    return;
                }
            }
            sign.setLine(1, ChatColor.RED + "Inactive");
        } else {
            sign.setLine(1, ChatColor.RED + "Not Connected");
        }
        sign.setLine(2, "0 BV/second");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.ITEMS);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
}
