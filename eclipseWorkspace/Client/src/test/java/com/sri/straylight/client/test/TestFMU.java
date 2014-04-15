package com.sri.straylight.client.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sri.straylight.client.util.FmuConnectionLocal;
import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class TestFMU {
	
	private ThreadedFMUcontroller threadedFMUcontroller_;
	
	@Test
	public void fmuWrapperConfig() {
		
		FMUwrapperConfig fmuWrapperConfig = FMUwrapperConfig.load();
		
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit2", fmuWrapperConfig.fmuFolderAbsolutePath);
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_edit2", fmuWrapperConfig.fmuFolderName);
		assertEquals("Client-Debug", fmuWrapperConfig.id);
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig.nativeLibFolderAbsolutePath);
		assertEquals("\\..\\..\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig.nativeLibFolderRelativePath);
		assertEquals(500, (int)fmuWrapperConfig.parseInternalVariableLimit);
		assertEquals("500", fmuWrapperConfig.parseInternalVariableLimitStr);

		assertEquals(false, fmuWrapperConfig.parseInternalVariablesFlag);
		assertEquals("false", fmuWrapperConfig.parseInternalVariablesFlagStr);
	
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs", fmuWrapperConfig.unzipFolderAbsolutePath);
		assertEquals("\\..\\..\\assets\\FMUs\\", fmuWrapperConfig.unzipFolderRelativePath);
		
		
		return;
		
	}
	

	
	

	
	
	@Test
	public void fmuConnectionLocal() {
		
		FmuConnectionLocal fmuConnect = new FmuConnectionLocal();
		fmuConnect.requestStateChange(SimStateNative.simStateNative_1_connect_requested  );
		
		return;
		
	}
	
	
	@Test
	public void fmuConnectionLocal2() {
		
		FMUcontroller fmuController = new FMUcontroller();
		fmuController.setConcurrency(false);

		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController);
		registerSimulationListeners_();
		
		
		threadedFMUcontroller_.requestStateChange(SimStateNative.simStateNative_1_connect_requested);
		
		
		return;
		
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
						
						return;
						
					}
				});
		

		//MessageEvent
		fmuController
		.registerEventListener(
				MessageEvent.class,
				new StraylightEventListener<MessageEvent, MessageStruct>() {
					@Override
					public void handleEvent(MessageEvent event) {
						return;
					}
				});
		
		//ResultEvent
		fmuController
		.registerEventListener(
				ResultEvent.class,
				new StraylightEventListener<ResultEvent, ScalarValueResults>() {
					@Override
					public void handleEvent(ResultEvent event) {
						return;
					}
				});

		
		//XMLparsedEvent
		fmuController
		.registerEventListener(
				XMLparsedEvent.class,
				new StraylightEventListener<XMLparsedEvent, XMLparsedInfo>() {
					@Override
					public void handleEvent(XMLparsedEvent event) {
						return;
					}
				});
		
		
		//ConfigChangeNotify
		fmuController
		.registerEventListener(
				ConfigChangeNotify.class,
				new StraylightEventListener<ConfigChangeNotify, ConfigStruct>() {
					@Override
					public void handleEvent(ConfigChangeNotify event) {
						return;
					}
				});
	}
	
	

	
	
	
	
	

}
