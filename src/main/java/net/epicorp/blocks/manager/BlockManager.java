package net.epicorp.blocks.manager;

import net.epicorp.blocks.ICustomBlock;
import net.epicorp.blocks.manager.iterators.HopperIterator;
import net.epicorp.persistance.Persistent;
import net.epicorp.persistance.database.BlockDatabase;
import net.epicorp.persistance.database.IBlockDatabase;
import net.epicorp.persistance.registry.IPersistenceRegistry;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import java.util.Collections;
import java.util.Set;

public class BlockManager implements IBlockManager {
	protected final IBlockDatabase blockDatabase;
	protected final IPersistenceRegistry persistenceRegistry;
	protected final Set<IBlockIterator> onIteration;

	public BlockManager(IBlockDatabase blockDatabase, IPersistenceRegistry persistenceRegistry, Plugin plugin, Set<IBlockIterator> blockIterators) {
		onIteration = blockIterators;
		this.blockDatabase = blockDatabase;
		this.persistenceRegistry = persistenceRegistry;
		plugin.getServer().getScheduler().runTaskTimer(plugin, () -> blockDatabase.forEach((l, p) -> {
			if(p instanceof ICustomBlock) {
				onIteration.forEach(c -> c.consume((ICustomBlock) p));
				((ICustomBlock) p).update();
			}
		}), 0, 8);
	}

	public BlockManager(IBlockDatabase blockDatabase, IPersistenceRegistry persistenceRegistry, Plugin plugin) {
		this(blockDatabase, persistenceRegistry, plugin, Collections.singleton(new HopperIterator()));
	}

	public BlockManager(Plugin plugin, String name, IPersistenceRegistry persistenceRegistry) {
		this(new BlockDatabase(persistenceRegistry, name, plugin), persistenceRegistry, plugin);
	}

	@Override
	public <T extends ICustomBlock & Persistent> void placeBlock(Location location, T instance) {
		if(instance != null) {
			instance.setLocation(location);
			instance.onPlace();
			blockDatabase.setData(instance, location);
		}
	}

	@Override
	public IPersistenceRegistry getRegistry() {
		return persistenceRegistry;
	}

	@Override
	public ICustomBlock getBlock(Location location) {
		return blockDatabase.getData(location);
	}

	@Override
	public ICustomBlock deleteBlock(Location location) {
		return blockDatabase.removeData(location);
	}

	@Override
	public void registerBlockIterator(IBlockIterator iterator) {
		onIteration.add(iterator);
	}

	@Override
	public void removeBlockIterator(IBlockIterator iterator) {
		onIteration.remove(iterator);
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void destroy(BlockBreakEvent event) {
		ICustomBlock customBlock = removeBlock(event.getBlock().getLocation());
		if(customBlock != null)
			event.setDropItems(false);
	}
}
