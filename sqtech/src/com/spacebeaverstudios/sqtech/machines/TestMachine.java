package com.spacebeaverstudios.sqtech.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;

public class TestMachine extends Machine {
    public TestMachine(Block sign) {
        super(sign, "Test Machine");
        SQTech.getInstance().getLogger().info("new TestMachine()");
    }

    public boolean detect(Block sign) {
        BlockFace opposite = ((Directional) sign.getBlockData()).getFacing().getOppositeFace();
        return sign.getRelative(opposite).getType().equals(Material.SPONGE);
    }

    public void init() {
        Machine.getMachines().add(this);
        SQTech.getInstance().getLogger().info("TestMachine#init");
        Sign sign = (Sign) this.getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, "");
        sign.setLine(1, ChatColor.BLUE + "Test Machine");
        sign.setLine(2, ChatColor.RED + "Inactive");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        // TODO
    }

    public String getMachineInfo() {
        return "Test Machine does test things";
    }
}
