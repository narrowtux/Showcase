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
import org.bukkit.entity.Player;

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;

import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcasePlayer;

public class ExchangeAssistant extends Assistant {
	ShowcaseItem showcase;
	ExchangeShowcaseExtra extra;
	ShowcasePlayer player;
	public ExchangeAssistant(Player p, ShowcaseItem item) {
		super(p);
		setTitle("Exchange Assistant");
		player = ShowcasePlayer.getPlayer(p);
		showcase = item;
		extra = (ExchangeShowcaseExtra)showcase.getExtra();
		AssistantPage page = new AssistantPage(this){
			{
				//TODO: Add translation!!!
				setTitle("How many items you want to get?");
				String text = "";
				String itemName = Showcase.getName(showcase.getMaterial(), showcase.getData());
				String exchangeName = Showcase.getName(extra.getExchangeType(), extra.getExchangeData());
				text+="Get "+ChatColor.YELLOW+extra.getExchangeRateLeft()+" "+itemName+ChatColor.WHITE;
				text+=" for "+ChatColor.YELLOW+extra.getExchangeRateRight()+" "+exchangeName+"\n";
				int playeramount = player.getAmountOfType(extra.getExchangeType(), extra.getExchangeData());
				text+="You have got "+ChatColor.YELLOW+playeramount+" "+exchangeName+"\n";
				text+="The showcase has got "+ChatColor.YELLOW+extra.getItemAmount()+" "+itemName+"\n";
				text+="Type "+ChatColor.YELLOW+"0 or go away"+ChatColor.WHITE+" to cancel.";
				setText(text);
			}

			@Override
			public AssistantAction onPageInput(String text){
				String itemName = Showcase.getName(showcase.getMaterial(), showcase.getData());
				String exchangeName = Showcase.getName(extra.getExchangeType(), extra.getExchangeData());
				int amount;
				try{
					amount = Integer.valueOf(text);
				} catch(Exception e){
					amount = 0;
				}
				int playeramount = player.getAmountOfType(extra.getExchangeType(), extra.getExchangeData());
				if(amount<=0){
					return AssistantAction.CANCEL;
				}
				//Check if amount is divisable by the rate
				int left = extra.getExchangeRateLeft();
				int right = extra.getExchangeRateRight();
				if(amount%left!=0){
					repeatCurrentPage();
					sendMessage(formatLine("This amount isn't available."));
					return AssistantAction.CONTINUE;
				}
				int exAmount = amount/left*right;
				if(exAmount>playeramount){
					repeatCurrentPage();
					sendMessage(formatLine("You haven't got enough items for this amount."));
					return AssistantAction.CONTINUE;
				}
				if(amount>extra.getItemAmount()){
					sendMessage(formatLine("This amount isn't available. Enter again."));
					return AssistantAction.SILENT_REPEAT;
				}
				player.addItems(showcase.getMaterial(), showcase.getData(), amount);
				player.remove(extra.getExchangeType(), extra.getExchangeData(), exAmount);
				extra.addExchanges(exAmount);
				extra.addItems(-amount);
				sendMessage(formatLine("You got "+amount+" "+itemName+" for "+exAmount+" "+exchangeName));
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(showcase.getPlayer());
				owner.sendMessage(player.getPlayer().getName()+" got "+amount+" "+itemName+" for "+exAmount+" "+exchangeName);
				return AssistantAction.CONTINUE;
			}
		};

		addPage(page);
	}

	@Override
	public void onAssistantFinish(){
	}

	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Assistant aborted"));
	}

	@Override
	public boolean useGUI() {
		return false;
	}
}
