package de.moritzschmale.Showcase;

public interface ShowcaseProvider {
	public String getType();
	
	public String getPermission();
	
	public boolean isOpMethod();
	
	public ShowcaseExtra loadShowcase(String values);
	
	public String getDescription();
	
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant);
	
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant);
}
