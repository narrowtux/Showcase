package de.moritzschmale.Showcase;

import net.minecraft.server.EntityHuman;

public class TileEntityDispenser extends
		net.minecraft.server.TileEntityDispenser {
	TileEntityDispenser(){
		super();
	}
	
	@Override
	public boolean a_(EntityHuman h){
		return true;
	}
}
