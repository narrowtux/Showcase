package de.moritzschmale.Showcase;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;

public class ShowcasePlayer {
	private String player;
	private ShowcaseType requestedType = ShowcaseType.NONE;
	private Block requestedBlock = null;
	private int dialogState = 0;
	private ItemStack requestedItem = null;
	private ShowcaseItem lastClickedShowcase = null;
	private double requestedPrice = 0;
	private boolean hasReadPrice;
	private Location readPriceLocation;
	private static Map<String,ShowcasePlayer> instances = new HashMap<String, ShowcasePlayer>();
	private ShowcasePlayer(String player){
		this.player = player;
	}
	
	public static ShowcasePlayer getPlayer(String name){
		if(instances.containsKey(name)){
			return instances.get(name);
		} else {
			ShowcasePlayer player = new ShowcasePlayer(name);
			instances.put(name, player);
			return player;
		}
	}
	
	public static ShowcasePlayer getPlayer(Player player){
		return getPlayer(player.getName());
	}
	
	public Player getPlayer(){
		return ShowcaseMain.instance.getServer().getPlayer(player);
	}
	
	public boolean hasPermission(String node, boolean adminMethod){
		return ShowcaseMain.hasPermission(getPlayer(), node, adminMethod);
	}

	/**
	 * @param requestedType the requestedType to set
	 */
	public void setRequestedType(ShowcaseType requestedType) {
		this.requestedType = requestedType;
	}

	/**
	 * @return the requestedType
	 */
	public ShowcaseType getRequestedType() {
		return requestedType;
	}

	/**
	 * @param requestedBlock the requestedBlock to set
	 */
	public void setRequestedBlock(Block requestedBlock) {
		this.requestedBlock = requestedBlock;
	}

	/**
	 * @return the requestedBlock
	 */
	public Block getRequestedBlock() {
		return requestedBlock;
	}

	/**
	 * @param dialogState the dialogState to set
	 */
	public void setDialogState(int dialogState) {
		this.dialogState = dialogState;
	}

	/**
	 * @return the dialogState
	 */
	public int getDialogState() {
		return dialogState;
	}
	
	public void resetDialog(){
		dialogState = 0;
		requestedBlock = null;
		requestedType = ShowcaseType.NONE;
		requestedItem = null;
		requestedPrice = 0;
	}
	
	public void sendMessage(String message){
		for(String line:message.split("\n")){
			getPlayer().sendMessage(line);
		}
	}

	/**
	 * @param requestedItem the requestedItem to set
	 */
	public void setRequestedItem(ItemStack requestedItem) {
		this.requestedItem = requestedItem;
	}

	/**
	 * @return the requestedItem
	 */
	public ItemStack getRequestedItem() {
		return requestedItem;
	}

	/**
	 * @param requestedPrice the requestedPrice to set
	 */
	public void setRequestedPrice(double requestedPrice) {
		this.requestedPrice = requestedPrice;
	}

	/**
	 * @return the requestedPrice
	 */
	public double getRequestedPrice() {
		return requestedPrice;
	}
	
	public int getAmountOfType(Material mat, short data){
		Inventory inv = getPlayer().getInventory();
		int ret = 0;
		for(int i = 0; i<inv.getSize();i++){
			ItemStack stack = inv.getItem(i);
			if(stack.getType().equals(mat)&&stack.getDurability()==data){
				ret+=stack.getAmount();
			}
		}
		return ret;
	}

	public void remove(Material mat, short data, int amount) {
		Inventory inv = getPlayer().getInventory();
		for(int i = 0; i<inv.getSize();i++){
			ItemStack stack = inv.getItem(i).clone();
			if(stack.getType().equals(mat)&&stack.getDurability()==data){
				if(stack.getAmount()>amount){
					stack.setAmount(stack.getAmount()-amount);
					inv.setItem(i, stack);
					return;
				} else {
					amount-=stack.getAmount();
					inv.setItem(i, null);
				}
			}
		}
	}

	/**
	 * @param hasReadPrice the hasReadPrice to set
	 */
	public void setHasReadPrice(boolean hasReadPrice) {
		this.hasReadPrice = hasReadPrice;
	}

	/**
	 * @return the hasReadPrice
	 */
	public boolean hasReadPrice() {
		return hasReadPrice;
	}

	/**
	 * @param readPriceLocation the readPriceLocation to set
	 */
	public void setReadPriceLocation(Location readPriceLocation) {
		this.readPriceLocation = readPriceLocation;
	}

	/**
	 * @return the readPriceLocation
	 */
	public Location getReadPriceLocation() {
		return readPriceLocation;
	}
	
	public boolean canAfford(double price){
		MethodAccount account = getAccount();
		if(account!=null)
		{
			return account.hasEnough(price);
		} else {
			return false;
		}
	}
	
	public boolean withdraw(double price){
		if(canAfford(price)){
			MethodAccount account = getAccount();
			if(account!=null)
			{
				account.subtract(price);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void giveMoney(double amount){
		MethodAccount account = getAccount();
		if(account!=null)
		{
			account.add(amount);
		}
	}
	
	public boolean standsOnReadPosition(){
		Location pos1 = new Location(readPriceLocation.getWorld(), readPriceLocation.getBlockX(), readPriceLocation.getBlockY(), readPriceLocation.getBlockZ());
		Location tmp = getPlayer().getLocation();
		Location pos2 = new Location(tmp.getWorld(), tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
		return pos1.equals(pos2);
	}
	
	public MethodAccount getAccount(){
		Method method = ShowcaseMain.instance.method;
		if(method!=null)
		{
			if(method.hasAccount(player)){
				return method.getAccount(player);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * @param lastClickedShowcase the lastClickedShowcase to set
	 */
	public void setLastClickedShowcase(ShowcaseItem lastClickedShowcase) {
		this.lastClickedShowcase = lastClickedShowcase;
	}

	/**
	 * @return the lastClickedShowcase
	 */
	public ShowcaseItem getLastClickedShowcase() {
		return lastClickedShowcase;
	}
}
