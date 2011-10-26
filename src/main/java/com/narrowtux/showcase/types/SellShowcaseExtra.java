/*
 * Copyright (C) 2011 Moritz Schmale <narrow.m@gmail.com>
 *
 * Showcase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */

package com.narrowtux.showcase.types;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;
import com.nijikokun.register.narrowtuxlib.payment.Method;

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
		player.sendMessage(Showcase.tr("sell.moneygiveback", method.format(amount)));
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
