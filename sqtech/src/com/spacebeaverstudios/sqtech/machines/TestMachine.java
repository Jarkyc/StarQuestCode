package com.spacebeaverstudios.sqtech.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;

public class TestMachine extends MachineBase {
    public TestMachine(Block sign) {
        super(sign);
        SQTech.getInstance().getLogger().info("new TestMachine()");
    }

    public boolean detect(Block sign) {
        BlockFace opposite = ((Directional) sign.getBlockData()).getFacing().getOppositeFace();
        if (sign.getRelative(opposite).getType().equals(Material.SPONGE)) return true;
        else return false;
    }
    public void init() {
        MachineBase.getMachines().add(this);
        SQTech.getInstance().getLogger().info("TestMachine#init");
        ((Sign) this.getSign().getState()).setLine(0, "");
        ((Sign) this.getSign().getState()).setLine(1, ChatColor.BLUE + "Test Machine");
    }

    public void tick() {
        // TODO
    }
}
