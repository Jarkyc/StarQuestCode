package com.spacebeaverstudios.sqcore;

import com.spacebeaverstudios.sqcore.commands.World.WorldCmd;
import com.spacebeaverstudios.sqcore.generator.VoidGenerator;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import com.spacebeaverstudios.sqcore.listeners.*;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

        getCommand("world").setExecutor(new WorldCmd());

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) this.saveDefaultConfig();

        loadWorlds();

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

    private void loadWorlds(){

        File file = new File(getDataFolder().getAbsolutePath() + "/worlds.yml");

        if(!file.exists()) this.saveResource("worlds.yml", false);

        FileConfiguration config = new YamlConfiguration();

        try{
            config.load(file);

            System.out.println(config.getConfigurationSection("worlds.").getKeys(false));

            for(String key : config.getConfigurationSection("worlds.").getKeys(false)){
                WorldCreator world = new WorldCreator(key.toLowerCase());
                world.environment(World.Environment.NORMAL);
                world.generator(new VoidGenerator());
                world.generateStructures(false);
                if(Bukkit.getWorld(key) != null) Bukkit.unloadWorld(key, false);
                World w = Bukkit.createWorld(world);
                w.setGameRule(GameRule.DO_FIRE_TICK, false);
                w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            }

        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
