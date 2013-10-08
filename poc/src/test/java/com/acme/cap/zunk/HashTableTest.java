package com.acme.cap.zunk;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Objects;

public class HashTableTest {

    public static class Key {
        String name;

        public Key(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Key)) {
                return false;
            }

            Key other = (Key) obj;
            return Objects.equal(name, other.name);
        }

        @Override
        public int hashCode() {
            return 100;
//            return Objects.hashCode(name);
        }
    }

    @Test
    public void testMap() {

        Map<Key, String> items = new HashMap<>();
        Key keyOne = new Key("one");
        items.put(keyOne, "one");
        
        Key keyTwo = new Key("two");
        items.put(keyTwo, "two");
        
        Key keyThree = new Key("three");
        items.put(keyThree, "three");

        Key keyGhost = new Key("one");

        assertEquals("one", items.get(keyOne));
        assertEquals("two", items.get(keyTwo));
        assertEquals("one", items.get(keyGhost));
        assertEquals(keyGhost, keyOne);
    }
}
