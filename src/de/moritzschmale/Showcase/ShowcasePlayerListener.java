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
import org.bukkit.util.Vector;

import com.narrowtux.Assistant.Assistant;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;


public class ShowcasePlayerListener extends PlayerListener {
	
	public Configuration config = null;
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.hasBlock()&&event.getClickedBlock().getType().equals(Material.GLASS)){
			ShowcaseItem showItem = ShowcaseMain.instance.getItemByBlock(event.getClickedBlock());
			ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(!event.getPlayer().isSneaking()){
					return;
				}
				if(event.hasBlock()&&showItem == null){
					if(event.getItem()==null){
						player.sendMessage(ChatColor.RED+"You have got to hold something in your hand!");
						event.setCancelled(true);
						return;
					}
					if(event.getClickedBlock().getType().equals(Material.GLASS)){
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
					Method method = ShowcaseMain.instance.method;
					if(method==null){
						return;
					}
					String itemCount = "";
					if(showItem.getType().equals(ShowcaseType.FINITE_SHOP)){
						itemCount = ChatColor.YELLOW+" ("+ChatColor.WHITE+"x"+showItem.getItemAmount()+ChatColor.YELLOW+")";
					}
					String print = ShowcaseMain.getName(showItem.getMaterial(), showItem.getData())+itemCount;
					print+=ChatColor.YELLOW+" for "+ChatColor.WHITE+method.format(showItem.getPricePerItem())+ChatColor.YELLOW+" each.\n";
					print+=ChatColor.YELLOW+"This is "+ChatColor.WHITE+showItem.getPlayer()+ChatColor.YELLOW+"'s shop.\n";
					print+=ChatColor.YELLOW+"How many items do you want?\n"+ChatColor.YELLOW+"Type the number in chat, "+ChatColor.WHITE+"0"+ChatColor.YELLOW+" to abort.";
					if(showItem.getType().equals(ShowcaseType.FINITE_SHOP)&&showItem.getItemAmount()==0){
						print = ChatColor.YELLOW+"This showcase is currently sold-out.";
					}
					if(showItem.getType().equals(ShowcaseType.FINITE_SHOP)&&showItem.getPlayer().equals(player.getPlayer().getName())){
						print = ChatColor.YELLOW+"This showcase has got "+showItem.getItemAmount()+" "+ShowcaseMain.getName(showItem.getMaterial(), showItem.getData())+". To add some, simply drop them.";
					}
					player.setHasReadPrice(true);
					player.setLastClickedShowcase(showItem);
					player.sendMessage(print);
					//player.getPlayer().sendBlockChange(event.getClickedBlock().getLocation(), Material.STONE_PLATE, (byte) 0);
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
	
	public void addShowcase(Location loc, Material material, short data, Player owner, ShowcaseType type, int amount, double price){
		ShowcaseItem shit = new ShowcaseItem(loc, material, data, owner.getName(), type, amount, price); //Lol, it's ShIt for short :D
		ShowcaseMain.instance.showcasedItems.add(shit);
	}
	
	public void printTypeMenu(Player p){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(p);
		String priceBasic = "";
		String priceFinite = "";
		Method method = ShowcaseMain.instance.method;
		if(method!=null){
			if(config.getPriceForBasic()>0){
				priceBasic = ChatColor.YELLOW+" ("+ChatColor.WHITE+method.format(config.getPriceForBasic())+ChatColor.YELLOW+")";
			}
			if(config.getPriceForFiniteShop()>0){

				priceFinite = ChatColor.YELLOW+" ("+ChatColor.WHITE+method.format(config.getPriceForFiniteShop())+ChatColor.YELLOW+")";
			}
			
		}
		String print = ChatColor.GOLD+"======[ "+ChatColor.YELLOW+"Showcase Type Selection"+ChatColor.GOLD+" ]======\n";
		if(player.hasPermission("showcase.basic", false)){
			print+=ChatColor.YELLOW+"(basic)"+ChatColor.WHITE+" Basic Showcase. Just displays an item"+priceBasic+"\n";
		}
		if(player.hasPermission("showcase.finite", false)){
			print+=ChatColor.YELLOW+"(finite)"+ChatColor.WHITE+" Finite Shop Showcase. You can sell your items there"+priceFinite+"\n";
		}
		if(player.hasPermission("showcase.infinite", true)){
			print+=ChatColor.YELLOW+"(infinite)"+ChatColor.WHITE+" Infinite Shop Showcase. Others can buy unlimited items\n";
		}
		print += ChatColor.GOLD+"======[ "+ChatColor.YELLOW+"Enter Selection"+ChatColor.GOLD+" ]======";
		player.sendMessage(print);
	}
	
	public void printPriceMenu(Player p){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(p);
		ShowcaseType type = player.getRequestedType();
		String print = ChatColor.YELLOW+"You want a "+type.toString().toLowerCase()+" Showcase.\n";
		print+=ChatColor.YELLOW+"Please enter the price per item:";
		player.sendMessage(print);
	}
	
	public void printAmountMenu(Player p){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(p);
		String print = ChatColor.YELLOW+"You want "+player.getRequestedPrice()+ChatColor.YELLOW+" dollars per item.\n";
		print+=ChatColor.YELLOW+"Please enter the desired amount of items:\n";
		print+=ChatColor.YELLOW+"(You have got "+ChatColor.WHITE;
		ItemStack stack = player.getRequestedItem();
		print+=player.getAmountOfType(stack.getType(), stack.getDurability());
		print+=ChatColor.YELLOW+" of "+ChatColor.WHITE+stack.getType()+ChatColor.YELLOW+")\n";
		player.sendMessage(print);
	}
	
	public boolean isSafePlace(Block glass){
		Block below = glass.getFace(BlockFace.DOWN);
		Material nonsafe[] = {
			Material.GLASS,
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
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(player.hasReadPrice()){
			Vector showcase = player.getLastClickedShowcase().getLocation().toVector();
			Vector pl = event.getTo().toVector();
			if(pl.distanceSquared(showcase)>16){
				player.sendMessage("Not interested? Bah, screw it!");
				player.setHasReadPrice(false);
				player.setLastClickedShowcase(null);
			}
		}
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
		}
	}
}
