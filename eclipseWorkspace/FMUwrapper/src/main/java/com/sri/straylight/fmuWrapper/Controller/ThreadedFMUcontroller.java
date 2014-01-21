package com.sri.straylight.fmuWrapper.Controller;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

/**
 * The Class FmuConnectLocal.
 */
public class ThreadedFMUcontroller extends AbstractController {

	/** The fmu_. */
	private FMUcontroller fmuController_;
	
	private Object FMUcontrollerSync_ = new Object();
	//private WorkerInstantiateFMU workerInstantiateFMU_;
	private WorkerSetConfig workerSetConfig_;
	
	private WorkerRequestStateChange workerRequestStateChange_;
	private WorkerSetScalarValues workerSetScalarValues_;
	private WorkerSetScalarValueCollection workerSetScalarValueCollection_;


	//private AbstractController parent_;
	
	
	//constructor
	public ThreadedFMUcontroller() {
		super(null);
	}
	
	public ThreadedFMUcontroller(FMUcontroller fmuController) {
		super(null);
		
		fmuController_ = fmuController;
		
	}
	
/*	public void instantiateFMU() {
		workerInstantiateFMU_ = new WorkerInstantiateFMU();
		workerInstantiateFMU_.execute();
	}*/
	
	
	public FMUcontroller getFMUcontroller() {
		return fmuController_;
	}
	
	
	public void setConfig(ConfigStruct configStruct) {
		workerSetConfig_ = new WorkerSetConfig(configStruct);
		workerSetConfig_.execute();
	}
	
	public void requestStateChange(SimStateNative simStateNative) {
		workerRequestStateChange_ = new WorkerRequestStateChange(simStateNative);
		workerRequestStateChange_.execute();
	}

	public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		workerSetScalarValues_ = new WorkerSetScalarValues(scalarValueList);
		workerSetScalarValues_.execute();
	}

	public void setScalarValueCollection(ScalarValueCollection collection) {
		workerSetScalarValueCollection_ = new WorkerSetScalarValueCollection(collection);
		workerSetScalarValueCollection_.execute();
	}
	
	
/*	
	protected class WorkerInstantiateFMU extends WorkerThreadAbstract {
		
		WorkerInstantiateFMU() {
			setSyncObject(FMUcontrollerSync_);
		}
		
		//called by superclass
		@Override
		public void doIt_() {
			setName_("WorkerInstantiateFMU");
			fmuController_ = new FMUcontrollerGlobal(parent_);
		}
		
		//called by superclass
		@Override
		public void doneIt_() {
			workerInstantiateFMU_ = null;
		}
	}
	*/
	
	
	protected class WorkerSetConfig extends WorkerThreadAbstract {
		
		private ConfigStruct configStruct_;
		
		WorkerSetConfig(ConfigStruct configStruct) {
			configStruct_ = configStruct;
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			setName_("WorkerSetConfig");
			fmuController_.setConfig(configStruct_);
		}
		
		@Override
		public void doneIt_() {
			workerSetConfig_ = null;
		}
	}
	

	protected class WorkerRequestStateChange extends WorkerThreadAbstract {
		
		private SimStateNative simStateNative_;
		
		WorkerRequestStateChange(SimStateNative simStateNative) {
			simStateNative_ = simStateNative;
			
			//do not block a stop request
			if (simStateNative != SimStateNative.simStateNative_5_stop_requested 
					) {
				setSyncObject(FMUcontrollerSync_);
			}
		}
		
		@Override
		public void doIt_() {
			
			setName_("WorkerRequestStateChange " + simStateNative_.toString());
			fmuController_.requestStateChange(simStateNative_);
		}
		
		@Override
		public void doneIt_() {
			workerRequestStateChange_ = null;
		}
	}
	
	protected class WorkerSetScalarValues extends WorkerThreadAbstract {
		
		private Vector<ScalarValueRealStruct> scalarValueList_;
		
		WorkerSetScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
			scalarValueList_ = scalarValueList;
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			setName_("WorkerSetScalarValues");
			fmuController_.setScalarValues(scalarValueList_);
		}
		
		@Override
		public void doneIt_() {
			workerSetScalarValues_ = null;
		}
	}
	
	protected class WorkerSetScalarValueCollection extends WorkerThreadAbstract {
		
		private ScalarValueCollection collection_;
		
		WorkerSetScalarValueCollection(ScalarValueCollection collection) {
			collection_ = collection;
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			setName_("WorkerSetScalarValueCollection");
			
			fmuController_.setScalarValueCollection(collection_);
		}
		
		@Override
		public void doneIt_() {
			workerSetScalarValueCollection_ = null;
		}
	}
	
	
	public SimStateNative getSimStateNative() {
		return fmuController_.getSimStateNative();	
	}



	
	


	
	


}
