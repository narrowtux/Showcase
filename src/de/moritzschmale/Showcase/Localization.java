package de.moritzschmale.Showcase;

import java.util.HashMap;
import java.util.Map;

public class Localization {
	public static Localization instance = null;
	private Map<String, Locale> locales = new HashMap<String, Locale>();
	private String currentLang;
	public Localization(String lang){
		currentLang = lang;
		load();
		instance = this;
	}
	
	private void load() {
		// TODO Auto-generated method stub
		
	}

	public static String get(String string, String ...args){
		if(instance.locales.containsKey(instance.currentLang)){
			Locale loc = instance.locales.get(instance.currentLang);
			if(loc.strings.containsKey(string)){
				String translation = loc.strings.get(string);
				for(int i = 0; i<args.length;i++){
					translation = translation.replace("%"+i, args[i]);
				}
				return translation;
			}
		}
		return string+" [TRANSLATE_ME]";
	}
}
