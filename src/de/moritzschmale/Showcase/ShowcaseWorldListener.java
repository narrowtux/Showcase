package de.moritzschmale.Showcase;

import org.bukkit.Material;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class ShowcaseWorldListener extends WorldListener {
	@Override
	public void onChunkLoad(ChunkLoadEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			try{
				if(event.getChunk().equals(item.getBlock().getChunk())){
					item.respawn();
					item.setChunkLoaded(true);
				}
			}catch(Exception e){
			}
		}
	}
	
	@Override
	public void onChunkUnload(ChunkUnloadEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			try{
				if(event.getChunk().equals(item.getBlock().getChunk())){
					item.remove();
					item.setChunkLoaded(false);
				}
			}catch(Exception e){
			}
		}
	}
}
