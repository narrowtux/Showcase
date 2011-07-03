package com.narrowtux.Assistant;

public class TextPage extends AssistantPage {
	public TextPage(Assistant assistant) {
		super(assistant);
	}

	private String input = "";
	
	@Override
	public AssistantAction onPageInput(String text){
		input=text;
		return AssistantAction.CONTINUE;
	}
	
	public String getInput(){
		return input;
	}
}
