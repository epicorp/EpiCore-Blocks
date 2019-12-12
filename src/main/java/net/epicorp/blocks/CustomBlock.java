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
		if(!methods.containsKey(this.getClass())) {
			Class<?> _class = this.getClass();
			Method[] methods = _class.getMethods();
			CustomBlock.methods.put(this.getClass(), new HashMap<>());
			for (Method method : methods) {
				Listening listening = method.getAnnotation(Listening.class);
				if (listening != null) {
					CustomBlock.methods.get(this.getClass()).computeIfAbsent(listening.event(), (k) -> new ArrayList<>()).add(c -> {
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


	@Override
	public final void update() {
		this.exec(CustomBlockEvent.ON_UPDATE);
	}

	@Override
	public final void onDestroy() {
		this.exec(CustomBlockEvent.ON_DESTROY);
	}

	@Override
	public final void onPlace() {
		this.exec(CustomBlockEvent.ON_PLACE);
	}

	private void exec(CustomBlockEvent event) {
		List<Consumer<CustomBlock>> consumers = methods.get(this.getClass()).get(event);
		if(consumers != null)
			consumers.forEach(c -> c.accept(this));
	}

	@Override
	public Location getLocation() {
		return this.location.clone(); // trust me, this is for the best
	}
	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	public void deserLocation(DataInputStream dis) throws IOException {
		this.setLocation(new Location(Bukkit.getWorld(new UUID(dis.readLong(), dis.readLong())), dis.readInt(), dis.readInt(), dis.readInt()));
	}

	public void serLocation(DataOutputStream dos) throws IOException {
		Location location = this.getLocation();
		UUID uuid = location.getWorld().getUID();
		dos.writeLong(uuid.getMostSignificantBits());
		dos.writeLong(uuid.getLeastSignificantBits());
		dos.writeInt(location.getBlockX());
		dos.writeInt(location.getBlockY());
		dos.writeInt(location.getBlockZ());
	}
}
