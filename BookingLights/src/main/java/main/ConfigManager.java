package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.Gson;

public class ConfigManager {
	
	static private  Preferences prefs = new Preferences();
	static private String preferenceFileName;
	
	static private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	static private Gson gson = new Gson();
	
	// read the preference file given by the parameter
	static public void setup(String fileName) {
		preferenceFileName = fileName;
		readPreferences();
	    writePreferences(); // uncomment this line if changing the prefs object to write a new file
	}

	// used by other classes to get preference information
	public static Preferences getPrefs() {
		return prefs;
	}

	// open the file configured by the setup method
	// and read in the preference information
	// expected to be in JSON format and to match
	// the Preferences Class
	static private void readPreferences() {
	    BufferedReader br;
		try {
			br = new BufferedReader(  
				     new FileReader(preferenceFileName));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open " + preferenceFileName + "... using defaults.\n" + prefs);
			return;
		}  
	    prefs = gson.fromJson(br, Preferences.class);
		System.out.println("Current Preferences are:\n" + prefs);
	}
	

	// calculate the begin and end date parameters 
    // start date is current day
	// end date is current day plus number of configured hours
	static public Calendar getToday() {
		// safer to calculate these values at the same time
		Calendar today = Calendar.getInstance();
		return today;
	}
	
	static public Calendar getEndDay() {
		Calendar endDay = Calendar.getInstance();
		endDay.add(Calendar.HOUR_OF_DAY, prefs.getHoursToCheck());
		return endDay;
	}
	
	public static String getStartDate() {
		Calendar today = Calendar.getInstance();
		return dateFormatter.format(today.getTime());
	}

	public  static String getEndDate() {
		Calendar endDay = Calendar.getInstance();
		endDay.add(Calendar.HOUR_OF_DAY, prefs.getHoursToCheck());
		return dateFormatter.format(endDay.getTime());
	}

	// calculate milliseconds to sleep given the
	// number of minutes configured for sleeping
	// this is used to sleep between calls to the 
	// booking API
	static public long getMillisecondsToSleep() {
		return 60000L * prefs.getMinutesToSleep();
	}
	
	static public long getMillisecondsToCheck() {
		return 60000L * prefs.getMinutesToCheck();
	}


	/* Activate this method if the preferences change so that 
	 * we can write out a good preference file and not worry about
	 * errors. 
	 */
	static private void writePreferences() {
		String jsonString = gson.toJson(prefs);
		System.out.println("Current Preferences are:\n" + jsonString);
		try {
			FileWriter fw = new FileWriter("output.json");
			fw.write(jsonString);
			fw.close();
		} catch (IOException e) {
			System.out.println("Unable to write out the preferences to file output.json");
			e.printStackTrace();
		}
	}
	
}
