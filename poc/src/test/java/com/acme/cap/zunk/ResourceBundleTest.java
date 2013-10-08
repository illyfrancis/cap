package com.acme.cap.zunk;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
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
    public void testResourceBundleWithExtension() {
        Locale locale = new Locale.Builder().setLocale(Locale.getDefault()).setExtension('x', "ACMES").build();
        ResourceBundle bundle = ResourceBundle.getBundle("Message", locale);
        String firstName = bundle.getString("firstname");
        String lastName = bundle.getString("lastname");
        assertEquals("ACMES", firstName);
        assertEquals("Last Name", lastName);
    }

}
