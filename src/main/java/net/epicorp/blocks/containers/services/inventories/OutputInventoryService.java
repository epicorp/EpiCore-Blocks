package net.epicorp.blocks.containers.services.inventories;

import net.epicorp.blocks.containers.services.Service;
import org.bukkit.inventory.Inventory;

/**
 * an inventory service that items shouldn't be inserted to externally
 */
public interface OutputInventoryService extends Service {
	Inventory inventory();
}
