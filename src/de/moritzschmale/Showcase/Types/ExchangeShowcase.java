package de.moritzschmale.Showcase.Types;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.narrowtux.Assistant.TextPage;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class ExchangeShowcase implements ShowcaseProvider {

	private Map<ShowcaseCreationAssistant, ShowcaseAmountPage> amountPages = new HashMap<ShowcaseCreationAssistant, ShowcaseAmountPage>();
	private Map<ShowcaseCreationAssistant, ExchangeTypePage> exchangeTypePages = new HashMap<ShowcaseCreationAssistant, ExchangeTypePage>();
	private Map<ShowcaseCreationAssistant, TextPage> ratePages = new HashMap<ShowcaseCreationAssistant, TextPage>();
	@Override
	public String getType() {
		return "exchange";
	}

	@Override
	public String getPermission() {
		return "showcase.exchange";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		//Values:
		//Exchange-type;Exchange-data;buy-amount;exchange-amount
		ExchangeShowcaseExtra extra = new ExchangeShowcaseExtra();
		String [] args = values.split(";");
		if(args.length==6){
			extra.setExchangeType(Material.getMaterial(Integer.valueOf(args[0])));
			extra.setExchangeData(Short.valueOf(args[1]));
			extra.setItemAmount(Integer.valueOf(args[2]));
			extra.setExchangeAmount(Integer.valueOf(args[3]));
			extra.setExchangeRateLeft(Integer.valueOf(args[4]));
			extra.setExchangeRateRight(Integer.valueOf(args[5]));
		} else {
			System.out.println("[Showcase] not enough arguments for exchange");
		}
		return extra;
	}

	@Override
	public String getDescription() {
		return "Get items for other items";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		/*
		 * Wizard:
		 * How many items of (type) you want to sell?
		 * Take the item you want to get for this one in your hand (type ok), or type id:data
		 * Type the exchangerate (x:y)
		 */
		ShowcaseAmountPage amountPage = new ShowcaseAmountPage(assistant);
		ExchangeTypePage typePage = new ExchangeTypePage();
		TextPage ratePage = new TextPage();
		assistant.addPage(amountPage);
		assistant.addPage(typePage);
		assistant.addPage(ratePage);
		ratePage.setTitle("Exchange Rate");
		ratePage.setText("Enter the exchange-rate (left:right)\nExample: 2 gold for 1 diamond = 1:2");
		amountPages.put(assistant, amountPage);
		exchangeTypePages.put(assistant, typePage);
		ratePages.put(assistant, ratePage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub
		return new ExchangeShowcaseExtra();
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForExchangeShop();
	}

}
