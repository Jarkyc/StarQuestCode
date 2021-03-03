package com.spacebeaverstudios.sqtech.objects.machines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BatteryMachine extends Machine {
    // TODO: multiple sizes
    private Integer power = 0;

    public Integer getPower() {
        return power;
    }
    public void setPower(Integer power) {
        this.power = power;
    }

    public BatteryMachine(Block sign) {
        super(sign, "Battery");
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.SPONGE); // TODO: different block?
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Machine.getMachines().add(this);
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, "");
        sign.setLine(1, ChatColor.BLUE + "Combustion Generator");
        sign.setLine(2, "0 POWER_UNITs");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(2, power + " POWER_UNITs");
        sign.update();
    }

    public String getMachineInfo() {
        return "Stores POWER_UNITs.";
    }
    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
}
