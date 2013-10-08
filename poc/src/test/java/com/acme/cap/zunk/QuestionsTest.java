package com.acme.cap.zunk;

import static org.junit.Assert.*;
import org.junit.Test;

public class QuestionsTest {

    @Test
    public void testStringReverse() {
        String dwarves = "bashful doc dopey grumpy happy sleepy sneezy";

        String reversed = "";
        String[] dwarfArray = dwarves.split(" ");
        for (int i = 0; i < dwarfArray.length; i++) {
            reversed = dwarfArray[i] + " " + reversed;
        }
        
        reversed = reversed.trim();
        assertEquals("sneezy sleepy happy grumpy dopey doc bashful", reversed);
    }
    
}
