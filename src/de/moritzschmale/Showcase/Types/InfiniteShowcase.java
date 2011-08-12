package de.moritzschmale.showcase.Types;

import java.util.HashMap;
import java.util.Map;


import de.moritzschmale.showcase.Types.InfiniteShowcaseExtra;
import de.moritzschmale.showcase.Types.ShowcasePricePage;
import de.moritzschmale.showcase.ShowcaseCreationAssistant;
import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseMain;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.ShowcaseProvider;

public class InfiniteShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	@Override
	public String getType() {
		return "infinite";
	}

	@Override
	public String getPermission() {
		return "showcase.infinite";
	}

	@Override
	public boolean isOpMethod() {
		return true;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		double price = Double.valueOf(values);
		InfiniteShowcaseExtra extra = new InfiniteShowcaseExtra();
		extra.setPrice(price);
		return extra;
	}

	@Override
	public String getDescription() {
		return ShowcaseMain.tr("types.infinite.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = new ShowcasePricePage(assistant);
		assistant.addPage(pricePage);
		pricePages.put(assistant, pricePage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = pricePages.get(assistant);
		InfiniteShowcaseExtra extra = new InfiniteShowcaseExtra();
		extra.setPrice(pricePage.price);
		pricePages.remove(assistant);
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return 0;
	}

}
