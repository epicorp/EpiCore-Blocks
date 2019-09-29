package net.epicorp.blocks;

import net.epicorp.blocks.events.CustomBlockEvent;
import net.epicorp.blocks.events.Listening;
import net.epicorp.persistance.Persistent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public abstract class CustomBlock implements Persistent, ICustomBlock {
	private Location location;
	private static Map<Class<?>, Map<CustomBlockEvent, List<Consumer<CustomBlock>>>> methods = new HashMap<>();

	public CustomBlock() {
		if(!methods.containsKey(getClass())) {
			Class<?> _class = getClass();
			Method[] methods = _class.getMethods();
			this.methods.put(getClass(), new HashMap<>());
			for (Method method : methods) {
				Listening listening = method.getAnnotation(Listening.class);
				if (listening != null) {
					this.methods.get(getClass()).computeIfAbsent(listening.event(), (k) -> new ArrayList<>()).add(c -> {
						try {
							method.invoke(c);
						} catch (ReflectiveOperationException e) {
							throw new RuntimeException(e);
						}
					});
				}
			}
		}
	}


	public final void update() {
		exec(CustomBlockEvent.ON_UPDATE);
	}

	public final void onDestroy() {
		exec(CustomBlockEvent.ON_DESTROY);
	}

	public final void onPlace() {
		exec(CustomBlockEvent.ON_PLACE);
	}

	private void exec(CustomBlockEvent event) {
		List<Consumer<CustomBlock>> consumers = methods.get(getClass()).get(event);
		if(consumers != null)
			consumers.forEach(c -> c.accept(this));
	}

	public Location getLocation() {
		return location.clone(); // trust me, this is for the best
	}
	public void setLocation(Location location) {
		this.location = location;
	}

	public void deserLocation(DataInputStream dis) throws IOException {
		setLocation(new Location(Bukkit.getWorld(new UUID(dis.readLong(), dis.readLong())), dis.readInt(), dis.readInt(), dis.readInt()));
	}

	public void serLocation(DataOutputStream dos) throws IOException {
		Location location = getLocation();
		UUID uuid = location.getWorld().getUID();
		dos.writeLong(uuid.getMostSignificantBits());
		dos.writeLong(uuid.getLeastSignificantBits());
		dos.writeInt(location.getBlockX());
		dos.writeInt(location.getBlockY());
		dos.writeInt(location.getBlockZ());
	}
}
