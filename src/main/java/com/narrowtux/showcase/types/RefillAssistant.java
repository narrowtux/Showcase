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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;

public class RefillAssistant extends Assistant {
	public ShowcaseItem showcase;
	public FiniteShowcaseExtra extra;

	public RefillAssistant(Player p, final ShowcaseItem showcase) {
		super(p);
		this.showcase = showcase;
		extra = (FiniteShowcaseExtra) showcase.getExtra();

		AssistantPage page = new AssistantPage(this) {
			@Override
			public AssistantAction onPageInput(String text) {
				int value = 0;
				try {
					value = Integer.valueOf(text);
				} catch (Exception e) {
					value = 0;
				}
				if (value == 0) {
					return AssistantAction.CANCEL;
				}
				ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
				Material mat = showcase.getMaterial();
				short data = showcase.getData();
				int amountAtPlayer = player.getAmountOfType(mat, data);
				int amountAtShowcase = extra.getItemAmount();
				if (value > amountAtPlayer) {
					value = amountAtPlayer;
				}
				if (value < 0 && value * -1 > amountAtShowcase) {
					value = -amountAtShowcase;
				}
				if (value > 0 && value <= amountAtPlayer) {
					player.remove(mat, data, value);
					extra.setItemAmount(amountAtShowcase + value);
					sendMessage(formatLine(ChatColor.YELLOW.toString() + value + ChatColor.WHITE + " items added."));
				} else if (value < 0 && value * -1 <= amountAtShowcase) {
					player.addItems(mat, data, -value);
					extra.setItemAmount(amountAtShowcase + value);
					sendMessage(formatLine(ChatColor.YELLOW.toString() + (-value) + ChatColor.WHITE + " items removed."));
				}
				updateText();
				addPage(this);
				return AssistantAction.CONTINUE;
			}
		};
		setTitle(Showcase.tr("assistant.refill.title"));
		page.setTitle("");
		addPage(page);
		updateText();
	}

	@Override
	public void onAssistantCancel() {
		sendMessage(formatLine(Showcase.tr("assistant.refill.finish")));
	}

	@Override
	public void onAssistantFinish() {
		sendMessage(formatLine(Showcase.tr("assistant.refill.finish")));
	}

	public void updateText() {
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
		Material mat = showcase.getMaterial();
		short data = showcase.getData();
		String text = "";
		String itemName = Showcase.getName(mat, data);
		text = Showcase.tr("assistant.refill.body", extra.getItemAmount(), itemName, NarrowtuxLib.getMethod().format(extra.getPricePerItem()), player.getAmountOfType(mat, data));
		for (AssistantPage page : getPages()) {
			page.setText(text);
		}
	}

	@Override
	public boolean useGUI() {
		return false;
	}
}
