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

import java.util.HashMap;
import java.util.Map;

import com.narrowtux.narrowtuxlib.assistant.Accuracy;
import com.narrowtux.narrowtuxlib.assistant.NumberPage;
import com.narrowtux.showcase.ShowcaseCreationAssistant;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcasePlayer;
import com.narrowtux.showcase.ShowcaseProvider;

public class SellShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	private Map<ShowcaseCreationAssistant, NumberPage> amountPages = new HashMap<ShowcaseCreationAssistant, NumberPage>();

	public String getType() {
		return "finitesell";
	}

	public String getPermission() {
		return "showcase.sell.finite";
	}

	public boolean isOpMethod() {
		return false;
	}

	public ShowcaseExtra loadShowcase(String values) {
		SellShowcaseExtra extra = new SellShowcaseExtra();
		String args[] = values.split(";");
		if (args.length >= 3) {
			extra.setAmountLeft(Integer.valueOf(args[0]));
			extra.setPricePerItem(Double.valueOf(args[1]));
			extra.setAmountOfItems(Integer.valueOf(args[2]));
			return extra;
		}
		return null;
	}

	public String getDescription() {
		// TODO: actually use translation
		return "Drop some money in the sell showcase, other players can sell their items there and get this money.";
	}

	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage price = new ShowcasePricePage(assistant);
		pricePages.put(assistant, price);
		assistant.addPage(price);
		NumberPage amount = new NumberPage(assistant, Accuracy.INT);
		// TODO: Translation!
		amount.setTitle("Item amount");
		amount.setText("How many items do you want to buy?\nPlease note: amount*price is taken from your balance after setup!");
		amount.setMinimum(0);
		amount.setMaximum(Integer.MAX_VALUE);
		amountPages.put(assistant, amount);
		assistant.addPage(amount);
	}

	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		SellShowcaseExtra extra = new SellShowcaseExtra();
		ShowcasePricePage price = pricePages.get(assistant);
		extra.setPricePerItem(price.price);
		NumberPage amount = amountPages.get(assistant);
		extra.setAmountLeft((int) amount.getValue());
		double totalPrice = extra.getAmountLeft() * extra.getPricePerItem();
		ShowcasePlayer player = ShowcasePlayer.getPlayer(assistant.getPlayer());
		if (!player.canAfford(totalPrice)) {
			// TODO: actual translation
			player.sendMessage("You can't afford so many items. Try again!");
			return null;
		} else {
			player.takeMoney(totalPrice);
		}
		return extra;
	}

	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Generate Config value
		return 0;
	}
}
