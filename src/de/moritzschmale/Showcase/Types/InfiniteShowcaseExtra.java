package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class InfiniteShowcaseExtra implements ShowcaseExtra {

	private double price;
	
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		// TODO Auto-generated method stub

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

}
