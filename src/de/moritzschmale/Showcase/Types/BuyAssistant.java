package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;
import com.narrowtux.translation.Translation;
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
		setTitle(Translation.tr("assistant.buy.title"));
		/*
		 Method method = ShowcaseMain.instance.method;
		if(method==null){
			return;
		}
		print+=ChatColor.YELLOW+"This is "+ChatColor.WHITE+showItem.getPlayer()+ChatColor.YELLOW+"'s shop.\n";
		print+=ChatColor.YELLOW+"How many items do you want?\n"+ChatColor.YELLOW+"Type the number in chat, "+ChatColor.WHITE+"0"+ChatColor.YELLOW+" to abort.";
		 */
		AssistantPage page = new AssistantPage(this){
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
		page.setText(Translation.tr("assistant.buy.text"));
		String itemCount = "";
		if(item.getType().equals("finite")){
			itemCount = ChatColor.YELLOW+" ("+ChatColor.WHITE+"x"+getAmount()+ChatColor.YELLOW+")";
		}
		String itemName = ShowcaseMain.getName(item.getMaterial(), item.getData());
		String print = "";
		print+=Translation.tr("assistant.buy.price", itemName+itemCount, method.format(getPrice()));;
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
			String itemName = ShowcaseMain.getName(item.getMaterial(), item.getData());
			String total = ShowcaseMain.instance.method.format(totalamount);
			player.takeMoney(totalamount);
			if(item.getType().equals("finite")){
				FiniteShowcaseExtra extra = (FiniteShowcaseExtra)item.getExtra();
				extra.setItemAmount(getAmount()-amount);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.giveMoney(totalamount);
				owner.sendMessage(Translation.tr("buyNotification", player.getPlayer().getName(), amount, itemName, total));
			}
			String text = "";
			text+=Translation.tr("buyMessage", amount, itemName, total);
			sendMessage(formatLine(text));
			if(getAmount()==0&&item.getType().equals("finite")&&ShowcaseMain.instance.config.isRemoveWhenEmpty()){
				item.remove();
				ShowcaseMain.instance.showcasedItems.remove(item);
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(item.getPlayer());
				owner.sendMessage(Translation.tr("shopSoldOut", itemName));
			}
		} else {
			sendMessage(formatLine(Translation.tr("notEnoughMoney")));
		}
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine(Translation.tr("checkoutCancel")));
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
