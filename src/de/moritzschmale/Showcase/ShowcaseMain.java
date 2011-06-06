package de.moritzschmale.Showcase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.*;
import com.nijikokun.bukkit.Permissions.*;
import com.nijikokun.register.payment.Method;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moritzschmale.Showcase.Types.BasicShowcase;
import de.moritzschmale.Showcase.Types.FiniteShowcase;
import de.moritzschmale.Showcase.Types.InfiniteShowcase;


public class ShowcaseMain extends JavaPlugin {
	private static PermissionHandler Permissions = null;
	private Logger log;
	private ShowcasePlayerListener playerListener = new ShowcasePlayerListener();
	private ShowcaseBlockListener blockListener = new ShowcaseBlockListener();
	private ShowcaseServerListener serverListener = new ShowcaseServerListener();
	private ShowcaseWorldListener worldListener = new ShowcaseWorldListener();
	private DropChestListener dclistener;
	public static ShowcaseMain instance;
	public List<ShowcaseItem> showcasedItems = new ArrayList<ShowcaseItem>();
	private ItemWatcher watcher = new ItemWatcher();
	public Method method = null;
	public Configuration config;
	public  WorldGuardPlugin worldguard;
	public Map<String, ShowcaseProvider> providers = new HashMap<String, ShowcaseProvider>();
	
	@Override
	public void onDisable() {
		//Read plugin file
		PluginDescriptionFile pdfFile = this.getDescription();
		log.log( Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
		save();
		for(ShowcaseItem item:showcasedItems){
			item.remove();
		}
	}
	
	@Override
	public void onEnable() {
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
		log.log( Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Lowest, this);
		pm.registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Normal, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
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
		//Register Providers _after_ loading
		registerProvider(new BasicShowcase());
		registerProvider(new FiniteShowcase());
		registerProvider(new InfiniteShowcase());
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 0, 40);
		setupPermissions();

		config = new Configuration();
		
		playerListener.config = config;
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
				String line = "";
				Location loc = item.getBlock().getLocation();
				Material type = item.getMaterial();
				short data = item.getData();
				String player = item.getPlayer();
				String showtype = item.getType();
				//Save
				//x,y,z,itemid,player,worldname,worldenviromnent,showtype,amount,price
				line+=loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+",";
				line+=type.getId()+","+data+",";
				line+=player+",";
				line+=loc.getWorld().getName()+",";
				line+=showtype+",";
				if(item.getExtra()!=null)
				{
					line+=item.getExtra().save();
				}
				line+="\n";
				w.write(line);
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
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						short data = Short.valueOf(line[4]);
						String player = line[5];
						World world = getServer().getWorld(line[6]);
						String showtype = line[7].toLowerCase();
						if(showtype.equals("finite_shop")){
							showtype="finite";
						}
						if(showtype.equals("infinite_shop")){
							showtype="infinite";
						}
						int amount = Integer.valueOf(line[8]);
						double price = Double.valueOf(line[9]);
						Location loc = new Location(world, x, y, z);
						ShowcaseItem showItem = new ShowcaseItem(loc, type, data, player, showtype);
						showcasedItems.add(showItem);
						if(showtype.equals("finite")){
							showItem.setExtraLoad(amount+";"+price);
						} else  if(showtype.equals("infinite")){
							showItem.setExtraLoad(""+price);
						}
					} else if(line.length==9){
						//New format
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

			if(ShowcaseMain.Permissions == null) {
				try{
					ShowcaseMain.Permissions = ((Permissions)test).getHandler();
				} catch(Exception e) {
					ShowcaseMain.Permissions = null;
					log.log(Level.WARNING, "Permissions is not enabled! All Operations are allowed!");
				}
			}
		} catch(java.lang.NoClassDefFoundError e){
			ShowcaseMain.Permissions = null;
			log.log(Level.WARNING, "Permissions not found! All Operations are allowed!");
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
		String ret = type.toString().toLowerCase();
		ret = ret.replace("_", " ");
		if(type.equals(Material.WOOL)){
			DyeColor color = DyeColor.getByData((byte) data);
			ret = color.toString().toLowerCase().replace("_", " ")+" Wool";
		}
		if(type.equals(Material.INK_SACK)){
			DyeColor color = DyeColor.getByData((byte) (15-data));
			ret = color.toString().toLowerCase().replace("_", " ")+" Dye";
		}
		if(type.equals(Material.STEP)){
			switch(data){
			case 0:
				ret = "Stone Slab";
				break;
			case 1:
				ret = "Sandstone Slab";
				break;
			case 2:
				ret = "Wooden Slab";
				break;
			case 3:
				ret = "Cobblestone Slab";
				break;
			}
		}
		if(type.equals(Material.COAL)){
			switch(data){
			case 0:
				ret = "Coal";
				break;
			case 1:
				ret = "Charcoal";
				break;
			}
		}
		if(type.equals(Material.LOG)||type.equals(Material.SAPLING)||type.equals(Material.LEAVES)){
			ret = "";
			switch(data){
			case 0:
				ret = "";
				break;
			case 1:
				ret = "Spruce ";
				break;
			case 2:
				ret = "Birch ";
				break;
			}
			switch(type){
			case LOG:
				ret+="Log";
				break;
			case SAPLING:
				ret+="Sapling";
				break;
			case LEAVES:
				ret+="Leaves";
				break;
			}
		}
		return ret;
	}
	
	public void registerProvider(ShowcaseProvider provider){
		providers.put(provider.getType(), provider);
		int a = 0;
		for(ShowcaseItem item:showcasedItems){
			if(item.getType().equals(provider.getType())){
				item.setExtra(provider.loadShowcase(item.getExtraLoad()));
				a++;
			}
		}
		System.out.println("[Showcase] registered type ["+provider.getType()+"] ("+a+" items)");
	}
}
