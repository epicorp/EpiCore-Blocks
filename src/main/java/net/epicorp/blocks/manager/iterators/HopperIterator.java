package net.epicorp.blocks.manager.iterators;

import net.epicorp.blocks.ICustomBlock;
import net.epicorp.blocks.containers.FunctionalBlock;
import net.epicorp.blocks.containers.services.Service;
import net.epicorp.blocks.containers.services.inventories.FilteredInventoryService;
import net.epicorp.blocks.containers.services.inventories.InputInventoryService;
import net.epicorp.blocks.containers.services.inventories.OutputInventoryService;
import net.epicorp.blocks.manager.IBlockIterator;
import net.epicorp.utilities.inventories.Inventories;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.function.Consumer;

public class HopperIterator implements IBlockIterator {
	@Override
	public void consume(ICustomBlock cblock) {
		if(cblock instanceof FunctionalBlock) {
			FunctionalBlock block = (FunctionalBlock) cblock;

			// hopper take
			Service service = block.getService(BlockFace.DOWN);
			if(service instanceof OutputInventoryService) { // check if output inventory
				Location location = cblock.getLocation();
				location.add(0, -1, 0);
				Block bhopper = location.getBlock();
				BlockState state = bhopper.getState(); // get hopper underneath
				if(state instanceof Hopper) { // check if hopper
					Hopper hopper = (Hopper) state;
					Inventories.mergeOne(((OutputInventoryService) service).inventory(), hopper.getInventory()); // take 1 item from output and put in inventory
				}
			}

			// hopper insert
			Consumer<BlockFace> hopperSides = f -> { // for input sides
				Service side = block.getService(f);
				if(side instanceof InputInventoryService) {
					Location location = cblock.getLocation();
					location.add(f.getModX(), f.getModY(), f.getModZ());

					Block bhopper = location.getBlock();
					BlockState state = bhopper.getState(); // get hopper on the side
					if (state instanceof Hopper) { // check if hopper
						Hopper hopper = (Hopper) state;
						org.bukkit.material.Hopper hopperData = (org.bukkit.material.Hopper) hopper.getData();
						if(hopperData.getFacing() == f.getOppositeFace()) {// if hopper is pointing into block
							Inventory inventory = hopper.getInventory();
							ItemStack stack = Inventories.getFirst(inventory);
							if(!(side instanceof FilteredInventoryService) || ((FilteredInventoryService) side).valid(stack)) // if filtered, then check filter
								Inventories.mergeOne(hopper.getInventory(), ((InputInventoryService) side).inventory()); // take 1 item from output and put in inventory
						}
					}
				}
			};

			hopperSides.accept(BlockFace.EAST);
			hopperSides.accept(BlockFace.WEST);
			hopperSides.accept(BlockFace.NORTH);
			hopperSides.accept(BlockFace.SOUTH);
			hopperSides.accept(BlockFace.UP);
		}
	}
}
