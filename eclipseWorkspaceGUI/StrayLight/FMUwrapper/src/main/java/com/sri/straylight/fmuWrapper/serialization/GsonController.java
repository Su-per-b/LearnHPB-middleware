package com.sri.straylight.fmuWrapper.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sri.straylight.fmuWrapper.InitializedStruct;
import com.sri.straylight.fmuWrapper.MessageStruct;
import com.sri.straylight.fmuWrapper.ResultItem;
import com.sri.straylight.fmuWrapper.ResultItemPrimitiveStruct;
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;




public class GsonController {


	private static GsonController inststance_ = new GsonController();
	
	private Gson gson_;
	
	/* Prevent direct access to the constructor */
	private GsonController() {
		super();
		init();
		
	}

    public static GsonController getInstance() {
        return inststance_;
    }
    
	public Gson getGson() {
		return gson_;
	}
	
	private void init() {
		
		
		GsonBuilder gb = new GsonBuilder();
		
		gb.registerTypeAdapter(MessageStruct.class, new MessageStructAdapter());
		gb.registerTypeAdapter(MessageEvent.class, new MessageEventAdapter());
		gb.registerTypeAdapter(ResultItemPrimitiveStruct.class, new ResultItemPrimitiveStructAdapter());
		gb.registerTypeAdapter(ResultItem.class, new ResultItemAdapter());
		gb.registerTypeAdapter(ResultEvent.class, new ResultEventAdapter());
		gb.registerTypeAdapter(State.class, new StateAdapter());
		gb.registerTypeAdapter(FMUstateEvent.class, new FMUstateEventAdapter());
		gb.registerTypeAdapter(InitializedStruct.class, new InitializedStructAdapter());
		gb.registerTypeAdapter(InitializedEvent.class, new InitializedEventAdapter());
		
		
		gson_ = gb.create();
		

		
	}
	

	
	
}


