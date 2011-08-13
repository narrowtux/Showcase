package de.moritzschmale.showcase.types;

import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.ShowcaseProvider;
import de.moritzschmale.showcase.assistants.ShowcaseCreationAssistant;

public class ShopShowcase implements ShowcaseProvider {

	@Override
	public String getType() {
		return "shop";
	}

	@Override
	public String getPermission() {
		return "showcase.shop";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return "Buy, Sell and Exchange in one!";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		return new ShopShowcaseExtra();
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Auto-generated method stub
		return 0;
	}

}