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

import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseCreationAssistant;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcasePlayer;
import com.narrowtux.showcase.ShowcaseProvider;

public class FiniteShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	private Map<ShowcaseCreationAssistant, ShowcaseAmountPage> amountPages = new HashMap<ShowcaseCreationAssistant, ShowcaseAmountPage>();
	@Override
	public String getType() {
		return "finite";
	}

	@Override
	public String getPermission() {
		return "showcase.finite";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		String [] args = values.split(";");
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		if(args.length==2){
			extra.setItemAmount(Integer.valueOf(args[0]));
			extra.setPricePerItem(Double.valueOf(args[1]));
		}
		return extra;
	}

	@Override
	public String getDescription() {
		return Showcase.tr("types.finite.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = new ShowcasePricePage(assistant);
		pricePages.put(assistant, pricePage);
		assistant.addPage(pricePage);
		ShowcaseAmountPage amountPage = new ShowcaseAmountPage(assistant);
		amountPages.put(assistant, amountPage);
		assistant.addPage(amountPage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		ShowcasePricePage pricePage = pricePages.get(assistant);
		pricePages.remove(assistant);
		ShowcaseAmountPage amountPage = amountPages.get(assistant);
		amountPages.remove(assistant);
		extra.setItemAmount(amountPage.amount);
		extra.setPricePerItem(pricePage.price);
		ShowcasePlayer player = assistant.player;
		player.remove(assistant.material, assistant.data, amountPage.amount);
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return Showcase.instance.config.getPriceForFiniteShop();
	}

}
