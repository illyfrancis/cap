package com.acme.cap.zunk;

import org.junit.Test;

import com.acme.cap.domain.UtrSnapshot;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

public class Hasher {

    @Test
    public void testHasher() {

        UtrSnapshot snapshot = new UtrSnapshot.Builder(100L, 1).accountNumber("A001").amount(100L)
                .currency("USD").build();

        Funnel<UtrSnapshot> funnel = new Funnel<UtrSnapshot>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void funnel(UtrSnapshot from, PrimitiveSink into) {
                into.putLong(from.getUtrRegisterId())
                        .putString(from.getAccountNumber(), Charsets.UTF_8)
                        .putLong(from.getAmount())
                        .putString(from.getCurrency(), Charsets.UTF_8);
            }
        };

        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher()
                .putObject(snapshot, funnel)
                .hash();

        System.out.println(">>> " + hc.asLong() + " : " + hc.asInt());

        snapshot = new UtrSnapshot.Builder(100L, 1).accountNumber("A001").amount(101L)
                .currency("USD").build();

        hc = hf.newHasher()
                .putObject(snapshot, funnel)
                .hash();

        System.out.println(">>> " + hc.asLong() + " : " + hc.asInt());

        snapshot = new UtrSnapshot.Builder(100L, 1).accountNumber("A001").amount(100L)
                .currency("USD").build();

        hc = hf.newHasher()
                .putObject(snapshot, funnel)
                .hash();

        System.out.println(">>> " + hc.asLong() + " : " + hc.asInt());
    }
}
