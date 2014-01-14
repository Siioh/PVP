package com.gmail.ramsarrantyler; //plugin.getServer().shutdown();

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVP extends JavaPlugin {

	@Override
	public void onEnable(){ //Upon enable of the plugin, do something.
	       getLogger().info("The great PVP plugin has be successfully loaded."); //Code goes here.
	       getLogger().info("Remember, if updating versions delete the previous configuration.");
	       getConfig().options().copyDefaults(true);
	       saveConfig();
	       new PVPListener(this);
	       this.getServer().getPluginManager().registerEvents(new PVPListener(this), this);
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule commandBlockOutput false");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory true");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobLoot false");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					player.openInventory(kit);
				}
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to choose a kit.");
			}
			return true;
		}
		return false;
	}
	
	public static Inventory kit = Bukkit.createInventory(null, 9, "Kit");
    static {
		
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemMeta bow_meta = bow.getItemMeta();
		ArrayList<String> bow_lore = new ArrayList<String>();
		bow_lore.add("Comes with a bow and some arrows."); //Set lore
		bow_meta.setLore(bow_lore);
		bow_meta.setDisplayName("Archer"); //Set name
		bow.setItemMeta(bow_meta);
		kit.setItem(0, bow);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta sword_meta = sword.getItemMeta();
		ArrayList<String> sword_lore = new ArrayList<String>();
		sword_lore.add("An iron sword and armor, perfect for a knight.");
		sword_meta.setLore(sword_lore);
		sword_meta.setDisplayName("Knight");
		sword.setItemMeta(sword_meta);
		kit.setItem(1, sword);
		
		ItemStack fire = new ItemStack(Material.FIRE, 1);
		ItemMeta fire_meta = fire.getItemMeta();
		ArrayList<String> fire_lore = new ArrayList<String>();
		fire_lore.add("A rapid firing bow with some armor.");
		fire_meta.setLore(fire_lore);
		fire_meta.setDisplayName("Quickshot");
		fire.setItemMeta(fire_meta);
		kit.setItem(2, fire);
		
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrow_meta = arrow.getItemMeta();
		ArrayList<String> arrow_lore = new ArrayList<String>();
		arrow_lore.add("A nearly 1 hit sniper bow.");
		arrow_meta.setLore(arrow_lore);
		arrow_meta.setDisplayName("Sniper");
		arrow.setItemMeta(arrow_meta);
		kit.setItem(3, arrow);
		
		ItemStack portal = new ItemStack(Material.PORTAL, 1);
		ItemMeta portal_meta = portal.getItemMeta();
		ArrayList<String> portal_lore = new ArrayList<String>();
		portal_lore.add("Just a little bit 'o' magic...");
		portal_meta.setLore(portal_lore);
		portal_meta.setDisplayName("Magician");
		portal.setItemMeta(portal_meta);
		kit.setItem(4,portal);
		
		ItemStack wither = new ItemStack(Material.LAVA, 1);
		ItemMeta wither_meta = wither.getItemMeta();
		ArrayList<String> wither_lore = new ArrayList<String>();
		wither_lore.add("Equipped with an exploding wither bow.");
		wither_meta.setLore(wither_lore);
		wither_meta.setDisplayName("Wither");
		wither.setItemMeta(wither_meta);
		kit.setItem(5, wither);
		
		}
	
	public class PVPListener implements Listener {
		public PVP plugin;
		
		public PVPListener(PVP plugin) {
			this.plugin = plugin;
		}
		
		@EventHandler
		public void onLogin(final PlayerJoinEvent evt) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				 
		        @Override
		        public void run() {
		            Player player = evt.getPlayer();
					PlayerInventory inventory = player.getInventory();
					inventory.clear();
					inventory.setHelmet(null);
					inventory.setChestplate(null);
					inventory.setLeggings(null);
					inventory.setBoots(null);
					player.setExp(0);
					evt.getPlayer().openInventory(kit);
		        }
		        
		    }, 10L);
		}
		
		@EventHandler
        public void onInventoryClick(InventoryClickEvent evt) {
			Player player = (Player) evt.getWhoClicked();
        	ItemStack clicked = evt.getCurrentItem();
        	Inventory inventory = evt.getInventory();
        	Inventory pinventory = player.getInventory();
        	if (inventory.getName().equals(kit.getName())) {
        		evt.setCancelled(true);
        		player.closeInventory();
        		if (clicked.getType() == Material.BOW) {
        			pinventory.addItem(new ItemStack(Material.BOW, 1));
        			pinventory.addItem(new ItemStack(Material.ARROW, 64));
        		}
        		
        		if(clicked.getType() == Material.DIAMOND_PICKAXE) {
        		}
        			
        		if(clicked.getType() == Material.FIRE) {
        		}
        			
        		if(clicked.getType() == Material.ARROW) {
        		}
        			
        		if(clicked.getType() == Material.IRON_INGOT) {
        		}
        		player.playSound(player.getLocation(), Sound.CLICK, 10, 1);
        	}
        }
		
		@EventHandler
		public void onRespawn(final PlayerRespawnEvent evt) {
			Player player = evt.getPlayer();
			PlayerInventory inventory = player.getInventory();
			inventory.clear();
			inventory.setHelmet(null);
			inventory.setChestplate(null);
			inventory.setLeggings(null);
			inventory.setBoots(null);
			player.setExp(0);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				 
		        @Override
		        public void run() {
		            evt.getPlayer().openInventory(kit);
		        }
		        
		}, 10L);
	}
	}
	
	@Override
    public void onDisable(){ //Upon disable of the plugin, do something.
 	   getLogger().info("The great PVP plugin is now off."); //Code goes here.
	}
}
