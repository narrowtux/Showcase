package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.AssistantPage;

public class ShowcasePricePage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	
	public ShowcasePricePage(){
		setTitle("Enter the price per item.");
		setText("");
	}
	
	@Override
	public boolean onPageInput(String text){
		double price;
		try{
			price = Double.valueOf(text);
		} catch(Exception e){
			price = -1;
		}
		if(price<0){
			return false;
		}
		assistant.price = price;
		assistant.sendMessage(assistant.formatLine(ChatColor.YELLOW+"You selected a price of "+ChatColor.WHITE+price));
		return true;
	}
	
}
