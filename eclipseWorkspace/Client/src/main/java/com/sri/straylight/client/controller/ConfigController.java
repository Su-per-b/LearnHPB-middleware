package com.sri.straylight.client.controller;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.TabViewInitialized;
import com.sri.straylight.client.model.ConfigModel;
import com.sri.straylight.client.view.ConfigView;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigController.
 */
public class ConfigController extends BaseController {
	

	
//	private SimStateNative simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
	
	private boolean isInititialized_ = false;
	
	protected ConfigModel dataModel_;
	
	protected ConfigView view_;
	
	/**
	 * Instantiates a new config controller.
	 *
	 * @param parentController the parent controller
	 */
	public ConfigController(AbstractController parentController) {
		super(parentController);
	}

	

	
	private void bind_() {
		
		registerEventListener (
				ConfigChangeRequest.class,
				new StraylightEventListener<ConfigChangeRequest, ConfigStruct>() {
					@Override
					public void handleEvent(ConfigChangeRequest event) {

						 EventBus.publish(event);
					}
				});
		
	}
	
	
	private void init(ConfigStruct struct) {
		
		dataModel_ = new ConfigModel("Config");
		dataModel_.setStruct(struct);
		
		view_ = new ConfigView( dataModel_, this);
	    setView_(view_);
	    
	    bind_();
		
	    
	    TabViewInitialized event = new TabViewInitialized(this, view_);
	    EventBus.publish(event);
		
	}
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ConfigChangeNotify.class)
	public void onConfigChangeNotify(final ConfigChangeNotify event) {

		
		if (isInititialized_) {
			dataModel_.setStruct(event.getPayload());
			view_.updateGUIFromDataModel();
			
		} else {
			
			isInititialized_ = true;
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
				    init(event.getPayload());
			    }

			});
			
		}
		
		

		
		
	}
	
	protected void updateSimStateNative(SimStateNative simStateNative) {
		
		this.dataModel_.setSimStateNative(simStateNative);
		view_.updateState();

	}

	
	@EventSubscriber(eventClass=SimStateClientNotify.class)
	public void onSimStateNotify(final SimStateClientNotify event) {
	
		if (view_ != null) {
			
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	
					SimStateNative simStateNative = event.getPayload();
					updateSimStateNative(simStateNative);

			    }

			});
			
		}


	}
	






	
	
}
