package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * TODO: This class should mirror the JSON that 
 * is received from the Booking API.
 * 
 * Getter and Setter required for all fields
 * that are not JSON ignored or transient.
 * 
 * hashCode() & equals() methods are used to 
 * sort the bookings by start date so they need
 * to be accurate.
 * 
 * toString() used for status printing 
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
public class Booking {
	
	// these are just an example of a
	// possible Booking object where all the
	// fields are strings
	private String id;
	private String roomId;
	private String start;
	private String end;
	
	// these store the start & end dates 
	// as Calendar Objects
	private transient Calendar startDate;
	private transient Calendar endDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	public Calendar getStartDate() {
		if (startDate == null && start != null && !start.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal  = Calendar.getInstance();
			try {
				cal.setTime(df.parse(start));
				startDate = cal;
			} catch (ParseException e) {
				startDate = null;
				e.printStackTrace();
			}
		}
		return startDate;
	}
	
	public Calendar getEndDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal  = Calendar.getInstance();
		if (endDate == null && end != null && !end.isEmpty()) {
			try {
				cal.setTime(df.parse(end));
				endDate = cal;
			} catch (ParseException e) {
				endDate = null;
				e.printStackTrace();
			}
		}
		return endDate;
	}
	
	@Override
	public String toString() {
		return "Booking [id=" + id + ", roomId=" + roomId + ", start=" + start
				+ ", end=" + end +  "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roomId == null) {
			if (other.roomId != null)
				return false;
		} else if (!roomId.equals(other.roomId))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
	
}
