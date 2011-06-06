package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseProvider;

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
		return "Just displays the item.";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		return new BasicShowcaseExtra();
	}
}
