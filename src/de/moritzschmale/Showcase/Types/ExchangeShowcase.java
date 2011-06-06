package de.moritzschmale.Showcase.Types;

import org.bukkit.Material;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class ExchangeShowcase implements ShowcaseProvider {

	@Override
	public String getType() {
		return "exchange";
	}

	@Override
	public String getPermission() {
		return "showcase.exchange";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		//Values:
		//Exchange-type;Exchange-data;buy-amount;exchange-amount
		ExchangeShowcaseExtra extra = new ExchangeShowcaseExtra();
		String [] args = values.split(";");
		if(args.length==6){
			extra.setExchangeType(Material.getMaterial(Integer.valueOf(args[0])));
			extra.setExchangeData(Short.valueOf(args[1]));
			extra.setItemAmount(Integer.valueOf(args[2]));
			extra.setExchangeAmount(Integer.valueOf(args[3]));
			extra.setExchangeRateLeft(Integer.valueOf(args[4]));
			extra.setExchangeRateRight(Integer.valueOf(args[5]));
		} else {
			System.out.println("[Showcase] not enough arguments for exchange");
		}
		return extra;
	}

	@Override
	public String getDescription() {
		return "Get items for other items";
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		// TODO Auto-generated method stub
		return new ExchangeShowcaseExtra();
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForExchangeShop();
	}

}
