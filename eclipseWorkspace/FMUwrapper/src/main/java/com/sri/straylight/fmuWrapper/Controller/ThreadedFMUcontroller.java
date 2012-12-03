package com.sri.straylight.fmuWrapper.Controller;

import java.io.IOException;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.Controller.FMUcontrollerGlobal;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

/**
 * The Class FmuConnectLocal.
 */
public class ThreadedFMUcontroller extends AbstractController {

	/** The fmu_. */
	private FMUcontrollerGlobal fmuController_;
	private Object FMUcontrollerSync_ = new Object();
	
	private WorkerInstantiateFMU workerInstantiateFMU_;
	private WorkerConnect workerConnect_;
	private WorkerXMLparse workerXMLparse_;
	private WorkerSetConfig workerSetConfig_;
	private WorkerRequestStateChange workerRequestStateChange_;
	private WorkerSetScalarValues workerSetScalarValues_;
	private WorkerRun workerRun_;



	//constructor
	public ThreadedFMUcontroller() {
		super(null);
	}

	public void instantiateFMU() {
		workerInstantiateFMU_ = new WorkerInstantiateFMU();
		workerInstantiateFMU_.execute();
	}
	
	
	public void connect() {
		workerConnect_ = new WorkerConnect();
		workerConnect_.execute();
	}
	
	public void xmlParse() {
		workerXMLparse_ = new WorkerXMLparse();
		workerXMLparse_.execute();
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

	public void run() {
		workerRun_ = new WorkerRun();
		workerRun_.execute();
	}


	protected class WorkerInstantiateFMU extends WorkerThreadAbstract {
		
		WorkerInstantiateFMU() {
			setSyncObject(FMUcontrollerSync_);
		}
		
		//called by superclass
		@Override
		public void doIt_() {
			fmuController_ = new FMUcontrollerGlobal();
		}
		
		//called by superclass
		@Override
		public void doneIt_() {
			workerConnect_ = null;
		}
	}
	
	
	protected class WorkerConnect extends WorkerThreadAbstract {
		
		WorkerConnect() {
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			try {
				fmuController_.connect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void doneIt_() {
			workerConnect_ = null;
		}
	}
	

	protected class WorkerXMLparse extends WorkerThreadAbstract {
		
		WorkerXMLparse() {
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			fmuController_.xmlParse();
		}
		
		@Override
		public void doneIt_() {
			workerRun_ = null;
		}
	}
	
	
	protected class WorkerSetConfig extends WorkerThreadAbstract {
		
		private ConfigStruct configStruct_;
		
		WorkerSetConfig(ConfigStruct configStruct) {
			configStruct_ = configStruct;
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
			fmuController_.setConfig(configStruct_);
		}
		
		@Override
		public void doneIt_() {
			workerConnect_ = null;
		}
	}
	

	protected class WorkerRequestStateChange extends WorkerThreadAbstract {
		
		private SimStateNative simStateNative_;
		
		WorkerRequestStateChange(SimStateNative simStateNative) {
			simStateNative_ = simStateNative;
			setSyncObject(FMUcontrollerSync_);
		}
		
		@Override
		public void doIt_() {
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
			fmuController_.setScalarValues(scalarValueList_);
		}
		
		@Override
		public void doneIt_() {
			workerSetScalarValues_ = null;
		}
	}
	
	protected class WorkerRun extends WorkerThreadAbstract {
		
		@Override
		public void doIt_() {
			fmuController_.run();
		}
		
		@Override
		public void doneIt_() {
			workerRun_ = null;
		}
	}

	public SimStateNative getSimStateNative() {
		return fmuController_.getSimStateNative();	
	}



	
	


	
	


}
