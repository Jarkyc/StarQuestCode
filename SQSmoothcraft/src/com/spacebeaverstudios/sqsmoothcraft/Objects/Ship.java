package com.spacebeaverstudios.sqsmoothcraft.Objects;

import com.spacebeaverstudios.sqsmoothcraft.Events.ShipDamageEvent;
import com.spacebeaverstudios.sqsmoothcraft.Events.ShipFireMainGunsEvent;
import com.spacebeaverstudios.sqsmoothcraft.Events.ShipPilotEvent;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Data.SolidShipData;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Jammer;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Module;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Shield;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.CannonTask;
import com.spacebeaverstudios.sqsmoothcraft.Utils.MathUtils;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ModuleUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Ship {

    private ShipClass shipClass;

    private HashSet<ShipBlock> blocks;
    private Player owner;
    private Location shipLocation;
    private ShipBlock core;
    public Vector autoPilotDirection;
    public boolean isAutopilot = false;
    private Location originVec;
    public ArrayList<ShipBlock> pistons;
    public ArrayList<Module> modules = new ArrayList<>();
    public Shield shieldCore;
    public int health = 100;
    public int maxHealth;
    public int shieldHealth = 100;
    public int maxShieldHealth;

    public Inventory infoWindow;
    public Inventory moduleWindow;

    public Inventory utilModulesWind;
    public Inventory defenseModulesWind;
    public Inventory weaponModulesWind;

    public Ship(HashSet<ShipBlock> blocks, Player owner, Location origin, ShipBlock core, Location originalVector, ArrayList<ShipBlock> pistons, ShipClass clazz) {
        this.blocks = blocks;
        this.owner = owner;
        this.shipLocation = origin;
        this.core = core;
        this.originVec = originalVector;
        this.pistons = pistons;

        this.shipClass = clazz;

        this.maxHealth = clazz.getHealth();

        ShipPilotEvent event = new ShipPilotEvent(owner, this);
        Bukkit.getPluginManager().callEvent(event);

        //Should theoretically allow data to be initialized for reading during event listening, but if method is returned at this stage, won't allow the ship to be placed into the ship list, so JVM garbage collector commits game end on object.
        if (event.isCancelled()) {
            return;
        }

        SQSmoothcraft.instance.allShips.add(this);

        for (ShipBlock block : blocks) {
            block.location.getWorld().getBlockAt(block.location).setType(Material.AIR);
            if (block.visible) {
                block.buildArmorStand();
            }
        }
        //prevents the player's view from lowering when the crouch
        //Future self here, I am just kidding, it only works sometimes
        owner.setFlying(true);
        core.armorStand.addPassenger(owner);

        infoWindow = Bukkit.createInventory(null, 9, ChatColor.BLUE + owner.getName() + "'s Ship Info");
        moduleWindow = Bukkit.createInventory(null, InventoryType.DROPPER, ChatColor.GREEN + owner.getName() + "'s Ship Modules");

        defenseModulesWind = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Defense Modules");
        weaponModulesWind = Bukkit.createInventory(null, 27, ChatColor.RED + "Weapon Modules");
        utilModulesWind = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Utility Modules");


        createGUIs();
        core.armorStand.setCanTick(true);
        core.armorStand.getEquipment().setHelmet(new ItemStack(Material.AIR));

        modules.add(new Jammer());
    }

    public Player getOwner() {
        return this.owner;
    }

    public HashSet<ShipBlock> getBlocks() {
        return this.blocks;
    }

    public ShipClass getShipClass(){
        return this.shipClass;
    }

    private void createGUIs() {

        //god help me making GUIs with spigot is so ugly
        ItemStack blockCount = new ItemStack(Material.BRICK);
        ItemMeta countMeta = blockCount.getItemMeta();
        countMeta.setDisplayName(ChatColor.GOLD + "Block Count");
        ArrayList<String> sub = new ArrayList<>();
        sub.add(String.valueOf(blocks.size()));
        countMeta.setLore(sub);
        blockCount.setItemMeta(countMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) head.getItemMeta();
        skull.setOwningPlayer(owner);
        skull.setDisplayName(ChatColor.GOLD + "Owner");
        ArrayList<String> nm = new ArrayList<>();
        nm.add(owner.getName());
        skull.setLore(nm);
        head.setItemMeta(skull);

        ItemStack modules = new ItemStack(Material.PISTON);
        ItemMeta meta = modules.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Modules");
        ArrayList<String> click = new ArrayList<>();
        click.add("Click here to open up the modules inventory");
        meta.setLore(click);
        modules.setItemMeta(meta);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName("");
        filler.setItemMeta(fm);

        ItemStack weapons = new ItemStack(Material.PISTON);
        ItemMeta wm = weapons.getItemMeta();
        wm.setDisplayName(ChatColor.RED + "Weapons");
        ArrayList<String> wml = new ArrayList<>();
        wml.add("Put capacity here");
        wm.setLore(wml);
        weapons.setItemMeta(wm);

        ItemStack defense = new ItemStack(Material.SHIELD);
        ItemMeta dm = defense.getItemMeta();
        dm.setDisplayName(ChatColor.BLUE + "Defense");
        ArrayList<String> dml = new ArrayList<>();
        dml.add("Put capacity here");
        dm.setLore(dml);
        defense.setItemMeta(dm);

        ItemStack util = new ItemStack(Material.HOPPER);
        ItemMeta um = util.getItemMeta();
        um.setDisplayName(ChatColor.GOLD + "Utility");
        ArrayList<String> uml = new ArrayList<>();
        uml.add("Put capacity here");
        um.setLore(uml);
        util.setItemMeta(um);


        moduleWindow.setItem(0, filler);
        moduleWindow.setItem(1, filler);
        moduleWindow.setItem(2, filler);
        moduleWindow.setItem(3, weapons);
        moduleWindow.setItem(4, defense);
        moduleWindow.setItem(5, util);
        moduleWindow.setItem(6, filler);
        moduleWindow.setItem(7, filler);
        moduleWindow.setItem(8, filler);

        infoWindow.setItem(0, blockCount);
        infoWindow.setItem(4, head);
        infoWindow.setItem(8, modules);
    }

    public void onTick() {
        updateData();
        handleModules();
        handleShiftFly();
        updateOrigin();
        rotateStands();
        handleAutoPilot();
    }

    public ShipBlock getCore() {
        return this.core;
    }

    public Location getLocation() {
        return core.armorStand.getLocation();
    }

    private void handleModules() {
        for (Module module : this.modules) {
            if (module.passive) {
                module.activate(this, owner);
            }
        }
    }

    private void updateData() {
        for (ShipBlock block : blocks) {
            if (!block.visible) continue;
            block.location = block.getArmorStand().getLocation();
        }
        if (((getOwner().getInventory().getItemInMainHand().getType() != Material.CLOCK && !isAutopilot) || (getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK && !getOwner().isSneaking())) && core.armorStand.getVelocity().getY() < 0) {
            core.armorStand.setVelocity(core.armorStand.getVelocity().clone().setY(0));
        }
    }

    public void damage(int x, DamageReason damageReason) {
        ShipDamageEvent event = new ShipDamageEvent(owner, this, x, damageReason);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if (shieldCore != null && shieldHealth > 0) {
                shieldHealth -= x;
            } else {
                health -= x;
            }
        }
    }

    private void updateOrigin() {
        this.shipLocation = core.armorStand.getLocation();
    }

    private void rotateStands() {

        double pitch = this.getOwner().getEyeLocation().getPitch();
        pitch = Math.toRadians(pitch);

        double yaw = this.getOwner().getEyeLocation().getYaw();
        yaw = Math.toRadians(yaw);

        double yawCos = Math.cos(yaw);
        double yawSin = Math.sin(yaw);

        double pitchCos = Math.cos(pitch);
        double pitchSin = Math.sin(pitch);

        double arg1 = yawSin * pitchSin * -1;
        double arg2 = yawSin * pitchCos;
        double arg3 = yawCos * pitchSin;
        double arg4 = yawCos * pitchCos;

        final double tempPitch = pitch;
        final double tempYaw = yaw;

        for (ShipBlock block : this.blocks) {

            if (!block.visible) continue;

            ShipLocation shipLocation = block.shipLoc;

            World world = this.shipLocation.getWorld();

            Location locationShip = new Location(world,
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());

            locationShip.setYaw(0);
            locationShip.setPitch(0);

            locationShip.add(0, block.getyOffset(), 0);

            block.armorStand.teleport(locationShip);


        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(SQSmoothcraft.instance, new Runnable() {
            @Override
            public void run() {

                for (ShipBlock block : blocks) {
                    if (!block.visible) continue;
                    block.armorStand.setHeadPose(new EulerAngle(tempPitch, tempYaw, 0));
                }
            }
        }, 1L);

    }

    private void handleShiftFly() {

        if (getOwner().isSneaking() && !isAutopilot && getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK) {

            //Don't ask me why but this makes chunk loading happy
            owner.setSneaking(false);

            core.armorStand.setVelocity(getOwner().getLocation().getDirection().normalize().multiply(1));
            owner.playSound(core.getArmorStand().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 5, 1);
        }

    }

    public void fireMainWeapons() {
        ShipFireMainGunsEvent event = new ShipFireMainGunsEvent(owner, this);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            for (ShipBlock block : pistons) {
                new CannonTask(block.getArmorStand().getLocation(), owner.getLocation().getDirection(), this);
            }

        }
    }

    private void handleAutoPilot() {
        if (isAutopilot) {

            //God is dead and we have killed him
            for (Ship other : SQSmoothcraft.instance.allShips) {
                if (other.getLocation().distance(this.getLocation()) <= 1000) {
                    for (Module module : other.modules) {
                        if (module instanceof Jammer && module.active) {
                            this.isAutopilot = false;
                            owner.sendMessage(ChatColor.RED + "Autopilot has been disabled due to a jammer nearby!");
                        }
                    }
                }
            }
            core.armorStand.setVelocity(this.autoPilotDirection.normalize().multiply(1));
        }
    }

    //Trigger on unpilot
    public void buildSolid() {

        double pitch = Math.toRadians(0);

        double euToDeg = core.armorStand.getHeadPose().getY() * 180 / Math.PI;
        if (euToDeg < 0) euToDeg += 360;


        double closestCard = MathUtils.getClosestCardinal(euToDeg);
        double yaw = Math.toRadians(closestCard);

        double yawCos = Math.cos(yaw);
        double yawSin = Math.sin(yaw);

        double pitchCos = Math.cos(pitch);
        double pitchSin = Math.sin(pitch);

        double arg1 = yawSin * pitchSin * -1;
        double arg2 = yawSin * pitchCos;
        double arg3 = yawCos * pitchSin;
        double arg4 = yawCos * pitchCos;

        boolean canCompile = true;

        for (ShipBlock block : this.blocks) {
            ShipLocation shipLocation = block.shipLoc;

            Location locationShip = new Location(this.shipLocation.getWorld(),
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());

            locationShip.add(0, 1, 0);

            if (locationShip.getBlock().getType() != Material.AIR) {
                canCompile = false;
                break;
            }

        }


        if (canCompile) {
            owner.setFlying(false);
            owner.leaveVehicle();
            Location orig = null;
            for (ShipBlock block : this.blocks) {

                ShipLocation shipLocation = block.shipLoc;

                Location locationShip = new Location(this.shipLocation.getWorld(),
                        ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                        ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                        ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());


                //Fixes ships building a block below where they are showing
                locationShip.add(0, 1, 0);

                if (block.getMaterial() == Material.NOTE_BLOCK) {
                    orig = locationShip;
                }

                locationShip.getWorld().getBlockAt(locationShip).setType(block.getMaterial());
                locationShip.getWorld().getBlockAt(locationShip).setBlockData(block.blockData);

                if (block.visible) block.getArmorStand().remove();
            }

           /* ArrayList<String> moduleNames = new ArrayList<>();
            for(Module module : modules){
                moduleNames.add(ModuleUtils.getModuleName(module));
            } */

            SQSmoothcraft.instance.solidShips.add(new SolidShipData(orig, modules, owner.getUniqueId().toString()));

            SQSmoothcraft.instance.allShips.remove(this);

        } else {
            owner.sendMessage(ChatColor.RED + "Can not unpilot ship; blocks are in the way!");
        }
    }


}
