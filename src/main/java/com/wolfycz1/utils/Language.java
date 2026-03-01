package com.wolfycz1.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for retrieving localized strings using ResourceBundle.
 * @author wolfycz1
 */
@Slf4j
public class Language {
    private static ResourceBundle rb;
    private static final Locale defaultLocale = Locale.ENGLISH;

    static {
        load(defaultLocale);
    }

    /**
     * Loads the resource bundle for the specified locale.
     * @param locale The target locale.
     */
    public static void load(Locale locale) {
        rb = ResourceBundle.getBundle("locale", locale);
    }

    /**
     * Fetches a localized string using the provided key.
     * @param key The identifier for the localized text.
     * @return The translated string, or "MISSING {key}" if the key is not found, or null.
     */
    public static String get(String key) {
        if (key == null) return null;

        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            log.warn("No String for the key {}", key);
            return String.format("MISSING: %s", key);
        } catch (ClassCastException e) {
            log.warn("Object for the key {} is not a String", key);
            return String.format("MISSING: %s", key);
        }
    }

    /**
     * Fetches a localized string and formats it using the provided arguments.
     * @param key The identifier for the localized text.
     * @param args The variables to inject into the localized format string.
     * @return The translated and formatted string, or null.
     * @see Language#get(String)
     */
    public static String get(String key, Object... args) {
        return String.format(get(key), args);
    }

    /**
     * Fetches a localized comma-separated string and splits it into an array.
     * @param key The identifier for the localized list.
     * @return An array of strings. Returns an empty array if the key is missing or invalid, or null.
     */
    public static String[] getArray(String key) {
        if (key == null) return null;

        try {
            String value = rb.getString(key);
            return value.split("\\s*,\\s*");
        } catch (MissingResourceException e) {
            log.warn("No Array for the key {}", key);
            return new String[0];
        } catch (ClassCastException e) {
            log.warn("Object for the key {} is not a String Array", key);
            return new String[0];
        }
    }
}
