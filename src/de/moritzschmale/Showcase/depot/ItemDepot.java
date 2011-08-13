package de.moritzschmale.showcase.depot;

import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.util.Item;

public class ItemDepot extends Depot {
	private Item stack;
	private int price;

	@Override
	public void addToDepot(int amount) {
		stack.setAmount(stack.getAmount()+amount*price);
	}

	@Override
	public boolean giveDepot(ShowcasePlayer player) {
		if( player.addItems(stack)!=0){
			stack.setAmount(0);
			return true;
		}
		return false;
	}

	@Override
	public boolean addToDepot(int amount, ShowcasePlayer player) {
		if(player.getAmountOfType(stack)>=amount*price){
			player.remove(stack.getMaterial(), stack.getData(), amount*price);
			addToDepot(amount);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return stack.getAmount()+"x "+stack.getMaterial().toString().toLowerCase();
	}
}
