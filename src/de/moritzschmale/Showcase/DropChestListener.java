package de.moritzschmale.Showcase;
import com.narrowtux.DropChest.API.DropChestSuckEvent;



public class DropChestListener extends
		com.narrowtux.DropChest.API.DropChestListener {
	@Override
	public void onDropChestSuck(DropChestSuckEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			if(item.getItem().equals(event.getItem())){
				event.setCancelled(true);
				return;
			}
		}
	}
}
