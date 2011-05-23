package de.moritzschmale.Showcase;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.*;
public class ShowcaseCreationAssistant extends Assistant {
	public ShowcaseType type = ShowcaseType.NONE;
	public double price = 0;
	public int amount = 1;
	public Material material;
	public short data;
	public Location loc;
	public ShowcaseCreationAssistant(Player p, ItemStack item, Location loc) {
		super(p);
		setTitle("Showcase creation assistant");
		ShowcaseTypeSelectionPage page = new ShowcaseTypeSelectionPage();
		page.assistant = this;
		addPage(page);
		material = item.getType();
		data = item.getDurability();
		this.loc = loc;
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Showcase creation cancelled."));
	}
	
	@Override
	public void onAssistantFinish(){
		sendMessage(formatLine("Showcase creation successful."));
		ShowcaseItem item = new ShowcaseItem(loc, material, data, getPlayer().toString(), type, amount, price);
		ShowcaseMain.instance.showcasedItems.add(item);
	}

}
