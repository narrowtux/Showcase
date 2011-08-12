package de.moritzschmale.showcase.types;

import de.moritzschmale.showcase.types.BasicShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseCreationAssistant;
import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseMain;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.ShowcaseProvider;

public class BasicShowcase implements ShowcaseProvider {

	@Override
	public String getType() {
		return "basic";
	}

	@Override
	public String getPermission() {
		return "showcase.basic";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		return new BasicShowcaseExtra();
	}

	@Override
	public String getDescription() {
		return ShowcaseMain.tr("types.basic.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		return new BasicShowcaseExtra();
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForBasic();
	}
}
