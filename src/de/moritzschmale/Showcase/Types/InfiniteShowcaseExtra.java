package de.moritzschmale.showcase.types;

import de.moritzschmale.showcase.types.BuyAssistant;
import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;

public class InfiniteShowcaseExtra implements ShowcaseExtra {

	private double price;
	private ShowcaseItem showcase = null;
	
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		//No special cases to do here.
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		if(!player.hasPermission("showcase.buy.infinite", false)){
			return;
		}
		BuyAssistant assistant = new BuyAssistant(player.getPlayer(), showcase);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}

	@Override
	public String save() {
		return String.valueOf(price);
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
	}

}
