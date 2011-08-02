package de.moritzschmale.Showcase;

import info.somethingodd.bukkit.OddItem.OddItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.*;
import com.narrowtux.Main.NarrowtuxLib;
import com.nijikokun.register.payment.Method;
public class ShowcaseCreationAssistant extends Assistant {
	public String type = "";
	public Material material;
	public short data;
	public Location loc;

	public ShowcasePlayer player;
	public Configuration config = ShowcaseMain.instance.config;
	public Method method = NarrowtuxLib.getMethod();
	
	public ShowcaseCreationAssistant(Player p, ItemStack item, Location loc) {
		super(p);

		player = ShowcasePlayer.getPlayer(getPlayer());
		setTitle(ShowcaseMain.tr("assistant.creation.title"));
		ShowcaseTypeSelectionPage typeSelectionPage = new ShowcaseTypeSelectionPage(player, this);
		typeSelectionPage.assistant = this;
		if(item==null)
		{
			addPage(new AssistantPage(this){
				{
					setTitle(ShowcaseMain.tr("creation.item.title"));
					setText(ShowcaseMain.tr("creation.item.text"));
				}
				
				@Override
				public AssistantAction onPageInput(String text){
					ItemStack result = null;
					OddItem odd = (OddItem)Bukkit.getServer().getPluginManager().getPlugin("OddItem");
					try{
						result = odd.getItemStack(text);
					} catch(IllegalArgumentException e){
						ShowcaseCreationAssistant.this.sendMessage(Icon.WARNING, "Showcase", ShowcaseMain.tr("creation.item.notfound", e.getMessage()));
						return AssistantAction.SILENT_REPEAT;
					}
					material = result.getType();
					data = result.getDurability();
					return AssistantAction.CONTINUE;
				}
			});
		} else {
			material = item.getType();
			data = item.getDurability();
		}
		addPage(typeSelectionPage);
		this.loc = loc;
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(Icon.WARNING, "Showcase", ShowcaseMain.tr("assistant.creation.cancel"));
	}
	
	@Override
	public void onAssistantFinish(){
		ShowcaseProvider provider = ShowcaseMain.instance.providers.get(type);
		ShowcaseExtra extra = provider.createShowcase(this);
		if(extra!=null){
			sendMessage(Icon.INFORMATION, "Showcase", ShowcaseMain.tr("assistant.creation.finish"));
			ShowcaseItem item = new ShowcaseItem(loc, material, data, getPlayer().getName(), type);
			ShowcaseMain.instance.showcasedItems.add(item);
			item.setExtra(extra);
			if(method!=null)
			{
				method.getAccount(getPlayer().getName()).subtract(provider.getPriceForCreation(player));
			}
		} else {
			sendMessage(Icon.WARNING, "Showcase", ShowcaseMain.tr("assistant.creation.cancel"));
		}
	}
}
