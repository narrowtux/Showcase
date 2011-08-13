package de.moritzschmale.showcase.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item {
	private int type;
	private short data = 0;
	private int amount = 1;
	
	public Item(int type, short data, int amount){
		this.type = type;
		this.data = data;
		this.amount = amount;
	}
	
	public Item(int type){
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public short getData() {
		return data;
	}

	public void setData(short data) {
		this.data = data;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public ItemStack toItemStack(){
		return new ItemStack(type, amount, data);
	}
	
	public Material getMaterial(){
		return Material.getMaterial(type);
	}
}
