package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import com.google.gson.Gson;

/**
 * This class manages the booking information including:
 * 
 * -- retrieving it from the Booking API
 * -- cleaning out old date from the list of bookings
 * -- telling the light manager which lights it should activate
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
public class BookingManager {
	
	// TODO: set true to use fake data or false to use API 
	static private final boolean testingInProgress = true;  
	
	static private Gson gson = new Gson();
	static private final String LINE_SEPARATOR = "========================================================";
	
	// store the bookings as a tree set to make sure they are sorted by the start time
	// TODO: Change the Comparator to work for your Booking instances
	private static Collection<Booking> bookings = new TreeSet<Booking>(
			new StartTimeComparator());
	
	// main booking processing also tells light manager which lights to set
	public static void process(String startDate, String endDate) {

		// get the bookings from the website using the start and end dates for 
		// filtering the request from the booking api
		Booking[] booked = retrieve(startDate, endDate);
		System.out.println(LINE_SEPARATOR);
		System.out.println("Number of total bookings found: " + booked.length);
		
		// add the bookings to the collection only if they match the roomId that 
		// was configured in the preferences && if the end time is after the
		// current time
		// Currently uses the booking's class name as the room identifier
		//...may need to change this?
		for (Booking b : booked) {
			if (b.getRoomId().contentEquals(ConfigManager.getPrefs().getRoomId())) {
				bookings.add(b);
			}
		}
		
		// clean out any old bookings so we don't take up more memory than we need
		clean(Calendar.getInstance());
		System.out.println(LINE_SEPARATOR);
		System.out.println("Number of bookings for today in " 
				+ ConfigManager.getPrefs().getRoomId()
				+ ": " + bookings.size());
		for (Booking b : bookings) {
			System.out.println("Booking " + b.toString());
		}
		System.out.println(LINE_SEPARATOR);
	}

	public static void checkAndSetLights() {
		System.out
				.println("<--Booking Lights--> ... Checking lights.\n");
		Booking[] currentBookings = bookings.toArray(new Booking[bookings.size()]);
		boolean found = false;
		for (Booking b : currentBookings) {
			if (setLights(b)) { 
				found = true;
				break;
			}
		}
		if (!found) {
			LightManager.turnOffAllLights();
			System.out
					.println("None of the bookings appear to be current.\n");
		}
	}

	// Only process the lights if the booking is current
	// if the booking is not current let the calling method handle it
	// however they see fit.
	private static boolean setLights(Booking booking) {
		if (isBookingCurrent(booking.getStartDate(), booking.getEndDate())) {
			System.out.println("Current Booking in Progress. Setting Lights.  Booking Info: " + booking.toString());
			if (isFinalWarningActivated(booking.getEndDate())) {
				LightManager.finalWarning();
			}
			else if (isFirstWarningActivated(booking.getEndDate())) {
				LightManager.firstWarning();
			} else {
				LightManager.goodtoGo();
			}
			return true;
		}
		return false;
	}
	
	// remove bookings from the collection that have an end time less than 
	// the current time so we don't keep a bunch of junk that can slow us down
	private static void clean(Calendar today) {
		Booking[] oldBookings = bookings.toArray(new Booking[bookings.size()]);
		System.out.println(LINE_SEPARATOR);
		System.out.println("Number of old bookings : " + oldBookings.length);
		for (Booking b : oldBookings) {
			if (b.getEndDate().getTimeInMillis() < today.getTimeInMillis()) {
				System.out.println("Removing old booking : " + b.toString());
				bookings.remove(b);
			}
		}
		System.out.println(LINE_SEPARATOR);
	}

	// GET the bookings from the booking API and store then in an array
	// must build the URL with the correct filter information
	static private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Booking[] retrieve(String startDate, String endDate) {
		if (!testingInProgress) {
			// build API URL and then get the booking in JSON Format
			String jsonBookings = getHTTPData(buildURL(startDate, endDate));
			System.out.println(jsonBookings);
		
			// parse the booking information
			return gson.fromJson(jsonBookings, Booking[].class);
		}
		else {
			// TODO: put fake booking data here when testingInProgress is true;
			System.out.println("Testing In Progress = true; Using FAKE Data.");
			if (bookings.isEmpty()) {
			Collection<Booking> fakeBookings = new HashSet<Booking>();
			Booking testBooking = new Booking();
			testBooking.setId("1235");
			testBooking.setRoomId(ConfigManager.getPrefs().getRoomId());
			Calendar today = Calendar.getInstance();
			testBooking.setStart(dateFormatter.format(today.getTime()));
			// warning this sets bookings for an hour... long test cycle!
			today.add(Calendar.HOUR_OF_DAY, 1);  
			testBooking.setEnd(dateFormatter.format(today.getTime()));
			fakeBookings.add(testBooking);
			Booking[] newBookings = fakeBookings.toArray(new Booking[fakeBookings.size()]);
			return newBookings;
			}
			return new Booking[0];
		}
	}

	// TODO: This is where you can build your URL string for filtering the data from the API
	private static String buildURL(String startDate, String endDate) {
		String filter = "?" + startDate + endDate; //  fix this to work with your api
		return ConfigManager.getPrefs().getApiURL() + filter;
	}

	
	// tests to see if booking is current
	private static boolean isBookingCurrent(Calendar startDate, Calendar endDate){
		Calendar now = Calendar.getInstance();
		return (startDate.getTimeInMillis() <= now.getTimeInMillis()
				&& endDate.getTimeInMillis() >= now.getTimeInMillis());
	}
	
	// tests to see if the first warning time has occurred
	private static boolean isFirstWarningActivated(Calendar endDate) {
		Calendar now = Calendar.getInstance();
		Calendar warnTime = calculateFirstWarningTime(endDate);
		return (warnTime.getTimeInMillis() <= now.getTimeInMillis());
	}
	
	// tests to see if the final warning time has occurred
	private static boolean isFinalWarningActivated(Calendar endDate) {
		Calendar now = Calendar.getInstance();
		Calendar warnTime = calculateFinalWarningTime(endDate);
		return (warnTime.getTimeInMillis() <= now.getTimeInMillis());
	}
	
	// calculates the time that the first warning should be activated
	private static Calendar calculateFirstWarningTime(Calendar endTime){
		Calendar warningTime = (Calendar) endTime.clone();
		warningTime.add(Calendar.MINUTE, ConfigManager.getPrefs().getFirstWarningMinutes());
		return warningTime;
	}
	
	// calculates the time that the final warning should be activated
	private static Calendar calculateFinalWarningTime(Calendar endTime){
		Calendar warningTime = (Calendar) endTime.clone();
		warningTime.add(Calendar.MINUTE, ConfigManager.getPrefs().getFinalWarningMinutes());
		return warningTime;
	}

	/**
	 * Common HTTP functionality to perform a GET operation which reads in a
	 * string in JSON format
	 *
	 * @param apiUrl
	 *            Url request
	 * @return String with the JSON data
	 */
	private static String getHTTPData(String apiUrl) {

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String apiJsonStr = null;

		try {

			URL url = new URL(apiUrl);
			System.out.println("Processing URL: <" + url.toString() + ">");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
			if (buffer.length() == 0) {
				System.out.println("NO Bookings for Today");
				return null;
			}
			apiJsonStr = buffer.toString();
		} catch (IOException e) {
			System.out.println("Exception Encountered: " + e.getMessage());
			return null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					// do nothing with this exception
					System.out.println("Exception Encountered: "
							+ e.getMessage());
				}
			}
		}
		return apiJsonStr;
	}
}

/**
 * 
 * Compares the starting time strings to allow
 * the bookings to be ordered.  
 * 
 * TODO: Expects that the strings are formatted as
 * YYYY-MM-DD HH:MM:SS in order for this to work
 * If the strings are ever changed a different
 * comparator should be used !
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
class StartTimeComparator implements Comparator<Booking> {

	public int compare(Booking b1, Booking b2) {
		return b1.getStart().compareTo(b2.getStart());
	}
}
