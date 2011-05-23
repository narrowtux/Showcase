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
							printTypeMenu(event.getPlayer());
							player.setDialogState(1);
							player.setRequestedItem(event.getItem().clone());
							player.setRequestedBlock(event.getClickedBlock());
						}
					}
				} else if(showItem!=null){
					showItem.openInventory(event.getPlayer());
					/*
					if(showItem.getPlayer().equals(event.getPlayer().getName())||player.hasPermission("showcase.admin", true)||!config.isShowcaseProtection()){
						showItem.giveItemsBack();
						showItem.remove();
						ShowcaseMain.instance.showcasedItems.remove(showItem);
						event.getPlayer().sendMessage(ChatColor.RED+"Removed Showcased item.");
					} else {
						event.getPlayer().sendMessage(ChatColor.RED+"This is "+showItem.getPlayer()+"'s Showcase!");
					}*/
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
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(player.hasReadPrice()){
			ShowcaseItem show = player.getLastClickedShowcase();
			//player.getPlayer().sendBlockChange(show.getLocation(), Material.GLASS, (byte) 0);
			event.setCancelled(true);
			int amount = 0;
			try{
				amount = Integer.valueOf(event.getMessage());
			}catch(Exception e){
				amount = 0;
			}
			if(amount<0){
				amount = 0;
			}
			if(amount==0){
				player.sendMessage("Aborting checkout.");
				player.setHasReadPrice(false);
				return;
			}
			double price = show.getPricePerItem();
			if(show.getType().equals(ShowcaseType.FINITE_SHOP)){
				if(amount>show.getItemAmount()){
					amount = show.getItemAmount();
				}
			}
			double priceToPay = (double)amount * price;
			MethodAccount account = player.getAccount();
			Method method = ShowcaseMain.instance.method;
			if(account==null)
			{
				player.sendMessage("You don't have got an account, ask your admin to give you one.");
				player.setHasReadPrice(false);
				return;
			}
			if(account.hasEnough(priceToPay)){
				player.sendMessage("Bought "+amount+" "+ShowcaseMain.getName(show.getMaterial(), show.getData())+" for "+method.format(priceToPay));
				int remaining = player.addItems(show.getMaterial(), show.getData(), amount);
				if(remaining>0){
					player.sendMessage("They didn't fit completely into your inventory. Selling you as much as I can.");
					priceToPay = (double)(amount-remaining)*show.getPricePerItem();
					amount = amount-remaining;
				}
				account.subtract(priceToPay);
				if(show.getType().equals(ShowcaseType.FINITE_SHOP)){
					show.setItemAmount(show.getItemAmount()-amount);
					ShowcasePlayer owner = ShowcasePlayer.getPlayer(show.getPlayer());
					owner.giveMoney(priceToPay);
					owner.sendMessage(player.getPlayer().getName()+" bought "+amount+" "+ShowcaseMain.getName(show.getMaterial(), show.getData())+" for a total of "+method.format(priceToPay)+".");
				}
			} else {
				player.sendMessage("You don't have enough money for "+amount+" items.");
			}
			player.setHasReadPrice(false);
			player.setLastClickedShowcase(null);
			return;
		}
		if(player.getDialogState()>=1){
			event.setCancelled(true);
			player.sendMessage(event.getMessage());
			if(player.getDialogState()==1){
				ShowcaseType type = ShowcaseType.NONE;
				String message = event.getMessage().toLowerCase();
				if(message.equals("basic")){
					type = ShowcaseType.BASIC;
					if(!player.takeMoney(config.getPriceForBasic())){
						player.resetDialog();
						player.sendMessage("You can't afford this showcase.");
						return;
					}
				} else if(message.equals("infinite")){
					type = ShowcaseType.INFINITE_SHOP;
				} else if(message.equals("finite")){
					type = ShowcaseType.FINITE_SHOP;
					if(!player.takeMoney(config.getPriceForFiniteShop())){
						player.resetDialog();
						player.sendMessage("You can't afford this showcase.");
						return;
					}
				}
				if(type.equals(ShowcaseType.NONE)){
					player.sendMessage("Invalid answer. Aborting.");
					player.resetDialog();
					return;
				}
				if(message.equals("basic")&&!player.hasPermission("showcase.basic", false)
						|| message.equals("infinite")&&!player.hasPermission("showcase.infinite", true)
						|| message.equals("finite")&&!player.hasPermission("showcase.finite", false)){
					player.sendMessage("Insufficient permissions. Aborting.");
					player.resetDialog();
					return;
				}
				player.setRequestedType(type);
				player.setDialogState(2);
				if(type.equals(ShowcaseType.BASIC)){
					Location loc = player.getRequestedBlock().getLocation();
					Material mat = player.getRequestedItem().getType();
					short data = player.getRequestedItem().getDurability();
					addShowcase(loc, mat, data, player.getPlayer(), type, 1, 0);
					player.sendMessage(ChatColor.GREEN+"Item "+mat+" showcased.");
					player.resetDialog();
				} else {
					printPriceMenu(event.getPlayer());
				}
			} else if(player.getDialogState()==2){
				double price = 0;
				try{
					price = Double.valueOf(event.getMessage());
				} catch(Exception e){
					price = 0;
				}
				player.setRequestedPrice(price);
				player.setDialogState(3);
				if(player.getRequestedType().equals(ShowcaseType.INFINITE_SHOP)){
					Location loc = player.getRequestedBlock().getLocation();
					Material mat = player.getRequestedItem().getType();
					short data = player.getRequestedItem().getDurability();
					addShowcase(loc, mat, data, event.getPlayer(), ShowcaseType.INFINITE_SHOP, -1, price);
					player.sendMessage(ChatColor.GREEN+"Setup of Infinite Showcase successful.");
					player.resetDialog();
				} else {
					printAmountMenu(event.getPlayer());
				}
			} else if(player.getDialogState()==3){
				int amount = 0;
				try{
					amount = Integer.valueOf(event.getMessage());
				} catch(Exception e){
					amount = 0;
				}
				ItemStack stack = player.getRequestedItem();
				if(amount>player.getAmountOfType(stack.getType(), stack.getDurability())){
					amount = player.getAmountOfType(stack.getType(), stack.getDurability());
				}
				if(amount<=0){
					amount = 1;
				}
				Location loc = player.getRequestedBlock().getLocation();
				Material mat = player.getRequestedItem().getType();
				short data = player.getRequestedItem().getDurability();
				addShowcase(loc, mat, data, player.getPlayer(), player.getRequestedType(), amount, player.getRequestedPrice());
				player.sendMessage(ChatColor.GREEN+"Finite Shop Showcase setup successful.");
				player.remove(mat, data, amount);
				player.resetDialog();
			}
		}
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
