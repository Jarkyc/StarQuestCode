package com.spacebeaverstudios.sqspace.Objects;

import org.bukkit.Location;

public class Planet {

    public Location location;
    public String name;
    public int radius;
    public int orbitRadius;
    public String system;
    public int orbitDegree;

    public Planet(String name, Location location, int radius, int orbitRadius, String system, int orbitDegree){
        this.name = name;
        this.location = location;
        this.radius = radius;
        this.orbitRadius = orbitRadius;
        this.system = system;
        this.orbitDegree = orbitDegree;
    }


}
