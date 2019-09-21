package net.epicorp.blocks.manager;

import net.epicorp.blocks.ICustomBlock;

public interface IBlockIterator {
	void consume(ICustomBlock block);
}
