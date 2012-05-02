package com.sri.straylight.fmuWrapper.event;



import java.util.EventListener;



public interface FMUeventListener extends EventListener

{

	public void onMessageEvent(MessageEvent event);

	public void onResultEvent(ResultEvent event);
	
	public void onFMUstateEvent(FMUstateEvent event);
	
	public void onInitializedEvent(InitializedEvent event);
	
}