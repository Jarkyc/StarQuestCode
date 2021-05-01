package com.spacebeaverstudios.sqcorporations.objects.facility;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.spacebeaverstudios.sqcore.objects.template.Template;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqcorporations.SQCorporations;
import com.spacebeaverstudios.sqcorporations.objects.Corporation;
import com.spacebeaverstudios.sqcorporations.objects.ReputationLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Facility {
    // static
    private static final ArrayList<Facility> facilities = new ArrayList<>();

    public static ArrayList<Facility> getFacilities() {
        return facilities;
    }

    protected static Template randomTemplate(Corporation owner, String type) {
        List<String> choices = owner.getConfig().getStringList("facilities." + type);
        return Template.getTemplates().get(choices.get(ThreadLocalRandom.current().nextInt(0, choices.size())));
    }

    public static int getNewId() {
        FileConfiguration config = SQCorporations.getInstance().getConfig();
        int id = config.getInt("facility-id-counter");
        config.set("facility-id-counter", id + 1);
        try {
            config.save(new File(SQCorporations.getInstance().getDataFolder().getAbsolutePath() + "/config.yml"));
        } catch (IOException e) {
            SQCorporations.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Error when trying to save new facility ID!");
            e.printStackTrace();
        }
        return id;
    }

    public static void checkForRemoval() {
        long currentTime = Instant.now().toEpochMilli();
        ArrayList<Facility> check = new ArrayList<>(facilities); // to prevent ConcurrentModificationException
        for (Facility facility : check) {
            // if removal time == 0, then it will be removed at custom time
            if (facility.getRemovalTime() != 0 && currentTime >= facility.getRemovalTime()) {
                // TODO: maybe don't remove if someone is interacting with it?
                facility.remove();
            }
        }
    }

    // instance
    protected final Corporation owner;
    protected final Location templateLocation;
    protected final Template template;
    protected final BoundingBox boundingBox;
    private final int id;
    private final long removalTime;

    public Facility(Corporation owner, Location templateLocation, Template template, BoundingBox boundingBox, long removalTime) {
        this.owner = owner;
        this.templateLocation = templateLocation;
        this.template = template;
        this.boundingBox = boundingBox;
        this.id = getNewId();
        this.removalTime = removalTime;

        // create the physical object
        template.paste(templateLocation);
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(templateLocation.getWorld()))
                .addRegion(new ProtectedCuboidRegion("facility_" + id,
                        BlockVector3.at(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ()),
                        BlockVector3.at(boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ())));

        facilities.add(this);
    }

    public Corporation getOwner() {
        return owner;
    }

    public Location getTemplateLocation() {
        return templateLocation;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public int getId() {
        return id;
    }

    public long getRemovalTime() {
        return removalTime;
    }

    public void remove() {
        Bukkit.broadcastMessage("removed");
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(templateLocation.getWorld()))
                .removeRegion("facility_" + id);
        // TODO: only removes top block layer for some reason
        template.pasteAir(templateLocation);
        facilities.remove(this);
    }

    public void playerEnter(Player player) {
        // tell players to leave if they're neutral or below
        if (ReputationLevel.getLevel(owner.getData().getReputation(player)).ordinal() > 3) {
            player.sendMessage(ChatColor.RED + "You are now entering " + owner.getConfig().get("name") + " airspace. " +
                    "Leave immediately or we will open fire.");
        }
    }
}
