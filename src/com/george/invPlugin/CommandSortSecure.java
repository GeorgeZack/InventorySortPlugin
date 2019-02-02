package com.george.invPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandSortSecure extends BukkitCommand {
	
	String help = InventorySort.getInstance().getConfig().getConfigurationSection("secure-options").getString("secure-help");
	String usage = InventorySort.getInstance().getConfig().getConfigurationSection("secure-options").getString("secure-usage");
	private File dataF;
	private FileConfiguration data;
	Player player;
	
	protected CommandSortSecure(String name) {
        super(name);
        this.description = help;
        this.usageMessage = usage;
        this.setPermission("inventorysort.secure");
        this.setAliases(new ArrayList<String>());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (exe != null) {
			if (sender instanceof Player) {
		    	player = (Player) sender;
		    	UUID playerId = player.getUniqueId();
				if (player.hasPermission("inventorysort.secure")) {
					if (isChest()) {
				        dataF = new File(InventorySort.getInstance().getDataFolder(), "data.yml");
				        data = new YamlConfiguration();
				        
				        try {
							data.load(dataF);
				        } catch (IOException e) {
							e.printStackTrace();
						} catch (InvalidConfigurationException e) {
							e.printStackTrace();
						}
				        
				        for (String key : data.getKeys(false)) {
				        	if (!(data.contains(key))) {
				        		data.createSection(key);
				        	}
				        }
				        
			        	if (!(data.contains(CommandSort.getLocation(getCurrentChest().getLocation())))) {
			        		data.set(CommandSort.getLocation(getCurrentChest().getLocation()), playerId.toString());
					        try {
						        data.save(dataF);
					        } catch (IOException e) {
								e.printStackTrace();
							}
				            player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureMessage()));
			        	} else {
				        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureMessageDuplicate()));
			        	}
					}
					else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureMessageFail()));
					}
				} else {   
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureNoPermission()));
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private String secureMessage() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("secure-message");
		return message;
	}

	private String secureMessageDuplicate() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("secure-message-duplicate");
		return message;
	}
	
	private String secureMessageFail() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("secure-message-error");
		return message;
	}
	
	private String secureNoPermission() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("secure-lackof-permission");
		return message;
	}

	private Boolean isChest() {
		Block block = player.getTargetBlock((Set<Material>)null, 5);
    	if (block.getState() instanceof Chest) {
            return true;
        }
    	
		return false;
	}
	
	private Chest getCurrentChest() {
		Block block = player.getTargetBlock((Set<Material>)null, 5);
		Chest chest = null;
    	if (block.getState() instanceof Chest) {
        	chest = (Chest) block.getState();
    	}
    	
    	return chest;
	}
    
    private CommandExecutor exe = null;
    
	public void setExecutor(CommandExecutor exe) {
        this.exe = exe;		
	}
		
}
