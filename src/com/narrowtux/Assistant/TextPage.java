package com.narrowtux.Assistant;

public class TextPage extends AssistantPage {
	public TextPage(Assistant assistant) {
		super(assistant);
	}

	private String input = "";
	
	@Override
	public boolean onPageInput(String text){
		input=text;
		return true;
	}
	
	public String getInput(){
		return input;
	}
}
