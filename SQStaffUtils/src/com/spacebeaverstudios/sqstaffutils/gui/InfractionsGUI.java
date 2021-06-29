package com.spacebeaverstudios.sqstaffutils.gui;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Infraction;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Note;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class InfractionsGUI extends ListGUI {
    private final UUID target;
    private final InfractionSender sender;
    private final boolean showNotes;

    public InfractionsGUI(UUID target, boolean showNotes) {
        super((showNotes ? Bukkit.getPlayer(target).getName() + "'s Infractions" : "Your Infractions"), 4);
        this.target = target;
        this.sender = null;
        this.showNotes = showNotes;
    }
    public InfractionsGUI(InfractionSender sender) {
        super("Infractions from " + sender.getName(), 4);
        this.target = null;
        this.sender = sender;
        this.showNotes = true;
    }

    public List<Object> getObjectList() {
        ArrayList<Object> objects;
        if (target != null) {
            objects = new ArrayList<>(Infraction.infractionsToPlayer(target));
        } else {
            objects = new ArrayList<>(Infraction.infractionsFromPlayer(sender.uuid));
        }
        if (!showNotes) {
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof Note) {
                    objects.remove(i);
                    i--;
                }
            }
        }
        Collections.reverse(objects);
        return objects;
    }

    public GUIItem getObjectItem(Object obj) {
        return ((Infraction) obj).getGUIItem();
    }
}
