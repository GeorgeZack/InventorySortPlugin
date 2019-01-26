package com.george.invPlugin;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener {

	String sortCommand = InventorySort.getInstance().getConfig().getConfigurationSection("sort-options").getString("sort-command");
	String toggleMessageOff = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("toggle-message-off");
	Boolean toggleMessageBoolean = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getBoolean("toggle-message-send");
	
	@SuppressWarnings("deprecation")
	// Even though updateInventory() is indicated as deprecated, that is not true according to the Spigot Documentation - 1/26/19
	@EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
    	Player player = e.getPlayer();
		Block block = player.getTargetBlock((Set<Material>)null, 5);
    	Action action = e.getAction();
    	if (player.hasMetadata("commandToggle") && (block.getType().equals(Material.CHEST) || (block.getType().equals(Material.TRAPPED_CHEST))) && (action == Action.LEFT_CLICK_BLOCK)) {
    		player.performCommand(sortCommand + " internalChestSort");
    		player.updateInventory();
    	} else if (!player.hasMetadata("commandToggle") && (block.getType().equals(Material.CHEST) || (block.getType().equals(Material.TRAPPED_CHEST))) && (action == Action.LEFT_CLICK_BLOCK) && toggleMessageBoolean) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', toggleMessageOff));
    	}
    }
    
	@SuppressWarnings("deprecation")
	// Even though updateInventory() is indicated as deprecated, that is not true according to the Spigot Documentation - 1/26/19
	@EventHandler
    private void onPlayerClick(InventoryClickEvent e) {
    	Player player = (Player) e.getWhoClicked();
    	ClickType clicktype = e.getClick();
    	if (clicktype == ClickType.MIDDLE) {
    		if (player.getOpenInventory().getType() == InventoryType.CHEST) {
	    		player.performCommand(sortCommand);  
	    		player.updateInventory();
    		} else {  	
//	    		player.sendMessage("internalPlayerSort");
	    		player.performCommand(sortCommand + " internalPlayerSort");
	    		player.updateInventory();		
    		}
    	}
    }
}