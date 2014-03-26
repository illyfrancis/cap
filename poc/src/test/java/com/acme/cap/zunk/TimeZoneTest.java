package com.acme.cap.zunk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Ignore;
import org.junit.Test;

public class TimeZoneTest {
    
    @Test
    @Ignore
    public void testAvailableIDs() {
        String[] tz = TimeZone.getAvailableIDs();
        for (String s : tz) {
            System.out.println("> " + s);
        }
    }

    @Test
    public void testDefaultTimeZone() {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat format = new SimpleDateFormat();
        System.out.println("> local > " + format.format(date));
        
        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        
        System.out.println("> sydoz > " + format.format(date));        
    }
    
    @Test
    public void testJPLocaleTimeZone() {
        Date date = new Date(System.currentTimeMillis());
        
        SimpleDateFormat format = new SimpleDateFormat("dd/M/YYYY", Locale.JAPANESE);
        System.out.println("> .norml > " + format.format(date));
        System.out.println("> .local > " + format.format(date));
        
        System.out.println(format.getTimeZone());
        
        
        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        
        System.out.println("> .sydoz > " + format.format(date));
    }

    @Test
    public void whatIsDefaultLocale() {
        SimpleDateFormat format = new SimpleDateFormat();
        format.isLenient();
    }
    
}
