package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;

public class ShowcasePricePage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public double price;
	
	public ShowcasePricePage(){
		setTitle("Enter the price per item.");
		setText("");
	}
	
	@Override
	public boolean onPageInput(String text){
		try{
			price = Double.valueOf(text);
		} catch(Exception e){
			price = -1;
		}
		if(price<0){
			return false;
		}
		assistant.sendMessage(assistant.formatLine(ChatColor.YELLOW+"You selected a price of "+ChatColor.WHITE+price));
		return true;
	}
	
}
