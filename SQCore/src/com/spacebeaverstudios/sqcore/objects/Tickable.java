package com.spacebeaverstudios.sqcore.objects;

import com.spacebeaverstudios.sqcore.SQCore;

public abstract class Tickable {

    public Tickable(){
        SQCore.getInstance().tickables.add(this);
    }

    public abstract void onTick();
}
