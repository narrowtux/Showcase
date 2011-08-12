package de.moritzschmale.showcase.types;

import de.moritzschmale.showcase.assistants.BuyAssistant;
import de.moritzschmale.showcase.assistants.RefillAssistant;
import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;

public class FiniteShowcaseExtra implements ShowcaseExtra {
	private int itemAmount = 0;
	private double pricePerItem = 0.0D;
	private ShowcaseItem item = null;
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
		owner.addItems(item.getMaterial(), item.getData(), itemAmount);
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		if(!player.hasPermission("showcase.buy.finite", false)){
			return;
		}
		BuyAssistant assistant = new BuyAssistant(player.getPlayer(), item);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}

	@Override
	public String save() {
		return itemAmount+";"+pricePerItem;
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

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		this.item = item;
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
		if(!player.getPlayer().getName().equals(item.getPlayer())){
			return;
		}
		RefillAssistant assistant = new RefillAssistant(player.getPlayer(), item);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}

}
