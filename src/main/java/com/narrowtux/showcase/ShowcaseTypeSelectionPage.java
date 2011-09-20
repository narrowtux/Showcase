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

package com.narrowtux.showcase;

import org.bukkit.ChatColor;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public boolean cancel = false;
	public ShowcaseTypeSelectionPage(ShowcasePlayer player, Assistant assistant){
		super(assistant);
		setTitle(Showcase.tr("assistant.creation.select.title"));
		String text = "";
		for(ShowcaseProvider provider:Showcase.instance.providers.values()){
			if(player.hasPermission(provider.getPermission(), provider.isOpMethod())){
				text+=ChatColor.YELLOW+provider.getType()+ChatColor.WHITE;
				text+=" ("+ChatColor.YELLOW+getPrice(provider.getPriceForCreation(player))+ChatColor.WHITE+"), ";
			}
		}
		if(text.equals("")){
			text = Showcase.tr("noShowcasePermission");
			cancel = true;
		} else {
			text = text.substring(0,text.length()-2)+"\n";
			text+=Showcase.tr("helpDescription", Showcase.tr("helpCommand"));
		}
		setText(text);
	}

	@Override
	public AssistantAction onPageInput(String text){
		if(text.startsWith(Showcase.tr("helpCommand"))){
			String args[] = text.split(" ");
			if(args.length>=2){
				String type = args[1];
				if(Showcase.instance.providers.containsKey(type)){
					ShowcaseProvider provider = Showcase.instance.providers.get(type);
					String msg = "";
					msg+=provider.getType()+"\n";
					msg+=provider.getDescription();
					sendMessage(msg);
					return AssistantAction.REPEAT;
				} else {
					sendMessage(Showcase.tr("typeNotFound"));
					return AssistantAction.SILENT_REPEAT;
				}
			}
		}
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getAssistant().getPlayer());
		text = text.toLowerCase();
		ShowcaseProvider type = null;
		for(ShowcaseProvider provider:Showcase.instance.providers.values()){
			if(text.equals(provider.getType())){
				//Selected this type
				type = provider;
			}
		}
		if(type==null)
		{
			return AssistantAction.CANCEL;
		}
		if(!player.canAfford(type.getPriceForCreation(player))){
			sendMessage(getAssistant().formatLine(Showcase.tr("notEnoughMoneyForShowcase")));
			return AssistantAction.CANCEL;
		}
		if(player.hasPermission(type.getPermission(), type.isOpMethod())){
			this.assistant.type = type.getType();
			type.addPagesToCreationWizard((ShowcaseCreationAssistant) getAssistant());
			return AssistantAction.CONTINUE;
		} else {
			player.sendMessage(Showcase.tr("permissionsFail"));
			return AssistantAction.CANCEL;
		}
	}

	private String getPrice(double price){
		if(NarrowtuxLib.getMethod()!=null)
		{
			return NarrowtuxLib.getMethod().format(price);
		} else {
			return price+" $";
		}
	}

	@Override
	public void play(){
		super.play();
		if(cancel)
			getAssistant().cancel();
	}
}
