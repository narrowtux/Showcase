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
				if(event.hasBlock()&&showItem == null&&player.mayCreateHere(event.getClickedBlock())){
					if(event.getItem()==null){
						player.sendMessage(ChatColor.RED+"You have got to hold something in your hand!");
						event.setCancelled(true);
						return;
					}
					if(event.getClickedBlock().getType().equals(Material.STEP)){
						event.setCancelled(true);
						if(!isSafePlace(event.getClickedBlock())){
							player.sendMessage(ChatColor.RED+"This is not a safe place for your item. It will fall down.");
							return;
						}
						player.setHasReadPrice(false);
						if(config.isBasicMode()||(player.hasPermission("showcase.basic", false)&&!player.hasPermission("showcase.infinite", true)&&!player.hasPermission("showcase.finite", false))){
							Location loc = event.getClickedBlock().getLocation();
							Material mat = event.getItem().getType();
							short data = event.getItem().getDurability();
							addShowcase(loc, mat, data, player.getPlayer(), ShowcaseType.BASIC, 1, 0);
							player.sendMessage(ChatColor.GREEN+ShowcaseMain.getName(event.getItem().getType(), event.getItem().getDurability())+" showcased.");
							player.resetDialog();
						} else {
							ShowcaseCreationAssistant assistant = new ShowcaseCreationAssistant(event.getPlayer(), event.getItem(), event.getClickedBlock().getLocation());
							assistant.start();
						}
					}
				} else if(showItem!=null){
					if(showItem.getPlayer().equals(event.getPlayer().getName())||player.hasPermission("showcase.admin", true)||!config.isShowcaseProtection()){
						showItem.giveItemsBack();
						showItem.remove();
						ShowcaseMain.instance.showcasedItems.remove(showItem);
						event.getPlayer().sendMessage(ChatColor.RED+"Removed Showcased item.");
					} else {
						event.getPlayer().sendMessage(ChatColor.RED+"This is "+showItem.getPlayer()+"'s Showcase!");
					}
					event.setCancelled(true);
				}
			}
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				if(showItem!=null&&showItem.getType().toString().contains("SHOP")){
					player.sendMessage(showItem.getItem().getLocation().toString());
					if(showItem.getPlayer().equals(event.getPlayer().getName())&&showItem.getType().equals(ShowcaseType.FINITE_SHOP)){
						RefillAssistant assistant = new RefillAssistant(event.getPlayer(), showItem);
						assistant.setAssistantStartLocation(showItem.getLocation());
						assistant.start();
					} else if(!(showItem.getItemAmount()==0&&showItem.getType().equals(ShowcaseType.FINITE_SHOP))){
						BuyAssistant assistant = new BuyAssistant(event.getPlayer(), showItem);
						assistant.setAssistantStartLocation(showItem.getLocation());
						assistant.start();
					} else {
						player.sendMessage("This shop is sold-out!");
					}
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
		
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(player.hasReadPrice()&&event.getMessage().toLowerCase().contains("cancel")){
			player.sendMessage("Cancelled");
			player.setHasReadPrice(false);
			event.setCancelled(true);
		}
	}
	
	public void addShowcase(Location loc, Material material, short data, Player owner, ShowcaseType type, int amount, double price){
		ShowcaseItem shit = new ShowcaseItem(loc, material, data, owner.getName(), type, amount, price); //Lol, it's ShIt for short :D
		ShowcaseMain.instance.showcasedItems.add(shit);
	}
	
	public boolean isSafePlace(Block glass){
		Block below = glass.getFace(BlockFace.DOWN);
		Material nonsafe[] = {
			Material.AIR,
			Material.RED_MUSHROOM,
			Material.RED_ROSE,
			Material.REDSTONE_WIRE,
			Material.REDSTONE_TORCH_ON,
			Material.REDSTONE_TORCH_OFF,
			Material.YELLOW_FLOWER,
			Material.TORCH,
			Material.DIODE_BLOCK_ON,
			Material.DIODE_BLOCK_OFF,
			Material.WOOD_DOOR,
			Material.IRON_DOOR,
			Material.WOODEN_DOOR,
			Material.IRON_DOOR_BLOCK,
			Material.WOOD_PLATE,
			Material.STONE_PLATE,
			Material.STONE_BUTTON,
			Material.DOUBLE_STEP,
			Material.WATER,
			Material.LAVA,
			Material.STATIONARY_LAVA,
			Material.STATIONARY_WATER,
			Material.WOOD_STAIRS,
			Material.COBBLESTONE_STAIRS,
			Material.STEP,
			Material.SIGN_POST,
			Material.WALL_SIGN,
			Material.FENCE,
			Material.CACTUS,
			Material.LEVER,
			Material.LADDER,
			Material.RAILS,
			Material.POWERED_RAIL,
			Material.DETECTOR_RAIL,
			Material.SAPLING,
			Material.BED_BLOCK,
			Material.WEB,
			Material.BROWN_MUSHROOM,
			Material.FIRE,
			Material.MOB_SPAWNER,
			Material.SEEDS,
			Material.SOIL,
			Material.SNOW,
			Material.SUGAR_CANE_BLOCK,
			Material.PORTAL,
			Material.CAKE_BLOCK,
			Material.SAND,
			Material.GRAVEL,
		};
		for(Material t:nonsafe){
			if(t.equals(below.getType())){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event){
		Assistant.onPlayerMove(event);
	}

	public void onPlayerDropItem(PlayerDropItemEvent event){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(player.hasReadPrice()&&player.getLastClickedShowcase().getType().equals(ShowcaseType.FINITE_SHOP)){
			Item item = event.getItemDrop();
			ShowcaseItem showcase = player.getLastClickedShowcase();
			if(!showcase.getPlayer().equals(event.getPlayer().getName())){
				return;
			}
			ItemStack stack = item.getItemStack();
			if(stack.getType().equals(showcase.getMaterial())&&stack.getDurability()==showcase.getData()){
				showcase.setItemAmount(showcase.getItemAmount()+stack.getAmount());
				player.sendMessage("Added "+stack.getAmount()+" "+ShowcaseMain.getName(stack.getType(), stack.getDurability())+" to the showcase.");
				item.remove();
			}
			player.setHasReadPrice(false);
		}
	}
}
