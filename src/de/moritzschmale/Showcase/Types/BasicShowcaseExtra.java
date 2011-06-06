package de.moritzschmale.Showcase.Types;

import de.moritzschmale.Showcase.ShowcaseExtra;
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
		return "lolthismakesnothing";
	}

}
