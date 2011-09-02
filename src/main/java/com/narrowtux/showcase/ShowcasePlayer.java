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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;

public class ShowcasePlayer {
	private String player;
	private static Map<String,ShowcasePlayer> instances = new HashMap<String, ShowcasePlayer>();
	private ShowcasePlayer(String player){
		this.player = player;
	}

	public static ShowcasePlayer getPlayer(String name){
		if(instances.containsKey(name)){
			return instances.get(name);
		} else {
			ShowcasePlayer player = new ShowcasePlayer(name);
			instances.put(name, player);
			return player;
		}
	}

	public static ShowcasePlayer getPlayer(Player player){
		return getPlayer(player.getName());
	}

	public Player getPlayer(){
		Player ret = Showcase.instance.getServer().getPlayer(player);
		return ret;
	}

	public boolean hasPermission(String node, boolean adminMethod){
		return Showcase.hasPermission(getPlayer(), node, adminMethod);
	}

	public void sendMessage(String message){
		if(getPlayer()==null)
		{
			return;
		}
		for(String line:message.split("\n")){
			getPlayer().sendMessage(line);
		}
	}

	public int getAmountOfType(Material mat, short data){
		Inventory inv = getPlayer().getInventory();
		int ret = 0;
		for(int i = 0; i<inv.getSize();i++){
			ItemStack stack = inv.getItem(i);
			if(stack!=null&&stack.getType().equals(mat)&&stack.getDurability()==data){
				ret+=stack.getAmount();
			}
		}
		return ret;
	}

	public void remove(Material mat, short data, int amount) {
		Inventory inv = getPlayer().getInventory();
		for(int i = 0; i<inv.getSize();i++){
			ItemStack stack = inv.getItem(i).clone();
			if(stack.getType().equals(mat)&&stack.getDurability()==data){
				if(stack.getAmount()>amount){
					stack.setAmount(stack.getAmount()-amount);
					inv.setItem(i, stack);
					return;
				} else {
					amount-=stack.getAmount();
					inv.setItem(i, null);
				}
			}
		}
	}

	public boolean canAfford(double price){
		if(price<=0){
			return true;
		}
		MethodAccount account = getAccount();
		if(account!=null)
		{
			return account.balance()>=price;
		} else {
			return false;
		}
	}

	public boolean takeMoney(double price){
		if(canAfford(price)){
			MethodAccount account = getAccount();
			if(account!=null)
			{
				account.subtract(price);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void giveMoney(double amount){
		MethodAccount account = getAccount();
		if(account!=null)
		{
			account.add(amount);
		}
	}

	public MethodAccount getAccount(){
		Method method = NarrowtuxLib.getMethod();
		if(method!=null)
		{
			if(method.hasAccount(player)){
				return method.getAccount(player);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}


	public int addItems(Material type, short data, int amount){
		//returns the number of items that did not fit.
		if(getPlayer()==null)
		{
			System.out.println("[Showcase] someone removed the items of "+player);
			return 0;
		}
		PlayerInventory inv = getPlayer().getInventory();
		while(amount>0){
			ItemStack stack = new ItemStack(type);
			stack.setDurability(data);
			int max = Showcase.instance.config.getMaxStackSize(stack.getType());
			if(amount>=max&&max!=-1){
				stack.setAmount(max);
			} else {
				stack.setAmount(amount);
			}
			amount-=stack.getAmount();
			Map<Integer, ItemStack> notFitting = inv.addItem(stack);
			if(notFitting.size()>0){
				amount+=notFitting.get(0).getAmount();
				break;
			}
		}
		return amount;
	}

	public boolean mayCreateHere(Block b){
		if(Showcase.instance.worldguard!=null){
			return Showcase.instance.worldguard.canBuild(getPlayer(), b);
		} else {
			return true;
		}
	}

	public void sendNotification(String title, String text){
		NarrowtuxLib.getNotificationManager().sendNotification(player, title, text);
	}
}
