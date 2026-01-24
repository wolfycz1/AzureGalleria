package com.wolfycz1;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Slf4j
public class Language {
    private static ResourceBundle rb;
    private static final Locale defaultLocale = Locale.ENGLISH;

    static {
        load(defaultLocale);
    }

    public static void load(Locale locale) {
        rb = ResourceBundle.getBundle("locale", locale);
    }

    public static String get(String key) {
        if (key == null) return null;

        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            log.warn("No String for the key {}", key);
            return String.format("MISING: %s", key);
        } catch (ClassCastException e) {
            log.warn("Object for the key {} is not a String", key);
            return String.format("MISING: %s", key);
        }
    }

    public static String get(String key, Object... args) {
        return String.format(get(key), args);
    }

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
