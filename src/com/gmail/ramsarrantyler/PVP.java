package com.gmail.ramsarrantyler; //plugin.getServer().shutdown();

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVP extends JavaPlugin {
	final Random generator = new Random();
	int game_time = 60;
	int game_time_refresh = game_time;
	boolean game_start = false;
	
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
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule mobGriefing false");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams add blue");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams add red");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option red friendlyfire false");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option blue friendlyfire false");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option red seefriendlyinvisibles true");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option blue seefriendlyinvisibles true");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option blue color aqua");
	       Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams option red color red");
	       getCommand("kit").setExecutor(new PVPCommandExecutor(this));
	       getCommand("join").setExecutor(new PVPCommandExecutor(this));
	       int delay = 15000; //15 seconds
	       ActionListener taskPerformer = new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	        	   if(game_time >= 0){
	        		   if(game_time != 0){
	        			   Bukkit.broadcastMessage("The game will begin in " + game_time + " seconds.");
	        		   }
	        		   game_time = game_time-15;
	        		   if(game_time == -15){
	        			   if(Bukkit.getOnlinePlayers().length >= 2){
	        				   Bukkit.broadcastMessage("The game will now begin!");
	        				   game_start = true;
	        				   for(Player players : Bukkit.getServer().getOnlinePlayers())
	        				   {
	        				       if(players.hasPermission("PVP.blue")){
	        				    	   Location blue_spawn = new Location(players.getWorld(), 2.5, 151, 104, 181, 7);
	        				    	   players.teleport(blue_spawn);
	        				       }
	        				       if(players.hasPermission("PVP.red")){
	        				    	   Location red_spawn = new Location(players.getWorld(), 2.5, 151, -102.5, -2, 0);
	        				    	   players.teleport(red_spawn);
	        				       }
	        				   }
	        			   } else {
		            		   Bukkit.broadcastMessage("Not enough players, count restarting.");
		            		   game_time = game_time_refresh;
	        			   }
	        		   }
	        	   }
	           }
	       };
	       new Timer(delay, taskPerformer).start();	       
	}
	
	public class PVPCommandExecutor implements CommandExecutor {
		public PVP plugin;
		 
		public PVPCommandExecutor(PVP plugin) {
			this.plugin = plugin;
		}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kit")){
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length == 0) {
					PlayerInventory inventory = player.getInventory();
					inventory.clear();
					inventory.setHelmet(null);
					inventory.setChestplate(null);
					inventory.setLeggings(null);
					inventory.setBoots(null);
					player.setExp(0);
					player.openInventory(kit);
				}
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage("You must be a player to choose a kit.");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("join")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				Random generator = new Random();
				String name = player.getName();
				int random = generator.nextInt(2) + 1;
				if(random == 1){
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join blue " + name);
					PermissionAttachment attachment = player.addAttachment(plugin);
					attachment.setPermission("PVP.blue", true);
					if(player.hasPermission("PVP.red")){
						attachment.setPermission("PVP.red", false);
					}
					player.sendMessage(ChatColor.AQUA + "You've joined the BLUE team!");
					if(game_start == true){
						Location blue_spawn = new Location(player.getWorld(), 2.5, 151, 104, 181, 7);
				    	player.teleport(blue_spawn);
					}
					return true;
				}
				if(random == 2){
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join red " + name);
					PermissionAttachment attachment = player.addAttachment(plugin);
					attachment.setPermission("PVP.red", true);
					if(player.hasPermission("PVP.blue")){
						attachment.setPermission("PVP.blue", false);
					}
					player.sendMessage(ChatColor.RED + "You've joined the RED team!");
					if(game_start == true){
						Location red_spawn = new Location(player.getWorld(), 2.5, 151, -102.5, -2, 0);
						player.teleport(red_spawn);
					}
					return true;
				}
			}
			if(!(sender instanceof Player)){
				sender.sendMessage("You must be a player in order to join a team");
				return true;
			}
		}
		return false;
	}
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
		public void onHit(EntityDamageByEntityEvent evt){
			Entity entity = evt.getEntity();
			Location loc = entity.getLocation();
			World world = entity.getWorld();
			world.playEffect(loc, Effect.STEP_SOUND, 55);
		}
		
		@EventHandler
		public void onLogin(final PlayerJoinEvent evt) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				 
		        @Override
		        public void run() {
		            Player player = evt.getPlayer();
					PlayerInventory inventory = player.getInventory();
					Location player_loc = player.getLocation();
					Location spawn = player_loc;
					spawn.setX(4.5);
					spawn.setY(101.5);
					spawn.setZ(1.5);
					inventory.clear();
					inventory.setHelmet(null);
					inventory.setChestplate(null);
					inventory.setLeggings(null);
					inventory.setBoots(null);
					player.setExp(0);
					player.teleport(spawn);
					if(player.isOp()){
						PermissionAttachment attachment = player.addAttachment(plugin);
						attachment.setPermission("PVP.blue", false);
						attachment.setPermission("PVP.red", false);
						attachment.setPermission("PVP.nofall", false);
					}
		        }
		        
		    }, 10L);
		}
		
		@EventHandler
		public void onFoodLevelChangeEvent(FoodLevelChangeEvent evt){
	        evt.setCancelled(true);
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
		public void onPlayerLeave(PlayerQuitEvent evt){
			Player player = evt.getPlayer();
			PermissionAttachment attachment = player.addAttachment(plugin);
			String name = player.getName();
			if(player.hasPermission("PVP.blue")){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams leave " + name);
				attachment.setPermission("PVP.blue", false);
			}
			if(player.hasPermission("PVP.red")){
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams leave " + name);
				attachment.setPermission("PVP.red", false);
			}
		}
		
		@EventHandler
		public void onRespawn(final PlayerRespawnEvent evt) {
			final Player player = evt.getPlayer();
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
		        	if(player.hasPermission("PVP.blue")){
		        		Location blue_spawn = new Location(player.getWorld(), 2.5, 151, 104, 181, 7);
				    	player.teleport(blue_spawn);
					}
					if(player.hasPermission("PVP.red")){
						Location red_spawn = new Location(player.getWorld(), 2.5, 151, -102.5, -2, 0);
						player.teleport(red_spawn);
					}
		            evt.getPlayer().openInventory(kit);
		        }
		        
			}, 10L);
			
		}
		
		@EventHandler
		public void EntityDamageFall(EntityDamageEvent evt) {
			DamageCause cause = evt.getCause();
			if(cause == DamageCause.FALL) {
				if(evt.getEntity() instanceof Player) {
					Player player = (Player) evt.getEntity();
					if(player.hasPermission("PVP.nofall")) {
						evt.setCancelled(true);
					}
				}
			}
		}
	}
	
	@Override
    public void onDisable(){ //Upon disable of the plugin, do something.
 	   getLogger().info("The great PVP plugin is now off."); //Code goes here.
	}
}
