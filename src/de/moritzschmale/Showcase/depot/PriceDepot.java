package de.moritzschmale.showcase.depot;

import com.narrowtux.Main.NarrowtuxLib;

import de.moritzschmale.showcase.ShowcasePlayer;

public class PriceDepot extends Depot {
	
	private double amount = 0.0D;
	private double price = 0.0D;
	
	public PriceDepot(double price, double amount)
	{
		this.amount = amount;
		this.price = price;
	}

	@Override
	public void addToDepot(int amount) {
		this.amount+=amount*price;
	}

	@Override
	public boolean giveDepot(ShowcasePlayer player) {
		player.getAccount().add(amount);
		amount = 0;
		return true;
	}

	@Override
	public String toString() {
		return NarrowtuxLib.getMethod().format(amount);
	}

	@Override
	public boolean addToDepot(int amount, ShowcasePlayer player) {
		if(player.canAfford(price*amount)){
			player.takeMoney(price*amount);
			addToDepot(amount);
			return true;
		}
		return false;
	}
}
