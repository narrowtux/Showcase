package de.moritzschmale.Showcase.Types;

import java.util.HashMap;
import java.util.Map;

import com.narrowtux.Assistant.Assistant;
import com.narrowtux.Assistant.AssistantAction;
import com.narrowtux.Assistant.AssistantPage;
import com.narrowtux.translation.Translation;

import de.moritzschmale.Showcase.ShowcaseCreationAssistant;
import de.moritzschmale.Showcase.ShowcaseExtra;
import de.moritzschmale.Showcase.ShowcaseMain;
import de.moritzschmale.Showcase.ShowcasePlayer;
import de.moritzschmale.Showcase.ShowcaseProvider;

public class TutorialShowcase implements ShowcaseProvider {

	private Map<Assistant, String> texts = new HashMap<Assistant, String>();
	
	@Override
	public String getType() {
		return "tutorial";
	}

	@Override
	public String getPermission() {
		return "showcase.tutorial";
	}

	@Override
	public boolean isOpMethod() {
		return false;
	}

	@Override
	public ShowcaseExtra loadShowcase(String values) {
		TutorialShowcaseExtra extra = new TutorialShowcaseExtra();
		extra.setText(values);
		return extra;
	}

	@Override
	public String getDescription() {
		return ShowcaseMain.tr("types.tutorial.description");
	}

	@Override
	public void addPagesToCreationWizard(ShowcaseCreationAssistant assistant) {
		AssistantPage page = new AssistantPage(assistant){
			{
				setTitle(ShowcaseMain.tr("tutorial.title"));
				setText(ShowcaseMain.tr("tutorial.text"));
			}
			@Override
			public AssistantAction onPageInput(String text){
				if(text.equals("done")){
					return AssistantAction.CONTINUE;
				} else {
					text = Translation.parseColors(text);
					String tmp = texts.get(getAssistant());
					if(tmp.length()>0){
						tmp+="\n";
					}
					tmp+=text;
					sendMessage(text);
					texts.remove(getAssistant());
					texts.put(getAssistant(), tmp);
					return AssistantAction.SILENT_REPEAT;
				}
			}
		};
		assistant.addPage(page);
		texts.put(assistant, "");
	}

	@Override
	public ShowcaseExtra createShowcase(ShowcaseCreationAssistant assistant) {
		TutorialShowcaseExtra extra = new TutorialShowcaseExtra();
		extra.setText(texts.get(assistant));
		return extra;
	}

	@Override
	public double getPriceForCreation(ShowcasePlayer player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
