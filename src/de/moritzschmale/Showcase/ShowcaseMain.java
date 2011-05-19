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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.*;
import com.nijikokun.bukkit.Permissions.*;
import com.nijikokun.register.payment.Method;


public class ShowcaseMain extends JavaPlugin {
	private static PermissionHandler Permissions = null;
	private Logger log;
	private ShowcasePlayerListener playerListener = new ShowcasePlayerListener();
	private ShowcaseBlockListener blockListener = new ShowcaseBlockListener();
	private ShowcaseServerListener serverListener = new ShowcaseServerListener();
	private DropChestListener dclistener;
	public static ShowcaseMain instance;
	public List<ShowcaseItem> showcasedItems = new ArrayList<ShowcaseItem>();
	private ItemWatcher watcher = new ItemWatcher();
	public Method method = null;
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
		
		//Read plugin file
		
		PluginDescriptionFile pdfFile = this.getDescription();
		log.log( Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Low, this);
		pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Normal, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Normal, this);
		if(dclistener!=null){
			pm.registerEvent(Type.CUSTOM_EVENT, dclistener, Priority.Normal, this);
		}
		getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 0, 10);
		
		//Remove probably duplicated items
		for(World w:getServer().getWorlds()){
			for(Entity e:w.getEntities()){
				if(e instanceof Item){
					Location loc = e.getLocation();
					Block b = loc.getBlock();
					if(b.getType().equals(Material.GLASS)){
						e.remove();
					}
				}
			}
		}
		
		load();
		setupPermissions();
	}
	
	public ShowcaseItem getItemByBlock(Block b){
		for(ShowcaseItem item:showcasedItems){
			if(b.equals(item.getBlock())){
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
				ShowcaseType showtype = item.getType();
				int amount = item.getItemAmount();
				double price = item.getPricePerItem();
				//Save
				//x,y,z,itemid,player,worldname,worldenviromnent,showtype,amount,price
				line+=loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+",";
				line+=type.getId()+","+data+",";
				line+=player+",";
				line+=loc.getWorld().getName()+",";
				line+=showtype+","+amount+","+price+"\n";
				w.write(line);
			}
			w.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unexpected error.");
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
					if(line.length==6){
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						String player = line[4];
						World world = getServer().getWorld(line[5]);
						Location loc = new Location(world, x, y, z);
						ItemStack stack = new ItemStack(type);
						Item item = world.dropItemNaturally(loc, stack);
						ShowcaseItem showItem = new ShowcaseItem(item, loc, player);
						showcasedItems.add(showItem);
					} else if(line.length==7){
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						short data = Short.valueOf(line[4]);
						String player = line[5];
						World world = getServer().getWorld(line[6]);
						Location loc = new Location(world, x, y, z);
						ItemStack stack = new ItemStack(type, 1, data);
						Item item = world.dropItemNaturally(loc, stack);
						ShowcaseItem showItem = new ShowcaseItem(item, loc, player);
						showcasedItems.add(showItem);
					} else if(line.length==10){
						int x,y,z;
						x = Integer.valueOf(line[0]);
						y = Integer.valueOf(line[1]);
						z = Integer.valueOf(line[2]);
						Material type = Material.getMaterial(Integer.valueOf(line[3]));
						short data = Short.valueOf(line[4]);
						String player = line[5];
						World world = getServer().getWorld(line[6]);
						ShowcaseType showtype = ShowcaseType.valueOf(line[7]);
						int amount = Integer.valueOf(line[8]);
						double price = Double.valueOf(line[9]);
						Location loc = new Location(world, x, y, z);
						ShowcaseItem showItem = new ShowcaseItem(loc, type, data, player, showtype, amount, price);
						showcasedItems.add(showItem);
					} else {
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

			if(this.Permissions == null) {
				try{
					this.Permissions = ((Permissions)test).getHandler();
				} catch(Exception e) {
					this.Permissions = null;
					log.log(Level.WARNING, "Permissions is not enabled! All Operations are allowed!");
				}
			}
		} catch(java.lang.NoClassDefFoundError e){
			this.Permissions = null;
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

}
