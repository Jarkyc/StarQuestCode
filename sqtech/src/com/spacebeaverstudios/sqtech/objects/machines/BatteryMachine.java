package com.spacebeaverstudios.sqtech.objects.machines;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BatteryMachine extends Machine {
    // TODO: multiple sizes? maximum power?
    // TODO: some way to not have all the power vaporise when machine broken
    private Integer power = 0;

    public Integer getPower() {
        return power;
    }
    public void setPower(Integer power) {
        this.power = power;
    }

    public BatteryMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.REDSTONE_BLOCK);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return new ArrayList<>(Collections.singletonList(schema));
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Battery");
        sign.setLine(1, "");
        sign.setLine(2, "0 BV");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(2, power + " BV");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
    public String getMachineName() {
        return "Battery";
    }
    public String getMachineInfo() {
        return "Stores BV.";
    }

    public String getSignText() {
        return "[battery]";
    }
    public String getCustomSaveText() {
        return power.toString();
    }
    public void loadCustomSaveText(String text) {
        power = Integer.parseInt(text);
    }
}
