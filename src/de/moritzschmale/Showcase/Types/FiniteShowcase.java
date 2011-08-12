package de.moritzschmale.showcase.types;

import java.util.HashMap;
import java.util.Map;

import de.moritzschmale.showcase.assistants.ShowcaseAmountPage;
import de.moritzschmale.showcase.assistants.ShowcaseCreationAssistant;
import de.moritzschmale.showcase.assistants.ShowcasePricePage;
import de.moritzschmale.showcase.types.FiniteShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseMain;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.ShowcaseProvider;

public class FiniteShowcase implements ShowcaseProvider {
	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	private Map<ShowcaseCreationAssistant, ShowcaseAmountPage> amountPages = new HashMap<ShowcaseCreationAssistant, ShowcaseAmountPage>();
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
		return ShowcaseMain.tr("types.finite.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage pricePage = new ShowcasePricePage(assistant);
		pricePages.put(assistant, pricePage);
		assistant.addPage(pricePage);
		ShowcaseAmountPage amountPage = new ShowcaseAmountPage(assistant);
		amountPages.put(assistant, amountPage);
		assistant.addPage(amountPage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		ShowcasePricePage pricePage = pricePages.get(assistant);
		pricePages.remove(assistant);
		ShowcaseAmountPage amountPage = amountPages.get(assistant);
		amountPages.remove(assistant);
		extra.setItemAmount(amountPage.amount);
		extra.setPricePerItem(pricePage.price);
		ShowcasePlayer player = assistant.player;
		player.remove(assistant.material, assistant.data, amountPage.amount);
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForFiniteShop();
	}

}
