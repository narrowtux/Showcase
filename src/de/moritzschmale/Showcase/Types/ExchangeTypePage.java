package de.moritzschmale.Showcase.Types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.Showcase.ShowcaseMain;

public class ExchangeTypePage extends AssistantPage {
	public Material type;
	public short data;
	
	public ExchangeTypePage(Assistant assistant){
		super(assistant);
		setTitle("Exchange item");
		setText("Put the item you want to get for exchange and type ok\nOr type the item-name/id");
	}
	
	@Override
	public boolean onPageInput(String text){
		if(text.equals("cancel")){
			return false;
		}
		if(text.equals("ok")){
			ItemStack stack = getAssistant().getPlayer().getItemInHand();
			if(stack!=null)
			{
				type = stack.getType();
				data = stack.getDurability();
			} else {
				getAssistant().repeatCurrentPage();
				sendMessage(getAssistant().formatLine("You don't hold an item in your hand. Try again."));
			}
			return true;
		} else {
			String [] args = text.split(":");
			type = Material.AIR;
			data = 0;
			if(args.length==1){
				try{
					type=Material.getMaterial(args[0].toUpperCase());
				} catch(Exception e){
					try{
						type=Material.getMaterial(Integer.valueOf(args[0]));
					} catch(Exception ex){
						type=Material.AIR;
					}
				}
			}
			if(args.length==2){
				try{
					data = Short.valueOf(args[1]);
				} catch(Exception e){
					data = 0;
				}
			}
			if(type.equals(Material.AIR)){
				getAssistant().repeatCurrentPage();
				sendMessage(getAssistant().formatLine("This item doesn't exist. Repeat your input"));
			} else {
				sendMessage("You selected "+ShowcaseMain.getName(type, data)+".");
			}
			return true;
		}
	}
}
