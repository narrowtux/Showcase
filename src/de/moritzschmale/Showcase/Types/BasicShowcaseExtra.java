package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class BasicShowcaseExtra implements ShowcaseExtra {

	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {

	}

	@Override
	public String save() {
		return "-";
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
	}
}
