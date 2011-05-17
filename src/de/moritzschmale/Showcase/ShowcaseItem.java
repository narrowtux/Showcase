package de.moritzschmale.Showcase;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShowcaseItem {
	private Item item;
	private Location location;
	private Block block;
	private String player;
	private boolean updatedPosition = false;
	public ShowcaseItem(Item item, Location location, String player){
		setItem(item);
		setLocation(location);
		setBlock(location.getBlock());
		setPlayer(player);
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		Vector vec = location.toVector();
		vec.add(new Vector(0.5,0,0.5));
		location = vec.toLocation(location.getWorld());
		this.location = location;
		item.teleport(location);
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	public void remove(){
		item.remove();
	}
	public void respawn() {
		ItemStack stack = item.getItemStack().clone();
		item = item.getLocation().getWorld().dropItemNaturally(location, stack);
		updatedPosition = false;
	}
	public void updatePosition() {
		if(!updatedPosition){
			item.teleport(location);
			item.setVelocity(new Vector(0,0,0));
			updatedPosition=true;
		}
	}
	/**
	 * @param block the block to set
	 */
	public void setBlock(Block block) {
		this.block = block;
	}
	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(String player) {
		this.player = player;
	}
	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}
}
