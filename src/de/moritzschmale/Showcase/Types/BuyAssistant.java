package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;
import com.nijikokun.register.payment.Method;

import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class BuyAssistant extends Assistant {
	public int amount;
	public ShowcaseItem item;
	public BuyAssistant(Player p, final ShowcaseItem item) {
		super(p);
		this.item = item;
		setTitle("Buy assistant");
		/*
		 Method method = ShowcaseMain.instance.method;
		if(method==null){
			return;
		}
		print+=ChatColor.YELLOW+"This is "+ChatColor.WHITE+showItem.getPlayer()+ChatColor.YELLOW+"'s shop.\n";
		print+=ChatColor.YELLOW+"How many items do you want?\n"+ChatColor.YELLOW+"Type the number in chat, "+ChatColor.WHITE+"0"+ChatColor.YELLOW+" to abort.";
		 */
		AssistantPage page = new AssistantPage(){
			@Override
			public boolean onPageInput(String text){
				try{
					amount = Integer.valueOf(text);
				} catch(Exception e){
					amount = -1;
				}
				if(amount<=0){
					return false;
				}
				
				if(getAmount()<amount&&item.getType().equals("finite")){
					amount = getAmount();
				}
				return true;
			}
		};
		Method method = ShowcaseMain.instance.method;
		page.setText("Enter the number of items you want.");
		String itemCount = "";
		if(item.getType().equals("finite")){
			itemCount = ChatColor.YELLOW+" ("+ChatColor.WHITE+"x"+getAmount()+ChatColor.YELLOW+")";
		}
		String print = ShowcaseMain.getName(item.getMaterial(), item.getData())+itemCount;
		print+=ChatColor.YELLOW+" for "+ChatColor.WHITE+method.format(getPrice())+ChatColor.YELLOW+" each.";
		page.setTitle(print);
		addPage(page);
	}
	
	@Override
	public void onAssistantFinish(){
		//Buy the actual items...
		double totalamount = amount*getPrice();
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
		if(player.canAfford(totalamount)){
			int remaining = player.addItems(item.getMaterial(), item.getData(), amount);
			if(remaining>0){
				totalamount = (amount-remaining)*getPrice();
				amount = amount-remaining;
			}
			player.takeMoney(totalamount);
			if(item.getType().equals("finite")){
				FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
				extra.setItemAmount(getAmount()-amount);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.giveMoney(totalamount);
				owner.sendMessage(player.getPlayer().getName()+" bought "+amount+" "+ShowcaseMain.getName(item.getMaterial(), item.getData())+" for a total of "+ShowcaseMain.instance.method.format(totalamount));
			}
			String text = "";
			text+=ChatColor.YELLOW+"You bought "+ChatColor.WHITE+amount;
			text+=ChatColor.YELLOW+" items for a total of "+ChatColor.WHITE+ShowcaseMain.instance.method.format(totalamount);
			sendMessage(formatLine(text));
			if(getAmount()==0&&item.getType().equals("finite")&&ShowcaseMain.instance.config.isRemoveWhenEmpty()){
				item.remove();
				ShowcaseMain.instance.showcasedItems.remove(item);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.sendMessage("Your shop with "+ShowcaseMain.getName(item.getMaterial(), item.getData())+" has been sold out and removed.");
			}
		} else {
			sendMessage(formatLine("You can't afford so much items."));
		}
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Checkout cancelled."));
	}
	
	public int getAmount(){
		if(item.getType().equals("finite")){
			FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
			return extra.getItemAmount();
		} else {
			return Integer.MAX_VALUE;
		}
	}
	
	public double getPrice(){
		if(item.getType().equals("finite")){
			FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
			return extra.getPricePerItem();
		} else if(item.getType().equals("infinite")) {
			InfiniteShowcaseExtra extra = (InfiniteShowcaseExtra)item.getExtra();
			return extra.getPrice();
		} else {
			return 0;
		}
	}
}
