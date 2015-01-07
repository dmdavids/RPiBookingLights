package main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * This is the main program for a Booking Traffic Light Program
 * that runs on a Raspberry Pi but is built by Eclipse on Windows
 * 
 * Define the preference file as an environment variable if the
 * default of lights.json is not used.
 * e.g.
 * sudo java -Dprefs=lights.json -jar lights.jar 
 * 
 * This application checks the Bookings API to get bookings 
 * and then set the GPIO pins for a red/yellow/green light 
 * i.e. traffic light signal.  
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
public class TrafficLightController {

	static final String DEFAULT_FILE_NAME = "lights.json";
	static final String PREFERENCES_FILE = "prefs";
	
	static SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) throws InterruptedException {
		
		//Check to see if the file name was given in the environment
		// for example -Dprefs=bandwidth.json
		String fileName = System.getProperty(PREFERENCES_FILE);
		if (fileName == null || fileName.isEmpty())
			fileName = DEFAULT_FILE_NAME;
		System.out.println("Reading Bookings Lights preferences from " + fileName);

		ConfigManager.setup(fileName);
		LightManager.initializeAndTest(ConfigManager.getPrefs());
		
		// forever loop 
		System.out.println("Booking Lights Program STARTING. Have a Good Day!");
		long sleepCount = ConfigManager.getMillisecondsToCheck();
		for (;;) {
			
			// Its Alive!
			String timestamp = timestampFormatter.format(Calendar.getInstance().getTime());
			if ( sleepCount >= ConfigManager.getMillisecondsToCheck()) {
				System.out.println(timestamp
						+ "<--Booking Lights--> ... Awake and checking bookings and lights.\n");
				// get the bookings and set the lights
				BookingManager.process(ConfigManager.getStartDate(), ConfigManager.getEndDate());
				sleepCount = 0;
			} else
			{
				System.out.println(timestamp
						+ "<--Booking Lights--> ... Awake setting lights only.\n");
			}
			
			// find a booking for the current time (assumes only one)
			// and set the lights appropriately 
			// if no current booking then turn all the lights off
			// NOTE: have to assume that the current light setting are 
			// good so we don't want to turn the lights off every time
			// or we will get a lot of flashing lights
			BookingManager.checkAndSetLights();

			// Finished
			timestamp = timestampFormatter.format(Calendar.getInstance().getTime());
			System.out.println(timestamp
							+ "<--Booking Lights--> ... Loop completed ... Sleeping for " 
							+ Integer.toString(ConfigManager.getPrefs().getMinutesToSleep()) + " minute(s).");
			
			// zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz
			Thread.sleep(ConfigManager.getMillisecondsToSleep());
			sleepCount += ConfigManager.getMillisecondsToSleep();
		} // end of forever loop
	}
}
