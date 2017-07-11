package com.george.invPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class InventorySort extends JavaPlugin {

    private static InventorySort instance;
	private File configF, dataF;
	private FileConfiguration config, data;
	private static CommandMap cmap;
	CraftServer server = (CraftServer) Bukkit.getServer();
	
    @Override
    public void onEnable() {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[InventorySort] Inventory" + ChatColor.AQUA + "Sort" + ChatColor.GRAY + " is loading...");
        try{
            if(Bukkit.getServer() instanceof CraftServer){
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap)f.get(Bukkit.getServer());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        
    	createFiles();
    	FileConfiguration config = getConfig();
        getLogger().info("Registering commands...");
    	if (config.getConfigurationSection("messages") == null) {
    		config.createSection("messages");
    	}
    	ConfigurationSection messages = config.getConfigurationSection("messages");
    	messages.addDefault("inv-message", "&8Sorted your inventory");
    	messages.addDefault("inv-message-send", true);
    	messages.addDefault("chest-message", "&8Sorted your chest");
    	messages.addDefault("chest-message-send", true);
    	messages.addDefault("toggle-message-on", "&8Chest sorting disabled");
    	messages.addDefault("toggle-message-off", "&8Chest sorting enabled");
    	messages.addDefault("toggle-message-send", true);
    	messages.addDefault("sort-type-message-int", "&8Sorting by ID");
    	messages.addDefault("sort-type-message-string", "&8Sorting by Name");
    	messages.addDefault("secure-message", "&8Chest secured, only you can sort this chest");
    	messages.addDefault("secure-message-duplicate", "&cChest is already secured");
    	messages.addDefault("secure-message-error", "&cNot a chest, try again");
    	messages.addDefault("secure-message-owned", "&cChest is secured and cannot be sorted by you");
    	if (config.getConfigurationSection("cooldown") == null) {
    		config.createSection("cooldown");
    	}
    	ConfigurationSection cooldown = config.getConfigurationSection("cooldown");
    	cooldown.addDefault("cooldown-hot-message", "&cYou still have time left before you can use the command");
    	cooldown.addDefault("cooldown-time", 0);
    	if (config.getConfigurationSection("permissions") == null) {
    		config.createSection("permissions");
    	}
    	ConfigurationSection permissions = config.getConfigurationSection("permissions");
    	permissions.addDefault("player-lackof-permission", "&cYou don't have permission to sort your inventory");
    	permissions.addDefault("chest-lackof-permission", "&cYou don't have permission to sort chests");
    	permissions.addDefault("toggle-lackof-permission", "&cYou don't have permission to toggle chest sorting");
    	permissions.addDefault("toggle-others-lackof-permission", "&cYou don't have permission to toggle other players chest sorting");
    	permissions.addDefault("type-lackof-permission", "&cYou don't have permission to change sorting behavior");
    	permissions.addDefault("secure-lackof-permission", "&cYou don't have permission to secure a chest");
    	if (config.getConfigurationSection("sort-options") == null) {
    		config.createSection("sort-options");
    	}
    	ConfigurationSection sortOptions = config.getConfigurationSection("sort-options");
    	sortOptions.addDefault("sort-command", "sort");
    	sortOptions.addDefault("sort-command-alias", "s");
    	sortOptions.addDefault("sort-help", "Sorts inventory of player or of a chest.");
    	sortOptions.addDefault("sort-usage", "/<command> [type] \n" +
            "\u00A76Type:\u00A7f (a, asc, d, des) or (up, abc, down, xyz) \n" +
            "\u00A76Example:\u00A7f /<command> descending - Sort inventory in descending order");
    	if (config.getConfigurationSection("toggle-options") == null) {
    		config.createSection("toggle-options");
    	}
    	ConfigurationSection toggleOptions = config.getConfigurationSection("toggle-options");
    	toggleOptions.addDefault("toggle-command", "sort-toggle");
    	toggleOptions.addDefault("toggle-command-alias", "st");
    	toggleOptions.addDefault("toggle-help", "Toggles the function of hitting chests to sort them.");
    	toggleOptions.addDefault("toggle-usage", "/<command> [target] \n" +
	        "\u00A76Target:\u00A7f playername \n" +
	        "\u00A76Example:\u00A7f /<command> - Toggles ability to sort chests on click \n" +
	        "\u00A76Example:\u00A7f /<command> Notch - Toggles ability to sort chests for Notch");
    	if (config.getConfigurationSection("type-options") == null) {
    		config.createSection("type-options");
    	}
    	ConfigurationSection typeOptions = config.getConfigurationSection("type-options");
    	typeOptions.addDefault("type-command", "sort-type");
    	typeOptions.addDefault("type-command-alias", "sy");
    	typeOptions.addDefault("type-help", "Changes the default sorting behavior.");
    	typeOptions.addDefault("type-usage", "/<command> [type] \n" +
			"\u00A76Type:\u00A7f id, name \n" +
			"\u00A76Example:\u00A7f /<command> - Toggles whether sorting should be by \n" +
			"ID or by names\n" +
			"\u00A76Example:\u00A7f /<command> id - Forces type to be by ID\n" +
			"\u00A76Example:\u00A7f /<command> name - Forces type to be by names");
    	if (config.getConfigurationSection("secure-options") == null) {
    		config.createSection("secure-options");
    	}
    	ConfigurationSection secureOptions = config.getConfigurationSection("secure-options");
    	secureOptions.addDefault("secure-command", "sort-secure");
		secureOptions.addDefault("secure-command-alias", "sc");
		secureOptions.addDefault("secure-help", "Secures chest so only you can sort this chest");
		secureOptions.addDefault("secure-usage", "/<command> \n" +
            "\u00A76Example:\u00A7f /<command> - Secures selected chest");
    	if (config.getConfigurationSection("debug") == null) {
    		config.createSection("debug");
    	}
    	ConfigurationSection debug = config.getConfigurationSection("debug");
    	debug.addDefault("toggle-player-offline", "&cPlayer is not online");
    	debug.addDefault("monster-egg-error", "&cMonster eggs can't be stacked");
	    config.options().copyDefaults(true);
	    saveConfig();
	    
	    getServer().getPluginManager().registerEvents(new EventListener(), this);
	    
		String sortCommand = sortOptions.getString("sort-command");
		String sortCommandAlias = sortOptions.getString("sort-command-alias");
		String toggleCommand = toggleOptions.getString("toggle-command");
		String toggleCommandAlias = toggleOptions.getString("toggle-command-alias");
		String typeCommand = typeOptions.getString("type-command");
		String typeCommandAlias = typeOptions.getString("type-command-alias");
		String secureCommand = secureOptions.getString("secure-command");
		String secureCommandAlias = secureOptions.getString("secure-command-alias");
	    
		Command sort = new CommandSort(sortCommand);
		cmap.register(sortCommand, sort);
		((CommandSort) sort).setExecutor(this);
		
		Command sortAlias = new CommandSort(sortCommandAlias);
		cmap.register(sortCommandAlias, sortAlias);
		((CommandSort) sortAlias).setExecutor(this);
		
		Command toggle = new CommandSortToggle(toggleCommand);
		cmap.register(toggleCommand, toggle);
		((CommandSortToggle) toggle).setExecutor(this);
		
		Command toggleAlias = new CommandSortToggle(toggleCommandAlias);
		cmap.register(toggleCommandAlias, toggleAlias);
		((CommandSortToggle) toggleAlias).setExecutor(this);

		Command type = new CommandSortType(typeCommand);
		cmap.register(typeCommand, type);
		((CommandSortType) type).setExecutor(this);
		
		Command typeAlias = new CommandSortType(typeCommandAlias);
		cmap.register(typeCommandAlias, typeAlias);
		((CommandSortType) typeAlias).setExecutor(this);
		
		Command secure = new CommandSortSecure(secureCommand);
		cmap.register(secureCommand, secure);
		((CommandSortSecure) secure).setExecutor(this);
		
		Command secureAliase = new CommandSortSecure(secureCommandAlias);
		cmap.register(secureCommandAlias, secureAliase);
		((CommandSortSecure) secureAliase).setExecutor(this);
    }

    public CommandMap getCommandMap(){
        return cmap;
    }
    
	@Override
    public void onDisable() {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[InventorySort] Inventory" + ChatColor.AQUA + "Sort" + ChatColor.GRAY + " disabled");
    }

    private void createFiles() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        configF = new File(getDataFolder(), "config.yml");
        dataF = new File(getDataFolder(), "data.yml");
        
        if (!configF.exists()) {
            configF.getParentFile().mkdirs();
            getLogger().info("config.yml not found, creating!");
            saveResource("config.yml", false);
            saveDefaultConfig();
        } else {
        	getLogger().info("config.yml found, loading...");
        }
        if (!dataF.exists()) {
            dataF.getParentFile().mkdirs();
            getLogger().info("data.yml not found, creating!");
            saveResource("data.yml", false);
            saveDefaultConfig();
        } else {
        	getLogger().info("data.yml found, loading...");
        }
        
        config = new YamlConfiguration();
        data = new YamlConfiguration();
        
        try {
        	config.load(configF);
        	data.load(dataF);
        } catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
    }
    
    public InventorySort() {
    	instance = this;
    }
    
    public static InventorySort getInstance() {
    	return instance;
    }
}
