package com.george.invPlugin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandSortType extends BukkitCommand {

	String help = InventorySort.getInstance().getConfig().getConfigurationSection("type-options").getString("type-help");
	String usage = InventorySort.getInstance().getConfig().getConfigurationSection("type-options").getString("type-usage");
	Player player;
	
	protected CommandSortType(String name) {
        super(name);
        this.description = help;
        this.usageMessage = usage;
        this.setPermission("inventorysort.type");
        this.setAliases(new ArrayList<String>());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (exe != null) {
			if (sender instanceof Player) {
		    	player = (Player) sender;
		    	if (args.length == 0) {
					if (player.hasPermission("inventorysort.type")) {
						if (player.hasMetadata("type-name")) {
							player.removeMetadata("type-name", InventorySort.getInstance());
				            player.sendMessage(ChatColor.translateAlternateColorCodes('&', typeMessageID()));
						} else if (!player.hasMetadata("type-name")) {
							player.setMetadata("type-name", new FixedMetadataValue(InventorySort.getInstance(), 1));
				            player.sendMessage(ChatColor.translateAlternateColorCodes('&', typeMessageName()));
						}
					} else {   
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', typeNoPermission()));
					}
		    	}
			}
			return true;
		}
		return false;
	}

	private String typeMessageID() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("sort-type-message-int");
		return message;
	}

	private String typeMessageName() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("sort-type-message-string");
		return message;
	}
	
	private String typeNoPermission() {
		String message = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("type-lackof-permission");
		return message;
	}

    private CommandExecutor exe = null;
    
	public void setExecutor(CommandExecutor exe) {
        this.exe = exe;		
	}
		
}
