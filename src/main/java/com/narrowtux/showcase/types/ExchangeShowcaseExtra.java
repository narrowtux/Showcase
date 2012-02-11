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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;

public class ExchangeShowcaseExtra implements ShowcaseExtra {
	private Material exchangeType = Material.GOLD_INGOT;
	private short exchangeData = 0;
	private int itemAmount = 1;
	private int exchangeAmount = 0;
	private int exchangeRateLeft = 1, exchangeRateRight = 1;
	private ShowcaseItem showcase = null;

	public boolean onDestroy(ShowcasePlayer player) {
		player.addItems(exchangeType, exchangeData, exchangeAmount);
		player.addItems(showcase.getMaterial(), showcase.getData(), itemAmount);
		return true;
	}

	public void onClick(ShowcasePlayer player) {
		if (player.getPlayer().getName().equals(showcase.getPlayer())) {
			String name = Showcase.getName(exchangeType, exchangeData);
			int remaining = player.addItems(exchangeType, exchangeData,
					exchangeAmount);
			player.sendMessage(Showcase.tr("exchange.addexchange",
					exchangeAmount - remaining, name));
			if (remaining != 0) {
				player.getPlayer()
						.getWorld()
						.dropItemNaturally(
								player.getPlayer().getLocation(),
								new ItemStack(exchangeType, remaining,
										exchangeData));
				player.sendMessage(Showcase.tr("exchange.remaining.dropped",
						remaining, name));
			}
			exchangeAmount = remaining;
		} else {
			ExchangeAssistant assistant = new ExchangeAssistant(
					player.getPlayer(), showcase);
			assistant.setAssistantStartLocation(player.getPlayer()
					.getLocation());
			assistant.start();
		}
	}

	public String save() {
		// Values:
		// Exchange-type;Exchange-data;buy-amount;exchange-amount;exchange-rate
		return exchangeType.getId() + ";" + exchangeData + ";" + itemAmount
				+ ";" + exchangeAmount + ";" + exchangeRateLeft + ";"
				+ exchangeRateRight;
	}

	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	/**
	 * @return the exchangeType
	 */
	public Material getExchangeType() {
		return exchangeType;
	}

	/**
	 * @param exchangeType
	 *            the exchangeType to set
	 */
	public void setExchangeType(Material exchangeType) {
		this.exchangeType = exchangeType;
	}

	/**
	 * @return the exchangeData
	 */
	public short getExchangeData() {
		return exchangeData;
	}

	/**
	 * @param exchangeData
	 *            the exchangeData to set
	 */
	public void setExchangeData(short exchangeData) {
		this.exchangeData = exchangeData;
	}

	/**
	 * @return the itemAmount
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	/**
	 * @param itemAmount
	 *            the itemAmount to set
	 */
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	/**
	 * @return the exchangeAmount
	 */
	public int getExchangeAmount() {
		return exchangeAmount;
	}

	/**
	 * @param exchangeAmount
	 *            the exchangeAmount to set
	 */
	public void setExchangeAmount(int exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
	}

	/**
	 * @return the exchangeRateLeft
	 */
	public int getExchangeRateLeft() {
		return exchangeRateLeft;
	}

	/**
	 * @param exchangeRateLeft
	 *            the exchangeRateLeft to set
	 */
	public void setExchangeRateLeft(int exchangeRateLeft) {
		this.exchangeRateLeft = exchangeRateLeft;
	}

	/**
	 * @return the exchangeRateRight
	 */
	public int getExchangeRateRight() {
		return exchangeRateRight;
	}

	/**
	 * @param exchangeRateRight
	 *            the exchangeRateRight to set
	 */
	public void setExchangeRateRight(int exchangeRateRight) {
		this.exchangeRateRight = exchangeRateRight;
	}

	public void addItems(int amount) {
		itemAmount += amount;
	}

	public void addExchanges(int amount) {
		exchangeAmount += amount;
	}

	public void onRightClick(ShowcasePlayer player) {
		// TODO Auto-generated method stub
	}
}
