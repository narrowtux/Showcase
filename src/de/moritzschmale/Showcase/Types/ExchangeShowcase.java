package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class ExchangeShowcase implements ShowcaseProvider {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Get items for other items";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForExchangeShop();
	}

}
