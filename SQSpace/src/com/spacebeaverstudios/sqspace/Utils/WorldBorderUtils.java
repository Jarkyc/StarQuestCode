package com.spacebeaverstudios.sqspace.Utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;

public class WorldBorderUtils {

    public static abstract class WorldBorder{
        private World world;

        public WorldBorder(World world){
            this.world = world;
        }

        public abstract Location getClosestWithin(Location location);
        public abstract boolean isWithin(Location location);

        public World getWorld(){
            return this.world;
        }
    }

    public static class CircularWorldBorder extends WorldBorder{
        private int x;
        private int z;
        private int radius;

        public CircularWorldBorder(World world, int x, int z, int radius){
            super(world);
            this.x = x;
            this.z = z;
            this.radius = radius;
        }

        @Override
        public Location getClosestWithin(Location location){
            int padding = 5;

            if (!location.getWorld().equals(this.getWorld()))
                return location;

            int radius = this.radius - padding;

            double radians = Math.atan2(location.getZ(), location.getX());

            double x = Math.cos(radians);
            double z = Math.sin(radians);

            if ((x < 0 && location.getX() > 0) || (x > 0 && location.getX() < 0)) {

                x = x * -1;

            }

            if ((z < 0 && location.getZ() > 0) || (z > 0 && location.getZ() < 0)) {

                z = z * -1;

            }

            x *= radius;
            z *= radius;

            return new Location(location.getWorld(), x, location.getY(), z, location.getYaw(), location.getPitch());

        }

        @Override
        public boolean isWithin(Location location){
            if(location.getWorld() != this.getWorld()){
                return false;
            }

            int x = location.getBlockX() - this.x;
            int z = location.getBlockZ() - this.z;

            if (x * x + z * z <= radius * radius) {

                return true;

            }

            return false;

        }
    }

    public static class SquareWorldBorder extends WorldBorder{

        private int x1;
        private int z1;
        private int x2;
        private int z2;

        public SquareWorldBorder(World world, int x1, int z1, int x2, int z2) {
            super (world);

            this.x1 = x1 < x2 ? x1 : x2;
            this.z1 = z1 < z2 ? z1 : z2;
            this.x2 = x1 < x2 ? x2 : x1;
            this.z2 = z1 < z2 ? z2 : z1;
        }

        @Override
        public Location getClosestWithin(Location location) {

            int padding = 1;

            if (!location.getWorld().equals(this.getWorld()))
                return location;

            int x = location.getBlockX();
            int z = location.getBlockZ();

            if (x < x1)
                x = x1 + padding;
            if (z < z1)
                z = z1 + padding;
            if (x > x2)
                x = x2 - padding;
            if (z > z2)
                z = z2 - padding;
            return new Location(location.getWorld(), x, location.getY(), z);

        }

        @Override
        public boolean isWithin(Location location){
            if(location.getWorld() != this.getWorld()) return false;

            int x = location.getBlockX();
            int z= location.getBlockZ();

            return x >= x1 && x <= x2 && z >= z1 && z >= z2;
        }
    }

    private static HashMap<World, WorldBorder> worldBorders = new HashMap<World, WorldBorder>();

    public static void setWorldBorder (WorldBorder worldBorder) {

        worldBorders.put(worldBorder.getWorld(), worldBorder);

    }

    public static Location getClosestWithinBorder (Location location) {

        if (!worldBorders.containsKey(location.getWorld()))
            return location;

        return worldBorders.get(location.getWorld()).getClosestWithin(location);

    }

    public static boolean isWithinBorder (Location location) {

        if (!worldBorders.containsKey(location.getWorld()))
            return true;

        return worldBorders.get(location.getWorld()).isWithin(location);

    }

}
