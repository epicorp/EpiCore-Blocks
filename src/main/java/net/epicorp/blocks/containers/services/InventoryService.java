package net.epicorp.blocks.containers.services;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class InventoryService implements Service {
	protected Inventory inventory;

	public InventoryService(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getSize() {return inventory.getSize();}

	public int getMaxStackSize() {return inventory.getMaxStackSize();}

	public void setMaxStackSize(int size) {inventory.setMaxStackSize(size);}

	public String getName() {return inventory.getName();}

	public ItemStack getItem(int index) {return inventory.getItem(index);}

	public void setItem(int index, ItemStack item) {inventory.setItem(index, item);}

	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {return inventory.addItem(items);}

	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {return inventory.removeItem(items);}

	public ItemStack[] getContents() {return inventory.getContents();}

	public void setContents(ItemStack[] items) throws IllegalArgumentException {inventory.setContents(items);}

	public ItemStack[] getStorageContents() {return inventory.getStorageContents();}

	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {inventory.setStorageContents(items);}

	@Deprecated
	public boolean contains(int materialId) {return inventory.contains(materialId);}

	public boolean contains(Material material) throws IllegalArgumentException {return inventory.contains(material);}

	public boolean contains(ItemStack item) {return inventory.contains(item);}

	@Deprecated
	public boolean contains(int materialId, int amount) {return inventory.contains(materialId, amount);}

	public boolean contains(Material material, int amount) throws IllegalArgumentException {return inventory.contains(material, amount);}

	public boolean contains(ItemStack item, int amount) {return inventory.contains(item, amount);}

	public boolean containsAtLeast(ItemStack item, int amount) {return inventory.containsAtLeast(item, amount);}

	@Deprecated
	public HashMap<Integer, ? extends ItemStack> all(int materialId) {return inventory.all(materialId);}

	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {return inventory.all(material);}

	public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {return inventory.all(item);}

	@Deprecated
	public int first(int materialId) {return inventory.first(materialId);}

	public int first(Material material) throws IllegalArgumentException {return inventory.first(material);}

	public int first(ItemStack item) {return inventory.first(item);}

	public int firstEmpty() {return inventory.firstEmpty();}

	@Deprecated
	public void remove(int materialId) {inventory.remove(materialId);}

	public void remove(Material material) throws IllegalArgumentException {inventory.remove(material);}

	public void remove(ItemStack item) {inventory.remove(item);}

	public void clear(int index) {inventory.clear(index);}

	public void clear() {inventory.clear();}

	public List<HumanEntity> getViewers() {return inventory.getViewers();}

	public String getTitle() {return inventory.getTitle();}

	public InventoryType getType() {return inventory.getType();}

	public InventoryHolder getHolder() {return inventory.getHolder();}

	public ListIterator<ItemStack> iterator() {return inventory.iterator();}

	public ListIterator<ItemStack> iterator(int index) {return inventory.iterator(index);}

	public Location getLocation() {return inventory.getLocation();}

	public void forEach(Consumer<? super ItemStack> action) {inventory.forEach(action);}

	public Spliterator<ItemStack> spliterator() {return inventory.spliterator();}
}
