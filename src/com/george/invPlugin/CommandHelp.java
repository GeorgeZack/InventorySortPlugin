package com.george.invPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandHelp extends BukkitCommand {

	String help = InventorySort.getInstance().getConfig().getConfigurationSection("help-options").getString("help-help");
	String usage = InventorySort.getInstance().getConfig().getConfigurationSection("help-options").getString("help-usage");
	Player player;
	
	protected CommandHelp(String name) {
        super(name);
        this.description = help;
        this.usageMessage = usage;
        this.setPermission("inventorysort.help");
        this.setAliases(new ArrayList<String>());
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (exe != null) {
			if (sender instanceof Player) {
		    	player = (Player) sender;
		    	if (args.length == 0) {
					if (player.hasPermission("inventorysort.help")) {
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

    private CommandExecutor exe = null;
    
	public void setExecutor(CommandExecutor exe) {
        this.exe = exe;		
	}
		
}
