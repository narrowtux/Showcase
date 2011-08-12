package de.moritzschmale.showcase.types;

import java.util.ArrayList;
import java.util.List;

import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.depot.Depot;

public class ShopShowcaseExtra implements ShowcaseExtra {
	
	private ShowcaseItem showcase;
	private List<Depot> buyOptions = new ArrayList<Depot>();
	private List<Depot> sellOptions = new ArrayList<Depot>();
	private boolean infinite = false;

	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}

}
