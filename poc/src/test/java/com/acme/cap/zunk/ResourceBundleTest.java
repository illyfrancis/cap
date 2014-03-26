package com.acme.cap.zunk;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

public class ResourceBundleTest {

    @Test
    public void testResourceBundle() {
        ResourceBundle bundle = ResourceBundle.getBundle("Message");
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("First Name", firstName);
        assertEquals("Last Name", lastName);
    }

    @Test
    public void testResourceBundleWithLocale() {
        Locale locale = new Locale.Builder().setLocale(Locale.getDefault()).setVariant("ACMES").build();
        ResourceBundle bundle = ResourceBundle.getBundle("Message", locale);
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("ACMES", firstName);
        assertEquals("Last Name", lastName);
    }

    @Test
    public void testResourceBundleWithShortVariant() {
        // Locale locale = new
        // Locale.Builder().setLocale(Locale.getDefault()).setExtension('x',
        // "BBH").build();
        Locale locale = new Locale("en", "US", "BBH");

        ResourceBundle bundle = ResourceBundle.getBundle("Message", locale);
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("BBH", firstName);
        assertEquals("Last Name", lastName);
    }

    @Test
    public void testResourceBundleWithNullCountry() {
        Locale locale = new Locale.Builder().setLocale(Locale.ENGLISH).setVariant("ABCDE").build();
        System.out.println(locale.toString());

        ResourceBundle bundle = ResourceBundle.getBundle("Message", locale);
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("ABCDE", firstName);
        assertEquals("Last Name", lastName);
    }

    @Test
    public void testResourceBundleWithExtension() {
        Locale locale = new Locale.Builder()
            .setLocale(Locale.getDefault())
            .setVariant("ABCDE")
            .setExtension(Locale.PRIVATE_USE_EXTENSION,"BBH")
            .build();
        
        System.out.println(locale);
        
        Locale[] locales = Locale.getAvailableLocales();
        for (int i=0; i < locales.length; i++) {
            System.out.println("- " + locales[i]);
        }
        
        ResourceBundle bundle = ResourceBundle.getBundle("Message", locale);
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("BBH", firstName);
        assertEquals("Last Name", lastName);
    }
    
    @Test
    public void testGettingUndefined() {
        ResourceBundle bundle = ResourceBundle.getBundle("Message");
        String value = bundle.getString("doesnt.exist");
        assertNotNull(value);
    }
}
