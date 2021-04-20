package com.spacebeaverstudios.sqtech.objects.machines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.*;

public class BottleFillerMachine extends Machine {
    public BottleFillerMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.CAULDRON);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return new ArrayList<>(Collections.singletonList(schema));
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Bottle Filler");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        Levelled cauldron = (Levelled) getSign().getBlock().getRelative(
                ((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getBlockData();
        if (cauldron.getLevel() == cauldron.getMaximumLevel()) {
            for (ItemStack itemStack : getInventory()) {
                if (itemStack.getType() == Material.GLASS_BOTTLE) {
                    if (tryUsePower(10)) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                        if (itemStack.getAmount() == 0) {
                            getInventory().remove(itemStack);
                        }

                        ItemStack waterBottle = new ItemStack(Material.POTION, 1);
                        PotionMeta waterBottleMeta = (PotionMeta) waterBottle.getItemMeta();
                        waterBottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
                        waterBottle.setItemMeta(waterBottleMeta);
                        tryOutput(waterBottle);

                        sign.setLine(1, ChatColor.GREEN + "Active");
                        sign.setLine(2, "-10 BV/second");
                    } else {
                        sign.setLine(1, ChatColor.RED + "No Power");
                        sign.setLine(2, "0 BV/second");
                    }
                    sign.update();
                    return;
                }
            }
            // no bottles
            sign.setLine(1, ChatColor.RED + "Inactive");
        } else {
            sign.setLine(1, ChatColor.RED + "No Water");
        }
        sign.setLine(2, "0 BV/second");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Bottle Filler";
    }
    public String getMachineInfo() {
        return "Fills glass bottles with water.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 item/second\n "
                + ChatColor.GOLD + "Power Cost: " + ChatColor.GRAY + "10 BV/item";
    }

    public String getSignText() {
        return "[bottle filler]";
    }
}
