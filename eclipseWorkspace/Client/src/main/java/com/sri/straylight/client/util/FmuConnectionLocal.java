package com.sri.straylight.client.util;

import java.io.File;
import java.util.Vector;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectLocal.
 */
public class FmuConnectionLocal extends FmuConnectionAbstract {

	/** The fmu_. */
	private ThreadedFMUcontroller threadedFMUcontroller_;

	//constructor
	public FmuConnectionLocal() {
		
		
		FMUcontroller fmuController = new FMUcontroller();
		fmuController.setConcurrency(false);

		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController);
		registerSimulationListeners_();
		
	}
	
	
	public FmuConnectionLocal(File fmuFile_) {
		
		FMUcontroller fmuController = new FMUcontroller();
		fmuController.setConcurrency(false);
		fmuController.setFmuFile(fmuFile_);
		
		
		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController);
		registerSimulationListeners_();
		
	}



private void registerSimulationListeners_() {
		
		
		FMUcontroller fmuController = threadedFMUcontroller_.getFMUcontroller();
		
		
		//SimStateNativeNotify
		fmuController.registerEventListener(
				SimStateNativeNotify.class,
				new StraylightEventListener<SimStateNativeNotify, SimStateNative>() {
					@Override
					public void handleEvent(SimStateNativeNotify event) {
						
						
						SimStateNative simStateNative = event.getPayload();
						
						SimStateClientNotify event2 = new SimStateClientNotify(this,
								simStateNative);
						EventBus.publish(event2);
						
					}
				});
		

		//MessageEvent
		fmuController
		.registerEventListener(
				MessageEvent.class,
				new StraylightEventListener<MessageEvent, MessageStruct>() {
					@Override
					public void handleEvent(MessageEvent event) {
						
						EventBus.publish(event);
						
						
					}
				});
		
		//ResultEvent
		fmuController
		.registerEventListener(
				ResultEvent.class,
				new StraylightEventListener<ResultEvent, ScalarValueResults>() {
					@Override
					public void handleEvent(ResultEvent event) {
						EventBus.publish(event);
					}
				});

		
		//XMLparsedEvent
		fmuController
		.registerEventListener(
				XMLparsedEvent.class,
				new StraylightEventListener<XMLparsedEvent, XMLparsedInfo>() {
					@Override
					public void handleEvent(XMLparsedEvent event) {
						EventBus.publish(event);
					}
				});
		
		
		//ConfigChangeNotify
		fmuController
		.registerEventListener(
				ConfigChangeNotify.class,
				new StraylightEventListener<ConfigChangeNotify, ConfigStruct>() {
					@Override
					public void handleEvent(ConfigChangeNotify event) {
						EventBus.publish(event);
					}
				});
	}
	

	
	public void setConfig(ConfigStruct configStruct) {
		threadedFMUcontroller_.setConfig(configStruct);
	}
	
	public void requestStateChange(SimStateNative simStateNative) {
		threadedFMUcontroller_.requestStateChange(simStateNative);
	}

	public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		threadedFMUcontroller_.setScalarValues(scalarValueList);
	}

	
	
	
	

	
	@Override
	public void setScalarValueCollection(ScalarValueCollection collection) {

		threadedFMUcontroller_.setScalarValueCollection(collection);
		
		
	}
}
