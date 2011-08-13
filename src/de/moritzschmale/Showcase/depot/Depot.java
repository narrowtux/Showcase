package de.moritzschmale.showcase.depot;

import de.moritzschmale.showcase.ShowcasePlayer;

public abstract class Depot {
	/**
	 * Add something to the depot (like money or items)
	 * @param amount to multiply the default value with.
	 */
	public abstract void addToDepot(int amount);
	/**
	 * Transfers the values in the depot to the given player
	 * @param player the player that receives the values
	 * @return if the transaction has been successful
	 */
	public abstract boolean giveDepot(ShowcasePlayer player);
	
	/**
	 * Transfers values from the given player to the depot
	 * @param amount to multiply the default value with.
	 * @param player from which the values are taken
	 * @return if the transaction has been successful
	 */
	public abstract boolean addToDepot(int amount, ShowcasePlayer player);
	
	@Override
	public abstract String toString();
}
