package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SolarPanelMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int POWER_GENERATED;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("SolarPanelMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, SolarPanelMachine::new);
        }
        POWER_GENERATED = configSection.getInt("power-generated");

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.LAPIS_BLOCK);
        schema.put(new Vector(1, 1, 0), Material.DAYLIGHT_DETECTOR);
        SCHEMAS.add(schema);
    }

    // instance
    private Location daylightDetector;

    public SolarPanelMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Solar Panel");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();

        daylightDetector = sign.getBlock().getRelative(((Directional) sign.getBlock().getBlockData())
                .getFacing().getOppositeFace()).getRelative(BlockFace.UP).getLocation();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        if (getPowerOutputPipe() != null && getPowerOutputPipe().connectedToBattery()) {
            if (getPowerOutputPipe().connectedToBatteryWithSpace()) {
                if (daylightDetector.getWorld().getTime() > 0 && daylightDetector.getWorld().getTime() < 12000
                        && daylightDetector.getBlock().getLightFromSky() == 15) {
                    getPowerOutputPipe().powerToBattery(POWER_GENERATED);
                    sign.setLine(1, ChatColor.GREEN + "Sunlight");
                    sign.setLine(2, "+" + POWER_GENERATED + " BV/second");
                    sign.update();
                    return;
                } else {
                    sign.setLine(1, ChatColor.RED + "No Sunlight");
                }
            } else {
                sign.setLine(1, ChatColor.RED + "Battery Full");
            }
        } else {
            sign.setLine(1, ChatColor.RED + "Not Connected");
        }
        sign.setLine(2, "0 BV/second");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return new ArrayList<>();
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
    public String getMachineName() {
        return "Solar Panel";
    }
    public String getMachineInfo() {
        return "Generates BV from sunlight.\n " + ChatColor.GOLD + "Power Generation: " + ChatColor.GRAY + POWER_GENERATED + " BV/second";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
}
