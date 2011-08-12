package de.moritzschmale.showcase.Types;

import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.showcase.ShowcaseCreationAssistant;
import de.moritzschmale.showcase.ShowcaseMain;

public class ShowcaseAmountPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public int maximumamount = 0;
	public int amount = 0;
		
	public ShowcaseAmountPage(ShowcaseCreationAssistant a){
		super(a);
		assistant = a;
		maximumamount = assistant.player.getAmountOfType(assistant.material, assistant.data);
		String title = ShowcaseMain.tr("assistant.amount.title", maximumamount, ShowcaseMain.getName(assistant.material, assistant.data));
		setTitle(title);
		setText("");
	}
	
	@Override
	public AssistantAction onPageInput(String text){
		maximumamount = assistant.player.getAmountOfType(assistant.material, assistant.data);
		try{
			amount = Integer.valueOf(text);
		} catch(Exception e){
			amount = -1;
		}
		if(amount<=0){
			return AssistantAction.CANCEL;
		}
		if(amount>maximumamount){
			amount = maximumamount;
		}
		assistant.sendMessage(assistant.formatLine(ShowcaseMain.tr("assistant.amount.add", amount)));
		return AssistantAction.CONTINUE;
	}
}
