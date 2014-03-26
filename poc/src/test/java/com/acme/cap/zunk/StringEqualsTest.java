package com.acme.cap.zunk;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringEqualsTest {

    @Test
    public void testShouldEqual() {
        String s1 = "abcd";
        String s2 = new String("abcd");
        
        assertTrue(s1.equals(s2));
        assertFalse(s1 == s2);
    }
}
