package com.spacebeaverstudios.sqcore.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.objects.SQMapRenderer;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

@SuppressWarnings("unused")
public class MapUtils {
    public static final BiMap<String, Integer> idsByName = HashBiMap.create();
    public static final HashMap<String, SQMapRenderer> renderersByName = new HashMap<>();

    // this function should only be called at startup
    public static void registerMapType(String name, SQMapRenderer renderer) {
        // connect the data to Spigot's map registry
        renderersByName.put(name, renderer);
        if (!idsByName.containsKey(name)) {
            idsByName.put(name, SQCore.getInstance().getServer().createMap(Bukkit.getWorlds().get(0)).getId());
        }
        MapView mapView = SQCore.getInstance().getServer().getMap(idsByName.get(name));

        // set up the MapView with the proper settings
        mapView.setTrackingPosition(false);
        mapView.setUnlimitedTracking(false);
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(renderer);
    }

    public static ItemStack getMapItem(String name) {
        if (!idsByName.containsKey(name)) {
            return null;
        }
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setMapView(SQCore.getInstance().getServer().getMap(idsByName.get(name)));
        map.setItemMeta(meta);
        return map;
    }

    public static void loadIdsByName() {
        try {
            File file = new File(SQCore.getInstance().getDataFolder().getAbsolutePath() + "/map-ids.txt");
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split(",");
                    idsByName.put(line[0], Integer.parseInt(line[1]));
                }
                scanner.close();
            }
        } catch (IOException e) {
            SQCore.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " can't save map ids");
            e.printStackTrace();
        }
    }
    public static void saveIdsByName() {
        try {
            FileWriter writer = new FileWriter(SQCore.getInstance().getDataFolder().getAbsolutePath() + "/map-ids.txt");
            for (String name : idsByName.keySet()) {
                writer.write(name + "," + idsByName.get(name) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            SQCore.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " can't save map ids");
            e.printStackTrace();
        }
    }
}
