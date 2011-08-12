package de.moritzschmale.showcase.Types;


import de.moritzschmale.showcase.ShowcaseExtra;
import de.moritzschmale.showcase.ShowcaseItem;
import de.moritzschmale.showcase.ShowcasePlayer;

public class TutorialShowcaseExtra implements ShowcaseExtra {

	private String text = "I've got no text";
	@SuppressWarnings("unused")
	private ShowcaseItem showcase = null;
	
	@Override
	public boolean onDestroy(ShowcasePlayer player) {
		return true;
	}

	@Override
	public void onClick(ShowcasePlayer player) {
		player.sendMessage(text);
	}

	@Override
	public String save() {
		return text.replace("\n", "\\n").replace(",", ";;");
	}

	@Override
	public void setShowcaseItem(ShowcaseItem item) {
		showcase = item;
	}
	
	public void setText(String text){
		this.text = text.replace("\\n", "\n").replace(";;", ",");
	}
	
	public String getText(){
		return text;
	}

	@Override
	public void onRightClick(ShowcasePlayer player) {
		
	}
}
