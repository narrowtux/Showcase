package de.moritzschmale.Showcase;

import org.bukkit.ChatColor;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantPage;
import com.narrowtux.translation.Translation;

public class ShowcaseTypeSelectionPage extends AssistantPage {
	public ShowcaseCreationAssistant assistant;
	public boolean cancel = false;
	public ShowcaseTypeSelectionPage(ShowcasePlayer player, Assistant assistant){
		super(assistant);
		setTitle(Translation.tr("assistant.creation.select.title"));
		String text = "";
		for(ShowcaseProvider provider:ShowcaseMain.instance.providers.values()){
			if(player.hasPermission(provider.getPermission(), provider.isOpMethod())){
				text+=ChatColor.YELLOW+provider.getType()+ChatColor.WHITE;
				text+=" ("+ChatColor.YELLOW+getPrice(provider.getPriceForCreation(player))+ChatColor.WHITE+"), ";
			}
		}
		if(text.equals("")){
			text = Translation.tr("noShowcasePermission");
			cancel = true;
		} else {
			text = text.substring(0,text.length()-2)+"\n";
			text+=Translation.tr("helpDescription", Translation.tr("helpCommand"));
		}
		setText(text);
	}

	@Override
	public boolean onPageInput(String text){
		if(text.startsWith(Translation.tr("helpCommand"))){
			String args[] = text.split(" ");
			if(args.length>=2){
				String type = args[1];
				if(ShowcaseMain.instance.providers.containsKey(type)){
					ShowcaseProvider provider = ShowcaseMain.instance.providers.get(type);
					String msg = "";
					msg+=provider.getType()+"\n";
					msg+=provider.getDescription();
					sendMessage(msg);
					getAssistant().repeatCurrentPage();
					return true;
				} else {
					sendMessage(Translation.tr("typeNotFound"));
					getAssistant().repeatCurrentPage();
					return true;
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
			return false;
		}
		if(player.hasPermission(type.getPermission(), type.isOpMethod())){
			this.assistant.type = type.getType();
			type.addPagesToCreationWizard((ShowcaseCreationAssistant) getAssistant());
			return true;
		} else {
			player.sendMessage(Translation.tr("permissionsFail"));
			return false;
		}
	}

	private String getPrice(double price){
		if(ShowcaseMain.instance.method!=null)
		{
			return ShowcaseMain.instance.method.format(price);
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
