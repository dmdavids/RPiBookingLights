package main;

import java.util.Map;
import java.util.concurrent.Future;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

/**
 * 
 * This is a class to allow creation of a debug version of a Pin
 * for this application.  
 * 
 * Currently used mostly for the name to be used for debug purposes
 * and to have non-null pins when debugging.
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */

public class TrafficLightDebugPin implements GpioPinDigitalOutput {

	private String name = "debug pin";
	
	public TrafficLightDebugPin(String string) {
		this.name = string;
	}

	public boolean isHigh() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLow() {
		// TODO Auto-generated method stub
		return false;
	}

	public PinState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isState(PinState state) {
		// TODO Auto-generated method stub
		return false;
	}

	public GpioProvider getProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public Pin getPin() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name) {
		this.name = name;
		
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public void setTag(Object tag) {
		// TODO Auto-generated method stub
		
	}

	public Object getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasProperty(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProperty(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeProperty(String key) {
		// TODO Auto-generated method stub
		
	}

	public void clearProperties() {
		// TODO Auto-generated method stub
		
	}

	public void export(PinMode mode) {
		// TODO Auto-generated method stub
		
	}

	public void unexport() {
		// TODO Auto-generated method stub
		
	}

	public boolean isExported() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setMode(PinMode mode) {
		// TODO Auto-generated method stub
		
	}

	public PinMode getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMode(PinMode mode) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPullResistance(PinPullResistance resistance) {
		// TODO Auto-generated method stub
		
	}

	public PinPullResistance getPullResistance() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPullResistance(PinPullResistance resistance) {
		// TODO Auto-generated method stub
		return false;
	}

	public GpioPinShutdown getShutdownOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setShutdownOptions(GpioPinShutdown options) {
		// TODO Auto-generated method stub
		
	}

	public void setShutdownOptions(Boolean unexport) {
		// TODO Auto-generated method stub
		
	}

	public void setShutdownOptions(Boolean unexport, PinState state) {
		// TODO Auto-generated method stub
		
	}

	public void setShutdownOptions(Boolean unexport, PinState state,
			PinPullResistance resistance) {
		// TODO Auto-generated method stub
		
	}

	public void setShutdownOptions(Boolean unexport, PinState state,
			PinPullResistance resistance, PinMode mode) {
		// TODO Auto-generated method stub
		
	}

	public void high() {
		// TODO Auto-generated method stub
		
	}

	public void low() {
		// TODO Auto-generated method stub
		
	}

	public void toggle() {
		// TODO Auto-generated method stub
		
	}

	public Future<?> blink(long delay) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> blink(long delay, PinState blinkState) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> blink(long delay, long duration) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> blink(long delay, long duration, PinState blinkState) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> pulse(long duration) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> pulse(long duration, boolean blocking) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> pulse(long duration, PinState pulseState) {
		// TODO Auto-generated method stub
		return null;
	}

	public Future<?> pulse(long duration, PinState pulseState, boolean blocking) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setState(PinState state) {
		// TODO Auto-generated method stub
		
	}

	public void setState(boolean state) {
		// TODO Auto-generated method stub
		
	}

}
