package com.sri.straylight.fmuWrapper;



import java.util.EventListener;


public interface FMUeventListener extends EventListener

{

	public void onMessageEvent(MessageEvent event);

	public void onResultEvent(ResultEvent event);
	
	public void onFMUstateEvent(FMUstateEvent event);
	
	
	
}