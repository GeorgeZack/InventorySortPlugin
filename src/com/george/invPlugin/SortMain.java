package com.george.invPlugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public final class SortMain {
	
//	private static int itemID(ItemStack item) {
//		return item.getType();
//	}
	
	private static String itemString(ItemStack item) {
		String name = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString();
		return name;
	}
	
//	public static ArrayList<ItemStack> SortArrayListID(ArrayList<ItemStack> itemlist) {
//		ArrayList<ItemStack> startItemList = new ArrayList<ItemStack>(itemlist);
//		ArrayList<ItemStack> sortedItemlist = SortAscending(itemlist);
//		if (equalLists(startItemList, sortedItemlist)) {
//			sortedItemlist = SortDescending(itemlist);
//		}
//		return sortedItemlist;
//	}

	public static ArrayList<ItemStack> SortArrayListString(ArrayList<ItemStack> itemlist) {
		ArrayList<ItemStack> startItemList = new ArrayList<ItemStack>(itemlist);
		ArrayList<ItemStack> sortedItemlist = SortAlphabetUp(itemlist);
		if (equalLists(startItemList, sortedItemlist)) {
			sortedItemlist = SortAlphabetDown(itemlist);
		}
		return sortedItemlist;
	}
	
//	public static ArrayList<ItemStack> SortAscending(ArrayList<ItemStack> itemlist) {
//		return BubbleSortInteger(itemlist, true);
//	}
	
//	public static ArrayList<ItemStack> SortDescending(ArrayList<ItemStack> itemlist) {
//		return BubbleSortInteger(itemlist, false);
//	}
	
	public static ArrayList<ItemStack> SortAlphabetUp(ArrayList<ItemStack> itemlist) {
		return BubbleSortString(itemlist, true);
	}
	
	public static ArrayList<ItemStack> SortAlphabetDown(ArrayList<ItemStack> itemlist) {
		return BubbleSortString(itemlist, false);
	}
	
//	private static ArrayList<ItemStack> BubbleSortInteger (ArrayList<ItemStack> itemlist, Boolean bAscending) {
//		ArrayList<ItemStack> sortedItemlist = itemlist;
//		for (int i = 0; i < sortedItemlist.size() - 1; i++) {			
//			for (int j = 1; j < sortedItemlist.size() - i; j++) {
//				if (bAscending) {
//					if (compareInt(sortedItemlist.get(j-1), sortedItemlist.get(j)) < 0) {
//						ItemStack temp = itemlist.get(j-1);
//						sortedItemlist.set(j-1, sortedItemlist.get(j));
//						sortedItemlist.set(j, temp);
//					}
//				} else {
//					if (compareInt(sortedItemlist.get(j-1), sortedItemlist.get(j)) > 0) {
//						ItemStack temp = itemlist.get(j-1);
//						sortedItemlist.set(j-1, sortedItemlist.get(j));
//						sortedItemlist.set(j, temp);
//					}
//				}
//			}
//		}
//		return sortedItemlist; 
//	}

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
	
//	private static int compareInt(ItemStack o1, ItemStack o2) {
//		return Integer.compare(itemID(o1),itemID(o2));
//	}

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
