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

import info.somethingodd.bukkit.odd.item.OddItem;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.ItemManager;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.translation.Translation;
import com.narrowtux.showcase.types.BasicShowcase;
import com.narrowtux.showcase.types.ExchangeShowcase;
import com.narrowtux.showcase.types.FiniteShowcase;
import com.narrowtux.showcase.types.InfiniteShowcase;
import com.narrowtux.showcase.types.TutorialShowcase;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Showcase extends JavaPlugin {
	private static PermissionHandler Permissions = null;
	private Logger log;
	private ShowcasePlayerListener playerListener = new ShowcasePlayerListener();
	private ShowcaseBlockListener blockListener = new ShowcaseBlockListener();
	private ShowcaseWorldListener worldListener = new ShowcaseWorldListener();
	private DropChestListener dclistener;
	public static Showcase instance;
	public List<ShowcaseItem> showcasedItems = new ArrayList<ShowcaseItem>();
	private ItemWatcher watcher = new ItemWatcher();
	public Configuration config;
	public  WorldGuardPlugin worldguard;
	public int autosaverId = -1;
	public Map<String, ShowcaseProvider> providers = new HashMap<String, ShowcaseProvider>();
	private Translation trans;
	private OddItem odditem = null;

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		//Read plugin file
		PluginDescriptionFile pdfFile = this.getDescription();
		String logText = tr("disableMessage", pdfFile.getName(), pdfFile.getVersion());
		save();
		for(ShowcaseItem item:showcasedItems){
			item.remove();
		}
		log.log( Level.INFO, logText);
	}

	public static String tr(String string, Object ...args) {
		return instance.trans.tr(string, args);
	}

	@Override
	public void onEnable() {
		checkForLibs();
		trans = new Translation();
		instance = this;
		log = getServer().getLogger();
		try{
			dclistener = new DropChestListener();
		} catch(NoClassDefFoundError e){
			dclistener = null;
		}
		try{
			worldguard = (WorldGuardPlugin)getServer().getPluginManager().getPlugin("WorldGuard");
		}catch(Exception e){
			worldguard = null;
		}

		//Read plugin file

		PluginDescriptionFile pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Low, this);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_DROP_ITEM, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.CHUNK_LOAD, worldListener, Priority.Normal, this);
		pm.registerEvent(Type.CHUNK_UNLOAD, worldListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
		if(dclistener!=null){
			//Listen for dropchest-suck events
			pm.registerEvent(Type.CUSTOM_EVENT, dclistener, Priority.Normal, this);
		}

		//Remove probably duplicated items
		for(World w:getServer().getWorlds()){
			for(Entity e:w.getEntities()){
				if(e instanceof Item){
					Location loc = e.getLocation();
					Block b = loc.getBlock();
					if(b.getType().equals(Material.GLASS)||b.getType().equals(Material.STEP)){
						e.remove();
					}
				}
			}
		}

		load();

		config = new Configuration();
		
		if(pm.getPlugin("Spout") != null){
			config.setUseSpout(true);
		}
		
		if(config.useSpout()){
			if(!NarrowtuxLib.getInstance().installSpout()){
				config.setUseSpout(false);
			}
		}
		
		odditem = (OddItem) pm.getPlugin("OddItem");

		trans.reload(new File(getDataFolder(), "showcase-"+config.getLocale()+".csv"));

		if(trans.getVersion()<5){
			try {
				copyFromJarToDisk("showcase-"+config.getLocale()+".csv", getDataFolder());
				log.log(Level.INFO, "[Showcase] copied new translation file for "+config.getLocale()+" to disk.");
				trans.reload(new File(getDataFolder(), "showcase-"+config.getLocale()+".csv"));
			} catch (IOException e) {
				System.out.println("Unable to copy default translation to plugin folder");
			}
		}

		playerListener.config = config;
		//Register Providers _after_ loading and loading config
		registerProvider(new BasicShowcase());
		registerProvider(new FiniteShowcase());
		registerProvider(new InfiniteShowcase());
		registerProvider(new ExchangeShowcase());
		registerProvider(new TutorialShowcase());
		//registerProvider(new SellShowcase());

		getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 0, 40);
		setupPermissions();

		if(config.getAutosaveInterval()!=-1){
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

				@Override
				public void run() {
					save();
					if(config.isShowingAutosaveNotification()){
						log.log(Level.INFO, "[Showcase] Autosaved");
					}
				}
			}, 0, config.getAutosaveInterval()*20);
		}

		String logText = trans.tr("enableMessage", pdfFile.getName(), pdfFile.getVersion());
		log.log( Level.INFO, logText);
	}

	private void checkForLibs() {
		PluginManager pm = getServer().getPluginManager();
		if(pm.getPlugin("NarrowtuxLib")==null){
			try{
				File toPut = new File("plugins/NarrowtuxLib.jar");
				download(getServer().getLogger(), new URL("http://tetragaming.com/narrowtux/plugins/NarrowtuxLib.jar"), toPut);
				pm.loadPlugin(toPut);
				pm.enablePlugin(pm.getPlugin("NarrowtuxLib"));
			} catch (Exception exception){
				log.severe("[Showcase] could not load NarrowtuxLib, try again or install it manually.");
				pm.disablePlugin(this);
			}
		}
	}

	public static void download(Logger log, URL url, File file) throws IOException {
	    if (!file.getParentFile().exists())
	        file.getParentFile().mkdir();
	    if (file.exists())
	        file.delete();
	    file.createNewFile();
	    final int size = url.openConnection().getContentLength();
	    log.info("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
	    final InputStream in = url.openStream();
	    final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
	    final byte[] buffer = new byte[1024];
	    int len, downloaded = 0, msgs = 0;
	    final long start = System.currentTimeMillis();
	    while ((len = in.read(buffer)) >= 0) {
	        out.write(buffer, 0, len);
	        downloaded += len;
	        if ((int)((System.currentTimeMillis() - start) / 500) > msgs) {
	            log.info((int)((double)downloaded / (double)size * 100d) + "%");
	            msgs++;
	        }
	    }
	    in.close();
	    out.close();
	    log.info("Download finished");
	}

	public ShowcaseItem getItemByBlock(Block b){
		for(ShowcaseItem item:showcasedItems){
			if(b.equals(item.getBlock())){
				return item;
			}
		}
		return null;
	}

	public ShowcaseItem getItemByDrop(Item i){
		for(ShowcaseItem item: showcasedItems){
			if(item.getItem().equals(i)){
				return item;
			}
		}
		return null;
	}

	public void save(){
		File folder = getDataFolder();
		if(!folder.exists()){
			//Create the folder
			folder.mkdir();
		}
		File datafile = new File(folder.getAbsolutePath()+"/showcases.csv");
		if(!datafile.exists()){
			try {
				datafile.createNewFile();
			} catch (IOException e) {
				System.out.println("Could not create Datafile ("+e.getCause()+"). Aborting.");
				return;
			}
		}
		try {
			FileOutputStream output = new FileOutputStream(datafile.getAbsoluteFile());
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(output));
			for(ShowcaseItem item:showcasedItems){
				try{
					String line = "";
					Location loc = item.getBlock().getLocation();
					Material type = item.getMaterial();
					short data = item.getData();
					String player = item.getPlayer();
					String showtype = item.getType();
					//Save
					//x,y,z,itemid,player,worldname,worldenviromnent,showtype
					line+=loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+",";
					line+=type.getId()+","+data+",";
					line+=player+",";
					line+=loc.getWorld().getName()+",";
					line+=showtype+",";
					line+=loc.getWorld().getEnvironment().toString()+",";
					if(item.getExtra()!=null)
					{
						line+=item.getExtra().save();
					} else {
						line+=item.getExtraLoad();
					}
					line+="\n";
					w.write(line);
				} catch(Exception e){
					continue;
				}
			}
			w.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error when writing file.");
		}
	}

	public void load(){
		File folder = getDataFolder();
		if(!folder.exists()){
			//Create the folder
			folder.mkdir();
		}
		File datafile = new File(folder.getAbsolutePath()+"/showcases.csv");
		if(datafile.exists()){
			FileInputStream input;
			try {
				input = new FileInputStream(datafile.getAbsoluteFile());
				InputStreamReader ir = new InputStreamReader(input);
				BufferedReader r = new BufferedReader(ir);
				String locline;
				while(true){
					locline = r.readLine();
					if(locline==null)
					{
						break;
					}
					String line[] = locline.split(",");
					if(line.length==10){
						//New format
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						short data = Short.valueOf(line[4]);
						String player = line[5];
						Environment environment = Environment.NORMAL;
						try{
							environment = Environment.valueOf(line[8]);
						} catch(Exception e){
							environment = Environment.NORMAL;
						}
						World world = getServer().createWorld(line[6], environment);
						String showtype = line[7].toLowerCase();
						Location loc = new Location(world, x, y, z);
						ShowcaseItem showItem = new ShowcaseItem(loc, type, data, player, showtype);
						showcasedItems.add(showItem);
						String extra = line[9];
						showItem.setExtraLoad(extra);
					} else if(line.length==9){
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						short data = Short.valueOf(line[4]);
						String player = line[5];
						World world = getServer().getWorld(line[6]);
						String showtype = line[7].toLowerCase();
						Location loc = new Location(world, x, y, z);
						ShowcaseItem showItem = new ShowcaseItem(loc, type, data, player, showtype);
						showcasedItems.add(showItem);
						String extra = line[8];
						showItem.setExtraLoad(extra);
					}else {
						continue;
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void setupPermissions() {
		try{
			Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

			if(Showcase.Permissions == null) {
				try{
					Showcase.Permissions = ((Permissions)test).getHandler();
				} catch(Exception e) {
					Showcase.Permissions = null;
					log.log(Level.WARNING, tr("permissionsUnavailable"));
				}
			}
		} catch(java.lang.NoClassDefFoundError e){
			Showcase.Permissions = null;
			log.log(Level.WARNING, tr("permissionsUnavailable"));
		}
	}

	public static boolean hasPermission(Player player, String node, boolean adminMethod){
		if(Permissions!=null)
		{
			return Permissions.has(player, node);
		} else {
			if(!player.isOp()){
				return !adminMethod;
			} else {
				return true;
			}
		}
	}

	public static String getName(Material type, short data){
		if(instance.config.useSpout()){
			ItemManager itemManager = SpoutManager.getItemManager();
			String custom = itemManager.getCustomItemName(type, (byte)data);
			if(custom==null)
			{
				return itemManager.getItemName(type, (byte) data);
			} else {
				return custom;
			}
		} else {
			return type.toString().toLowerCase();
		}
	}

	public void registerProvider(ShowcaseProvider provider){
		if(config.isTypeEnabled(provider.getType())&&!(!provider.getType().equals("basic")&&config.isBasicMode())){
			providers.put(provider.getType(), provider);
			int a = 0;
			for(ShowcaseItem item:showcasedItems){
				if(item.getType().equals(provider.getType())){
					item.setExtra(provider.loadShowcase(item.getExtraLoad()));
					a++;
				}
			}
			System.out.println(tr("registerShowcase", provider.getType(), a));
		} else {
			System.out.println(tr("registerFail", provider.getType()));
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
		if(cmd.getName().equals("showcase")){
			if(args.length>=1){
				if(args[0].equalsIgnoreCase("reload")){
					if(sender.isOp()){
						save();
						for(ShowcaseItem item:showcasedItems){
							item.remove();
						}
						showcasedItems.clear();
						config.load();
						trans.reload(new File(getDataFolder(), "showcase-"+config.getLocale()+".csv"));
						load();
						for(ShowcaseProvider provider:providers.values()){
							registerProvider(provider);
						}
						sender.sendMessage(tr("reloadSuccessful"));
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("save")){
					if(sender.isOp()){
						save();
						sender.sendMessage(tr("saveSuccessful"));
					}
					return true;
				}
				if(args[0].equalsIgnoreCase("load")){
					if(sender.isOp()){
						for(ShowcaseItem item:showcasedItems){
							item.remove();
						}
						showcasedItems.clear();
						config.load();
						trans.reload(new File(getDataFolder(), "showcase-"+config.getLocale()+".csv"));
						load();
						for(ShowcaseProvider provider:providers.values()){
							registerProvider(provider);
						}
						sender.sendMessage(tr("loadSuccessful"));
					}
					return true;
				}
			}
		}
		return false;
	}

	public void copyFromJarToDisk(String entry, File folder) throws IOException{
		JarFile jar = new JarFile(getFile());
		InputStream is = jar.getInputStream(jar.getJarEntry(entry));
		OutputStream os = new FileOutputStream(new File(getDataFolder(), entry));
		byte[] buffer = new byte[4096];
		int length;
		while (is!=null&&(length = is.read(buffer)) > 0) {
		    os.write(buffer, 0, length);
		}
		os.close();
		is.close();
	}
	
	public static boolean hasOddItem(){
		return instance.odditem!=null;
	}
}
