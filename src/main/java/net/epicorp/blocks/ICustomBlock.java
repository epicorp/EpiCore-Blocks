package net.epicorp.blocks;

import org.bukkit.Location;

/**
 * a block that can be stored in a block manager
 * @see CustomBlock
 */
public interface ICustomBlock {
	/**
	 * this method is invoked every "cycle" and is when you should update internal data
	 */
	void update();

	/**
	 * this method is called <b>after</b> the block is destroyed
	 */
	void onDestroy();

	/**
	 * this method is invoked after the block has been placed and initialized with its location
	 */
	void onPlace();

	/**
	 * get the location in which the block resides
	 * @return the bukkit location
	 */
	Location getLocation();

	/**
	 * don't call this, unless you
	 * 1) know what you are doing
	 * 2) are placing a block for the first time
	 * @param location
	 */
	@Deprecated
	void setLocation(Location location);
}
