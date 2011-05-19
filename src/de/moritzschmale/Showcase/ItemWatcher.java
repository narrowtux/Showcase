package de.moritzschmale.Showcase;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;


public class ItemWatcher implements Runnable {

	@Override
	public void run() {
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			Chunk c = item.getBlock().getChunk();
			if(item.getItem().getWorld().isChunkLoaded(c)){
				if(!item.isChunkLoaded()){
					//Scan chunk for eventually dropped items
					for(Entity e:c.getEntities()){
						if(e instanceof Item){
							if(e.getLocation().getBlock().equals(item.getBlock())){
								item.setItem((Item)e);
								System.out.println("Restored item from unloaded chunk.");
							}
						}
					}
				}
				item.setChunkLoaded(true);
			} else {
				item.setChunkLoaded(false);
			}
			if(item.getItem().isDead()){
				item.respawn();
			}
			item.updatePosition();
			/*
			if(item.getItem().getItemStack().getType().equals(Material.TORCH)){
				//try to make it glow
				int x, y, z;
				Chunk c = item.getBlock().getChunk();
				x = item.getBlock().getX()-c.getX()*16;
				z = item.getBlock().getZ()-c.getZ()*16;
				y = item.getBlock().getY();
				((CraftChunk)c).getHandle().g.a(x, y, z, 15);
			}*/ //Doesn't work.
			/*CraftItem citem = (CraftItem)item.getItem();
			EntityItem eitem = (EntityItem) citem.getHandle();
			eitem.b = 0;*/ //This is a bad idea, the client won't display the items nicely then.
		}
	}

}
