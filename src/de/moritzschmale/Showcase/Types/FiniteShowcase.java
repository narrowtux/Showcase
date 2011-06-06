package de.moritzschmale.Showcase.Types;

import java.util.HashMap;
import java.util.Map;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class FiniteShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	@Override
	public String getType() {
		return "finite";
	}

	@Override
	public String getPermission() {
		return "showcase.finite";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		String [] args = values.split(";");
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		if(args.length==2){
			extra.setItemAmount(Integer.valueOf(args[0]));
			extra.setPricePerItem(Double.valueOf(args[1]));
		}
		return extra;
	}

	@Override
	public String getDescription() {
		return "You can sell your own items";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage page = new ShowcasePricePage();
		pricePages.put(assistant, page);
		assistant.addPage(page);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		ShowcasePricePage pricePage = pricePages.get(assistant);
		extra.setItemAmount(10);
		extra.setPricePerItem(pricePage.price);
		//TODO: get the correct values from assistant
		//TODO: remove items from players inventory
		return extra;
	}

}
