package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;
import com.nijikokun.register.payment.Method;

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
				if(item.getItemAmount()<amount&&item.getType().equals(ShowcaseType.FINITE_SHOP)){
					amount = item.getItemAmount();
				}
				return true;
			}
		};
		Method method = ShowcaseMain.instance.method;
		page.setText("Enter the number of items you want.");
		String itemCount = "";
		if(item.getType().equals(ShowcaseType.FINITE_SHOP)){
			itemCount = ChatColor.YELLOW+" ("+ChatColor.WHITE+"x"+item.getItemAmount()+ChatColor.YELLOW+")";
		}
		String print = ShowcaseMain.getName(item.getMaterial(), item.getData())+itemCount;
		print+=ChatColor.YELLOW+" for "+ChatColor.WHITE+method.format(item.getPricePerItem())+ChatColor.YELLOW+" each.";
		page.setTitle(print);
		addPage(page);
	}
	
	@Override
	public void onAssistantFinish(){
		//Buy the actual items...
		double totalamount = amount*item.getPricePerItem();
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
		if(player.canAfford(totalamount)){
			int remaining = player.addItems(item.getMaterial(), item.getData(), amount);
			if(remaining>0){
				totalamount = (amount-remaining)*item.getPricePerItem();
				amount = amount-remaining;
			}
			item.setItemAmount(item.getItemAmount()-amount);
			player.takeMoney(totalamount);
			if(item.getType().equals(ShowcaseType.FINITE_SHOP)){
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.giveMoney(totalamount);
				owner.sendMessage(player.getPlayer().getName()+" bought "+amount+" "+ShowcaseMain.getName(item.getMaterial(), item.getData())+" for a total of "+ShowcaseMain.instance.method.format(totalamount));
			}
			String text = "";
			text+=ChatColor.YELLOW+"You bought "+ChatColor.WHITE+amount;
			text+=ChatColor.YELLOW+" items for a total of "+ChatColor.WHITE+ShowcaseMain.instance.method.format(totalamount);
			sendMessage(formatLine(text));
		} else {
			sendMessage(formatLine("You can't afford so much items."));
		}
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Checkout cancelled."));
	}
}
