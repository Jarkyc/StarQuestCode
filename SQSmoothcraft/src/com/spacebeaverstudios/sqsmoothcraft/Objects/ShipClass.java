package com.spacebeaverstudios.sqsmoothcraft.Objects;

public enum ShipClass {

    FIGHTER(26, 30 , 100, 1, 1, 1, 1, 1),
    GUNSHIP(31, 39, 101, 1, 1, 1, 1, 1),
    ADMINSHIP(26, 5000, 1000, 100, 100 ,100, 100, 1);

    private ShipClass(int minSize, int maxSize, int maxHeath, int wepCount, int defCount, int utilCount, int dropperCount, int speed){
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.health = maxHeath;
        this.wepCount = wepCount;
        this.defCount = defCount;
        this.utilCount = utilCount;
        this.dropperCount = dropperCount;
        this.speed = speed;
    }

    private int minSize;
    private int maxSize;
    private int health;
    private int wepCount;
    private int defCount;
    private int utilCount;
    private int speed;
    private int dropperCount;


    public int getMinSize(){
        return minSize;
    }

    public int getMaxSize(){
        return maxSize;
    }

    public int getWepCount(){
        return wepCount;
    }

    public int getDefCount(){
        return defCount;
    }

    public int getUtilCount(){
        return utilCount;
    }

    public int getDropperCount() {
        return this.dropperCount;
    }

    public int getSpeed(){
        return speed;
    }

    public int getHealth(){
        return health;
    }
}
