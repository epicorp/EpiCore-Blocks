package net.epicorp.blocks;

import net.epicorp.persistance.Persistent;
import org.bukkit.Location;

public interface CustomBlock extends Persistent {
	void tick(Location location);
}
