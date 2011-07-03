package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class ExchangeAssistant extends Assistant {
	ShowcaseItem showcase;
	ExchangeShowcaseExtra extra;
	ShowcasePlayer player;
	public ExchangeAssistant(Player p, ShowcaseItem item) {
		super(p);
		setTitle("Exchange Assistant");
		player = ShowcasePlayer.getPlayer(p);
		showcase = item;
		extra = (ExchangeShowcaseExtra)showcase.getExtra();
		AssistantPage page = new AssistantPage(this){
			{
				setTitle("How many items you want to get?");
				String text = "";
				String itemName = ShowcaseMain.getName(showcase.getMaterial(), showcase.getData());
				String exchangeName = ShowcaseMain.getName(extra.getExchangeType(), extra.getExchangeData());
				text+="Get "+ChatColor.YELLOW+extra.getExchangeRateLeft()+" "+itemName+ChatColor.WHITE;
				text+=" for "+ChatColor.YELLOW+extra.getExchangeRateRight()+" "+exchangeName+"\n";
				int playeramount = player.getAmountOfType(extra.getExchangeType(), extra.getExchangeData());
				text+="You have got "+ChatColor.YELLOW+playeramount+" "+exchangeName+"\n";
				text+="The showcase has got "+ChatColor.YELLOW+extra.getItemAmount()+" "+itemName+"\n";
				text+="Type "+ChatColor.YELLOW+"0 or go away"+ChatColor.WHITE+" to cancel.";
				setText(text);
			}
			
			@Override
			public AssistantAction onPageInput(String text){
				String itemName = ShowcaseMain.getName(showcase.getMaterial(), showcase.getData());
				String exchangeName = ShowcaseMain.getName(extra.getExchangeType(), extra.getExchangeData());
				int amount;
				try{
					amount = Integer.valueOf(text);
				} catch(Exception e){
					amount = 0;
				}
				int playeramount = player.getAmountOfType(extra.getExchangeType(), extra.getExchangeData());
				if(amount<=0){
					return AssistantAction.CANCEL;
				}
				//Check if amount is divisable by the rate
				int left = extra.getExchangeRateLeft();
				int right = extra.getExchangeRateRight();
				if(amount%left!=0){
					repeatCurrentPage();
					sendMessage(formatLine("This amount isn't available."));
					return AssistantAction.CONTINUE;
				}
				int exAmount = amount/left*right;
				if(exAmount>playeramount){
					repeatCurrentPage();
					sendMessage(formatLine("You haven't got enough items for this amount."));
					return AssistantAction.CONTINUE;
				}
				if(amount>extra.getItemAmount()){
					sendMessage(formatLine("This amount isn't available. Enter again."));
					return AssistantAction.SILENT_REPEAT;
				}
				player.addItems(showcase.getMaterial(), showcase.getData(), amount);
				player.remove(extra.getExchangeType(), extra.getExchangeData(), exAmount);
				extra.addExchanges(exAmount);
				extra.addItems(-amount);
				sendMessage(formatLine("You got "+amount+" "+itemName+" for "+exAmount+" "+exchangeName));
				ShowcasePlayer owner = ShowcasePlayer.getPlayer(showcase.getPlayer());
				owner.sendMessage(player.getPlayer().getName()+" got "+amount+" "+itemName+" for "+exAmount+" "+exchangeName);
				return AssistantAction.CONTINUE;
			}
		};
		
		addPage(page);
	}
	
	@Override
	public void onAssistantFinish(){
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Assistant aborted"));
	}

}
