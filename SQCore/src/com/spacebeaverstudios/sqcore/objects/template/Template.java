package com.spacebeaverstudios.sqcore.objects.template;

import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Template implements Serializable {
    // static
    private static final HashMap<String, Template> templates = new HashMap<>();

    public static HashMap<String, Template> getTemplates() {
        return templates;
    }

    public static void loadTemplates() {
        File folder = new File(SQCore.getInstance().getDataFolder().getAbsolutePath() + "/templates");
        if (!folder.mkdirs()) {
            try {
                for (File file : folder.listFiles()) {
                    if (file.getAbsolutePath().endsWith(".template")) {
                        FileInputStream fileIn = new FileInputStream(folder + "/" + file.getName());
                        ObjectInputStream ois = new ObjectInputStream(fileIn);
                        Template template = (Template) ois.readObject();
                        fileIn.close();
                        ois.close();
                        templates.put(template.getName(), template);
                        SQCore.getInstance().getLogger().info("Loaded template: " + template.name);
                    }
                }
            } catch (Exception e) {
                SQCore.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " Error when loading templates!");
                e.printStackTrace();
            }
        }
    }

    // instance
    private final String name;
    private final ArrayList<TemplateBlock> blocks = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Template(String name, Location origin, ArrayList<TemplateBlock> blocks) {
        this.name = name;
        this.blocks.addAll(blocks);
    }
    public Template(String name, Location origin, ArrayList<TemplateBlock> blocks, Player creator) {
        this(name, origin, blocks);

        // only save file when created, not when loaded
        try {
            FileOutputStream fileOut = new FileOutputStream(SQCore.getInstance().getDataFolder().getAbsolutePath()
                    + "/templates/" + name + ".template");
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(this);
            oos.close();
            fileOut.close();
        } catch (IOException e) {
            creator.sendMessage(ChatColor.RED + "There was an error when attempting to save your template to a file!");
            e.printStackTrace();
        }
    }

    public void paste(Location loc) {
        for (TemplateBlock block : blocks) {
            Vector vec = block.getVector();
            Location pasteLoc = new Location(loc.getWorld(), loc.getX() + vec.getX(), loc.getY() + vec.getY(), loc.getZ() + vec.getZ());
            block.paste(pasteLoc);
        }
    }

    public void pasteAir(Location loc) {
        for (TemplateBlock block : blocks) {
            Vector vec = block.getVector();
            Location pasteLoc = new Location(loc.getWorld(), loc.getX() - vec.getX(), loc.getY() - vec.getY(), loc.getZ() - vec.getZ());
            pasteLoc.getBlock().setType(Material.AIR);
        }
    }

    public ArrayList<TemplateBlock> getBlocks(){
        return this.blocks;
    }
}
