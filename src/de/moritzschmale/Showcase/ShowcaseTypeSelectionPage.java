package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.AssistantPage;
import com.nijikokun.register.payment.Method;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public ShowcaseTypeSelectionPage(ShowcasePlayer player){
		setTitle("Select Showcase Type");
		String text = "";
		if(player.hasPermission("showcase.basic", false)){
			text+="basic: Basic showcase ("+getPrice(ShowcaseMain.instance.config.getPriceForBasic())+")\n";
		}
		if(player.hasPermission("showcase.finite", false)){
			text+="finite: Shop showcase with finite ressources ("+getPrice(ShowcaseMain.instance.config.getPriceForFiniteShop())+")\n";
		}
		if(player.hasPermission("showcase.infinite", true)){
			text+="infinite: Shop showcase with infinite ressources";
		}
		setText(text);
	}
	@Override
	public boolean onPageInput(String text){
		text = text.toLowerCase();
		ShowcaseType type = ShowcaseType.NONE;
		Method method = assistant.method;
		if(text.contains("basic")){
			type = ShowcaseType.BASIC;
			if(!assistant.player.hasPermission("showcase.basic", false)){
				sendMessage("You haven't got the permission to create this showcase.");
				return false;
			}
			if(method!=null)
			{
				if(!method.getAccount(assistant.getPlayer().getName()).hasEnough(assistant.config.getPriceForBasic())){
					sendMessage("You haven't got enough money.");
					return false;
				}
			}
		}else if(text.contains("infinite")){
			type = ShowcaseType.INFINITE_SHOP;
			if(!assistant.player.hasPermission("showcase.infinite", true)){
				sendMessage("You haven't got the permission to create this showcase.");
				return false;
			}
		}else if(text.contains("finite")){
			type = ShowcaseType.FINITE_SHOP;
			if(!assistant.player.hasPermission("showcase.finite", false)){
				sendMessage("You haven't got the permission to create this showcase.");
				return false;
			}
			if(method!=null)
			{
				if(!method.getAccount(assistant.getPlayer().getName()).hasEnough(assistant.config.getPriceForFiniteShop())){
					sendMessage("You haven't got enough money.");
					return false;
				}
			}
		}
		if(type.equals(ShowcaseType.NONE)){
			return false;
		}
		assistant.sendMessage(assistant.formatLine(type.toString().toLowerCase()+" selected."));
		assistant.type = type;
		if(type.equals(ShowcaseType.FINITE_SHOP)||type.equals(ShowcaseType.INFINITE_SHOP)){
			ShowcasePricePage page = new ShowcasePricePage();
			page.assistant = assistant;
			assistant.addPage(page);
		}
		if(type.equals(ShowcaseType.FINITE_SHOP)){
			ShowcaseAmountPage page = new ShowcaseAmountPage(assistant);
			assistant.addPage(page);
		}
		return true;
	}
	
	private String getPrice(double price){
		if(ShowcaseMain.instance.method!=null)
		{
			return ShowcaseMain.instance.method.format(price);
		} else {
			return price+" $";
		}
	}
}
