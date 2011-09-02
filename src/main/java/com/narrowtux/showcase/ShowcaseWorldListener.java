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

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class ShowcaseWorldListener extends WorldListener {
	@Override
	public void onChunkLoad(ChunkLoadEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			try{
				if(event.getChunk().equals(item.getBlock().getChunk())){
					item.respawn();
					item.setChunkLoaded(true);
				}
			}catch(Exception e){
			}
		}
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event){
		for(ShowcaseItem item:ShowcaseMain.instance.showcasedItems){
			try{
				if(event.getChunk().equals(item.getBlock().getChunk())){
					item.remove();
					item.setChunkLoaded(false);
				}
			}catch(Exception e){
			}
		}
	}
}
