package de.moritzschmale.Showcase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
	private boolean showcaseProtection = true;
	private double priceForBasic = 0.0;
	private double priceForFiniteShop = 0.0;
	private double priceForExchangeShop = 0.0;
	private int maximumPerUser = 0;
	private boolean removeWhenEmpty = false;
	private FlatFileReader reader;
	private boolean basicMode;
	private List<String> disabledTypes = new ArrayList<String>();
	private String locale;
	/**
	 * @return the showcaseProtection
	 */
	public boolean isShowcaseProtection() {
		return showcaseProtection;
	}

	/**
	 * @return the priceForBasic
	 */
	public double getPriceForBasic() {
		return priceForBasic;
	}

	/**
	 * @return the priceForFiniteShop
	 */
	public double getPriceForFiniteShop() {
		return priceForFiniteShop;
	}
	
	public double getPriceForExchangeShop(){
		return priceForExchangeShop;
	}
	
	/**
	 * @return the maximumPerUser
	 */
	public int getMaximumPerUser() {
		return maximumPerUser;
	}
	public Configuration(){
		File pluginFolder = ShowcaseMain.instance.getDataFolder();
		reader = new FlatFileReader(new File(pluginFolder.getAbsolutePath()+"/showcase.cfg"), false);
		load();
		reader.write();
	}
	
	public void load(){
		disabledTypes.clear();
		showcaseProtection = reader.getBoolean("showcaseprotection", true);
		priceForBasic = reader.getDouble("priceforbasic", 0);
		priceForFiniteShop = reader.getDouble("priceforfinite", 0);
		basicMode = reader.getBoolean("basicmode", false);
		priceForExchangeShop = reader.getDouble("priceforexchange", 0);
		removeWhenEmpty = reader.getBoolean("removewhenempty", false);
		locale = reader.getString("locale", "en-US");
		String list = reader.getString("disabled", "");
		String items[] = list.split(",");
		for(String item:items){
			disabledTypes.add(item);
		}
	}

	/**
	 * @return the removeWhenEmpty
	 */
	public boolean isRemoveWhenEmpty() {
		return removeWhenEmpty;
	}

	/**
	 * @return the basicMode
	 */
	public boolean isBasicMode() {
		return basicMode;
	}
	
	public boolean isTypeEnabled(String type){
		return !disabledTypes.contains(type);
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
}
