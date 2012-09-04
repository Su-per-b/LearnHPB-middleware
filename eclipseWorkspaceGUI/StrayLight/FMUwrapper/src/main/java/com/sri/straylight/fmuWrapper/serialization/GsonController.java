package com.sri.straylight.fmuWrapper.serialization;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;




// TODO: Auto-generated Javadoc
/**
 * The Class GsonController.
 */
public class GsonController {


	/** The inststance_. */
	private static GsonController inststance_ = new GsonController();
	
	/** The gson_. */
	private Gson gson_;
	
	/* Prevent direct access to the constructor */
	/**
	 * Instantiates a new gson controller.
	 */
	private GsonController() {
		super();
		init();
		
	}

    /**
     * Gets the single instance of GsonController.
     *
     * @return single instance of GsonController
     */
    public static GsonController getInstance() {
        return inststance_;
    }
    
	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */
	public Gson getGson() {
		return gson_;
	}
	
	/**
	 * Inits the.
	 */
	private void init() {
		
		
		GsonBuilder gb = new GsonBuilder();
		
		gb.registerTypeAdapter(MessageStruct.class, new MessageStructAdapter());
		gb.registerTypeAdapter(MessageEvent.class, new MessageEventAdapter());
		gb.registerTypeAdapter(ScalarValueStruct.class, new ResultItemPrimitiveStructAdapter());
		gb.registerTypeAdapter(ResultOfStep.class, new ResultItemAdapter());
		gb.registerTypeAdapter(ResultEvent.class, new ResultEventAdapter());
		gb.registerTypeAdapter(SimStateNative.class, new StateAdapter());
		gb.registerTypeAdapter(XMLparsed.class, new InitializedStructAdapter());
		gb.registerTypeAdapter(InitializedEvent.class, new InitializedEventAdapter());
		gb.registerTypeAdapter(ScalarValue.class, new ResultItemPrimitiveAdapter());
		
		
		gson_ = gb.create();
		

		
	}
	

	
	
}


