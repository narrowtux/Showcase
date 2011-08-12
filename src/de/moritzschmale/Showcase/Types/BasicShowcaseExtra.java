package de.moritzschmale.showcase.types;

import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;

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
