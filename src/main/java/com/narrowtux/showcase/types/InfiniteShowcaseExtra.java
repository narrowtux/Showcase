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

public class InfiniteShowcaseExtra implements ShowcaseExtra {

	private double price;
	private ShowcaseItem showcase = null;

	public boolean onDestroy(ShowcasePlayer player) {
		// No special cases to do here.
		return true;
	}

	public void onClick(ShowcasePlayer player) {
		if (!player.hasPermission("showcase.buy.infinite", false)) {
			return;
		}
		BuyAssistant assistant = new BuyAssistant(player.getPlayer(), showcase);
		assistant.setAssistantStartLocation(player.getPlayer().getLocation());
		assistant.start();
	}

	public String save() {
		return String.valueOf(price);
	}

	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	/**
	 * @param price
	 *            the price to set
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

	public void onRightClick(ShowcasePlayer player) {
	}
}
