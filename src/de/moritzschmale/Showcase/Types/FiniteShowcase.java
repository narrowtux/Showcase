package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class FiniteShowcase implements ShowcaseProvider {

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
		// TODO Auto-generated method stub
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		FiniteShowcaseExtra extra = new FiniteShowcaseExtra();
		extra.setItemAmount(10);
		extra.setPricePerItem(10);
		//TODO: get the correct values from assistant
		//TODO: remove items from players inventory
		return extra;
	}

}
