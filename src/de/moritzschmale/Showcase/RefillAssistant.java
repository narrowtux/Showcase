package de.moritzschmale.Showcase;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;

public class RefillAssistant extends Assistant {
	public ShowcaseItem showcase;
	public RefillAssistant(Player p, final ShowcaseItem showcase) {
		super(p);
		this.showcase = showcase;
		AssistantPage page = new AssistantPage(){
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
				int amountAtShowcase = showcase.getItemAmount();
				if(value>amountAtPlayer){
					value = amountAtPlayer;
				}
				if(value<0&&value*-1>amountAtShowcase){
					value = -amountAtShowcase;
				}
				if(value>0&&value<=amountAtPlayer){
					player.remove(mat, data, value);
					showcase.setItemAmount(amountAtShowcase+value);
					sendMessage(formatLine(value+" items added."));
				} else if(value<0&&value*-1<=amountAtShowcase){
					player.addItems(mat, data, -value);
					showcase.setItemAmount(amountAtShowcase+value);
					sendMessage(formatLine(-value+" items removed."));
				}
				addPage(this);
				return true;
			}
		};
		setTitle("Refill Assistant");
		String text = "";
		text+=showcase.getItemAmount()+" of "+ShowcaseMain.getName(showcase.getMaterial(), showcase.getData())+" in showcase.\n";
		text+="Type in a positive amount (e.g. 10) to add items.\n";
		text+="Type in a negative amount (e.g. -5) to remove items.\n";
		text+="Type in 0 or go away to leave the assistant.";
		addPage(page);
	}
	
	@Override
	public void onAssistantCancel(){
		sendMessage(formatLine("Refill done."));
	}

	@Override
	public void onAssistantFinish(){
		sendMessage(formatLine("Refill done."));
	}
}
