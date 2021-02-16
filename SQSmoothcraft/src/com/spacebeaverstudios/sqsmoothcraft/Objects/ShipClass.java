package com.spacebeaverstudios.sqsmoothcraft.Objects;

public enum ShipClass {

    FIGHTER(30 , 100, 1, 1, 1, 1),
    GUNSHIP(31, 101, 1, 1, 1, 1);

    private ShipClass(int size, int maxHeath, int wepCount, int defCount, int utilCount, int speed){
        this.size = size;
        this.health = maxHeath;
        this.wepCount = wepCount;
        this.defCount = defCount;
        this.utilCount = utilCount;
        this.speed = speed;
    }

    private int size;
    private int health;
    private int wepCount;
    private int defCount;
    private int utilCount;
    private int speed;

    public int getSize(){
        return size;
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

    public int getSpeed(){
        return speed;
    }

    public int getHealth(){
        return health;
    }
}
