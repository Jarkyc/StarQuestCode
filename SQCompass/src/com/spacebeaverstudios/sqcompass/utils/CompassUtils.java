package com.spacebeaverstudios.sqcompass.utils;

import com.spacebeaverstudios.sqcompass.SQCompass;
import com.spacebeaverstudios.sqcompass.objects.*;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class CompassUtils {
    // data getting, setting, loading, saving
    private static final HashMap<Player, ArrayList<CompassMarker>> markers = new HashMap<>();
    private static final HashMap<Player, BossBar> bossBars = new HashMap<>();
    private static final HashMap<UUID, Boolean> showCompassHashMap = new HashMap<>();
    private static final HashMap<String, StaticLocationMarker> commonMarkers = new HashMap<>();
    private static final HashMap<UUID, ArrayList<String>> commonMarkersEnabled = new HashMap<>();
    private static final HashMap<UUID, HashMap<String, StaticLocationMarker>> customMarkers = new HashMap<>();

    public static HashMap<Player, ArrayList<CompassMarker>> getMarkers() {
        return markers;
    }
    public static HashMap<Player, BossBar> getBossBars() {
        return bossBars;
    }
    public static HashMap<UUID, Boolean> getShowCompassHashMap() {
        return showCompassHashMap;
    }
    public static HashMap<String, StaticLocationMarker> getCommonMarkers() {
        return commonMarkers;
    }
    public static HashMap<UUID, ArrayList<String>> getCommonMarkersEnabled() {
        return commonMarkersEnabled;
    }
    public static HashMap<UUID, HashMap<String, StaticLocationMarker>> getCustomMarkers() {
        return customMarkers;
    }

    public static void loadCommonMarkers() {
        for (String key : SQCompass.getInstance().getConfig().getConfigurationSection("commonmarkers").getKeys(false)) {
            try {
                ConfigurationSection configSection = SQCompass.getInstance().getConfig()
                        .getConfigurationSection("commonmarkers." + key);
                commonMarkers.put(key, new StaticLocationMarker(
                        new Location(Bukkit.getWorld( configSection.getString("world")), configSection.getInt("x"), 0,
                                configSection.getInt("z")),
                        configSection.getString("marker").charAt(0),
                        ChatColor.valueOf(configSection.getString("color").toUpperCase()),
                        configSection.getInt("importance")
                ));
            } catch (NullPointerException e) {
                SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                        + " Error when intializing marker with key " + key);
                e.printStackTrace();
            }
        }
    }

    public static void loadShowCompassHashMap() {
        try {
            File file = new File(SQCompass.getInstance().getDataFolder().getAbsolutePath() + "/hidden.txt");
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    showCompassHashMap.put(UUID.fromString(line), false);
                }
                scanner.close();
            }
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " Error loading SQCompass/hidden.txt");
            e.printStackTrace();
        }

        SQCompass.getInstance().getLogger().info("Loaded SQCompass/hidden.txt");
    }
    public static void saveShowCompassHashMap() {
        try {
            FileWriter writer = new FileWriter(SQCompass.getInstance().getDataFolder().getAbsolutePath() + "/hidden.txt");
            for (UUID uuid : showCompassHashMap.keySet())
                if (!showCompassHashMap.get(uuid))
                    writer.write(uuid.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " Error saving SQCompass/hidden.txt");
            e.printStackTrace();
        }

        SQCompass.getInstance().getLogger().info("Saved SQCompass/hidden.txt");
    }

    public static void loadCustomMarkers() {
        try {
            File file = new File(SQCompass.getInstance().getDataFolder().getAbsolutePath() + "/custommarkers.txt");
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split(",");
                    UUID uuid = UUID.fromString(line[0]);
                    if (!customMarkers.containsKey(uuid)) customMarkers.put(uuid, new HashMap<>());
                    customMarkers.get(uuid).put(line[1], new StaticLocationMarker(
                            new Location(Bukkit.getWorld(line[2]), Integer.parseInt(line[3]), 0, Integer.parseInt(line[4])),
                            line[5].charAt(0), ChatColor.valueOf(line[6]), 10
                    ));
                }
                scanner.close();
            }
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Error loading SQCompass/custommarkers.txt");
            e.printStackTrace();
        }

        SQCompass.getInstance().getLogger().info("Loaded SQCompass/custommarkers.txt");
    }
    public static void saveCustomMarkers() {
        try {
            FileWriter writer = new FileWriter(SQCompass.getInstance().getDataFolder().getAbsolutePath() + "/custommarkers.txt");
            for (UUID uuid : customMarkers.keySet()) {
                for (String name : customMarkers.get(uuid).keySet()) {
                    StaticLocationMarker marker = customMarkers.get(uuid).get(name);
                    writer.write(uuid.toString() + "," + name + "," + marker.getLocation().getWorld().getName() + ","
                            + marker.getLocation().getBlockX() + "," + marker.getLocation().getBlockZ() + "," + marker.getMarker()
                            + "," + marker.getColor().name() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Error saving SQCompass/custommarkers.txt");
            e.printStackTrace();
        }

        SQCompass.getInstance().getLogger().info("Saved SQCompass/custommarkers.txt");
    }

    public static void loadCommonMarkersEnabled() {
        try {
            File file = new File(SQCompass.getInstance().getDataFolder().getAbsolutePath() + "/commonmarkersenabled.txt");
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split(",");
                    UUID uuid = UUID.fromString(line[0]);
                    if (!commonMarkersEnabled.containsKey(uuid)) commonMarkersEnabled.put(uuid, new ArrayList<>());
                    commonMarkersEnabled.get(uuid).add(line[1]);
                }
                scanner.close();
            }
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Error saving SQCompass/commonmarkersenabled.txt");
            e.printStackTrace();
        }
    }
    public static void saveCommonMarkersEnabled() {
        try {
            FileWriter writer = new FileWriter(SQCompass.getInstance().getDataFolder().getAbsolutePath()
                    + "/commonmarkersenabled.txt");
            for (UUID uuid : commonMarkersEnabled.keySet())
                for (String name : commonMarkersEnabled.get(uuid))
                    writer.write(uuid.toString() + "," + name + "\n");
            writer.close();
        } catch (IOException e) {
            SQCompass.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Error loading SQCompass/commonmarkersenabled.txt");
            e.printStackTrace();
        }
    }

    // actual functions
    public static void playerJoin(Player player) {
        markers.put(player, new ArrayList<>());
        markers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.NORTH));
        markers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.EAST));
        markers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.SOUTH));
        markers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.WEST));

        if (commonMarkersEnabled.containsKey(player.getUniqueId())) {
            for (String name : commonMarkersEnabled.get(player.getUniqueId())) {
                markers.get(player).add(commonMarkers.get(name));
            }
        } else commonMarkersEnabled.put(player.getUniqueId(), new ArrayList<>());

        if (customMarkers.containsKey(player.getUniqueId())) {
            for (CompassMarker marker : customMarkers.get(player.getUniqueId()).values()) {
                markers.get(player).add(marker);
            }
        } else customMarkers.put(player.getUniqueId(), new HashMap<>());

        bossBars.put(player, Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID));
        if (!showCompassHashMap.containsKey(player.getUniqueId())) {
            showCompassHashMap.put(player.getUniqueId(), true);
            bossBars.get(player).addPlayer(player);
        }

        render(player);
    }
    public static void playerLeave(Player player) {
        markers.remove(player);
        bossBars.get(player).removeAll();
        bossBars.remove(player);
    }

    public static void render(Player player) {
        if (!showCompassHashMap.get(player.getUniqueId())) return;

        HashMap<Integer, CompassMarker> fovPositions = new HashMap<>();
        for (CompassMarker marker : markers.get(player)) {
            if (marker.isWorldSpecific() && !player.getLocation().getWorld().equals(marker.getWorld())) continue;

            int markerYaw = marker.getYaw(player);
            int yawDiff = Math.round(markerYaw-player.getLocation().getYaw()) % 360;
            if (yawDiff < -180) yawDiff += 360;
            else if (yawDiff > 180) yawDiff -= 360;

            if (Math.abs(yawDiff) <= 60) {
                int index = (yawDiff/4)+16;
                if (!fovPositions.containsKey(index)) fovPositions.put(index, marker);
                else if (fovPositions.get(index).getImportance() < marker.getImportance()) fovPositions.put(index, marker);
            }
        }

        StringBuilder compassText = new StringBuilder();
        for (int i = 0; i <= 31; i++) {
            if (i == 16) {
                if (fovPositions.containsKey(i)) compassText.append(fovPositions.get(i).getColor()).append(ChatColor.UNDERLINE)
                        .append(fovPositions.get(i).getMarker());
                else compassText.append(ChatColor.GRAY).append(ChatColor.UNDERLINE).append("-");
            } else {
                if (fovPositions.containsKey(i)) compassText.append(fovPositions.get(i).getColor())
                        .append(fovPositions.get(i).getMarker());
                else compassText.append(ChatColor.GRAY).append("-");
            }
        }
        bossBars.get(player).setTitle(compassText.toString());
    }
}
