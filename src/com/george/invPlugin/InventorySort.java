package com.george.invPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class InventorySort extends JavaPlugin {

    private static InventorySort instance;
	private File configF, dataF;
	private FileConfiguration config, data;
	private static CommandMap cmap;
	Server server = Bukkit.getServer();
	
    @Override
    public void onEnable() {
    	Bukkit.getConsoleSender().sendMessage(org.bukkit.ChatColor.GRAY + "[InventorySort] Inventory" + org.bukkit.ChatColor.AQUA + "Sort" + org.bukkit.ChatColor.GRAY + " is loading...");
        try {
            if (Bukkit.getServer() instanceof CraftServer) {
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap)f.get(Bukkit.getServer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    	createFiles();
    	FileConfiguration config = getConfig();
        getLogger().info("Registering commands...");
        
        // HEADER
        config.options().header("InventorySort\nBy George Zackrison\n\n");
        
        // GENERAL
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
    	messages.addDefault("toggle-message-send", false);
    	messages.addDefault("sort-type-message-name", "&8Sorting by Name");
    	messages.addDefault("sort-type-message-material", "&8Sorting by Material");
    	messages.addDefault("secure-message", "&8Chest secured, only you can sort this chest");
    	messages.addDefault("secure-message-duplicate", "&cChest is already secured");
    	messages.addDefault("secure-message-error", "&cNot a chest, try again");
    	messages.addDefault("secure-message-owned", "&cChest is secured and cannot be sorted by you");
    	
    	// COOLDOWN
    	if (config.getConfigurationSection("cooldown") == null) {
    		config.createSection("cooldown");
    	}
    	ConfigurationSection cooldown = config.getConfigurationSection("cooldown");
    	cooldown.addDefault("cooldown-hot-message", "&cYou still have time left before you can use the command");
    	cooldown.addDefault("cooldown-time", 0);
    	
    	// PERMISSIONS
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
    	
    	// SORT OPTIONS
    	if (config.getConfigurationSection("sort-options") == null) {
    		config.createSection("sort-options");
    	}
    	ConfigurationSection sortOptions = config.getConfigurationSection("sort-options");
    	sortOptions.addDefault("sort-command", "sort");
    	sortOptions.addDefault("sort-command-alias", "s");
    	sortOptions.addDefault("sort-help", "Sorts inventory of player or of a chest.");
    	sortOptions.addDefault("sort-usage", "/<command> [type] \n" +
            "\u00A76Type:\u00A7f (up, u, abc, down, d, xyz) \n" +
            "\u00A76Example:\u00A7f /<command> up - Sort inventory in alphabetical order");
    	
    	// TOGGLE OPTIONS
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
    	
    	// TYPE OPTIONS
    	if (config.getConfigurationSection("type-options") == null) {
    		config.createSection("type-options");
    	}
    	ConfigurationSection typeOptions = config.getConfigurationSection("type-options");
    	typeOptions.addDefault("type-command", "sort-type");
    	typeOptions.addDefault("type-command-alias", "sy");
    	typeOptions.addDefault("type-help", "Changes the default sorting behavior.");
    	typeOptions.addDefault("type-usage", "/<command> [type] \n" +
			"\u00A76Type:\u00A7f material, name \n" +
			"\u00A76Example:\u00A7f /<command> - Toggles whether sorting should be by \n" +
			"material or by name\n" +
			"\u00A76Example:\u00A7f /<command> material - Forces type to be by material\n" +
			"\u00A76Example:\u00A7f /<command> name - Forces type to be by name");
    	
    	// SECURE OPTIONS
    	if (config.getConfigurationSection("secure-options") == null) {
    		config.createSection("secure-options");
    	}
    	ConfigurationSection secureOptions = config.getConfigurationSection("secure-options");
    	secureOptions.addDefault("secure-command", "sort-secure");
		secureOptions.addDefault("secure-command-alias", "sc");
		secureOptions.addDefault("secure-help", "Secures chest so only you can sort this chest");
		secureOptions.addDefault("secure-usage", "/<command> \n" +
            "\u00A76Example:\u00A7f /<command> - Secures selected chest");

    	// HELP OPTIONS
    	if (config.getConfigurationSection("help-options") == null) {
    		config.createSection("help-options");
    	}
    	ConfigurationSection helpOptions = config.getConfigurationSection("help-options");
    	helpOptions.addDefault("help-command", "InventorySort");
    	helpOptions.addDefault("help-command-alias", "inventorysort");
    	helpOptions.addDefault("help-help", "InventorySort Help");
    	helpOptions.addDefault("help-usage", "/<command> \n" +
            "\u00A76Example:\u00A7f /<command> - InventorySort help");
		
		// DEBUG
    	if (config.getConfigurationSection("debug") == null) {
    		config.createSection("debug");
    	}
    	ConfigurationSection debug = config.getConfigurationSection("debug");
    	debug.addDefault("toggle-player-offline", "&cPlayer is not online");
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
//		String helpCommand = helpOptions.getString("help-command");
//		String helpCommandAlias = helpOptions.getString("help-command-alias");
	    
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
		
		Command secureAlias = new CommandSortSecure(secureCommandAlias);
		cmap.register(secureCommandAlias, secureAlias);
		((CommandSortSecure) secureAlias).setExecutor(this);
		
//		Command help = new CommandHelp(helpCommand);
//		cmap.register(helpCommand, help);
//		((CommandHelp) help).setExecutor(this);
//		
//		Command helpAlias = new CommandHelp(helpCommandAlias);
//		cmap.register(helpCommandAlias, helpAlias);
//		((CommandHelp) helpAlias).setExecutor(this);
		
        getLogger().info("Initiliazed");
    }

    public CommandMap getCommandMap(){
        return cmap;
    }
    
	@Override
    public void onDisable() {
    	Bukkit.getConsoleSender().sendMessage(org.bukkit.ChatColor.GRAY + "[InventorySort] Inventory" + org.bukkit.ChatColor.AQUA + "Sort" + org.bukkit.ChatColor.GRAY + " disabled");
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
