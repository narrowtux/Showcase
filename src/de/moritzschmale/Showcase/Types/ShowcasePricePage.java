package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;

public class ShowcasePricePage extends AssistantPage {
	public double price;
	
	public ShowcasePricePage(Assistant assistant){
		super(assistant);
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
		getAssistant().sendMessage(getAssistant().formatLine(ChatColor.YELLOW+"You selected a price of "+ChatColor.WHITE+price));
		return true;
	}
	
}
