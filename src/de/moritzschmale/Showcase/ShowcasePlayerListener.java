package de.moritzschmale.Showcase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.Assistant;


public class ShowcasePlayerListener extends PlayerListener {
	
	public Configuration config = null;
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.hasBlock()&&event.getClickedBlock().getType().equals(Material.STEP)){
			ShowcaseItem showItem = ShowcaseMain.instance.getItemByBlock(event.getClickedBlock());
			ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(!event.getPlayer().isSneaking()){
					return;
				}
				if(event.getPlayer().getLocation().getBlock().getFace(BlockFace.DOWN).getTypeId()==0){
					return;
				}
				if(event.hasBlock()&&showItem == null&&player.mayCreateHere(event.getClickedBlock())){
					if(event.getItem()==null){
						player.sendMessage(ChatColor.RED+"You have got to hold something in your hand!");
						event.setCancelled(true);
						return;
					}
					if(event.getClickedBlock().getType().equals(Material.STEP)){
						event.setCancelled(true);
						ShowcaseCreationAssistant assistant = new ShowcaseCreationAssistant(event.getPlayer(), event.getItem(), event.getClickedBlock().getLocation());
						assistant.start();
					}
				} else if(showItem!=null){
					if(showItem.getPlayer().equals(event.getPlayer().getName())||player.hasPermission("showcase.admin", true)){
						if(showItem.getExtra().onDestroy(player)){
							showItem.remove();
							ShowcaseMain.instance.showcasedItems.remove(showItem);
							event.getPlayer().sendMessage(ChatColor.RED+"Removed Showcased item.");
						}
					} else {
						event.getPlayer().sendMessage(ChatColor.RED+"This is "+showItem.getPlayer()+"'s Showcase!");
					}
					event.setCancelled(true);
				}
			}
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				if(showItem!=null){
					showItem.getExtra().onClick(player);
				}
			}
		}
	}
	
	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		ShowcaseItem shit = ShowcaseMain.instance.getItemByDrop(event.getItem());
		if(shit!=null)
		{
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event){
		//Cool, not?
		Assistant.onPlayerChat(event);
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event){
		Assistant.onPlayerMove(event);
	}
}
