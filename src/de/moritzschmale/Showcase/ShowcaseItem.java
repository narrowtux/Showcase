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
	private ShowcaseType type = ShowcaseType.BASIC;
	private int itemAmount = 1;
	private double pricePerItem = 0;
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
		vec.add(new Vector(0.5,0.1,0.5));
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
		if(type.equals(ShowcaseType.FINITE_SHOP)){
			ShowcasePlayer player = ShowcasePlayer.getPlayer(this.player);
			ItemStack stack = item.getItemStack().clone();
			stack.setAmount(itemAmount);
			player.getPlayer().getInventory().addItem(stack);
		}
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
	/**
	 * @param type the type to set
	 */
	public void setType(ShowcaseType type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public ShowcaseType getType() {
		return type;
	}
	/**
	 * @param itemAmount the itemAmount to set
	 */
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}
	/**
	 * @return the itemAmount
	 */
	public int getItemAmount() {
		return itemAmount;
	}
	/**
	 * @param pricePerItem the pricePerItem to set
	 */
	public void setPricePerItem(double pricePerItem) {
		this.pricePerItem = pricePerItem;
	}
	/**
	 * @return the pricePerItem
	 */
	public double getPricePerItem() {
		return pricePerItem;
	}
}
