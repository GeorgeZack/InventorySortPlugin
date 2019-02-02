package com.george.invPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public final class SortMain {
	
	private static String itemString(ItemStack item) {
		String name = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString();
		return name;
	}

	public static ArrayList<ItemStack> SortArrayListMaterial(ArrayList<ItemStack> itemlist) {
		ArrayList<ItemStack> startItemList = new ArrayList<ItemStack>(itemlist);
		ArrayList<ItemStack> sortedItemlist = SortMaterialUp(itemlist);
		
		if (equalLists(startItemList, sortedItemlist)) {
			sortedItemlist = SortMaterialDown(itemlist);
		}
		
		return sortedItemlist;
	}
	
	public static ArrayList<ItemStack> SortMaterialUp(ArrayList<ItemStack> itemlist) {
		return BubbleSortString(itemlist, true);
	}
	
	public static ArrayList<ItemStack> SortMaterialDown(ArrayList<ItemStack> itemlist) {
		return BubbleSortString(itemlist, false);
	}

	private static ArrayList<ItemStack> BubbleSortString (ArrayList<ItemStack> itemlist, Boolean bAscending) {
		ArrayList<ItemStack> sortedItemlist = itemlist;
		for (int i = 0; i < sortedItemlist.size() - 1; i++) {			
			for (int j = 1; j < sortedItemlist.size() - i; j++) {
				if (bAscending) {
					if (compareString(sortedItemlist.get(j-1), sortedItemlist.get(j)) < 0) {
						ItemStack temp = itemlist.get(j-1);
						sortedItemlist.set(j-1, sortedItemlist.get(j));
						sortedItemlist.set(j, temp);
					}
				} else {
					if (compareString(sortedItemlist.get(j-1), sortedItemlist.get(j)) > 0) {
						ItemStack temp = itemlist.get(j-1);
						sortedItemlist.set(j-1, sortedItemlist.get(j));
						sortedItemlist.set(j, temp);
					}
				}
			}
		}
		
		return sortedItemlist; 
	}

	private static int compareString(ItemStack o1, ItemStack o2) {
		int compare = itemString(o1).compareTo(itemString(o2));
		return compare;
	}
	
	private static boolean equalLists(ArrayList<ItemStack> one, ArrayList<ItemStack> two) {
	    if (one == null && two == null) {
	        return true;
	    }	
	    
	    if((one == null && two != null) || one != null && two == null || one.size() != two.size()){
	        return false;
	    }
	    
	    for (int i = 0; i < one.size() - 1; i++) {
	    	if (one.get(i).getType() != two.get(i).getType()) {
	    		return false;
	    	}
	    }
	    
	    return true;
	}
}
