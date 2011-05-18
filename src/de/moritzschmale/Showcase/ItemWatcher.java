package de.moritzschmale.Showcase;

import net.minecraft.server.EntityItem;

import org.bukkit.craftbukkit.entity.CraftItem;

public class ItemWatcher implements Runnable {

	@Override
	public void run() {
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(item.getItem().isDead()){
				item.respawn();
			}
			item.updatePosition();
			CraftItem citem = (CraftItem)item.getItem();
			EntityItem eitem = (EntityItem) citem.getHandle();
			eitem.b = 0;
		}
	}

}
