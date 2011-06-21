package de.moritzschmale.Showcase.Types;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.narrowtux.Assistant.TextPage;
import com.narrowtux.translation.Translation;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class ExchangeShowcase implements ShowcaseProvider {

	private Map<ShowcaseCreationAssistant, ShowcaseAmountPage> amountPages = new HashMap<ShowcaseCreationAssistant, ShowcaseAmountPage>();
	private Map<ShowcaseCreationAssistant, ExchangeTypePage> exchangeTypePages = new HashMap<ShowcaseCreationAssistant, ExchangeTypePage>();
	private Map<ShowcaseCreationAssistant, TextPage> ratePages = new HashMap<ShowcaseCreationAssistant, TextPage>();
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
		return Translation.tr("types.exchange.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		/*
		 * Wizard:
		 * How many items of (type) you want to sell?
		 * Take the item you want to get for this one in your hand (type ok), or type id:data
		 * Type the exchangerate (x:y)
		 */
		ShowcaseAmountPage amountPage = new ShowcaseAmountPage(assistant);
		ExchangeTypePage typePage = new ExchangeTypePage(assistant);
		TextPage ratePage = new TextPage(assistant);
		assistant.addPage(typePage);
		assistant.addPage(ratePage);
		assistant.addPage(amountPage);
		ratePage.setTitle(Translation.tr("assistant.exchange.create.rate.title"));
		ratePage.setText(Translation.tr("assistant.exchange.create.rate.text"));
		amountPages.put(assistant, amountPage);
		exchangeTypePages.put(assistant, typePage);
		ratePages.put(assistant, ratePage);
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		ExchangeShowcaseExtra extra = new ExchangeShowcaseExtra();
		ShowcaseAmountPage amountPage = amountPages.get(assistant);
		ExchangeTypePage typePage = exchangeTypePages.get(assistant);
		TextPage ratePage = ratePages.get(assistant);
		extra.setExchangeAmount(0);
		extra.setExchangeType(typePage.type);
		extra.setExchangeData(typePage.data);
		extra.setItemAmount(amountPage.amount);
		
		String [] rate = ratePage.getInput().split(":");
		if(rate.length==2){
			int left, right;
			left = Integer.valueOf(rate[0]);
			right = Integer.valueOf(rate[1]);
			if(left!=0&&right!=0){
				extra.setExchangeRateLeft(left);
				extra.setExchangeRateRight(right);
			}else {
				extra.setExchangeRateLeft(1);
				extra.setExchangeRateRight(1);
			}
		} else {
			extra.setExchangeRateLeft(1);
			extra.setExchangeRateRight(1);
		}
		
		ShowcasePlayer player = ShowcasePlayer.getPlayer(assistant.getPlayer());
		player.remove(assistant.material, assistant.data, extra.getItemAmount());
		
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		return ShowcaseMain.instance.config.getPriceForExchangeShop();
	}

}
