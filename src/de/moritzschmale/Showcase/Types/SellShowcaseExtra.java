package de.moritzschmale.Showcase.Types;

import com.narrowtux.Main.NarrowtuxLib;
import com.nijikokun.register.payment.Method;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class SellShowcaseExtra implements ShowcaseExtra {
	private ShowcaseItem showcase;
	private int amountLeft = 0;
	private double pricePerItem = 0;
	private int amountOfItems = 0;
	
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		double amount = amountLeft*pricePerItem;
		player.giveMoney(amount);
		player.addItems(showcase.getMaterial(), showcase.getData(), amountOfItems);
		Method method = NarrowtuxLib.getMethod();
		//TODO: put a string in all translation files!
		player.sendMessage(ShowcaseMain.tr("sell.moneygiveback", method.format(amount)));
		return true;
	}
	
	@Override
	public void onClick(ShowcasePlayer player) {
		//TODO: Sell assistant
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
		//TODO: Refill assistant (maybe later)
	}

	@Override
	public String save() {
		return amountLeft+";"+pricePerItem+";"+amountOfItems;
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	public void setAmountLeft(int amountLeft) {
		this.amountLeft = amountLeft;
	}

	public int getAmountLeft() {
		return amountLeft;
	}

	public void setPricePerItem(double pricePerItem) {
		this.pricePerItem = pricePerItem;
	}

	public double getPricePerItem() {
		return pricePerItem;
	}

	public void setAmountOfItems(int amountOfItems) {
		this.amountOfItems = amountOfItems;
	}

	public int getAmountOfItems() {
		return amountOfItems;
	}

}
