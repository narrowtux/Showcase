package com.narrowtux.Utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BlockRestore implements Runnable {
	private Map<Location, ItemStack> blocks = new HashMap<Location, ItemStack>();
	
	public BlockRestore(Map<Location, ItemStack> b){
		blocks = b;
	}
	
	@Override
	public void run() {
		for(Location loc:blocks.keySet()){
			ItemStack type = blocks.get(loc);
			Block b = loc.getBlock();
			b.setType(type.getType());
			b.setData((byte) type.getDurability());
		}
	}

}
