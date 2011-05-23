package com.narrowtux.Assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class Assistant {
	private List<AssistantPage> pages = new ArrayList<AssistantPage>();
	private String title = "Generic assistant";
	private AssistantPage currentPage = null;
	private int currentPageIndex = 0;
	private Player player = null;
	private String heldBackChat = "";
	private static Map<Player, Assistant> instances = new HashMap<Player, Assistant>();
	
	public Assistant(Player p){
		setPlayer(p);
	}
	
	public static boolean onPlayerChat(PlayerChatEvent event){
		//Dispatch the chat to the right assistant
		Assistant current = null;
		for(Player p:instances.keySet()){
			event.getRecipients().remove(p);
			if(!event.getPlayer().equals(p)){
				instances.get(p).heldBackChat+= event.getFormat()+"\n";
			}
		}
		if(instances.containsKey(event.getPlayer())){
			current = instances.get(event.getPlayer());
		} else {
			return false;
		}
		//Handle the input, send it to the right page
		String text = event.getMessage();
		if(current.getCurrentPage()!=null)
		{
			event.setCancelled(true);
			if(!current.getCurrentPage().onPageInput(text)){
				current.cancel();
				return false;
			} else {
				current.currentPageIndex++;
				if(current.pages.size()>current.currentPageIndex){
					current.currentPage = current.pages.get(current.currentPageIndex);
					current.currentPage.play();
				} else {
					current.stop();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(AssistantPage currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the currentPage
	 */
	public AssistantPage getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the pages
	 */
	public List<AssistantPage> getPages(){
		return pages;
	}
	
	public void addPage(AssistantPage page){
		pages.add(page);
		page.setAssistant(this);
		if(pages.size()==1){
			currentPage = page;
		}
	}
	
	/*
	 * Assistant events
	 */
	
	public void onAssistantCancel(){
		getPlayer().sendMessage(ChatColor.YELLOW+"Assistant cancelled.");
	}
	
	public void onAssistantFinish(){
		//TODO: some default implementation here.
	}
	
	/*
	 * Assistant actions
	 */
	
	public void start(){
		instances.put(getPlayer(),this);
		String message = getSeparator()+"\n";
		message += formatLine(getTitle())+"\n";
		message += getSeparator();
		sendMessage(message);
		currentPage.play();
	}
	
	public void cancel(){
		onAssistantCancel();
		sendMessage(getSeparator());
		remove();
	}
	
	public void stop(){
		onAssistantFinish();
		sendMessage(getSeparator());
		remove();
	}
	
	private void remove(){
		instances.remove(getPlayer());
		if(!heldBackChat.equals("")){
			sendMessage(ChatColor.YELLOW+"This is the chat held back for you:");
			sendMessage(heldBackChat);
		}
	}
	/*
	 * Misc actions
	 */
	
	public void sendMessage(String text){
		for(String line:text.split("\n")){
			getPlayer().sendMessage(line);
		}
	}
	
	public String getSeparator(){
		return ChatColor.LIGHT_PURPLE+"|---------------------------------------------------|";
	}
	
	public String formatLine(String line){
		return ChatColor.LIGHT_PURPLE+"| "+ChatColor.WHITE+line;
	}
}
