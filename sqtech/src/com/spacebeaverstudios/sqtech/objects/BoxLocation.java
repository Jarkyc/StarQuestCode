package com.spacebeaverstudios.sqtech.objects;

import org.bukkit.Location;

public class BoxLocation {
    private Location pointOne;
    private Location pointTwo;

    public BoxLocation(Location pointOne, Location pointTwo) {
        this.pointOne = pointOne;
        this.pointTwo = pointTwo;
    }

    public Location getPointOne() {
        return pointOne;
    }
    public Location getPointTwo() {
        return pointTwo;
    }

    public void setPointOne(Location pointOne) {
        this.pointOne = pointOne;
    }
    public void setPointTwo(Location pointTwo) {
        this.pointTwo = pointTwo;
    }
}
