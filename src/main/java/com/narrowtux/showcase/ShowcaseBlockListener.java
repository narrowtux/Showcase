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

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class ShowcaseBlockListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getBlock().getType().equals(Material.STEP)) {
			// nothing to do for us.
			return;
		}
		ShowcaseItem item = Showcase.instance.getItemByBlock(event.getBlock());
		if (item != null) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(
					Showcase.tr("showcaseOwner", item.getPlayer()));
		}
		if (event.isCancelled()) {
			event.getPlayer().sendBlockChange(event.getBlock().getLocation(),
					event.getBlock().getType(), event.getBlock().getData());
		}
	}

	@Override
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (Showcase.instance.getItemByBlock(event.getBlock()) != null) {
			if (event.getChangedType().equals(Material.STEP)) {
				event.setCancelled(true);
			}
		}
	}
}
