package uk0ok.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author : uk0ok
 * @version : 1.0
 */
public class Config {
	// Keep the information read from the properties in the map.
	private static HashMap<String, String> config = new HashMap<String, String>();

	/**
	 * Read the properties in the inputted property path and put them in the map.
	 * @param configLocation
	 * @throws IOException
	 */
	public static void setConfig(String configLocation) throws IOException {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(configLocation));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Configuration File not Found.");
		} catch (IOException e) {
			throw new IOException(e);
		}

		prop.entrySet().forEach(t -> setConfig(t.getKey(), t.getValue()));
	}

	/**
	 * Store the inputted Object key and value in the map.
	 * @param key
	 * @param value
	 */
	public static void setConfig(Object key, Object value) {
		if (key != null) { setConfig(key.toString(), value != null ? value.toString() : null); }
	}

	/**
	 * Store the inputted key and value in the map.
	 * @param key
	 * @param value
	 */
	public static void setConfig(String key, String value) {
		config.put(key, value);
	}

	/**
	 * Returns a value in the form of a string that is symmetrical to the key.
	 * @param key
	 * @return string
	 * @throws NullPointerException
	 */
	public static String getConfig(String key) throws NullPointerException {
		String value = config.get(key);
		if (value == null || value == "") {
			throw new NullPointerException("Configuration [ " + key + " ] is Null");
		}

		return value;
	}

	/**
	 * Returns a value in the form of a integer that is symmetrical to key.
	 * @param key
	 * @return int
	 * @throws NumberFormatException
	 */
	public static int getIntConfig(String key) throws NumberFormatException {
		String value = config.get(key);
		if (value == null || value == "") {
			throw new NullPointerException("Configuration [ " + key + " ] is Null");
		}
		return Integer.parseInt(config.get(key));
	}

	/**
	 * Returns a value in the form of a array that is symmetrical to the key.
	 * @param key
	 * @return String[]
	 */
	public static String[] getArrConfig(String key) {
		return config.get(key).split(",");
	}
}