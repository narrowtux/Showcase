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

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;

import com.narrowtux.showcase.Showcase;

public class ShowcasePricePage extends AssistantPage {
	public double price;

	public ShowcasePricePage(Assistant assistant){
		super(assistant);
		setTitle(Showcase.tr("assistant.price.title"));
		setText("");
	}

	@Override
	public AssistantAction onPageInput(String text){
		try{
			price = Double.valueOf(text);
		} catch(Exception e){
			price = -1;
		}
		if(price<0){
			return AssistantAction.CANCEL;
		}
		getAssistant().sendMessage(getAssistant().formatLine(Showcase.tr("assistant.price.done", price)));
		return AssistantAction.CONTINUE;
	}
}
