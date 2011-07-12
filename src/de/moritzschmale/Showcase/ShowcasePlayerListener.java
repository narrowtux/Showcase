package de.moritzschmale.Showcase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

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
					if(showItem!=null)
					{
						showItem.getExtra().onRightClick(player);
						return;
					} else {
						return;
					}
				}
				if(event.getPlayer().getLocation().getBlock().getFace(BlockFace.DOWN).getTypeId()==0){
					return;
				}
				if(event.hasBlock()&&showItem == null&&player.mayCreateHere(event.getClickedBlock())){
					if(event.getItem()==null){
						player.sendMessage(ShowcaseMain.tr("noItemError"));
						event.setCancelled(true);
						return;
					}
					if(event.getClickedBlock().getType().equals(Material.STEP)){
						event.setCancelled(true);
						if(ShowcaseMain.instance.providers.size()==1&&ShowcaseMain.instance.providers.containsKey("basic")){
							Location loc = event.getClickedBlock().getLocation();
							Material mat = event.getItem().getType();
							short data = event.getItem().getDurability();
							ShowcaseMain.instance.showcasedItems.add(new ShowcaseItem(loc, mat, data, event.getPlayer().getName(), "basic"));
							event.getPlayer().sendMessage(ShowcaseMain.tr("basicCreationSuccess"));
						} else {
							ShowcaseCreationAssistant assistant = new ShowcaseCreationAssistant(event.getPlayer(), event.getItem(), event.getClickedBlock().getLocation());
							assistant.start();
						}
					}
				} else if(showItem!=null){
					if(showItem.getPlayer().equals(event.getPlayer().getName())||player.hasPermission("showcase.admin", true)){
						if(showItem.getExtra()==null){
							showItem.remove();
							ShowcaseMain.instance.showcasedItems.remove(showItem);
							event.getPlayer().sendMessage(ShowcaseMain.tr("showcaseRemoveSuccess"));
							System.out.println("null Extra");
							return;
						}
						if(showItem.getExtra().onDestroy(player)){
							showItem.remove();
							ShowcaseMain.instance.showcasedItems.remove(showItem);
							event.getPlayer().sendMessage(ShowcaseMain.tr("showcaseRemoveSuccess"));
						}
					} else {
						event.getPlayer().sendMessage(ShowcaseMain.tr("showcaseOwner", showItem.getPlayer()));
					}
					event.setCancelled(true);
				}
			}
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				if(showItem!=null){
					if(showItem.getExtra()!=null)
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
