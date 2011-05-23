package de.moritzschmale.Showcase;

import java.io.File;

import org.bukkit.Material;

public class Configuration {
	private boolean showcaseProtection = true;
	private double priceForBasic = 0.0;
	private double priceForFiniteShop = 0.0;
	private int maximumPerUser = 0;
	private FlatFileReader reader;
	private boolean basicMode;
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
		showcaseProtection = reader.getBoolean("showcaseprotection", true);
		priceForBasic = reader.getDouble("priceforbasic", 0);
		priceForFiniteShop = reader.getDouble("priceforfinite", 0);
		basicMode = reader.getBoolean("basicmode", false);
	}

	/**
	 * @return the basicMode
	 */
	public boolean isBasicMode() {
		return basicMode;
	}
}
