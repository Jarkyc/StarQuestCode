package com.spacebeaverstudios.sqcore;

import com.spacebeaverstudios.sqcore.commands.Template.TemplateCmd;
import com.spacebeaverstudios.sqcore.commands.World.WorldCmd;
import com.spacebeaverstudios.sqcore.objects.TabList;
import com.spacebeaverstudios.sqcore.objects.Tickable;
import com.spacebeaverstudios.sqcore.objects.template.Template;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqcore.listeners.*;
import com.spacebeaverstudios.sqcore.utils.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class SQCore extends JavaPlugin {
    private static SQCore instance;
    public ArrayList<Tickable> tickables = new ArrayList<>();
    public static SQCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        GUIUtils.initializeGUIItemNamespacedKey();

        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

        Template.loadTemplates();

        WorldCmd worldCmd = new WorldCmd();
        getCommand("world").setExecutor(worldCmd);
        getCommand("world").setTabCompleter(worldCmd);

        TemplateCmd templateCmd = new TemplateCmd();
        getCommand("template").setExecutor(templateCmd);
        getCommand("template").setTabCompleter(templateCmd);

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) {
            this.saveDefaultConfig();
        }

        this.reloadConfig();
        MapUtils.loadIdsByName();

        new TabList();

        // check for gui contraband
        Bukkit.getScheduler().scheduleSyncRepeatingTask (this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < player.getInventory().getSize(); i ++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (GUIUtils.isWanted(itemStack)) {
                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                }
                //Tickables
                for(Tickable tick : tickables){
                    tick.onTick();
                }
            }
        }, 1, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, MapUtils::saveIdsByName);
    }
}
