package main;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *  This class manages the Raspberry PI B+ GPIO for the 
 *  red, yellow and green lights.
 *  
 *  This class allows any pin to be set to any of the 20
 *  GPIO ports and does NOT validate that the
 *  pins are different from each other.  If an invalid
 *  pin number is chosen it will default to the original
 *  pin numbers green=0, yellow=4, red=5.
 *  The pins used for each light are configurable 
 *  via the preferences.
 * 
 *  Testing on a non-pi device can be done by
 *  setting the ignoreGPIO preference to true.
 *  
 *  Includes a testing cycle on initialization 
 *  to allow user to make sure that the lights
 *  are activating.  The number of cycles are
 *  configurable through preferences so setting
 *  the number to 0 will effectively turn this off.
 *  
 * 
 * @author Daun Davids for Sky Woman Technology LLC
 *
 */
public class LightManager { 
	
	// configuration used for the light manager
	static private Preferences prefs;
	// configuration for setting GPIO to high or low 
	// when "on"
	static boolean isOnHigh = true;

	// true allows to test code on non-pi device
	// set actual value via the preferences
	static boolean ignoreGPIO = false; 

	// expects three pins to be used
	// but configuration can make it look like less
	static GpioPinDigitalOutput greenPin;
	static GpioPinDigitalOutput yellowPin;
	static GpioPinDigitalOutput redPin;

	
	// milliseconds so 1000 = 1 second
	static long testCycleSleepTime = (long) (1000 * 1.5); 


	public static void setPrefs(Preferences prefs) {
		LightManager.prefs = prefs;
	}
	
	public static void setIgnoreGPIO(boolean ignoreGPIO) {
		LightManager.ignoreGPIO = ignoreGPIO;
	}

	public static void initializeAndTest(Preferences prefs) {
		LightManager.prefs = prefs;
		ignoreGPIO = prefs.isIgnoreGPIO();
		if (ignoreGPIO)
			System.out
					.println("Warning! GPIO pins are being ignored. "
							+ "Modify preference file to activate.");
		isOnHigh =  prefs.isOnSetHigh();
		System.out.println("GPIO will set pins to " 
		                   + (isOnHigh ? "high" : "low") 
				           + " when turning them on.");
		setupGPIOpins();
		try {
			testLights();
		} catch (InterruptedException e) {
			System.out.println("-- Warning! Light Test Interrupted. --");
			e.printStackTrace();
		}
	}

	private static void setupGPIOpins() {

		System.out.println("GPIO Pin Settings ->" + "\nGREEN  : "
				+ Integer.toString(prefs.getGreenPin()) + "\nYELLOW : "
				+ Integer.toString(prefs.getYellowPin()) + "\nRED    : "
				+ Integer.toString(prefs.getRedPin()));

		if (!ignoreGPIO) {
			GpioController gpio = GpioFactory.getInstance();
			greenPin = gpio
					.provisionDigitalOutputPin(
							getRaspiPin(prefs.getGreenPin()), "GREEN_LED",
							PinState.LOW);
			
			yellowPin = gpio.provisionDigitalOutputPin(
					getRaspiPin(prefs.getYellowPin()), "YELLOW_LED",
					PinState.LOW);
			
			redPin = gpio.provisionDigitalOutputPin(
					getRaspiPin(prefs.getRedPin()), "RED_LED", PinState.LOW);
		} else {
			greenPin = new TrafficLightDebugPin("GREEN pin (debug only NO RPi)");
			redPin = new TrafficLightDebugPin("RED pin (debug only NO RPi)");
			yellowPin = new TrafficLightDebugPin("YELLOW pin ((debug only NO RPi)");
		}
		
	}

	private static void testLights() throws InterruptedException {
		System.out.println("===> Running "
				+ Integer.toString(prefs.getTestCycles())
				+ " Light Test Cycles. <===");
		turnOffAllLights();
		for (int i = 0; i < prefs.getTestCycles(); i++) {
			System.out.println("===> Light Test Cycle "
					+ Integer.toString(i + 1));
			System.out.println("         >> ON/OFF Test");
			turnOnLight(greenPin);
			Thread.sleep(testCycleSleepTime);
			turnOnLight(yellowPin);
			Thread.sleep(testCycleSleepTime);
			turnOnLight(redPin);
			Thread.sleep(testCycleSleepTime);
			System.out.println("         >> Blinking Test");
			blinkLight(greenPin, 3);
			Thread.sleep(testCycleSleepTime);
			blinkLight(redPin, 3);
			Thread.sleep(testCycleSleepTime);
			blinkLight(yellowPin, 3);
			Thread.sleep(testCycleSleepTime);
		}
		turnOffAllLights();
		System.out.println("===> Light Test Cycles Completed.<===");
	}

	public static void goodtoGo() {
		try {
			blinkLight(greenPin, prefs.getBlinkGreen());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		turnOnLight(greenPin);
	}
	
	public static void firstWarning() {
		try {
			blinkLight(yellowPin, prefs.getBlinkYellow());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		turnOnLight(yellowPin);
	}
	
	
	public static void finalWarning() {
		try {
			blinkLight(redPin, prefs.getBlinkRed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		turnOnLight(redPin);
	}
	
	public static void turnOnLight(GpioPinDigitalOutput pin){
		System.out.println(pin.getName() + " is being Turned On.");
		if (!ignoreGPIO) {
			turnOffAllLights();
			if(isOnHigh) 
				pin.high();
			else
				pin.low();
		}
	}
	
	public static void turnOffLight(GpioPinDigitalOutput pin){
		System.out.println(pin.getName() + " is being Turned OFF.");
		if(isOnHigh)
			pin.low();
		else
			pin.high();
	}
	
	public static void blinkLight(GpioPinDigitalOutput pin, int blinks) throws InterruptedException{
		System.out.println(pin.getName() + " is blinking " + blinks 
				           + " times every " + prefs.getBlinkTime() 
				           + " milliseconds.");
		turnOffAllLights();
		for(int i = 0; i < blinks; i++) {
			turnOffAllLights();
			turnOnLight(pin);
			Thread.sleep(prefs.getBlinkTime());
		}
		turnOffAllLights();
	}

	
	public static void turnOffAllLights() {
		if (!ignoreGPIO) {
			turnOffLight(greenPin);
			turnOffLight(redPin);
			turnOffLight(yellowPin);
		} 
		System.out.println("ALL lights OFF.");
	}

	private static Pin getRaspiPin(int value) {
		Pin returnPin;

		switch (value) {
		case 0:
			returnPin = RaspiPin.GPIO_00;
			break;
		case 1:
			returnPin = RaspiPin.GPIO_01;
			break;
		case 2:
			returnPin = RaspiPin.GPIO_02;
			break;
		case 3:
			returnPin = RaspiPin.GPIO_03;
			break;
		case 4:
			returnPin = RaspiPin.GPIO_04;
			break;
		case 5:
			returnPin = RaspiPin.GPIO_05;
			break;
		case 6:
			returnPin = RaspiPin.GPIO_06;
			break;
		case 7:
			returnPin = RaspiPin.GPIO_07;
			break;
		case 8:
			returnPin = RaspiPin.GPIO_08;
			break;
		case 9:
			returnPin = RaspiPin.GPIO_09;
			break;
		case 10:
			returnPin = RaspiPin.GPIO_10;
			break;
		case 11:
			returnPin = RaspiPin.GPIO_11;
			break;
		case 12:
			returnPin = RaspiPin.GPIO_12;
			break;
		case 13:
			returnPin = RaspiPin.GPIO_13;
			break;
		case 14:
			returnPin = RaspiPin.GPIO_14;
			break;
		case 15:
			returnPin = RaspiPin.GPIO_15;
			break;
		case 16:
			returnPin = RaspiPin.GPIO_16;
			break;
		case 17:
			returnPin = RaspiPin.GPIO_17;
			break;
		case 18:
			returnPin = RaspiPin.GPIO_18;
			break;
		case 19:
			returnPin = RaspiPin.GPIO_19;
			break;
		case 20:
			returnPin = RaspiPin.GPIO_20;
			break;
		default:
			returnPin = RaspiPin.GPIO_00;
		}
		return returnPin;
	}
}
