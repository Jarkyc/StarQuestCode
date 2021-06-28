package com.spacebeaverstudios.sqstaffutils.objects.infraction;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqstaffutils.SQStaffUtils;
import com.spacebeaverstudios.sqstaffutils.events.InfractionLogEvent;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;

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
            if (infraction.sender.uuid.equals(player)) {
                toReturn.add(infraction);
            }
        }
        toReturn.sort((o1, o2) -> (int) (o1.date - o2.date));
        return toReturn;
    }

    protected static String durationString(long duration) {
        ArrayList<String> subDurations = new ArrayList<>();
        if (duration <= 0) {
            return "0 seconds";
        }
        if (duration >= 31_536_000) {
            subDurations.add((duration / 31_536_000) + " year" + (duration >= 2 * 31_536_000 ? "s" : ""));
            duration = duration % 31_536_000;
        }
        if (duration >= 604_800) {
            subDurations.add((duration / 604_800) + " week" + (duration >= 2 * 604_800 ? "s" : ""));
            duration = duration % 604_800;
        }
        if (duration >= 86_400) {
            subDurations.add((duration / 86_400) + " day" + (duration >= 2 * 86_400 ? "s" : ""));
            duration = duration % 86_400;
        }
        if (duration >= 3_600) {
            subDurations.add((duration / 3_600) + " hour" + (duration >= 2 * 3_600 ? "s" : ""));
            duration = duration % 3_600;
        }
        if (duration >= 60) {
            subDurations.add((duration / 60) + " minute" + (duration >= 2 * 60 ? "s" : ""));
            duration = duration % 60;
        }
        if (duration >= 1) {
            subDurations.add(duration + " second" + (duration >= 2 ? "s" : ""));
        }
        return String.join(", ", subDurations);
    }

    // instance
    public final InfractionSender sender; // if null, it's from console
    public final UUID target;
    public final long date;
    protected final int id; // this doesn't have a specific use, but it's been useful in previous versions and I don't want to re-add it to all

    protected Infraction(InfractionSender sender, UUID target, long date) {
        this.sender = sender;
        this.target = target;
        this.date = date;
        this.id = infractions.size();
        infractions.add(this);
        SQStaffUtils.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SQStaffUtils.getInstance(),
                () -> SQStaffUtils.getInstance().getServer().getPluginManager().callEvent(getInfractionLogEvent()));
    }
    protected Infraction(InfractionSender sender, UUID target, long date, int id) {
        this.sender = sender;
        this.target = target;
        this.date = date;
        this.id = id;
        infractions.add(this);
    }

    public abstract InfractionLogEvent getInfractionLogEvent();
    public abstract GUIItem getGUIItem();
    public abstract String getSaveString();
}
