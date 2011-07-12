package de.moritzschmale.Showcase;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class ShowcaseBlockListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event){
		if(!event.getBlock().getType().equals(Material.STEP)){
			//nothing to do for us.
			return;
		}
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(event.getBlock().equals(item.getBlock())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ShowcaseMain.tr("showcaseOwner", item.getPlayer()));
				break;
			}
		}
		if(event.isCancelled()){
			event.getPlayer().sendBlockChange(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
		}
	}
	
	@Override
	public void onBlockPhysics(BlockPhysicsEvent event){
		if(ShowcaseMain.instance.getItemByBlock(event.getBlock())!=null){
			if(event.getChangedType().equals(Material.STEP)){
				event.setCancelled(true);
			}
		}
	}
}
