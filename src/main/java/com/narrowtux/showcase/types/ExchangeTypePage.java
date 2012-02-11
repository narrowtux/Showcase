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

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase.Showcase;

public class ExchangeTypePage extends AssistantPage {
	public Material type;
	public short data;

	public ExchangeTypePage(Assistant assistant) {
		super(assistant);
		setTitle(Showcase.tr("assistant.exchange.create.type.title"));
		setText(Showcase.tr("assistant.exchange.create.type.text"));
	}

	@Override
	public AssistantAction onPageInput(String text) {
		if (text.equals("cancel")) {
			return AssistantAction.CANCEL;
		}
		if (text.equals("ok")) {
			ItemStack stack = getAssistant().getPlayer().getItemInHand();
			if (stack != null) {
				type = stack.getType();
				data = stack.getDurability();
			} else {
				sendMessage(getAssistant().formatLine(Showcase.tr("noItemError")));
				return AssistantAction.SILENT_REPEAT;
			}
			return AssistantAction.CONTINUE;
		} else {
			String[] args = text.split(":");
			type = Material.AIR;
			data = 0;
			if (args.length == 1) {
				try {
					type = Material.getMaterial(args[0].toUpperCase());
				} catch (Exception e) {
					try {
						type = Material.getMaterial(Integer.valueOf(args[0]));
					} catch (Exception ex) {
						type = Material.AIR;
					}
				}
			}
			if (args.length == 2) {
				try {
					data = Short.valueOf(args[1]);
				} catch (Exception e) {
					data = 0;
				}
			}
			if (type == null || type.equals(Material.AIR)) {
				sendMessage(getAssistant().formatLine(Showcase.tr("itemExistError")));
				return AssistantAction.SILENT_REPEAT;
			} else {
				sendMessage(Showcase.tr("assistant.exchange.create.type.selected", Showcase.getName(type, data)));
			}
			return AssistantAction.CONTINUE;
		}
	}
}
