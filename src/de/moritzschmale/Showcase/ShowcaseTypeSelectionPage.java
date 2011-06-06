package de.moritzschmale.Showcase;

import com.narrowtux.Assistant.AssistantPage;
import com.nijikokun.register.payment.Method;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public ShowcaseTypeSelectionPage(ShowcasePlayer player){
		setTitle("Select Showcase Type");
		String text = "";
		for(ShowcaseProvider provider:ShowcaseMain.instance.providers.values()){
			if(player.hasPermission(provider.getPermission(), provider.isOpMethod())){
				text+=provider.getType()+": "+provider.getDescription()+"\n";
			}
		}
		setText(text);
	}
	@Override
	public boolean onPageInput(String text){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getAssistant().getPlayer());
		text = text.toLowerCase();
		ShowcaseProvider type = null;
		for(ShowcaseProvider provider:ShowcaseMain.instance.providers.values()){
			if(text.equals(provider.getType())){
				//Selected this type
				type = provider;
			}
		}
		if(type==null)
		{
			return false;
		}
		if(player.hasPermission(type.getPermission(), type.isOpMethod())){
			this.assistant.type = type.getType();
			type.addPagesToCreationWizard((ShowcaseCreationAssistant) getAssistant());
			return true;
		} else {
			player.sendMessage("You don't have sufficient permissions.");
			return false;
		}
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
