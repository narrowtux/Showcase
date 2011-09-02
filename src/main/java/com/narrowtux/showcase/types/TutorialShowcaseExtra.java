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

public class TutorialShowcaseExtra implements ShowcaseExtra {
	private String text = "I've got no text";
	@SuppressWarnings("unused")
	private ShowcaseItem showcase = null;

	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		player.sendMessage(text);
	}

	@Override
	public String save() {
		return text.replace("\n", "\\n").replace(",", ";;");
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

	public void setText(String text){
		this.text = text.replace("\\n", "\n").replace(";;", ",");
	}

	public String getText(){
		return text;
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {

	}
}
