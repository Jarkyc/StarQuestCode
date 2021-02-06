package com.spacebeaverstudios.sqhelpgui;

import com.spacebeaverstudios.sqhelpgui.Functions.BookOpenFunction;
import com.spacebeaverstudios.sqhelpgui.Functions.MessageSendFunction;
import com.spacebeaverstudios.sqhelpgui.listeners.onClickEvent;
import com.spacebeaverstudios.sqhelpgui.objects.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class SQHelpGUI extends JavaPlugin {

    public static Inventory mainInv;

    public void onEnable(){

        System.out.println("Help GUI Started");

        HelpCmd command = new HelpCmd();

        getCommand("help").setExecutor(command);

        buildGUI();

        getServer().getPluginManager().registerEvents(new onClickEvent(), this);
    }

    public void buildGUI(){

        this.mainInv = Bukkit.getServer().createInventory(null, 54, ChatColor.GOLD + "Help GUI");

        GUIItem border = new GUIItem(" ", "", Material.GRAY_STAINED_GLASS_PANE, null);

        int[] borderPlacement = new int[]{0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 48, 50, 52};

        for(int place : borderPlacement){

            mainInv.setItem(place, border.getItemStack());

        }


        GUIItem disclaimer = new GUIItem(ChatColor.RED + "!!Disclaimer!!", "Some stuff in here may not be 100% finished, as the server is constantly changing. We try our best to update it as we go.", Material.RED_CONCRETE, null);
        mainInv.setItem(4, disclaimer.getItemStack());

        GUIItem youtube = new GUIItem(ChatColor.RED + "SBS YouTube", "", Material.RED_SHULKER_BOX, new MessageSendFunction("https://www.youtube.com/channel/UCGxApWyShU25G9L_qjGTI6A"));
        mainInv.setItem(45, youtube.getItemStack());

        GUIItem twitter = new GUIItem(ChatColor.AQUA + "SBS Twitter", "", Material.LIGHT_BLUE_SHULKER_BOX, new MessageSendFunction("https://twitter.com/SpaceBeaverSTU") );
        mainInv.setItem(47, twitter.getItemStack());

        GUIItem discord = new GUIItem(ChatColor.GRAY + "SBS Discord", "", Material.LIGHT_GRAY_SHULKER_BOX, new MessageSendFunction("https://discordapp.com/invite/0tUluUyNrchydOIb"));
        mainInv.setItem(49, discord.getItemStack());

        GUIItem website = new GUIItem(ChatColor.BLUE + "SBS Website", "", Material.BLUE_SHULKER_BOX, new MessageSendFunction("http://www.spacebeaverstudios.com/"));
        mainInv.setItem(51, website.getItemStack());

        GUIItem dynmap = new GUIItem(ChatColor.GREEN + "SQ Dynmap", "", Material.GREEN_SHULKER_BOX, new MessageSendFunction("http://starquest.spacebeaverstudios.com:8123/index.html"));
        mainInv.setItem(53, dynmap.getItemStack());

        /*
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor("The Lone Spacebeaver");
        bookMeta.setTitle("What is SQ?");

        ArrayList<String> pages = new ArrayList<>();

        pages.add(ChatColor.GOLD + "What is StarQuest?");
        pages.add("test");

        bookMeta.setPages(pages);

        book.setItemMeta(bookMeta);

        GUIItem whatIsSQ = new GUIItem(ChatColor.DARK_BLUE + "What Is StarQuest?", "The fundamentals of StarQuest, and what it is.", Material.ENDER_EYE, new BookOpenFunction(book));
        mainInv.setItem(10, whatIsSQ.getItemStack());

        GUIItem planets = new GUIItem(ChatColor.YELLOW + "Planets", "About StarQuests Planets", Material.NETHER_STAR, null);
        mainInv.setItem(12, planets.getItemStack());

        GUIItem ships = new GUIItem(ChatColor.RED + "Starships", "Starquest's most unique feature!", Material.MINECART, null);
        mainInv.setItem(14, ships.getItemStack());

        GUIItem holodeck = new GUIItem(ChatColor.AQUA + "Holodeck", "About Starquest holodecks!", Material.ENDER_CHEST, null);
        mainInv.setItem(16, holodeck.getItemStack());

        GUIItem tech = new GUIItem(ChatColor.GRAY + "Technology", "About technology on Starquest", Material.PISTON, null);
        mainInv.setItem(38, tech.getItemStack());

        GUIItem towns = new GUIItem(ChatColor.DARK_AQUA + "Towns", "Towny on Starquest", Material.SHIELD, null);
        mainInv.setItem(40, towns.getItemStack());

        GUIItem eco = new GUIItem(ChatColor.GOLD + "Economy", "Making money on StarQuest", Material.SUNFLOWER, null);
        mainInv.setItem(42, eco.getItemStack());

*
 */

        GUIItem comingSoon = new GUIItem(ChatColor.GOLD + "COMING SOON!", "This project is currently still under development! If you need help with something at this moment in time, please refer to the wiki.", Material.BARRIER, null);
        mainInv.setItem(22, comingSoon.getItemStack());
    }


}
