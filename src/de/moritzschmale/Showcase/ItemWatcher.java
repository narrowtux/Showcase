package de.moritzschmale.Showcase;


public class ItemWatcher implements Runnable {

	@Override
	public void run() {
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
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
