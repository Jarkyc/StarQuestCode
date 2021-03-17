package com.spacebeaverstudios.sqtech.objects.pipes;

import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import org.bukkit.Location;

import java.util.ArrayList;

public interface Pipe extends CanCheckIntact {
    ArrayList<Location> getBlocks();
    void calculate();
}
