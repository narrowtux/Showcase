package de.moritzschmale.Showcase.Types;

import java.util.HashMap;
import java.util.Map;

import com.narrowtux.Assistant.Accuracy;
import com.narrowtux.Assistant.NumberPage;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class SellShowcase implements ShowcaseProvider {

	private Map<ShowcaseCreationAssistant, ShowcasePricePage> pricePages = new HashMap<ShowcaseCreationAssistant, ShowcasePricePage>();
	private Map<ShowcaseCreationAssistant, NumberPage> amountPages = new HashMap<ShowcaseCreationAssistant, NumberPage>();
	
	@Override
	public String getType() {
		return "finitesell";
	}

	@Override
	public String getPermission() {
		return "showcase.sell.finite";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		SellShowcaseExtra extra = new SellShowcaseExtra();
		String args [] = values.split(";");
		if(args.length>=3){
			extra.setAmountLeft(Integer.valueOf(args[0]));
			extra.setPricePerItem(Double.valueOf(args[1]));
			extra.setAmountOfItems(Integer.valueOf(args[2]));
			return extra;
		}
		return null;
	}

	@Override
	public String getDescription() {
		//TODO: actually use translation
		return "Drop some money in the sell showcase, other players can sell their items there and get this money.";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		ShowcasePricePage price = new ShowcasePricePage(assistant);
		pricePages.put(assistant, price);
		assistant.addPage(price);
		NumberPage amount = new NumberPage(assistant, Accuracy.INT);
		//TODO: Translation!
		amount.setTitle("Item amount");
		amount.setText("How many items do you want to buy?\nPlease note: amount*price is taken from your balance after setup!");
		amount.setMinimum(0);
		amount.setMaximum(Integer.MAX_VALUE);
		amountPages.put(assistant, amount);
		assistant.addPage(amount);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		SellShowcaseExtra extra = new SellShowcaseExtra();
		ShowcasePricePage price = pricePages.get(assistant);
		extra.setPricePerItem(price.price);
		NumberPage amount = amountPages.get(assistant);
		extra.setAmountLeft((int) amount.getValue());
		double totalPrice = extra.getAmountLeft()*extra.getPricePerItem();
		ShowcasePlayer player = ShowcasePlayer.getPlayer(assistant.getPlayer());
		if(!player.canAfford(totalPrice)){
			//TODO: actual translation
			player.sendMessage("You can't afford so many items. Try again!");
			return null;
		} else {
			player.takeMoney(totalPrice);
		}
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Generate Config value
		return 0;
	}

}
