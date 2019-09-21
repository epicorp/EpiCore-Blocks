package net.epicorp.blocks;

import net.epicorp.persistance.database.IBlockDatabase;
import net.epicorp.persistance.objects.InstanceListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import java.util.function.Function;

public abstract class AbstractCustomBlock extends CustomBlock implements Listener {

	public AbstractCustomBlock() {
		this(null, (Plugin) null, null);
	}

	public AbstractCustomBlock(Plugin plugin, Function<Event, Block> eventBlockTransformer, IBlockDatabase database) {
		this(e -> eventBlockTransformer.apply(e).getLocation(), plugin, database);
	}

	public AbstractCustomBlock(Function<Event, Location> eventLocationTransformer, Plugin plugin, IBlockDatabase database) {
		if (plugin != null && database != null && eventLocationTransformer != null) // if no transformers, or no database / plugin no custom listeners can be made
			InstanceListener.register(this, plugin, BlockEventHandler.class, eventLocationTransformer, l -> database.getData(l) == this, BlockEventHandler::ignoreCancelled, BlockEventHandler::priority);
	}

	@Override
	public void close() {
		HandlerList.unregisterAll(this);
	}
}
