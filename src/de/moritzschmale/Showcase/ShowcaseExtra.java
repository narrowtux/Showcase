package de.moritzschmale.showcase;

import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;

public interface ShowcaseExtra {
	public boolean onDestroy(ShowcasePlayer player);
	
	public void onClick(ShowcasePlayer player);
	
	public void onRightClick(ShowcasePlayer player);
	
	public String save();
	
	public void setShowcaseItem(ShowcaseItem item);
}
