package net.epicorp.blocks.blocks;

import net.epicorp.blocks.ICustomBlock;
import net.epicorp.blocks.events.CustomBlockEvent;
import net.epicorp.blocks.events.Listening;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * a custom block that explodes all the items in the inventory out at once
 */
public interface IExplodeInventory extends ICustomBlock {
	@Listening(event = CustomBlockEvent.ON_DESTROY)
	default void destroy() {
		Location location = this.getLocation().add(.5, .5, .5);
		World world = location.getWorld();
		this.inventories().stream().map(Inventory::getContents).flatMap(Arrays::stream).filter(Objects::nonNull).forEach(i -> world.dropItemNaturally(location, i));
	}

	List<Inventory> inventories();
}
