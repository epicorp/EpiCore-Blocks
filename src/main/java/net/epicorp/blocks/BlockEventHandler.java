package net.epicorp.blocks;

import org.bukkit.event.EventPriority;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BlockEventHandler {
	/**
	 * Highest is executed last, monitor should not modify the event, and lowest is executed first
	 * @return the priority of the event
	 */
	EventPriority priority() default EventPriority.NORMAL;

	/**
	 * if the event should still listen to cancelled events
	 * @return
	 */
	boolean ignoreCancelled() default false;
}
