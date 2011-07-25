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
		SellShowcaseExtra extra = new SellShowcaseExtra();
		String args [] = values.split(";");
		if(args.length>=3){
			extra.setAmountLeft(Integer.valueOf(args[0]));
			extra.setPricePerItem(Double.valueOf(args[1]));
			extra.setAmountOfItems(Integer.valueOf(args[2]));
			return extra;
		}
		return null;
	}

	@Override
	public String getDescription() {
		//TODO: actually use translation
		return "Drop some money in the sell showcase, other players can sell their items there and get this money.";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		// TODO Add some pages here!
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		// TODO Use the pages!
		return new SellShowcaseExtra();
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Generate Config value
		return 0;
	}

}
