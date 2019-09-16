package net.epicorp.blocks;

import org.bukkit.event.EventPriority;

public @interface BlockEventHandler {
	/**
	 * Highest is executed last, monitor should not modify the event, and lowest is executed first
	 * @return the priority of the event
	 */
	EventPriority priority();

	/**
	 * if the event should still listen to cancelled events
	 * @return
	 */
	boolean ignoreCancelled();
}
