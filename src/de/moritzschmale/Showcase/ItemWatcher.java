package de.moritzschmale.Showcase;

public class ItemWatcher implements Runnable {

	@Override
	public void run() {
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(item.getItem().isDead()){
				item.respawn();
			}
			item.updatePosition();
		}
	}

}
