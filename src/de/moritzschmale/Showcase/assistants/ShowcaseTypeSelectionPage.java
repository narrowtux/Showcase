package de.moritzschmale.showcase.assistants;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;
import com.narrowtux.Main.NarrowtuxLib;

import de.moritzschmale.showcase.ShowcaseMain;
import de.moritzschmale.showcase.ShowcasePlayer;
import de.moritzschmale.showcase.ShowcaseProvider;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public boolean cancel = false;
	public ShowcaseTypeSelectionPage(ShowcasePlayer player, Assistant assistant){
		super(assistant);
		setTitle(ShowcaseMain.tr("assistant.creation.select.title"));
		String text = "";
		for(ShowcaseProvider provider:ShowcaseMain.instance.providers.values()){
			if(player.hasPermission(provider.getPermission(), provider.isOpMethod())){
				text+=ChatColor.YELLOW+provider.getType()+ChatColor.WHITE;
				text+=" ("+ChatColor.YELLOW+getPrice(provider.getPriceForCreation(player))+ChatColor.WHITE+"), ";
			}
		}
		if(text.equals("")){
			text = ShowcaseMain.tr("noShowcasePermission");
			cancel = true;
		} else {
			text = text.substring(0,text.length()-2)+"\n";
			text+=ShowcaseMain.tr("helpDescription", ShowcaseMain.tr("helpCommand"));
		}
		setText(text);
	}

	@Override
	public AssistantAction onPageInput(String text){
		if(text.startsWith(ShowcaseMain.tr("helpCommand"))){
			String args[] = text.split(" ");
			if(args.length>=2){
				String type = args[1];
				if(ShowcaseMain.instance.providers.containsKey(type)){
					ShowcaseProvider provider = ShowcaseMain.instance.providers.get(type);
					String msg = "";
					msg+=provider.getType()+"\n";
					msg+=provider.getDescription();
					sendMessage(msg);
					return AssistantAction.REPEAT;
				} else {
					sendMessage(ShowcaseMain.tr("typeNotFound"));
					return AssistantAction.SILENT_REPEAT;
				}
			}
		}
		ShowcasePlayer player = ShowcasePlayer.getPlayer(getAssistant().getPlayer());
		text = text.toLowerCase();
		ShowcaseProvider type = null;
		for(ShowcaseProvider provider:ShowcaseMain.instance.providers.values()){
			if(text.equals(provider.getType())){
				//Selected this type
				type = provider;
			}
		}
		if(type==null)
		{
			return AssistantAction.CANCEL;
		}
		if(!player.canAfford(type.getPriceForCreation(player))){
			sendMessage(getAssistant().formatLine(ShowcaseMain.tr("notEnoughMoneyForShowcase")));
			return AssistantAction.CANCEL;
		}
		if(player.hasPermission(type.getPermission(), type.isOpMethod())){
			this.assistant.type = type.getType();
			type.addPagesToCreationWizard((ShowcaseCreationAssistant) getAssistant());
			return AssistantAction.CONTINUE;
		} else {
			player.sendMessage(ShowcaseMain.tr("permissionsFail"));
			return AssistantAction.CANCEL;
		}
	}

	private String getPrice(double price){
		if(NarrowtuxLib.getMethod()!=null)
		{
			return NarrowtuxLib.getMethod().format(price);
		} else {
			return price+" $";
		}
	}
	
	@Override
	public void play(){
		super.play();
		if(cancel)
			getAssistant().cancel();
	}
}
