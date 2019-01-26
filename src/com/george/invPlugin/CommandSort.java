package com.george.invPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

@SuppressWarnings("deprecation")
public class CommandSort extends BukkitCommand {
	
	String help = InventorySort.getInstance().getConfig().getConfigurationSection("sort-options").getString("sort-help");
	String usage = InventorySort.getInstance().getConfig().getConfigurationSection("sort-options").getString("sort-usage");
	int cooldownTime = InventorySort.getInstance().getConfig().getConfigurationSection("cooldown").getInt("cooldown-time");
	String cooldownHot = InventorySort.getInstance().getConfig().getConfigurationSection("cooldown").getString("cooldown-hot-message");
	String invSortMessage = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("inv-message");
	String chestSortMessage = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("chest-message");
	String secureMessageOwned = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getString("secure-message-owned");
	String invNoPermission = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("player-lackof-permission");
	String chestNoPermission = InventorySort.getInstance().getConfig().getConfigurationSection("permissions").getString("chest-lackof-permission");
	String monsterEggError = InventorySort.getInstance().getConfig().getConfigurationSection("debug").getString("monster-egg-error");
	Boolean invSortMessageBoolean = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getBoolean("inv-message-send");
	Boolean chestSortMessageBoolean = InventorySort.getInstance().getConfig().getConfigurationSection("messages").getBoolean("chest-message-send");
    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
//	final int INT_TOGGLE = 0;
//	final int INT_ASCENDING = 1;
//	final int INT_DESCENDING = 2;
	final int STRING_TOGGLE = 3;
	final int STRING_UP = 4;
	final int STRING_DOWN = 5;
	final int INV_PLAYER = 0;
	final int INV_CHEST = 1;
	final int INV_DOUBLE_CHEST = 2;
	File dataF;
	FileConfiguration data;
	Player player;
	Inventory inventory;
	
	protected CommandSort(String name) {
        super(name);
        this.description = help;
        this.usageMessage = usage;
        this.setPermission("inventorysort.sort");
        this.setAliases(new ArrayList<String>());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (exe != null) {
			if (sender instanceof Player) {
	        	player = (Player) sender;
		    	String playerId = player.getUniqueId().toString();
	    		if (cooldowns.containsKey(player.getName())) {
	    			long secondsLeft = ((cooldowns.get(player.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
	    			if (secondsLeft > 0) {
	    				player.sendMessage(ChatColor.translateAlternateColorCodes('&', cooldownHot));
	    				return true;
	    			}
	    		}
		        cooldowns.put(player.getName(), System.currentTimeMillis());
				if (player.hasPermission("inventorysort.sort")) {
//					player.sendMessage(args);
		            if (Arrays.asList(args).contains("internalChestSort")) {
			            int invType = getInvType("internalChestSort");
			    		switch (invType) {
							case 1:
							case 2:
//								player.sendMessage("Chest " + Integer.toString(invType));
								if (player.hasPermission("inventorysort.sortchest")){
							        dataF = new File(InventorySort.getInstance().getDataFolder(), "data.yml");
							        data = new YamlConfiguration();
							        try {
										data.load(dataF);
							        } catch (IOException e) {
										e.printStackTrace();
									} catch (InvalidConfigurationException e) {
										e.printStackTrace();
									}
									Set<String> inData = data.getKeys(true);
									String thisChest = getLocation(getCurrentChest().getLocation());
									if (inData.contains(thisChest)) {
										if (data.get(thisChest).equals(playerId)) {
											sortFromType(invType, args);
							            	break;
										} else {
							            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureMessageOwned));
											break;
										}
									} else {
										sortFromType(invType, args);
									}
									break;
								} else if (!player.hasPermission("inventorysort.sortchest")){
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', chestNoPermission));
									break;
								}
			    		}
						return true;
		            } else if (Arrays.asList(args).contains("internalPlayerSort")) {
//						player.sendMessage("Player");
						sortFromType(0, args);
		            } else {
			            int invType = getInvType(null);
			    		switch (invType) {
							case 1:
							case 2:
//								player.sendMessage("InvTypeNull " + Integer.toString(invType));
								if (player.hasPermission("inventorysort.sortchest")){
							        dataF = new File(InventorySort.getInstance().getDataFolder(), "data.yml");
							        data = new YamlConfiguration();
							        try {
										data.load(dataF);
							        } catch (IOException e) {
										e.printStackTrace();
									} catch (InvalidConfigurationException e) {
										e.printStackTrace();
									}
									Set<String> inData = data.getKeys(true);
									String thisChest = getLocation(getCurrentChest().getLocation());
									if (inData.contains(thisChest)) {
										if (data.get(thisChest).equals(playerId)) {
											sortFromType(invType, args);
							            	break;
										} else {
							            	player.sendMessage(ChatColor.translateAlternateColorCodes('&', secureMessageOwned));
											break;
										}
									} else {
										sortFromType(invType, args);
									}
									break;
								} else if (!player.hasPermission("inventorysort.sortchest")){
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', chestNoPermission));
									break;
								}
							default:
								sortFromType(invType, args);
								break;
			    		}
						return true;
		            }
				} else if (!player.hasPermission("inventorysort.sort")){
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', invNoPermission));
				}
			}
			return true;
		}
		return false;
	}

	private void combineStacks(Inventory inventory) {
		ItemStack[] items = inventory.getContents();
        int len = inventory.getSize();
        int affected = 0;
        for (int i = 0; i < len; i++) {
            ItemStack item = items[i];
            if (item == null || item.getAmount() <= 0) {
                continue;
            }
        	int max = item.getMaxStackSize();
        	if (item.getAmount() < max) {
            	int needed = max - item.getAmount();
                for (int j = i + 1; j < inventory.getSize(); j++) {
                    ItemStack item2 = items[j];
                    if (item2 == null || item2.getAmount() <= 0 || (item.getMaxStackSize() == 1)) {
                        continue;
                    }
                    if (item2.getType() == item.getType() && item2.getData().getData() == item.getData().getData() && !item2.hasItemMeta() && !item.hasItemMeta()) {
                    	if (item2.getData().getItemType() == Material.TIPPED_ARROW && item.getData().getItemType() == Material.TIPPED_ARROW) {
	            			PotionMeta meta2 = (PotionMeta) item2.getItemMeta();
	            			PotionMeta meta = (PotionMeta) item.getItemMeta();
	            			if (meta2.equals(meta)) {
	                            if (item2.getAmount() > needed) {
	                                item.setAmount(max);
	                                item2.setAmount(item2.getAmount() - needed);
	                                break;
	                            } else {
	                                items[j] = null;
	                                item.setAmount(item.getAmount() + item2.getAmount());
	                                needed = max - item.getAmount();
	                            }
	                    	}
	            			// LEGACY
//	            		} else if (item2.getType() == Material.MONSTER_EGG && item.getType() == Material.MONSTER_EGG) {
//							player.sendMessage(ChatColor.translateAlternateColorCodes('&', monsterEggError));
//	            			EntityType tag2 = ((SpawnEgg) item2.getItemMeta()).getSpawnedType();
//	            			EntityType tag = ((SpawnEgg) item.getItemMeta()).getSpawnedType();
//	            			if (tag2.equals(tag)) {
//	                            if (item2.getAmount() > needed) {
//	                                item.setAmount(max);
//	                                item2.setAmount(item2.getAmount() - needed);
//	                                break;
//	                            } else {
//	                                items[j] = null;
//	                                item.setAmount(item.getAmount() + item2.getAmount());
//	                                needed = max - item.getAmount();
//	                            }
//	            			}
                    	} else {
	                        if (item2.getAmount() > needed) {
	                            item.setAmount(max);
	                            item2.setAmount(item2.getAmount() - needed);
	                            break;
	                        } else {
                                items[j] = null;
                                item.setAmount(item.getAmount() + item2.getAmount());
                                needed = max - item.getAmount();
                    		}
                        }
                        affected ++;
                    }
        		}
        	}
            if (affected > 0) {
                inventory.setContents(items);
            }
        }
	}
	
	private void sortFromType(int invType, String[] args) {
		sortInv(invType, args);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1, 1);
        if (invSortMessageBoolean) {
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', invSortMessage));        	
        }
	}
	
	private void sortInv(int invType, String[] args) {
//		player.sendMessage("SORTINV INVTYPE "+Integer.toString(invType));
		inventory = getInventoryFromType(invType);
        combineStacks(inventory);
        ArrayList<ItemStack> sortedlist = getSortedList(getSortType(args), getUnsortedList(invType));
        addItemList(invType, sortedlist);
	}
	
	private Inventory getCurrentChest() {
		Block block = player.getTargetBlock((Set<Material>)null, 5);
		Chest chest = null;
    	chest = (Chest) block.getState();
    	return chest.getInventory();
	}
	
	public static String getLocation(Location l) {
	    StringBuilder sb = new StringBuilder();
	    sb.append( l.getWorld().getName() ).append( "~" );
	    sb.append( l.getBlockX() ).append( "~" );
	    sb.append( l.getBlockY() ).append( "~" );
	    sb.append( l.getBlockZ() );
		return sb.toString();
	}

	private int getInvType(String string) {
		Block block = player.getTargetBlock((Set<Material>)null, 5);
		if (string == "internalChestSort") {
//            player.sendMessage("internalChestSort");
    		Chest chest = (Chest) block.getState();
        	if (chest.getInventory().getSize() == 54) {
                return INV_DOUBLE_CHEST;
            }
            return INV_CHEST;
		} else if (block.getState() instanceof Chest) {
    		Chest chest = (Chest) block.getState();
        	if (chest.getInventory().getSize() == 54) {
                return INV_DOUBLE_CHEST;
            }
            return INV_CHEST;
        } else {
        	return INV_PLAYER;
        }
	}
	
	private Inventory getInventoryFromType(int type) {
//		player.sendMessage("GETINVFROMTYPE "+Integer.toString(type));
		switch (type) {
			case INV_PLAYER:
//				player.sendMessage("Player Inv");
				return player.getInventory();
			case INV_CHEST:
			case INV_DOUBLE_CHEST:
//				player.sendMessage("Chest Inv");
				return getCurrentChest();
			default:
				return player.getInventory();
		}
	}
	
	private int getSortType(String[] args) {
		if ((args == null || args.length <= 0) && (!player.hasMetadata("type-name"))) {
			return STRING_TOGGLE;
		}
		
//		if (!player.hasMetadata("type-name")) {
//			String cmd = args[0];
//			switch (cmd) {
//				case "d":
//				case "des":
//					return INT_DESCENDING;
//				case "a":
//				case "asc":
//					return INT_ASCENDING;
//				default:
//					return INT_TOGGLE;
//			}		
//		} else 
		if (player.hasMetadata("type-name")) {
			String cmd = args[0];
			switch (cmd) {
				case "xyz":
				case "down":
					return STRING_DOWN;
				case "abc":
				case "up":
					return STRING_UP;
				default:
					return STRING_TOGGLE;
			}
		} else {
			return STRING_TOGGLE;
		}
	}
	
	private ArrayList<ItemStack> getUnsortedList(int type) {
		ArrayList<ItemStack> unsortedItemlist = new ArrayList<ItemStack>();		
		int offset = 0;	
		int max = 0;
		
		switch (type) {
			case INV_PLAYER:
				offset = 9;
				max = 36;
				break;
			case INV_CHEST:
				offset = 0;
				max = 27;
				break;
			case INV_DOUBLE_CHEST:
				offset = 0;
				max = 54;
				break;	
			default:
				break;
		}
		
		for (int i = offset; i < max ; i ++) {
            ItemStack item = inventory.getItem(i);
        	if (item != null) {
        		unsortedItemlist.add(item);
                inventory.clear(i);
            }
        }
		
		return unsortedItemlist;
	}
	
	private ArrayList<ItemStack> getSortedList(int type, ArrayList<ItemStack> items) {
		if (items == null || items.size() <= 0) {
			return null;
		}
		
		switch (type) {
//			case INT_TOGGLE:
//				return SortMain.SortArrayListID(items);
//			case INT_ASCENDING:
//				return SortMain.SortAscending(items);
//			case INT_DESCENDING:
//				return SortMain.SortDescending(items);
			case STRING_TOGGLE:
				return SortMain.SortArrayListString(items);
			case STRING_UP:
				return SortMain.SortAlphabetUp(items);
			case STRING_DOWN:
				return SortMain.SortAlphabetDown(items);			
			default:
				return null;
		}	
	}
	
	private void addItemList(int type, ArrayList<ItemStack> items) {
		if (items == null || items.size() <= 0) {
			return;
		}
		
		int offset = 0;		
		switch (type) {
			case INV_PLAYER:
				offset = 9;
				break;
			case INV_CHEST:
				offset = 0;
				break;
			case INV_DOUBLE_CHEST:
				offset = 0;
				break;	
			default:
				break;
		}
		
        for (ItemStack item : items) {
        	inventory.setItem(offset, item);
        	offset++;
        }
	}

    private CommandExecutor exe = null;
    
	public void setExecutor(CommandExecutor exe) {
        this.exe = exe;		
	}
}
