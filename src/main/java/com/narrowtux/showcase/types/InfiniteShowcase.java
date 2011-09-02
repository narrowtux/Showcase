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

public class InfiniteShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	@Override
	public String getType() {
		return "infinite";
	}

	@Override
	public String getPermission() {
		return "showcase.infinite";
	}

	@Override
	public boolean isOpMethod() {
		return true;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		double price = Double.valueOf(values);
		InfiniteShowcaseExtra extra = new InfiniteShowcaseExtra();
		extra.setPrice(price);
		return extra;
	}

	@Override
	public String getDescription() {
		return ShowcaseMain.tr("types.infinite.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = new ShowcasePricePage(assistant);
		assistant.addPage(pricePage);
		pricePages.put(assistant, pricePage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = pricePages.get(assistant);
		InfiniteShowcaseExtra extra = new InfiniteShowcaseExtra();
		extra.setPrice(pricePage.price);
		pricePages.remove(assistant);
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return 0;
	}
}
