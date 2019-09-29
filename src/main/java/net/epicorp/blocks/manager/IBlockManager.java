package net.epicorp.blocks.manager;

import net.epicorp.blocks.ICustomBlock;
import net.epicorp.persistance.Persistent;
import net.epicorp.persistance.registry.IPersistenceRegistry;
import org.bukkit.Location;
import org.bukkit.event.Listener;

/**
 * a manager that manages the ticking, placing and breaking of custom blocks
 */
public interface IBlockManager extends Listener {

	/**
	 * place a new block at the given location
	 * <p>
	 * for implementation make sure to call {@link ICustomBlock#setLocation(Location)} and {@link ICustomBlock#onPlace()}
	 *
	 * @param instance
	 */
	<T extends ICustomBlock & Persistent> void placeBlock(Location location, T instance);

	/**
	 * get the persistence registry backing this manager
	 *
	 * @return
	 */
	IPersistenceRegistry getRegistry();

	/**
	 * get the block at the location
	 *
	 * @param location
	 * @return the instance of the block or null
	 */
	ICustomBlock getBlock(Location location);

	/**
	 * removes the block at the given location and returns the instance
	 *
	 * @param location the location the block is at
	 * @return the old instance of the block
	 */
	default ICustomBlock removeBlock(Location location) {
		ICustomBlock customBlock = deleteBlock(location);
		if (customBlock != null) customBlock.onDestroy();
		return customBlock;
	}

	/**
	 * deletes the block without calling {@link ICustomBlock#onDestroy()}
	 *
	 * @param location
	 * @return
	 */
	ICustomBlock deleteBlock(Location location);

	/**
	 * place a new block of the specified class
	 *
	 * @param _class
	 * @param <T>
	 * @return the newly placed block
	 */
	default <T extends ICustomBlock & Persistent> T placeBlock(Location location, Class<T> _class) {
		T customBlock = getRegistry().newInstance(_class);
		placeBlock(location, customBlock);
		return customBlock;
	}

	/**
	 * place a new custom block with the specified id
	 *
	 * @param id
	 * @return the newly placed block
	 */
	default <T extends ICustomBlock & Persistent> T placeBlock(Location location, int id) {
		T customBlock = (T) getRegistry().newInstance(id);
		placeBlock(location, customBlock);
		return customBlock;
	}

	/**
	 * register a new block iterator
	 * {@link IBlockIterator#consume(ICustomBlock)} is called for every block on block iteration
	 *
	 * @param iterator
	 */
	void registerBlockIterator(IBlockIterator iterator);

	/**
	 * unregisters a block iterator
	 *
	 * @param iterator
	 */
	void removeBlockIterator(IBlockIterator iterator);
}
