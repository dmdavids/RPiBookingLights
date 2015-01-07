package main;

/**
 * This class holds the preference information that 
 * is read from a file. 
 * 
 * The preference file is expected to be in JSON format
 * and should mirror this class or it will fail.
 * 
 * This program will only have one preference object.
 * 
 * TODO:  Add or modify these for your needs
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
public class Preferences {
	
	// These are the fields and the defaults to use for Booking Lights
	// These use defaults in case there is no prefs file but you can 
	// remove the defaults and force the use of a prefs file
	
	// this group is for the booking manager
	private String roomId = "Room_A";  // compares to class name
	private String apiURL = "http://dev.putyourapihere.com/";  // api to use to access bookings
	private int firstWarningMinutes = -15; // negative value needed for minutes before end time
	private int finalWarningMinutes = -5;  // negative value needed for minutes before end time
	private int minutesToSleep = 1;  // this is between updates to lights
	private int minutesToCheck = 5;  // this is between calls to the api
	private String requestDateFormat = "yyyy-MM-dd";  // for the filter of the dates in the api call
	private int hoursToCheck  = 24; // indicates how far ahead to check for bookings
	
	// this group is for the lights manager
	// this is the GPIO pins to use for lights
	private int greenPin = 0;
	private int yellowPin = 4;
	private int redPin = 5;
	private int testCycles = 3;  // number of times to cycle thru the light to prove they are up & running
	private boolean ignoreGPIO = true;  // false = running on RPi true = running in test environment e.g. windows
	

	public boolean isIgnoreGPIO() {
		return ignoreGPIO;
	}
	public void setIgnoreGPIO(boolean ignoreGPIO) {
		this.ignoreGPIO = ignoreGPIO;
	}
	public String getApiURL() {
		return apiURL;
	}
	public void setApiURL(String httpApi) {
		this.apiURL = httpApi;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		if (roomId == null || roomId.isEmpty()) 
			roomId = "Room_A";
		this.roomId = roomId;
	}
	public int getFirstWarningMinutes() {
		return firstWarningMinutes;
	}
	public void setFirstWarningMinutes(int firstWarningMinutes) {
		// first warning minutes need to be negative value
		if (firstWarningMinutes > 0)
			firstWarningMinutes *= -1;
		this.firstWarningMinutes = firstWarningMinutes;
	}
	public int getFinalWarningMinutes() {
		return finalWarningMinutes;
	}
	public void setFinalWarningMinutes(int finalWarningMinutes) {
		// final warning minutes need to be negative value
		if (finalWarningMinutes > 0)
			finalWarningMinutes *= -1;
		this.finalWarningMinutes = finalWarningMinutes;
	}
	public int getMinutesToSleep() {
		return minutesToSleep;
	}
	public void setMinutesToSleep(int minutesToSleep) {
		this.minutesToSleep = minutesToSleep;
	}
	
	public int getMinutesToCheck() {
		return minutesToCheck;
	}
	public void setMinutesToCheck(int minutesToCheck) {
		this.minutesToCheck = minutesToCheck;
	}
	public String getRequestDateFormat() {
		return requestDateFormat;
	}
	public void setRequestDateFormat(String requestDateFormat) {
		this.requestDateFormat = requestDateFormat;
	}
	public int getHoursToCheck() {
		return hoursToCheck;
	}
	public void setHoursToCheck(int hoursToCheck) {
		if (hoursToCheck < 0 || hoursToCheck > 60) 
			hoursToCheck = 24;
		this.hoursToCheck = hoursToCheck;
	}
	public int getGreenPin() {
		return greenPin;
	}
	public void setGreenPin(int greenPin) {
		if (greenPin < 0 || greenPin > 20) 
			greenPin = 1;
		this.greenPin = greenPin;
	}
	public int getYellowPin() {
		
		return yellowPin;
	}
	
	public void setYellowPin(int yellowPin) {
		if (yellowPin < 0 || yellowPin > 20) 
			yellowPin = 4;
		this.yellowPin = yellowPin;
	}
	public int getRedPin() {
		return redPin;
	}
	
	public void setRedPin(int redPin) {
		if (redPin < 0 || redPin > 20) 
			redPin = 5;
		this.redPin = redPin;
	}
	public int getTestCycles() {
		return testCycles;
	}
	public void setTestCycles(int testCycles) {
		this.testCycles = testCycles;
	}
	
	@Override
	public String toString() {
		return "Preferences [roomId=" + roomId + ", apiURL=" + apiURL
				+ ", firstWarningMinutes=" + firstWarningMinutes
				+ ", finalWarningMinutes=" + finalWarningMinutes
				+ ", minutesToSleep=" + minutesToSleep + ", minutesToCheck="
				+ minutesToCheck + ", requestDateFormat=" + requestDateFormat
				+ ", hoursToCheck=" + hoursToCheck + ", greenPin=" + greenPin
				+ ", yellowPin=" + yellowPin + ", redPin=" + redPin
				+ ", testCycles=" + testCycles + ", ignoreGPIO=" + ignoreGPIO
				+ "]";
	}
	
}
