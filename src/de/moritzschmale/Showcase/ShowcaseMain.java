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
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class ShowcaseMain extends JavaPlugin {
	private Logger log;
	private ShowcasePlayerListener playerListener = new ShowcasePlayerListener();
	private ShowcaseBlockListener blockListener = new ShowcaseBlockListener();
	public static ShowcaseMain instance;
	public List<ShowcaseItem> showcasedItems = new ArrayList<ShowcaseItem>();
	private ItemWatcher watcher = new ItemWatcher();
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
		//Read plugin file
		PluginDescriptionFile pdfFile = this.getDescription();
		log.log( Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, watcher, 0, 10);
		load();
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
				Material type = item.getItem().getItemStack().getType();
				String player = item.getPlayer();
				//Save
				//x,y,z,itemid,player,worldname,worldenviromnent
				line+=loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+",";
				line+=type.getId()+",";
				line+=player+",";
				line+=loc.getWorld().getName()+"\n";
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
					} else {
						continue;
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
