package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultOfStep.
 */
public class ResultOfStep  extends PointerType implements NativeMapped  {

	/** The time_. */
	private double time;

	/** The input_. */
	private double[] input_;

	/** The output_. */
	private double[] output_;



	/**
	 * Instantiates a new result of step.
	 */
	public ResultOfStep() {

	}


	/**
	 * Instantiates a new result of step.
	 *
	 * @param resultOfStepStruct the result of step struct
	 */
	public ResultOfStep(ResultOfStepStruct resultOfStepStruct) {

		time = resultOfStepStruct.time;

		Pointer p = resultOfStepStruct.input.getPointer();
		input_ = p.getDoubleArray(0, resultOfStepStruct.inputLength);

		Pointer p2 = resultOfStepStruct.output.getPointer();
		output_ = p2.getDoubleArray(0, resultOfStepStruct.outputLength);
	}

	/**
	 * Gets the output list.
	 *
	 * @return the output list
	 */
	public Vector<String> getOutputList() {
		return convertToString(output_);
	}

	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	public double[] getInput() {

		return input_;
	}

	/**
	 * Gets the input list.
	 *
	 * @return the input list
	 */
	public Vector<String> getInputList() {

		return convertToString(input_);
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public double getTime() {

		return time;
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

	/**
	 * Input to string.
	 *
	 * @return the string
	 */
	public String inputToString() {

		Vector<String> strList = getInputList();
		String timeStr = "time: " + Double.toString(time);
		strList.insertElementAt(timeStr,0);

		return strList.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		Vector<String> strList = getOutputList();
		String timeStr = "time: " + Double.toString(time);
		strList.insertElementAt(timeStr,0);

		return strList.toString();
	}


	@Override
	public Object fromNative(Object nativeValue, FromNativeContext context) {

		// TODO Auto-generated method stub
		if (nativeValue == null) {

			return null;
		}


		try {
			PointerType pt = (PointerType)getClass().newInstance();

			pt.setPointer  ((Pointer)nativeValue);

			return pt;
		}
		catch (InstantiationException e) {
			throw new IllegalArgumentException("Can't instantiate " + getClass());
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Not allowed to instantiate " + getClass());

		}
	}





	@Override
	public Object toNative() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Class nativeType() {
		// TODO Auto-generated method stub
		return null;
	}

}
