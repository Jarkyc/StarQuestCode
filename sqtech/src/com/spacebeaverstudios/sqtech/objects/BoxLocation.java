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

    public Location getSortedPointOne() {
        return new Location(pointOne.getWorld(), Math.min(pointOne.getX(), pointTwo.getX()),
                Math.min(pointOne.getY(), pointTwo.getY()), Math.min(pointOne.getZ(), pointTwo.getZ()));
    }
    public Location getSortedPointTwo() {
        return new Location(pointTwo.getWorld(), Math.max(pointOne.getX(), pointTwo.getX()),
                Math.max(pointOne.getY(), pointTwo.getY()), Math.max(pointOne.getZ(), pointTwo.getZ()));
    }
}
