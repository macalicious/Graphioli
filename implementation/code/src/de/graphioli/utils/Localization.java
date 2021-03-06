package de.graphioli.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class is responsible for loading the correct localization file.
 * 
 * @author Team Graphioli
 */
public final class Localization {

	/**
	 * Logging instance.
	 */
	private static final Logger LOG = Logger.getLogger(Localization.class.getName());

	/**
	 * Private constructor to ensure that no instance of this class is created.
	 */
	private Localization() {
	}

	/**
	 * Gets the respective string to a given key value.
	 * 
	 * @param key
	 *            given key
	 * @return the string that corresponds to the key
	 */
	public static String getLanguageString(String key) {

		ResourceBundle bundle = null;

		try {
			bundle = ResourceBundle.getBundle("language/lang");
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			LOG.severe("Can't find locale file, switch to default language.");
		}

		return "";
	}
}
