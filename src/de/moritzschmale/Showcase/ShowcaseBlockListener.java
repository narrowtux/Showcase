package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class ShowcaseBlockListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(event.getBlock().equals(item.getBlock())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED+"This is "+item.getPlayer()+"'s showcase.");
				break;
			}
			if(event.getBlock().getFace(BlockFace.UP).equals(item.getBlock())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED+"This is below a Showcase. You can't destroy it, because the item would fall down otherwise.");
				break;
			}
		}
		if(event.isCancelled()){
			event.getPlayer().sendBlockChange(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
		}
	}
}
