package net.epicorp.blocks;

import net.epicorp.blocks.events.CustomBlockEvent;
import net.epicorp.blocks.events.Listening;
import net.epicorp.persistance.Persistent;
import org.bukkit.Location;
import java.lang.reflect.Method;
import java.util.*;

public abstract class CustomBlock implements Persistent, ICustomBlock {
	private Location location;
	private static Map<Class<?>, Map<CustomBlockEvent, List<Runnable>>> methods = new HashMap<>();

	public CustomBlock() {
		if(!methods.containsKey(getClass())) {
			Class<?> _class = getClass();
			Method[] methods = _class.getMethods();
			this.methods.put(getClass(), new HashMap<>());
			for (Method method : methods) {
				Listening listening = method.getAnnotation(Listening.class);
				if (listening != null) {
					this.methods.get(getClass()).computeIfAbsent(listening.event(), (k) -> new ArrayList<>()).add(() -> {
						try {
							method.invoke(this);
						} catch (ReflectiveOperationException e) {
							throw new RuntimeException(e);
						}
					});
				}
			}
		}
	}


	public void update() {
		methods.get(getClass()).getOrDefault(CustomBlockEvent.ON_UPDATE, (List<Runnable>)Collections.EMPTY_LIST).forEach(Runnable::run);
	}

	public void onDestroy() {
		methods.get(getClass()).getOrDefault(CustomBlockEvent.ON_DESTROY, (List<Runnable>)Collections.EMPTY_LIST).forEach(Runnable::run);
	}

	public void onPlace() {
		methods.get(getClass()).getOrDefault(CustomBlockEvent.ON_PLACE, (List<Runnable>)Collections.EMPTY_LIST).forEach(Runnable::run);
	}

	public Location getLocation() {
		return location.clone(); // trust me, this is for the best
	}
	public void setLocation(Location location) {
		this.location = location;
	}
}
