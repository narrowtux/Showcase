/*
 * Copyright (C) 2011 Moritz Schmale <narrow.m@gmail.com>
 *
 * Showcase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */

package com.narrowtux.showcase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.narrowtux.narrowtuxlib.utils.FileUtils;

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
	private int autoSaveInterval;
	private boolean showAutosaveNotification = false;
	private String locale;
	private Map<Material, Integer> maxStackSize = new HashMap<Material, Integer>();
	private boolean useSpout = false;
	private boolean basicUsesItem = false;

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

	public double getPriceForExchangeShop() {
		return priceForExchangeShop;
	}

	/**
	 * @return the maximumPerUser
	 */
	public int getMaximumPerUser() {
		return maximumPerUser;
	}

	public Configuration() {
		File pluginFolder = Showcase.instance.getDataFolder();
		reader = new FlatFileReader(new File(pluginFolder.getAbsolutePath() + "/showcase.cfg"), false);
		load();
		reader.write();
	}

	public void load() {
		disabledTypes.clear();
		showcaseProtection = reader.getBoolean("showcaseprotection", true);
		priceForBasic = reader.getDouble("priceforbasic", 0);
		priceForFiniteShop = reader.getDouble("priceforfinite", 0);
		basicMode = reader.getBoolean("basicmode", false);
		priceForExchangeShop = reader.getDouble("priceforexchange", 0);
		removeWhenEmpty = reader.getBoolean("removewhenempty", false);
		locale = reader.getString("locale", "en-US");
		autoSaveInterval = reader.getInteger("autosaveinterval", 60);
		showAutosaveNotification = reader.getBoolean("autosavenotification", false);
		useSpout = reader.getBoolean("usespout", false);
		basicUsesItem = reader.getBoolean("basicusesitem", false);
		maxStackSize.clear();
		loadMaxStackSize();
		String list = reader.getString("disabled", "");
		String items[] = list.split(",");
		for (String item : items) {
			disabledTypes.add(item);
		}
	}

	private void loadMaxStackSize() {
		File file = new File(Showcase.instance.getDataFolder(), "stacks.csv");
		if (!file.exists()) {
			try {
				Showcase.instance.copyFromJarToDisk("stacks.csv", Showcase.instance.getDataFolder());
			} catch (IOException e) {
				return;
			}
		}
		try {
			String cnt = FileUtils.getContents(file);
			for (String line : cnt.split("\n")) {
				if (line.startsWith("#") || line.startsWith("//")) {
					continue;
				}
				String args[] = line.split(",");
				if (args.length == 2) {
					Material type;
					int count;
					try {
						type = Material.matchMaterial(args[0]);
						count = Integer.valueOf(args[1]);
					} catch (Exception e) {
						continue;
					}
					maxStackSize.put(type, count);
				}
			}
		} catch (FileNotFoundException e) {
			return;
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

	public boolean isTypeEnabled(String type) {
		return !disabledTypes.contains(type);
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	public int getAutosaveInterval() {
		return autoSaveInterval;
	}

	public int getMaxStackSize(Material type) {
		if (maxStackSize.containsKey(type)) {
			return maxStackSize.get(type);
		} else {
			return 64;
		}
	}

	public boolean isShowingAutosaveNotification() {
		return showAutosaveNotification;
	}

	public boolean useSpout() {
		return useSpout;
	}

	public void setUseSpout(boolean b) {
		useSpout = false;
	}

	public boolean isBasicUseItem() {
		return basicUsesItem;
	}
}
