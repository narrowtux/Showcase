package de.moritzschmale.Showcase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;


public class ShowcasePlayerListener extends PlayerListener {
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(!event.getPlayer().isSneaking()){
				return;
			}
			ShowcaseItem showItem = ShowcaseMain.instance.getItemByBlock(event.getClickedBlock());
			if(event.hasBlock()&&event.hasItem()&&showItem == null){
				if(event.getClickedBlock().getType().equals(Material.GLASS)){
					Location spawnLoc = event.getClickedBlock().getLocation();
					ItemStack stack = new ItemStack(event.getItem().getType());
					Item item = spawnLoc.getWorld().dropItemNaturally(spawnLoc, stack);
					event.getPlayer().sendMessage(ChatColor.GREEN+"Item "+stack.getType()+" showcased");
					ShowcaseMain.instance.showcasedItems.add(new ShowcaseItem(item, spawnLoc, event.getPlayer().getName()));
					event.setCancelled(true);
				}
			} else if(showItem!=null){
				if(showItem.getPlayer().equals(event.getPlayer().getName())){
					showItem.remove();
					ShowcaseMain.instance.showcasedItems.remove(showItem);
					event.getPlayer().sendMessage(ChatColor.RED+"Removed Showcased item.");
				} else {
					event.getPlayer().sendMessage(ChatColor.RED+"This is "+showItem.getPlayer()+"'s Showcase!");
				}
				event.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		Item item = event.getItem();
		for(ShowcaseItem compare:ShowcaseMain.instance.showcasedItems){
			if(compare.getItem().equals(item)){
				event.setCancelled(true);
				return;
			}
		}
	}
}
