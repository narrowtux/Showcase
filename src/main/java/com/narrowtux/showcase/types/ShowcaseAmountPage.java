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

import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseCreationAssistant;

public class ShowcaseAmountPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public int maximumamount = 0;
	public int amount = 0;

	public ShowcaseAmountPage(ShowcaseCreationAssistant a) {
		super(a);
		assistant = a;
		maximumamount = assistant.player.getAmountOfType(assistant.material,
				assistant.data);
		String title = Showcase.tr("assistant.amount.title", maximumamount,
				Showcase.getName(assistant.material, assistant.data));
		setTitle(title);
		setText("");
	}

	@Override
	public AssistantAction onPageInput(String text) {
		maximumamount = assistant.player.getAmountOfType(assistant.material,
				assistant.data);
		try {
			amount = Integer.valueOf(text);
		} catch (Exception e) {
			amount = -1;
		}
		if (amount <= 0) {
			return AssistantAction.CANCEL;
		}
		if (amount > maximumamount) {
			amount = maximumamount;
		}
		assistant.sendMessage(assistant.formatLine(Showcase.tr(
				"assistant.amount.add", amount)));
		return AssistantAction.CONTINUE;
	}
}
