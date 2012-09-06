package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sun.jna.Structure;

public class ScalarValueResults {
	

	public ScalarValueCollection input;
	public ScalarValueCollection output;
	private double time_;
	
	
	public ScalarValueResults(ScalarValueResultsStruct st1) {
		time_ = st1.time;
		input = new ScalarValueCollection(st1.input);
		output = new ScalarValueCollection(st1.output);
	}
	
	
	
	public String toString() {
		
		
		
		
		//convertToString(output.realValueAry);
		
		
	//	Vector<String> strList = getOutputList();
		//String str = "time: " + Double.toString(time);
		String str = output.toString();
				
		//strList.insertElementAt(timeStr,0);

		return str;
	}
	
	
	
	/**
	 * Convert to string.
	 *
	 * @param ary the ary
	 * @return the vector
	 */
	private Vector<String> convertToString(double[] ary) {

		String[] strArray = new String[ary.length];
		int len = ary.length;

		for (int i = 0; i < len; i++) {
			strArray[i] = Double.toString(ary[i]);
		}

		List<String> list = Arrays.asList(strArray);
		Vector<String> vector = new Vector<String>(list);

		return vector;
	}



	public double getTime() {
		// TODO Auto-generated method stub
		return time_;
	}







	
	
	
	
}
