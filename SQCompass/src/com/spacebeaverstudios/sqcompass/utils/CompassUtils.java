package com.spacebeaverstudios.sqcompass.utils;

import com.spacebeaverstudios.sqcompass.SQCompass;
import com.spacebeaverstudios.sqcompass.objects.CardinalDirectionMarker;
import com.spacebeaverstudios.sqcompass.objects.CompassMarker;
import com.spacebeaverstudios.sqcompass.objects.StaticLocationMarker;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CompassUtils {
    private static final HashMap<Player, ArrayList<CompassMarker>> compassMarkers = new HashMap<>();
    private static final HashMap<Player, BossBar> bossBars = new HashMap<>();
    private static final HashMap<Player, Boolean> showCompassHashMap = new HashMap<>();
    private static final HashMap<String, CompassMarker> commonMarkers = new HashMap<>();
    private static final HashMap<UUID, HashMap<String, StaticLocationMarker>> customMarkers = new HashMap<>();

    public static HashMap<Player, ArrayList<CompassMarker>> getCompassMarkers() {
        return compassMarkers;
    }
    public static HashMap<Player, BossBar> getBossBars() {
        return bossBars;
    }
    public static HashMap<Player, Boolean> getShowCompassHashMap() {
        return showCompassHashMap;
    }
    public static HashMap<String, CompassMarker> getCommonMarkers() {
        return commonMarkers;
    }
    public static HashMap<UUID, HashMap<String, StaticLocationMarker>> getCustomMarkers() {
        return customMarkers;
    }

    public static void loadCommonMarkers() {
        for (String key : SQCompass.getInstance().getConfig().getConfigurationSection("commonmarkers").getKeys(false)) {
            try {
                ConfigurationSection configSection = SQCompass.getInstance().getConfig().getConfigurationSection("commonmarkers." + key);
                commonMarkers.put(key, new StaticLocationMarker(
                        new Location(Bukkit.getWorld(
                                configSection.getString("world")),
                                configSection.getInt("x"),
                                0,
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
        // TODO
    }
    public static void saveShowCompassHashMap() {
        // TODO
    }

    public static void loadCustomMarkers() {
        // TODO
    }
    public static void saveCustomMarkers() {
        // TODO
    }

    public static void playerJoin(Player player) {
        compassMarkers.put(player, new ArrayList<>());
        compassMarkers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.NORTH));
        compassMarkers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.EAST));
        compassMarkers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.SOUTH));
        compassMarkers.get(player).add(new CardinalDirectionMarker(CardinalDirectionMarker.Direction.WEST));

        bossBars.put(player, Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID));
        bossBars.get(player).addPlayer(player);

        showCompassHashMap.put(player, true);

        if (!customMarkers.containsKey(player.getUniqueId())) customMarkers.put(player.getUniqueId(), new HashMap<>());

        render(player);
    }
    public static void playerLeave(Player player) {
        compassMarkers.remove(player);
        bossBars.get(player).removeAll();
        bossBars.remove(player);
    }

    public static void render(Player player) {
        // fix 0/360 bug
        if (!showCompassHashMap.get(player)) return;

        HashMap<Integer, CompassMarker> fovPositions = new HashMap<>();
        for (CompassMarker marker : compassMarkers.get(player)) {
            if (marker.isWorldSpecific() && !player.getLocation().getWorld().equals(marker.getWorld())) continue;

            int yawDiff = Math.round(Math.abs(marker.getYaw(player) % 360)-player.getLocation().getYaw());
            if (Math.abs(yawDiff) <= 60) {
                int index = (yawDiff/4)+16;
                if (!fovPositions.containsKey(index)) fovPositions.put(index, marker);
                else if (fovPositions.get(index).getImportance() < marker.getImportance()) fovPositions.put(index, marker);
            }
        }

        StringBuilder compassText = new StringBuilder();
        for (int i = 0; i <= 31; i++) {
            if (fovPositions.containsKey(i)) compassText.append(fovPositions.get(i).getColor()).append(fovPositions.get(i).getMarker());
            else compassText.append(ChatColor.GRAY).append("-");
        }
        bossBars.get(player).setTitle(compassText.toString());
    }
}
