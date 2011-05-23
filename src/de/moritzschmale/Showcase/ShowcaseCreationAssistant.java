package de.moritzschmale.Showcase;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.*;
import com.nijikokun.register.payment.Method;
public class ShowcaseCreationAssistant extends Assistant {
	public ShowcaseType type = ShowcaseType.NONE;
	public double price = 0;
	public int amount = 1;
	public Material material;
	public short data;
	public Location loc;

	public ShowcasePlayer player;
	public Configuration config = ShowcaseMain.instance.config;
	public Method method = ShowcaseMain.instance.method;
	
	public ShowcaseCreationAssistant(Player p, ItemStack item, Location loc) {
		super(p);

		player = ShowcasePlayer.getPlayer(getPlayer());
		setTitle("Showcase creation assistant");
		ShowcaseTypeSelectionPage page = new ShowcaseTypeSelectionPage(player);
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
		ShowcaseItem item = new ShowcaseItem(loc, material, data, getPlayer().getName(), type, amount, price);
		ShowcaseMain.instance.showcasedItems.add(item);
		if(method!=null)
		{
			if(type.equals(ShowcaseType.BASIC)){
				method.getAccount(getPlayer().getName()).subtract(config.getPriceForBasic());
			}
			if(type.equals(ShowcaseType.FINITE_SHOP)){
				method.getAccount(getPlayer().getName()).subtract(config.getPriceForFiniteShop());
			}
		}
		if(type.equals(ShowcaseType.FINITE_SHOP)){
			player.remove(material, data, amount);
		}
	}

}
