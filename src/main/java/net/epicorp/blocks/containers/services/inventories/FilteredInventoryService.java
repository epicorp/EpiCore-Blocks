package net.epicorp.blocks.containers.services.inventories;

import org.bukkit.inventory.ItemStack;

/**
 * an inventory service that only accepts certain itemstacks
 */
public interface FilteredInventoryService extends InputInventoryService {
	boolean valid(ItemStack stack);
}
