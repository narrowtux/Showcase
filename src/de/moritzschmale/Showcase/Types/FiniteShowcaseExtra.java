package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcasePlayer;

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
		if(player.getPlayer().getName().equals(item.getPlayer())){
			RefillAssistant assistant = new RefillAssistant(player.getPlayer(), item);
			assistant.setAssistantStartLocation(player.getPlayer().getLocation());
			assistant.start();
		} else {
			BuyAssistant assistant = new BuyAssistant(player.getPlayer(), item);
			assistant.setAssistantStartLocation(player.getPlayer().getLocation());
			assistant.start();
		}
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

}
