package de.moritzschmale.showcase;

import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.assistants.ShowcaseCreationAssistant;

public interface ShowcaseProvider {
	public String getType();
	
	public String getPermission();
	
	public boolean isOpMethod();
	
	public ShowcaseExtra loadShowcase(String values);
	
	public String getDescription();
	
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant);
	
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant);
	
	public double getPriceForCreation(ShowcasePlayer player);
}
