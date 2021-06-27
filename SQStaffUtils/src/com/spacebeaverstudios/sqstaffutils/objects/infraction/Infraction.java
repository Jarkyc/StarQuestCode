package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Infraction {
    // static
    public static final ArrayList<Infraction> infractions = new ArrayList<>();

    public static ArrayList<Infraction> infractionsToPlayer(UUID player) {
        ArrayList<Infraction> toReturn = new ArrayList<>();
        for (Infraction infraction : infractions) {
            if (infraction.target.equals(player)) {
                toReturn.add(infraction);
            }
        }
        toReturn.sort((o1, o2) -> (int) (o1.date - o2.date));
        return toReturn;
    }
    public static ArrayList<Infraction> infractionsFromPlayer(UUID player) {
        ArrayList<Infraction> toReturn = new ArrayList<>();
        for (Infraction infraction : infractions) {
            if (infraction.sender.equals(player)) {
                toReturn.add(infraction);
            }
        }
        toReturn.sort((o1, o2) -> (int) (o1.date - o2.date));
        return toReturn;
    }

    protected static String durationString(long duration) {
        // TODO
    }

    // instance
    public final UUID sender; // if null, it's from console
    public final UUID target;
    public final long date;
    private final int id;

    protected Infraction(UUID sender, UUID target, long date) {
        this(sender, target, date, infractions.size());
    }
    protected Infraction(UUID sender, UUID target, long date, int id) {
        this.sender = sender;
        this.target = target;
        this.date = date;
        this.id = id;
        infractions.add(this);
    }

    public abstract GUIItem getEntry();
    public abstract String saveString();
}
