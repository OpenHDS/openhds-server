package org.openhds.report.calculations;

/**
 * @author Ime
 * This class contains methods for demographic rate calculations
 */
public class Formulas {

	public static int min(int i1, int i2) {
		return i1 < i2 ? i1 : i2;
    }
    
    // note that the addition of 365.25 to intervalEnd is used to capture those who have not entered the next interval
    // e.g. someone who is 4 years and 11 months will fall under 0-4 and 1-4 intervals, not into the 5 - 9 interval
    // all interval checking depends on this method
    public static boolean inInterval(double value, double intervalStart, double intervalEnd){
    	return ( (intervalEnd + 365.25) > value && !(value < intervalStart)) ? true : false;
    }  
}
