package net.epicorp.blocks;

import net.epicorp.persistance.database.IBlockDatabase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractCustomBlock implements CustomBlock, Listener {

	public AbstractCustomBlock() {
		this(null, (Plugin) null,null);
	}

	public AbstractCustomBlock(Plugin plugin, Map<Class<? extends Event>, Function<Event, Block>> eventBlockTransformer, IBlockDatabase database) {
		this(eventBlockTransformer.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (a) -> e.getValue().apply(a).getLocation())), plugin, database);
	}

	public AbstractCustomBlock(Map<Class<? extends Event>, Function<Event, Location>> eventLocationTransformer, Plugin plugin, IBlockDatabase database) {
		if (eventLocationTransformer != null || eventLocationTransformer.size() > 0 || plugin == null || database == null) { // if no transformers, or no database / plugin no custom listeners can be made
			Class<? extends AbstractCustomBlock> thisClass = getClass(); // get the current class
			for (Method method : thisClass.getMethods()) { // get all public methods including inherited ones
				BlockEventHandler eventHandler = method.getDeclaredAnnotation(BlockEventHandler.class); // find custom event handler
				Class<?>[] paramTypes = method.getParameterTypes();
				if (eventHandler != null && paramTypes.length > 0) { // filter unannotated methods
					Class<?> eventClass = paramTypes[0];
					boolean includeLocation = paramTypes.length == 2; // if the method also gets the location when listening to the event, this is just for convienince
					if (Event.class.isAssignableFrom(eventClass)) { // make sure the method is listening to an event
						EventExecutor executor;
						if (includeLocation) { // if the location is included in method invoc, make it seperate
							executor = (t, e) -> {
								try {
									Location transform = getLocation(eventLocationTransformer, e); // convert the event to location
									if(database.getData(transform) == this) // must make sure it's the current instance
										method.invoke(t, e, transform); // invoke the listener
								} catch (ReflectiveOperationException e1) {
									throw new RuntimeException(e1);
								}
							};
						} else {
							executor = (t, e) -> {
								try {
									Location transform = getLocation(eventLocationTransformer, e);  // convert the event to location
									if(database.getData(transform) == this) // must make sure it's the current instance
										method.invoke(t, e); // invoke the listener
								} catch (ReflectiveOperationException e1) {
									throw new RuntimeException(e1);
								}
							};
						}
						plugin.getServer().getPluginManager().registerEvent((Class<? extends Event>) eventClass, this, eventHandler.priority(), executor, plugin, eventHandler.ignoreCancelled());
						// register the listener after the event is called
					} else
						throw new IllegalArgumentException(method + " is not listening to an event but is annotated with BlockEventHandler");
				}
			}
		}
	}

	@Override
	public void close() {
		HandlerList.unregisterAll(this);
	}

	protected Location getLocation(Map<Class<? extends Event>, Function<Event, Location>> trans, Event event) {
		Class<? extends Event> eventClass = event.getClass();
		if (trans.containsKey(eventClass)) {
			return trans.get(eventClass).apply(event);
		} else throw new IllegalArgumentException("Unable to getLocation " + eventClass);
	}
}
