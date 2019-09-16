package net.epicorp.blocks.containers;

import net.epicorp.blocks.CustomBlock;
import net.epicorp.blocks.containers.services.Service;
import org.bukkit.block.BlockFace;

public interface FunctionalBlock extends CustomBlock {
	/**
	 * get the service of the block from a face, for example, a furnace could be represented as having an input {@link net.epicorp.blocks.containers.services.InventoryService}
	 * from the top, a fuel input
	 * @param face the side that is being accessed
	 * @return the service
	 */
	Service getService(BlockFace face);
}