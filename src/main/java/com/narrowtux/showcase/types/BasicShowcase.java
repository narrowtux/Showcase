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

import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseCreationAssistant;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcasePlayer;
import com.narrowtux.showcase.ShowcaseProvider;

public class BasicShowcase implements ShowcaseProvider {
	public String getType() {
		return "basic";
	}

	public String getPermission() {
		return "showcase.basic";
	}

	public boolean isOpMethod() {
		return false;
	}

	public ShowcaseExtra loadShowcase(String values) {
		return new BasicShowcaseExtra();
	}

	public String getDescription() {
		return Showcase.tr("types.basic.description");
	}

	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
	}

	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		if (Showcase.instance.config.isBasicUseItem()) {
			ShowcasePlayer player = ShowcasePlayer.getPlayer(assistant
					.getPlayer());
			player.remove(assistant.material, assistant.data, 1);
		}
		return new BasicShowcaseExtra();
	}

	public double getPriceForCreation(ShowcasePlayer player) {
		return Showcase.instance.config.getPriceForBasic();
	}
}
