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

import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;

public class FiniteShowcaseExtra implements ShowcaseExtra {
	private int itemAmount = 0;
	private double pricePerItem = 0.0D;
	private ShowcaseItem item = null;

	public boolean onDestroy(ShowcasePlayer player) {
		ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
		owner.addItems(item.getMaterial(), item.getData(), itemAmount);
		itemAmount = 0;
		return true;
	}

	public void onClick(ShowcasePlayer player) {
		if (!player.hasPermission("showcase.buy.finite", false)) {
			return;
		}
		BuyAssistant assistant = new BuyAssistant(player.getPlayer(), item);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}

	public String save() {
		return itemAmount + ";" + pricePerItem;
	}

	/**
	 * @param itemAmount
	 *            the itemAmount to set
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
	 * @param pricePerItem
	 *            the pricePerItem to set
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

	public void setShowcaseItem(ShowcaseItem item) {
		this.item = item;
	}

	public void onRightClick(ShowcasePlayer player) {
		if (!player.getPlayer().getName().equals(item.getPlayer())) {
			return;
		}
		RefillAssistant assistant = new RefillAssistant(player.getPlayer(),
				item);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}
}
