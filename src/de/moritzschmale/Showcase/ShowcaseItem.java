package de.moritzschmale.Showcase;

import org.bukkit.Location;
import org.bukkit.Material;
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
	private Material material;
	private short data;
	private boolean chunkLoaded = true;
	public ShowcaseItem(Item item, Location location, String player){
		setItem(item);
		setLocation(location);
		setBlock(location.getBlock());
		setPlayer(player);
	}
	
	public ShowcaseItem(Location loc, Material mat, short data, String player, ShowcaseType type, int amount, double price){
		setItem(loc.getWorld().dropItemNaturally(loc, new ItemStack(mat, 1, data)));
		setLocation(loc);
		setMaterial(mat);
		setData(data);
		setPlayer(player);
		setType(type);
		setItemAmount(amount);
		setPricePerItem(price);
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
		updatedPosition = false;
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
		location = location.getBlock().getLocation(); //simply clear everything after the comma.
		Vector vec = location.toVector();
		vec.add(new Vector(0.5,0.2,0.5));
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
	
	public void giveItemsBack(){
		if(type.equals(ShowcaseType.FINITE_SHOP)){
			ShowcasePlayer player = ShowcasePlayer.getPlayer(this.player);
			ItemStack stack = item.getItemStack().clone();
			stack.setAmount(itemAmount);
			player.getPlayer().getInventory().addItem(stack);
		}
	}
	
	public void respawn() {
		item.remove();
		ItemStack stack = new ItemStack(getMaterial(), 1, getData());
		item = item.getLocation().getWorld().dropItemNaturally(location, stack);
		updatedPosition = false;
	}
	
	public void updatePosition() {
		if(!updatedPosition||item.getLocation().getY()>=block.getLocation().getBlockY()+0.5){
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
		if(itemAmount==0){
			remove();
			ShowcaseMain.instance.showcasedItems.remove(this);
		}
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

	/**
	 * @param material the material to set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(short data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public short getData() {
		return data;
	}

	/**
	 * @param chunkLoaded the chunkLoaded to set
	 */
	public void setChunkLoaded(boolean chunkLoaded) {
		this.chunkLoaded = chunkLoaded;
	}

	/**
	 * @return the chunkLoaded
	 */
	public boolean isChunkLoaded() {
		return chunkLoaded;
	}
}
