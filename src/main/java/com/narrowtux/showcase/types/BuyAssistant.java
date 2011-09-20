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

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;
import com.nijikokun.register.payment.Method;

public class BuyAssistant extends Assistant {
	public int amount;
	public ShowcaseItem item;
	public BuyAssistant(Player p, final ShowcaseItem item) {
		super(p);
		this.item = item;
		setTitle(Showcase.tr("assistant.buy.title"));
		/*
		 Method method = Showcase.instance.method;
		if(method==null){
			return;
		}
		print+=ChatColor.YELLOW+"This is "+ChatColor.WHITE+showItem.getPlayer()+ChatColor.YELLOW+"'s shop.\n";
		print+=ChatColor.YELLOW+"How many items do you want?\n"+ChatColor.YELLOW+"Type the number in chat, "+ChatColor.WHITE+"0"+ChatColor.YELLOW+" to abort.";
		 */
		AssistantPage page = new AssistantPage(this){
			@Override
			public AssistantAction onPageInput(String text){
				try{
					amount = Integer.valueOf(text);
				} catch(Exception e){
					amount = -1;
				}
				if(amount<=0){
					return AssistantAction.CANCEL;
				}

				if(getAmount()<amount&&item.getType().equals("finite")){
					amount = getAmount();
				}
				return AssistantAction.FINISH;
			}
		};
		Method method = NarrowtuxLib.getMethod();
		page.setText(Showcase.tr("assistant.buy.text"));
		String itemCount = "";
		if(item.getType().equals("finite")){
			itemCount = ChatColor.YELLOW+" ("+ChatColor.WHITE+"x"+getAmount()+ChatColor.YELLOW+")";
		}
		String itemName = Showcase.getName(item.getMaterial(), item.getData());
		String print = "";
		print+=Showcase.tr("assistant.buy.price", itemName+itemCount, method.format(getPrice()));
		page.setTitle(print);
		addPage(page);
	}

	@Override
	public void onAssistantFinish(){
		//Buy the actual items...
		double totalamount = amount*getPrice();
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
		if(player.canAfford(totalamount)){
			int remaining = player.addItems(item.getMaterial(), item.getData(), amount);
			if(remaining>0){
				totalamount = (amount-remaining)*getPrice();
				amount = amount-remaining;
			}
			String itemName = Showcase.getName(item.getMaterial(), item.getData());
			String total = NarrowtuxLib.getMethod().format(totalamount);
			player.takeMoney(totalamount);
			if(item.getType().equals("finite")){
				FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
				extra.setItemAmount(getAmount()-amount);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.giveMoney(totalamount);
				owner.sendNotification("Showcase", Showcase.tr("buyNotification", player.getPlayer().getName(), amount, itemName, total));
			}
			String text = "";
			text+=Showcase.tr("buyMessage", amount, itemName, total);
			sendMessage(formatLine(text));
			if(getAmount()==0&&item.getType().equals("finite")&&Showcase.instance.config.isRemoveWhenEmpty()){
				item.remove();
				Showcase.instance.showcasedItems.remove(item);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.sendNotification("Showcase", Showcase.tr("shopSoldOut", itemName));
			}
		} else {
			sendMessage(formatLine(Showcase.tr("notEnoughMoney")));
		}
	}

	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine(Showcase.tr("checkoutCancel")));
	}

	public int getAmount(){
		if(item.getType().equals("finite")){
			FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
			return extra.getItemAmount();
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public double getPrice(){
		if(item.getType().equals("finite")){
			FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
			return extra.getPricePerItem();
		} else if(item.getType().equals("infinite")) {
			InfiniteShowcaseExtra extra = (InfiniteShowcaseExtra)item.getExtra();
			return extra.getPrice();
		} else {
			return 0;
		}
	}

	@Override
	public boolean useGUI() {
		return false;
	}
}
