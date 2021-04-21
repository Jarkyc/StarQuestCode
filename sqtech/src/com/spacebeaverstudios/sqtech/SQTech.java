package com.spacebeaverstudios.sqtech;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.commands.*;
import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.listeners.*;
import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.Pipe;
import com.spacebeaverstudios.sqtech.objects.machines.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SQTech extends JavaPlugin {
    // TODO: ownership of machines

	private static SQTech instance;

    public static SQTech getInstance() {
        return instance;
    }

    private Integer checkIntactsIndex = 0;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);

        getCommand("pipe").setExecutor(new PipeCmd());

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) {
            this.saveDefaultConfig();
        }
        BatteryMachine.staticInitialize();
        BottleFillerMachine.staticInitialize();
        BrewerMachine.staticInitialize();
        CoalGeneratorMachine.staticInitialize();
        CrafterMachine.staticInitialize();
        HopperMachine.staticInitialize();
        PlanterMachine.staticInitialize();
        ReplicatorMachine.staticInitialize();
        SmelterMachine.staticInitialize();
        SolarPanelMachine.staticInitialize();

        // should run it after all plugins initialize their sign texts
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::loadMachines, 1);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            ArrayList<Machine> batteryMachines = new ArrayList<>();
            for (Machine machine : Machine.getMachines()) {
                if (machine instanceof BatteryMachine) {
                    // done last so the amount of power is shown properly
                    batteryMachines.add(machine);
                } else {
                    Location sign = machine.getSign();
                    if (sign.getWorld().isChunkLoaded((int) Math.floor(sign.getBlockX() / 16f),
                            (int) Math.floor(sign.getBlockZ() / 16f))) {
                    	try {
	                        machine.tick();
	                    } catch (Exception e) {
                            getLogger().warning(DiscordUtils.tag("blankman") + " Error at machine with sign location: "
                                    + machine.getSign().getWorld().getName() + ", " + machine.getSign().getBlockX() + ", "
                                    + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ());
                            machine.checkIntact();
	                    	e.printStackTrace();
	                    }
                    }
                }
            }
            for (Machine machine : batteryMachines) {
                Location sign = machine.getSign();
                if (sign.getWorld().isChunkLoaded((int) Math.floor(sign.getBlockX() / 16f), (int) Math.floor(sign.getBlockZ() / 16f))) {
                    try {
                        machine.tick();
                    } catch (Exception e) {
                        getLogger().warning(DiscordUtils.tag("blankman") + " Error at machine with sign location: "
                                + machine.getSign().getWorld().getName() + ", " + machine.getSign().getBlockX() + ", "
                                + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ());
                        machine.checkIntact();
                        e.printStackTrace();
                    }
                }
            }

            MachineInventoryGUI.refreshAll();

            // check 3 machines/pipes to see if intact, just in case the listeners missed something
            ArrayList<CanCheckIntact> checkIntacts = new ArrayList<>();
            checkIntacts.addAll(Machine.getMachines());
            checkIntacts.addAll(Pipe.getAllPipes());
            if (checkIntacts.size() != 0) {
                for (int i = 0; i < 3; i++) {
                    if (checkIntactsIndex >= checkIntacts.size() - 1) {
                        checkIntactsIndex = 0;
                    } else {
                        checkIntactsIndex++;
                    }
                    checkIntacts.get(checkIntactsIndex).checkIntact();
                }
            }
        }, 20, 20);
    }

    @Override
    public void onDisable() {
        PlanterMachine.sweepersToDefaultPosition();
        saveMachines();
    }

    public void loadMachines() {
        try {
            File file = new File(getDataFolder().getAbsolutePath() + "/machines.txt");
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    try {
                        String[] line = nextLine.split(",");

                        Sign sign = (Sign) (new Location(Bukkit.getWorld(line[1]), Integer.parseInt(line[2]),
                                Integer.parseInt(line[3]), Integer.parseInt(line[4]))).getBlock().getState();
                        sign.setLine(0, line[0]);
                        sign.update();
                        if (!Machine.createFromSign(sign.getBlock())) {
                            getLogger().warning(DiscordUtils.tag("blankman")
                                    + " Failed to load machine: no machine from sign text. Line in file: " + nextLine);
                            continue;
                        }

                        Machine machine = Machine.getMachinesByBlock().get(sign.getLocation());

                        // pipe materials
                        if (!line[5].equals("0")) {
                            machine.setOutputPipeMaterial(Material.getMaterial(line[5]), null);
                        }
                        if (!line[6].equals("0")) {
                            ArrayList<Material> materials = new ArrayList<>();
                            for (String material : line[6].split(";")) {
                                materials.add(Material.getMaterial(material));
                            }
                            machine.setItemInputPipeMaterials(materials, null);
                        }
                        if (!line[7].equals("0")) {
                            ArrayList<Material> materials = new ArrayList<>();
                            for (String material : line[7].split(";")) {
                                materials.add(Material.getMaterial(material));
                            }
                            machine.setPowerInputPipeMaterials(materials, null);
                        }

                        // inventory
                        if (!line[8].equals("0")) {
                            for (String item : line[8].split(";")) {
                                String[] itemSplit = item.split(":");
                                machine.tryAddItemStack(new ItemStack(Material.getMaterial(itemSplit[0]), Integer.parseInt(itemSplit[1])));
                            }
                        }

                        // custom options
                        if (!line[9].equals("0")) {
                            machine.loadCustomSaveText(line[9]);
                        }
                    } catch (Exception e) {
                        getLogger().warning(DiscordUtils.tag("blankman")
                                + "Exception when loading a machine! Line: " + nextLine);
                        e.printStackTrace();
                    }
                }

                scanner.close();
                getLogger().info("Loaded SQTech/machines.txt");
            }
        } catch (IOException e) {
            getLogger().warning(DiscordUtils.tag("blankman") + "IOException when loading SQTech/machines.txt");
            e.printStackTrace();
        }
    }

    public void saveMachines() {
        try {
            FileWriter writer = new FileWriter(getDataFolder().getAbsolutePath() + "/machines.txt");
            for (Machine machine : Machine.getMachines()) {
                StringBuilder text = new StringBuilder(machine.getSignText() + "," + machine.getSign().getWorld().getName()
                        + "," + machine.getSign().getBlockX() + "," + machine.getSign().getBlockY() + "," + machine.getSign().getBlockZ()
                        + "," + (machine.getOutputPipeMaterial() == null ? "0" : machine.getOutputPipeMaterial().toString()) + ",");

                // input materials
                if (machine.getItemInputPipeMaterials().size() == 0) {
                    text.append("0");
                } else {
                    for (Material material : machine.getItemInputPipeMaterials()) {
                        text.append(material.toString()).append(";");
                    }
                }
                text.append(",");
                if (machine.getPowerInputPipeMaterials().size() == 0) {
                    text.append("0");
                } else {
                    for (Material material : machine.getPowerInputPipeMaterials()) {
                        text.append(material.toString()).append(";");
                    }
                }
                text.append(",");

                // inventory
                if (machine.getInventory().size() == 0) {
                    text.append("0");
                } else {
                    for (ItemStack itemStack : machine.getInventory()) {
                        text.append(itemStack.getType().toString()).append(":").append(itemStack.getAmount()).append(";");
                    }
                }

                writer.write(text.toString() + "," + machine.getCustomSaveText() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
