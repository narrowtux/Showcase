package de.moritzschmale.Showcase;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ShowcasePlayer {
	private String player;
	private static Map<String,ShowcasePlayer> instances = new HashMap<String, ShowcasePlayer>();
	private ShowcasePlayer(String player){
		this.player = player;
	}
	
	public static ShowcasePlayer getPlayer(String name){
		if(instances.containsKey(name)){
			return instances.get(name);
		} else {
			ShowcasePlayer player = new ShowcasePlayer(name);
			instances.put(name, player);
			return player;
		}
	}
	
	public static ShowcasePlayer getPlayer(Player player){
		return getPlayer(player.getName());
	}
	
	public Player getPlayer(){
		return ShowcaseMain.instance.getServer().getPlayer(player);
	}
}
