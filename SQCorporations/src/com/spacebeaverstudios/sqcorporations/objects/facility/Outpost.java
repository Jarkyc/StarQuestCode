package com.spacebeaverstudios.sqcorporations.objects.facility;

import com.spacebeaverstudios.sqcorporations.objects.Corporation;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

import java.time.Instant;

public class Outpost extends Facility {
    public Outpost(Corporation owner, Location location) {
        super(owner, location, randomTemplate(owner, "outpost"),
                new BoundingBox(location.getBlockX() - 100, 0, location.getBlockZ() - 100,
                        location.getBlockX() + 100, 255, location.getBlockZ() + 100),
                Instant.now().toEpochMilli() + 60000); // 1 minute from now
    }
}
