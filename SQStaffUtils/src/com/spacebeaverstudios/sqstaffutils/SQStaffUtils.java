package com.spacebeaverstudios.sqstaffutils;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqstaffutils.commands.*;
import com.spacebeaverstudios.sqstaffutils.listeners.*;
import com.spacebeaverstudios.sqstaffutils.objects.LogListener;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class SQStaffUtils extends JavaPlugin {
    private static SQStaffUtils instance;
    @SuppressWarnings("unused")
    private static LogListener logListener; // just has to be stored so that the object exists

    public static SQStaffUtils getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        logListener = new LogListener();

        getServer().getPluginManager().registerEvents(new KickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);

        loadInfractions();

        InfractionsCmd infractionsCmd = new InfractionsCmd();
        getCommand("infractions").setExecutor(infractionsCmd);
        getCommand("infractions").setTabCompleter(infractionsCmd);
        InfractionsFromCmd infractionsFromCmd = new InfractionsFromCmd();
        getCommand("infractionsfrom").setExecutor(infractionsFromCmd);
        getCommand("infractionsfrom").setTabCompleter(infractionsFromCmd);
        MyInfractionsCmd myInfractionsCmd = new MyInfractionsCmd();
        getCommand("myinfractions").setExecutor(myInfractionsCmd);
        getCommand("myinfractions").setTabCompleter(myInfractionsCmd);
        NoteCmd noteCmd = new NoteCmd();
        getCommand("note").setExecutor(noteCmd);
        getCommand("note").setTabCompleter(noteCmd);
        WarnCmd warnCmd = new WarnCmd();
        getCommand("warn").setExecutor(warnCmd);
        getCommand("warn").setTabCompleter(warnCmd);
    }

    public void onDisable() {
        saveInfractions();
    }

    private void loadInfractions() {
        try {
            File file = new File(getDataFolder().getAbsolutePath() + "/infractions.txt");
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split(",");
                    switch (line[0]) {
                        case "Ban":
                            new Ban(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Kick":
                            new Kick(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Mute":
                            new Mute(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Note":
                            new Note(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Unban":
                            new Unban(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Unmute":
                            new Unmute(Arrays.copyOfRange(line, 1, line.length));
                            break;
                        case "Warning":
                            new Warning(Arrays.copyOfRange(line, 1, line.length));
                            break;
                    }
                }
                scanner.close();
                getLogger().info("Loaded SQStaffUtils/infractions.txt");
            }
        } catch (IOException e) {
            getLogger().warning(DiscordUtils.tag("blankman") + "Couldn't load infractions.txt");
            e.printStackTrace();
        }
    }
    public void saveInfractions() {
        try {
            FileWriter writer = new FileWriter(getDataFolder().getAbsolutePath() + "/infractions.txt");
            for (Infraction infraction : Infraction.infractions) {
                writer.write(infraction.getClass().getSimpleName() + "," + infraction.getSaveString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            getLogger().warning(DiscordUtils.tag("blankman") + " Couldn't save infractions.txt");
            e.printStackTrace();
        }
    }
}
