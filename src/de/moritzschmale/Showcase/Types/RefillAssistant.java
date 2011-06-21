package de.moritzschmale.Showcase.Types;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;
import com.narrowtux.translation.Translation;

import de.moritzschmale.Showcase.ShowcaseItem;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;

public class RefillAssistant extends Assistant {
	public ShowcaseItem showcase;
	public FiniteShowcaseExtra extra;
	public RefillAssistant(Player p, final ShowcaseItem showcase) {
		super(p);
		this.showcase = showcase;
		extra = (FiniteShowcaseExtra)showcase.getExtra();
		
		AssistantPage page = new AssistantPage(this){
			@Override
			public boolean onPageInput(String text){
				int value = 0;
				try{
					value = Integer.valueOf(text);
				} catch(Exception e){
					value = 0;
				}
				if(value==0){
					return false;
				}
				ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
				Material mat = showcase.getMaterial();
				short data = showcase.getData();
				int amountAtPlayer = player.getAmountOfType(mat, data);
				int amountAtShowcase = extra.getItemAmount();
				if(value>amountAtPlayer){
					value = amountAtPlayer;
				}
				if(value<0&&value*-1>amountAtShowcase){
					value = -amountAtShowcase;
				}
				if(value>0&&value<=amountAtPlayer){
					player.remove(mat, data, value);
					extra.setItemAmount(amountAtShowcase+value);
					sendMessage(formatLine(ChatColor.YELLOW.toString()+value+ChatColor.WHITE+" items added."));
				} else if(value<0&&value*-1<=amountAtShowcase){
					player.addItems(mat, data, -value);
					extra.setItemAmount(amountAtShowcase+value);
					sendMessage(formatLine(ChatColor.YELLOW.toString()+(-value)+ChatColor.WHITE+" items removed."));
				}
				updateText();
				addPage(this);
				return true;
			}
		};
		setTitle(Translation.tr("assistant.refill.title"));
		page.setTitle("");
		addPage(page);
		updateText();
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine(Translation.tr("assistant.refill.finish")));
	}

	@Override
	public void onAssistantFinish(){
		sendMessage(formatLine(Translation.tr("assistant.refill.finish")));
	}
	
	public void updateText(){
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getPlayer());
		Material mat = showcase.getMaterial();
		short data = showcase.getData();
		String text = "";
		String itemName = ShowcaseMain.getName(mat, data);
		text = Translation.tr("assistant.refill.body", extra.getItemAmount(), itemName, 
				ShowcaseMain.instance.method.format(extra.getPricePerItem()),
				player.getAmountOfType(mat, data));
		for(AssistantPage page:getPages()){
			page.setText(text);
		}
	}
}
