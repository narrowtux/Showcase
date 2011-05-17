package de.moritzschmale.Showcase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;


public class ShowcasePlayerListener extends PlayerListener {
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(!event.getPlayer().isSneaking()){
				return;
			}
			if(!ShowcaseMain.hasPermission(event.getPlayer(), "showcase.basic", false)){
				return;
			}
			ShowcaseItem showItem = ShowcaseMain.instance.getItemByBlock(event.getClickedBlock());
			if(event.hasBlock()&&event.hasItem()&&showItem == null){
				if(event.getClickedBlock().getType().equals(Material.GLASS)){
					/*
					Location spawnLoc = event.getClickedBlock().getLocation();
					ItemStack stack = event.getItem();
					addShowcase(spawnLoc, stack.getType(), stack.getDurability(), event.getPlayer(), ShowcaseType.BASIC, 1, 0);
					event.getPlayer().sendMessage(ChatColor.GREEN+"Item "+stack.getType()+" showcased!");
					*/
					printTypeMenu(event.getPlayer());
					player.setDialogState(1);
					player.setRequestedItem(event.getItem().clone());
					player.setRequestedBlock(event.getClickedBlock());
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
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		Item item = event.getItem();
		for(ShowcaseItem compare:ShowcaseMain.instance.showcasedItems){
			if(compare.getItem().equals(item)){
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
		if(player.getDialogState()>=1){
			event.setCancelled(true);
			player.sendMessage(event.getMessage());
			if(player.getDialogState()==1){
				ShowcaseType type = ShowcaseType.NONE;
				String message = event.getMessage().toLowerCase();
				if(message.equals("basic")){
					type = ShowcaseType.BASIC;
				} else if(message.equals("infinite")){
					type = ShowcaseType.INFINITE_SHOP;
				} else if(message.equals("finite")){
					type = ShowcaseType.FINITE_SHOP;
				}
				if(type.equals(ShowcaseType.NONE)){
					player.sendMessage("Invalid answer. Aborting.");
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
					addShowcase(loc, mat, data, event.getPlayer(), player.getRequestedType(), 0, price);
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
			}
		}
	}
	
	public void addShowcase(Location loc, Material material, short data, Player owner, ShowcaseType type, int amount, double price){
		ItemStack stack = new ItemStack(material,1,data);
		Item item = loc.getWorld().dropItemNaturally(loc, stack);
		ShowcaseItem shit = new ShowcaseItem(item, loc, owner.getName()); //Lol, it's ShIt for short :D
		ShowcaseMain.instance.showcasedItems.add(shit);
		shit.setItemAmount(amount);
		shit.setPricePerItem(price);
		shit.setType(type);
	}
	
	public void printTypeMenu(Player p){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(p);
		String print = ChatColor.GOLD+"======[ "+ChatColor.YELLOW+"Showcase Type Selection"+ChatColor.GOLD+" ]======\n";
		if(player.hasPermission("showcase.basic", false)){
			print+=ChatColor.YELLOW+"(basic)"+ChatColor.WHITE+" Basic Showcase. Just displays an item\n";
		}
		if(player.hasPermission("showcase.finite", false)){
			print+=ChatColor.YELLOW+"(finite)"+ChatColor.WHITE+" Fininite Shop Showcase. You can sell your items there\n";
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
		ShowcaseType type = player.getRequestedType();
		String print = ChatColor.YELLOW+"You want "+player.getRequestedPrice()+ChatColor.YELLOW+" dollars per item.\n";
		print+=ChatColor.YELLOW+"Please enter the desired amount of items:\n";
		print+=ChatColor.YELLOW+"(You have got "+ChatColor.WHITE;
		ItemStack stack = player.getRequestedItem();
		print+=player.getAmountOfType(stack.getType(), stack.getDurability());
		print+=ChatColor.YELLOW+" of "+ChatColor.WHITE+stack.getType()+ChatColor.YELLOW+")\n";
		player.sendMessage(print);
	}
}
