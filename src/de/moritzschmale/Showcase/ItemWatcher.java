package de.moritzschmale.Showcase;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;


public class ItemWatcher implements Runnable {

	@Override
	public void run() {
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(item.getItem()==null||item.getItem().isDead()){
				item.respawn();
			}
			item.updatePosition();
			
			/*
			if(item.getMaterial().equals(Material.TORCH)){
				//try to make it glow
				int x,y,z;
				x = item.getBlock().getX();
				y = item.getBlock().getY();
				z = item.getBlock().getZ();
				CraftWorld world = (CraftWorld)item.getBlock().getWorld();
				world.getHandle().b(EnumSkyBlock.BLOCK, x, y, z, 14);
			}*/
			//Doesn't work.
			/*CraftItem citem = (CraftItem)item.getItem();
			EntityItem eitem = (EntityItem) citem.getHandle();
			eitem.b = 0;*/ //This is a bad idea, the client won't display the items nicely then.
		}
	}

}
