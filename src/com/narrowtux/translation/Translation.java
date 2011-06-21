package com.narrowtux.translation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public class Translation {
	private static Map<String, String> translations = new HashMap<String, String>();
	private static File file;
	private static String version;
	
	public static void reload(File file){
		Translation.file = file;
		translations.clear();
		load();
	}

	private static void load() {
		FlatFileReader reader = new FlatFileReader(file, true);
		for(String key:reader.keys()){
			if(!key.equals("version")){
				String trans = parseColors(reader.getString(key, "No translation for '"+key+"' found!"));
				trans = trans.replaceAll("\\\\n", "\n");
				translations.put(key, trans);
			}
		}
		version = reader.getString("version", "0.0");
	}
	
	public static String tr(String key, Object... args){
		if(!translations.containsKey(key)){
			return "Can't find key "+key+" for translation!";
		} else {
			String trans = translations.get(key);
			return String.format(trans, args);
		}
	}
	
	public static String tr(String key){
		if(!translations.containsKey(key)){
			return "Can't find key "+key+" for translation!";
		} else {
			String trans = translations.get(key);
			return trans;
		}
	}
	
	public static String parseColors(String str) {
		//Method written by Afforess
		for (ChatColor color : ChatColor.values()) {
			String name = "\\[" + color.name().toUpperCase() + "]";
			str = str.replaceAll(name, color.toString());
		}
		return str;
	}
	
	public static int getVersion(){
		String args[] = version.split(".");
		int result = 0;
		for(int i = 0; i<args.length; i++)
		{
			try{
				int v = Integer.valueOf(args[args.length-i-1]);
				result+=v*Math.pow(i, 10);
			} catch(Exception e){
			}
		}
		System.out.println(version+" "+result);
		return result;
	}
}
