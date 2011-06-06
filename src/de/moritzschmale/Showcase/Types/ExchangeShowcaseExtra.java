package de.moritzschmale.Showcase.Types;

import org.bukkit.Material;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class ExchangeShowcaseExtra implements ShowcaseExtra {

	private Material exchangeType = Material.GOLD_INGOT;
	private short exchangeData = 0;
	private int itemAmount = 1;
	private int exchangeAmount = 0;
	private int exchangeRateLeft = 1, exchangeRateRight = 1;
	private ShowcaseItem showcase = null;
	
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		player.addItems(exchangeType, exchangeData, exchangeAmount);
		player.addItems(showcase.getMaterial(), showcase.getData(), itemAmount);
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		if(player.getPlayer().getName().equals(showcase.getPlayer())){
			String name = ShowcaseMain.getName(exchangeType, exchangeData);
			int remaining = player.addItems(exchangeType, exchangeData, exchangeAmount);
			player.sendMessage("Added "+(exchangeAmount-remaining)+" "+name+" to your inventory.");
			if(remaining!=0){
				player.sendMessage("Your inventory is full, "+remaining+" items are still in the showcase.");
			}
			exchangeAmount = remaining;
		} else {
			ExchangeAssistant assistant = new ExchangeAssistant(player.getPlayer(), showcase);
			assistant.setAssistantStartLocation(player.getPlayer().getLocation());
			assistant.start();
		}
	}

	@Override
	public String save() {
		//Values:
		//Exchange-type;Exchange-data;buy-amount;exchange-amount;exchange-rate
		return exchangeType.getId()+";"+exchangeData+";"+itemAmount+";"+exchangeAmount+";"+exchangeRateLeft+";"+exchangeRateRight;
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	/**
	 * @return the exchangeType
	 */
	public Material getExchangeType() {
		return exchangeType;
	}

	/**
	 * @param exchangeType the exchangeType to set
	 */
	public void setExchangeType(Material exchangeType) {
		this.exchangeType = exchangeType;
	}

	/**
	 * @return the exchangeData
	 */
	public short getExchangeData() {
		return exchangeData;
	}

	/**
	 * @param exchangeData the exchangeData to set
	 */
	public void setExchangeData(short exchangeData) {
		this.exchangeData = exchangeData;
	}

	/**
	 * @return the itemAmount
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	/**
	 * @param itemAmount the itemAmount to set
	 */
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	/**
	 * @return the exchangeAmount
	 */
	public int getExchangeAmount() {
		return exchangeAmount;
	}

	/**
	 * @param exchangeAmount the exchangeAmount to set
	 */
	public void setExchangeAmount(int exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}

	/**
	 * @return the exchangeRateLeft
	 */
	public int getExchangeRateLeft() {
		return exchangeRateLeft;
	}

	/**
	 * @param exchangeRateLeft the exchangeRateLeft to set
	 */
	public void setExchangeRateLeft(int exchangeRateLeft) {
		this.exchangeRateLeft = exchangeRateLeft;
	}

	/**
	 * @return the exchangeRateRight
	 */
	public int getExchangeRateRight() {
		return exchangeRateRight;
	}

	/**
	 * @param exchangeRateRight the exchangeRateRight to set
	 */
	public void setExchangeRateRight(int exchangeRateRight) {
		this.exchangeRateRight = exchangeRateRight;
	}
	
	public void addItems(int amount){
		itemAmount+=amount;
	}
	
	public void addExchanges(int amount){
		exchangeAmount+=amount;
	}

}
