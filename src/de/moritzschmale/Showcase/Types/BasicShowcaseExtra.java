package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class BasicShowcaseExtra implements ShowcaseExtra {

	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		player.sendMessage("Destroy basic showcase");
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		player.sendMessage("Click basic showcase");

	}

	@Override
	public String save() {
		return "";
	}

}
