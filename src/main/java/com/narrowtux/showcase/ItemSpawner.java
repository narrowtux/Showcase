package com.narrowtux.showcase;

/**
 * This class is used one second after onEnable() to prevent that the items get spawned weirdly.
 * @author tux
 *
 */
public class ItemSpawner extends Object implements Runnable {

	public void run() {
		for(ShowcaseItem item:Showcase.instance.showcasedItems) {
			if(item.getItem() == null && item.isChunkLoaded()) {
				item.respawn();
			}
		}
		Showcase.instance.startup = false;
	}
}
