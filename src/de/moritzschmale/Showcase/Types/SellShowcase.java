package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class SellShowcase implements ShowcaseProvider {

	@Override
	public String getType() {
		return "finitesell";
	}

	@Override
	public String getPermission() {
		return "showcase.sell.finite";
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
		return "Drop some money in the sell showcase, other players can sell their items there and get this money.";
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
		// TODO Auto-generated method stub
		return 0;
	}

}
