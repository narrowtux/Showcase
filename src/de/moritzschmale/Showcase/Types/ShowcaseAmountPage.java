package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseMain;

public class ShowcaseAmountPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public int maximumamount = 0;
	public int amount = 0;
		
	public ShowcaseAmountPage(ShowcaseCreationAssistant a){
		assistant = a;
		maximumamount = assistant.player.getAmountOfType(assistant.material, assistant.data);
		String title = "";
		title+="Enter the amount of items.";
		title+=" You have got "+maximumamount+" of "+ShowcaseMain.getName(assistant.material, assistant.data)+".";
		setTitle(title);
		setText("");
	}
	
	@Override
	public boolean onPageInput(String text){
		maximumamount = assistant.player.getAmountOfType(assistant.material, assistant.data);
		try{
			amount = Integer.valueOf(text);
		} catch(Exception e){
			amount = -1;
		}
		if(amount<=0){
			return false;
		}
		if(amount>maximumamount){
			amount = maximumamount;
		}
		assistant.sendMessage(assistant.formatLine(ChatColor.YELLOW+"You put in "+ChatColor.WHITE+amount+ChatColor.YELLOW+" items."));
		return true;
	}
}
