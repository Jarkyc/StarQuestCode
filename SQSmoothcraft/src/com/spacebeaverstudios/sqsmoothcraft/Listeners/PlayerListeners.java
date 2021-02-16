package com.spacebeaverstudios.sqsmoothcraft.Listeners;

import com.spacebeaverstudios.sqsmoothcraft.Events.ShipAutopilotEvent;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.DetectionTask;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.spigotmc.event.entity.EntityDismountEvent;


public class PlayerListeners implements Listener {


    @EventHandler
    public static void onInteract(PlayerInteractEvent e){

        if(e.getPlayer().getInventory().getItemInMainHand().getType() != Material.CLOCK)
            return;

        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && ShipUtils.isAPilot(e.getPlayer())){
            Ship ship = ShipUtils.getShipByPlayer(e.getPlayer());

            if(ship.isAutopilot){
                ship.isAutopilot = false;
                ship.autoPilotDirection = null;
                e.getPlayer().sendMessage(ChatColor.GREEN + "Stopping auto-pilot");
            } else {
                ShipAutopilotEvent event = new ShipAutopilotEvent(e.getPlayer(), ship, e.getPlayer().getLocation().getDirection().normalize());
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()){
                    ship.isAutopilot = true;
                    ship.autoPilotDirection = e.getPlayer().getLocation().getDirection();
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Initiating auto-pilot");
                }
            }
        }

        if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && ShipUtils.isAPilot(e.getPlayer())){
            ShipUtils.getShipByPlayer(e.getPlayer()).fireMainWeapons();
        }
        if(e.getClickedBlock() == null) return;

        if(e.getClickedBlock().getType() == Material.NOTE_BLOCK && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!ShipUtils.isAPilot(e.getPlayer())) {
                    e.setCancelled(true);
                    new DetectionTask(e.getClickedBlock().getLocation(), e.getPlayer());
            } else {
                e.getPlayer().sendMessage(ChatColor.RED + "You are already piloting a ship!");
            }
        }



    }

    @EventHandler
    public static void onDismount(EntityDismountEvent e){

        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if(ShipUtils.isAPilot(player)){
               e.setCancelled(true);
            }

        }

    }

    @EventHandler
    public static void entityInteract(PlayerInteractAtEntityEvent e){
        Player player = e.getPlayer();
        Entity ent = e.getRightClicked();

        if(ent.getType() == EntityType.ARMOR_STAND){

            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CLOCK && ShipUtils.isAPilot(e.getPlayer())){
                Ship ship = ShipUtils.getShipByPlayer(e.getPlayer());

                if(ship.isAutopilot){
                    ship.isAutopilot = false;
                    ship.autoPilotDirection = null;
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Stopping auto-pilot");
                } else {
                    ship.isAutopilot = true;
                    ship.autoPilotDirection = e.getPlayer().getLocation().getDirection();
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Initiating auto-pilot");
                }
            }
        }
    }

    @EventHandler
    public static void onClick(InventoryClickEvent e){
        Inventory inv = e.getInventory();

        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        //Ship info window
        if(e.getView().getTitle().equals(ChatColor.BLUE + e.getWhoClicked().getName() + "'s Ship Info")){
            if(e.getCurrentItem().getType() == Material.PISTON && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Modules")){
                e.getWhoClicked().openInventory(ShipUtils.getShipByPlayer((Player) e.getWhoClicked()).moduleWindow);
            }
            e.setCancelled(true);

        // Module inv
        } else if(e.getView().getTitle().equals(ChatColor.GREEN + e.getWhoClicked().getName() + "'s Ship Modules")){
            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Weapons")){
                e.getWhoClicked().openInventory(ShipUtils.getShipByPlayer((Player) e.getWhoClicked()).weaponModulesWind);
            } else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Defense")){
                e.getWhoClicked().openInventory(ShipUtils.getShipByPlayer((Player) e.getWhoClicked()).defenseModulesWind);
            } else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Utility")){
                e.getWhoClicked().openInventory(ShipUtils.getShipByPlayer((Player) e.getWhoClicked()).utilModulesWind);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void armorStandManipulate(PlayerArmorStandManipulateEvent e){
        ArmorStand stand = e.getRightClicked();
        Player player = e.getPlayer();

        if(ShipUtils.getShipByStand(stand) != null){
            e.setCancelled(true);
        }
    }

}
