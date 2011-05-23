package de.moritzschmale.Showcase;

import com.narrowtux.Assistant.AssistantPage;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public ShowcaseTypeSelectionPage(){
		setTitle("Select Showcase Type");
		setText("basic - Basic showcase\nfinite - Shop showcase with finite ressources\ninfinite - Shop showcase with infinite ressources");
	}
	@Override
	public boolean onPageInput(String text){
		text = text.toLowerCase();
		ShowcaseType type = ShowcaseType.NONE;
		if(text.contains("basic")){
			type = ShowcaseType.BASIC;
		}
		if(text.contains("finite")){
			type = ShowcaseType.FINITE_SHOP;
		}
		if(text.contains("infinite")){
			type = ShowcaseType.INFINITE_SHOP;
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
			ShowcaseAmountPage page = new ShowcaseAmountPage();
			page.assistant = assistant;
			assistant.addPage(page);
		}
		return true;
	}
}
