package net.epicorp.blocks.containers.services.inventories;

import net.epicorp.blocks.containers.services.Service;
import org.bukkit.inventory.Inventory;

/**
 * an inventory that represents the input of a machine, avoid taking items out of these
 */
public interface InputInventoryService extends Service {
	Inventory inventory();
}
