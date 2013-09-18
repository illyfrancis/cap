package com.acme.cap.integration;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Stopwatch;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashingTest {

    @Test
    public void testOne() {
        HashCode hash = Hashing.murmur3_128().newHasher().putUnencodedChars("hello").hash();
        long val = hash.asLong();
        // int val = hash.asInt();
        System.out.println("> hash : " + val);

        hash = Hashing.murmur3_32().newHasher().putUnencodedChars("hello").hash();
        // long val = hash.asLong();
        val = hash.asInt();
        System.out.println("> hash : " + val);
        
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();

        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putLong(123L).putBoolean(false).hash();
        HashCode hc2 = hf.newHasher().putBoolean(false).putLong(123L).hash();
        
        stopwatch.stop();
        
        long mili = stopwatch.elapsed(TimeUnit.MICROSECONDS);
        long another = stopwatch.elapsed(TimeUnit.NANOSECONDS);

        System.out.format("hc [%s] hc2 [%s] mili [%d] nano [%d]", hc.asLong(), hc2.asLong(), mili, another);
    }

}
