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

package com.narrowtux.showcase.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseItem;

public class ShowcaseBlockListener implements Listener {
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getBlock().getType().equals(Material.STEP)) {
			// nothing to do for us.
			return;
		}
		ShowcaseItem item = Showcase.instance.getItemByBlock(event.getBlock());
		if (item != null) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(Showcase.tr("showcaseOwner", item.getPlayer()));
		}
		if (event.isCancelled()) {
			event.getPlayer().sendBlockChange(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
			if(item != null) {
				item.getItem().setVelocity(new Vector(0, 0.2, 0));
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(Showcase.instance.getItemByBlock(event.getBlock()) != null) {
			event.setCancelled(true);
		}
	}
}
