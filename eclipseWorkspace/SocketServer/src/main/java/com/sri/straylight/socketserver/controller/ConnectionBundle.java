package com.sri.straylight.socketserver.controller;

import java.io.IOException;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.StraylightWebSocket;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;


public class ConnectionBundle extends AbstractController {

	private SimulationController simulationController_;
	private WebSocketConnectionController webSocketConnectionController_;

	private FMUcontroller fmuController_;

	private int idx_;

	public ConnectionBundle(AbstractController parent,
			StraylightWebSocket webSocketConnection, int idx) {
		super(null);
		idx_ = idx;

		webSocketConnectionController_ = new WebSocketConnectionController(
				this, webSocketConnection, idx);
	}

	public void init() {

		webSocketConnectionController_.init();

		webSocketConnectionController_
				.registerEventListener(
						SimStateNativeRequest.class,

						new StraylightEventListener<SimStateNativeRequest, SimStateNative>() {

							@Override
							public void handleEvent(SimStateNativeRequest event) {
								SimStateNative requestedState = event
										.getPayload();

								switch (requestedState) {
								case simStateNative_1_connect_requested:
									// try {
									// fmuController_.connect();
									// } catch (IOException e) {
									// e.printStackTrace();
									// }
									break;
								case simStateNative_2_xmlParse_requested:
									fmuController_.xmlParse();
									break;
								case simStateNative_4_run_requested:
									fmuController_.run();
									break;
								default:
									fmuController_.requestStateChange(requestedState);
								}

							}

						}

				);

		fmuController_ = new FMUcontroller(this);
		


		fmuController_
		.registerEventListener(
				SimStateNativeNotify.class,
				new StraylightEventListener<SimStateNativeNotify, SimStateNative>() {
					@Override
					public void handleEvent(SimStateNativeNotify event) {
						webSocketConnectionController_.send(event);
					}
				});
		
		
		fmuController_
		.registerEventListener(
				MessageEvent.class,
				new StraylightEventListener<MessageEvent, MessageStruct>() {
					@Override
					public void handleEvent(MessageEvent event) {
						webSocketConnectionController_.send(event);
					}
				});
		
		
		fmuController_
		.registerEventListener(
				ResultEvent.class,
				new StraylightEventListener<ResultEvent, ScalarValueResults>() {
					@Override
					public void handleEvent(ResultEvent event) {
						webSocketConnectionController_.send(event);
					}
				});

		
		fmuController_
		.registerEventListener(
				XMLparsedEvent.class,
				new StraylightEventListener<XMLparsedEvent, XMLparsedInfo>() {
					@Override
					public void handleEvent(XMLparsedEvent event) {
						webSocketConnectionController_.send(event);
					}
				});

		try {
			fmuController_.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	// @Override
	// public void handleEvent(SimStateNativeNotify event) {
	// webSocketConnectionController_.send(event);
	// }
	//
	//
	// public void handleEvent(SimStateNativeRequest event) {
	//
	// SimStateNative requestedState = event.getPayload();
	//
	// switch (requestedState) {
	// case simStateNative_1_connect_requested :
	// try {
	// fmuController_.connect();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// break;
	// case simStateNative_2_xmlParse_requested :
	// fmuController_.xmlParse();
	// break;
	// case simStateNative_4_run_requested :
	// fmuController_.run();
	// break;
	// default:
	// fmuController_.requestStateChange(requestedState);
	// }
	//
	// }

}
