package de.moritzschmale.Showcase;

public interface ShowcaseExtra {
	public boolean onDestroy(ShowcasePlayer player);
	
	public void onClick(ShowcasePlayer player);
	
	public String save();
}
