package de.moritzschmale.showcase.Types;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.showcase.ShowcaseMain;

public class ShowcasePricePage extends AssistantPage {
	public double price;
	
	public ShowcasePricePage(Assistant assistant){
		super(assistant);
		setTitle(ShowcaseMain.tr("assistant.price.title"));
		setText("");
	}
	
	@Override
	public AssistantAction onPageInput(String text){
		try{
			price = Double.valueOf(text);
		} catch(Exception e){
			price = -1;
		}
		if(price<0){
			return AssistantAction.CANCEL;
		}
		getAssistant().sendMessage(getAssistant().formatLine(ShowcaseMain.tr("assistant.price.done", price)));
		return AssistantAction.CONTINUE;
	}
	
}
