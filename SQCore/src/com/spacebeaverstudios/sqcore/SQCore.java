package com.spacebeaverstudios.sqcore;

import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqcore.listeners.*;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SQCore extends JavaPlugin {
    private static SQCore instance;

    public static SQCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) this.saveDefaultConfig();

        this.reloadConfig();

        // check for gui contraband
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

        // test
        DiscordUtils.reportError(DiscordUtils.tag("blankman") + " test");
    }
}
