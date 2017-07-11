package com.george.invPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandSortToggle extends BukkitCommand {

	String help = InventorySort.getInstance().getConfig().getConfigurationSection("toggle-options").getString("toggle-help");
	String usage = InventorySort.getInstance().getConfig().getConfigurationSection("toggle-options").getString("toggle-usage");
	Player player;
	
	protected CommandSortToggle(String name) {
        super(name);
        this.description = help;
        this.usageMessage = usage;
        this.setPermission("inventorysort.toggle");
        this.setAliases(new ArrayList<String>());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (exe != null) {
			if (sender instanceof Player) {
		    	player = (Player) sender;
		    	if (args.length == 0) {
					if (player.hasPermission("inventorysort.toggle")) {
						if (player.hasMetadata("commandToggle")) {
							player.removeMetadata("commandToggle", InventorySort.getInstance());
				            player.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleMessageOn()));
						} else if (!player.hasMetadata("commandToggle")) {
							player.setMetadata("commandToggle", new FixedMetadataValue(InventorySort.getInstance(), 1));
				            player.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleMessageOff()));
						}
					} else {   
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleNoPermission()));
					}
		    	} else if (args.length == 1) {
		        	Player target = Bukkit.getPlayer(args[0]);
					if (player.hasPermission("inventorysort.sorttoggle.others")) {
						if (target != null) {
		    				if (target.hasMetadata("commandToggle")) {
		    					target.removeMetadata("commandToggle", InventorySort.getInstance());
		    					target.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleMessageOn()));
		    				} else if (!target.hasMetadata("commandToggle")) {
		    					target.setMetadata("commandToggle", new FixedMetadataValue(InventorySort.getInstance(), 1));
		    					target.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleMessageOff()));
		    				}
		    			} else {
		    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleOthersNoPermission()));
		    			}
		        	} else {
		        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', togglePlayerOffline()));
		        	}
		    	}
			}
			return true;
		}
		return false;
	}

	private String toggleMessageOn() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("toggle-message-on");
		return message;
	}

	private String toggleMessageOff() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("toggle-message-off");
		return message;
	}
	
	private String toggleNoPermission() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("toggle-lackof-permission");
		return message;
	}

	private String toggleOthersNoPermission() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("toggle-others-lackof-permission");
		return message;
	}
	
	private String togglePlayerOffline() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("debug").getString("toggle-player-offline");
		return message;
	}

    private CommandExecutor exe = null;
    
	public void setExecutor(CommandExecutor exe) {
        this.exe = exe;		
	}
		
}
