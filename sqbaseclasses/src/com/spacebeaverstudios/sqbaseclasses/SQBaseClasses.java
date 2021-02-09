package com.spacebeaverstudios.sqbaseclasses;

import com.spacebeaverstudios.sqbaseclasses.gui.GUIUtils;
import com.spacebeaverstudios.sqbaseclasses.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SQBaseClasses extends JavaPlugin {
    private static SQBaseClasses instance;

    public static SQBaseClasses getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask (this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < player.getInventory().getSize(); i ++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (GUIUtils.isWanted(itemStack)) {
                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }
        }, 1, 1);
    }
}
