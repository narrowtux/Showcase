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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.showcase.Configuration;
import com.narrowtux.showcase.Showcase;
import com.narrowtux.showcase.ShowcaseCreationAssistant;
import com.narrowtux.showcase.ShowcaseItem;
import com.narrowtux.showcase.ShowcasePlayer;

public class ShowcasePlayerListener implements Listener {
	public Configuration config = null;

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.STEP)) {
			ShowcaseItem showItem = Showcase.instance.getItemByBlock(event.getClickedBlock());
			ShowcasePlayer player = ShowcasePlayer.getPlayer(event.getPlayer());
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (!event.getPlayer().isSneaking()) {
					if (showItem != null) {
						showItem.getExtra().onRightClick(player);
						return;
					} else {
						return;
					}
				}
				if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId() == 0) {
					return;
				}
				if (event.hasBlock() && showItem == null && player.mayCreateHere(event.getClickedBlock())) {
					if (event.getItem() == null && !Showcase.hasOddItem()) {
						player.sendMessage(Showcase.tr("noItemError"));
						event.setCancelled(true);
						return;
					}
					if (event.getClickedBlock().getType().equals(Material.STEP)) {
						event.setCancelled(true);
						if (Showcase.instance.providers.size() == 1 && Showcase.instance.providers.containsKey("basic")) {
							Location loc = event.getClickedBlock().getLocation();
							Material mat = event.getItem().getType();
							short data = event.getItem().getDurability();
							Showcase.instance.addShowcase(new ShowcaseItem(loc, mat, data, event.getPlayer().getName(), "basic"));
							event.getPlayer().sendMessage(Showcase.tr("basicCreationSuccess"));
						} else {
							try {
								ShowcaseCreationAssistant assistant = new ShowcaseCreationAssistant(event.getPlayer(), event.getItem(), event.getClickedBlock().getLocation());
								assistant.start();
							} catch (NoClassDefFoundError e) {
								for (StackTraceElement element : e.getCause().getStackTrace()) {
									System.out.println(element.getFileName() + " line " + element.getLineNumber());
								}
							}
						}
					}
				} else if (showItem != null) {
					if (showItem.getPlayer().equals(event.getPlayer().getName()) || player.hasPermission("showcase.admin", true)) {
						if (showItem.getExtra() == null) {
							showItem.remove();
							Showcase.instance.removeShowcase(showItem);
							event.getPlayer().sendMessage(Showcase.tr("showcaseRemoveSuccess"));
							System.out.println("null Extra");
							return;
						}
						if (showItem.getExtra().onDestroy(player)) {
							showItem.remove();
							Showcase.instance.removeShowcase(showItem);
							event.getPlayer().sendMessage(Showcase.tr("showcaseRemoveSuccess"));
						}
					} else {
						event.getPlayer().sendMessage(Showcase.tr("showcaseOwner", showItem.getPlayer()));
					}
					event.setCancelled(true);
				}
			}
			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (showItem != null) {
					if (showItem.getExtra() != null)
						showItem.getExtra().onClick(player);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (Showcase.instance.isShowcaseItem(event.getItem())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Assistant.onPlayerMove(event);
	}
}
