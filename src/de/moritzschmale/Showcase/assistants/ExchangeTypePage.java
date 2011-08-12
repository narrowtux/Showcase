package de.moritzschmale.showcase.assistants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;

import de.moritzschmale.showcase.ShowcaseMain;

public class ExchangeTypePage extends AssistantPage {
	public Material type;
	public short data;
	
	public ExchangeTypePage(Assistant assistant){
		super(assistant);
		setTitle(ShowcaseMain.tr("assistant.exchange.create.type.title"));
		setText(ShowcaseMain.tr("assistant.exchange.create.type.text"));
	}
	
	@Override
	public AssistantAction onPageInput(String text){
		if(text.equals("cancel")){
			return AssistantAction.CANCEL;
		}
		if(text.equals("ok")){
			ItemStack stack = getAssistant().getPlayer().getItemInHand();
			if(stack!=null)
			{
				type = stack.getType();
				data = stack.getDurability();
			} else {
				sendMessage(getAssistant().formatLine(ShowcaseMain.tr("noItemError")));
				return AssistantAction.SILENT_REPEAT;
			}
			return AssistantAction.CONTINUE;
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
			if(type==null||type.equals(Material.AIR)){
				getAssistant().repeatCurrentPage();
				sendMessage(getAssistant().formatLine(ShowcaseMain.tr("itemExistError")));
			} else {
				sendMessage(ShowcaseMain.tr("assistant.exchange.create.type.selected", ShowcaseMain.getName(type, data)));
			}
			return AssistantAction.CONTINUE;
		}
	}
}
