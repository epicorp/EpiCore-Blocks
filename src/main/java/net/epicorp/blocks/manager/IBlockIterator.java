package net.epicorp.blocks.manager;

import net.epicorp.blocks.ICustomBlock;

/**
 * this is effectively a consumer for custom blocks, and is meant to consume all the blocks in a block database in a blockmanager,
 * the block iterator should be invoked <b>before</b> {@link ICustomBlock#update()} is called
 */
public interface IBlockIterator {
	void consume(ICustomBlock block);
}
