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

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.narrowtuxlib.translation.Translation;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseCreationAssistant;
import com.narrowtux.showcase.ShowcaseExtra;
import com.narrowtux.showcase.ShowcasePlayer;
import com.narrowtux.showcase.ShowcaseProvider;

public class TutorialShowcase implements ShowcaseProvider {

	private Map<Assistant, String> texts = new HashMap<Assistant, String>();

	public String getType() {
		return "tutorial";
	}

	public String getPermission() {
		return "showcase.tutorial";
	}

	public boolean isOpMethod() {
		return false;
	}

	public ShowcaseExtra loadShowcase(String values) {
		TutorialShowcaseExtra extra = new TutorialShowcaseExtra();
		extra.setText(values);
		return extra;
	}

	public String getDescription() {
		return Showcase.tr("types.tutorial.description");
	}

	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		AssistantPage page = new AssistantPage(assistant) {
			{
				setTitle(Showcase.tr("tutorial.title"));
				setText(Showcase.tr("tutorial.text"));
			}

			public AssistantAction onPageInput(String text) {
				if (text.equals("done")) {
					return AssistantAction.CONTINUE;
				} else {
					text = Translation.parseColors(text);
					String tmp = texts.get(getAssistant());
					if (tmp.length() > 0) {
						tmp += "\n";
					}
					tmp += text;
					sendMessage(text);
					texts.remove(getAssistant());
					texts.put(getAssistant(), tmp);
					return AssistantAction.SILENT_REPEAT;
				}
			}
		};
		assistant.addPage(page);
		texts.put(assistant, "");
	}

	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		TutorialShowcaseExtra extra = new TutorialShowcaseExtra();
		extra.setText(texts.get(assistant));
		return extra;
	}

	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Auto-generated method stub
		return 0;
	}
}
