package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.AssistantPage;

public class ShowcaseAmountPage extends AssistantPage {
public ShowcaseCreationAssistant assistant;
	
	public ShowcaseAmountPage(){
		setTitle("Enter the amount of items.");
		setText("");
	}
	
	@Override
	public boolean onPageInput(String text){
		int amount;
		try{
			amount = Integer.valueOf(text);
		} catch(Exception e){
			amount = -1;
		}
		if(amount<0){
			return false;
		}
		assistant.amount = amount;
		assistant.sendMessage(assistant.formatLine(ChatColor.YELLOW+"You put in "+ChatColor.WHITE+amount+ChatColor.YELLOW+" items."));
		return true;
	}
}
