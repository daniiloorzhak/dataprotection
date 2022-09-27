package com.mycompany.app;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.mycompany.app.CryptoLib;

import org.junit.Assert;

public class AppTest 
{

    @Test
    public void testModExp() {
        assertEquals(17, CryptoLib.modExp(5, 7, 23));
        assertEquals(1,  CryptoLib.modExp(3, 2, 4));
        assertEquals(4,  CryptoLib.modExp(2, 6, 10));
        assertEquals(3,  CryptoLib.modExp(2, 3, 5));
        assertEquals(6,  CryptoLib.modExp(2, 5, 13));
    }

    @Test
    public void testDhKeyExchange() {
        assertEquals(10, CryptoLib.dhke(7, 13, 23, 5));
    }

    @Test
    public void testEEA() {
        assertArrayEquals(new int[] {10, 1, -1}, CryptoLib.eea(30, 20));
        assertArrayEquals(new int[] {1, -2, 3},  CryptoLib.eea(28, 19));
        assertArrayEquals(new int[] {5, 1, -2},  CryptoLib.eea(35, 15));
    }

    @Test
    public void testDSGS () {
        assertEquals(5, CryptoLib.bsgs(2, 9, 23));
    }

    @Test 
    public void testShamir () {
        int encodedInfo = CryptoLib.encodeShamir(10, 23, 7, 19, 5);
        assertEquals(19, encodedInfo);
        
        int decodedInfo = CryptoLib.decodeShamir(encodedInfo, 9, 23);
        assertEquals(10, decodedInfo);
    }

    @Test 
    public void testElgamal () {
        int encodedInfo = CryptoLib.encodeElgamal(15, 23, 5, 13, 7);
        assertEquals(12, encodedInfo);
    
        int decodedInfo = CryptoLib.decodeElgamal(encodedInfo, 5, 7, 23, 13);
        assertEquals(15, decodedInfo);
    }

    @Test 
    public void testVernam () {
        int encodedInfo = CryptoLib.encodeVernam(0b01001, 0b11010);
        assertEquals(0b10011, encodedInfo);

        int decodedInfo = CryptoLib.decodeVernam(encodedInfo, 0b11010);
        assertEquals(0b01001, decodedInfo);
    }

    @Test 
    public void testRSA () {
        int encodedInfo = CryptoLib.encodeRSA(15, 3, 11, 33, 3);
        assertEquals(9, encodedInfo);

        int decodedInfo = CryptoLib.decodeRSA(3, 11, 33, 3, encodedInfo);
        assertEquals(15, decodedInfo);
    }

    @Test 
    public void testEncodeAndDecodeFileShamir() {
        int p, Ca, Da, Cb, db;
        p = (int) (Math.random() * (Integer.MAX_VALUE - 100000000) + 100000000);
        
        System.out.println(p);
        CryptoLib.encodeFileShamir("origin", "encoded_file", 23, 7, 19, 5);
        CryptoLib.decodeFileShamir("encoded_file", "out", 9, 23);
        // System.out.println(CryptoLib.encodeShamir(0x73, new Random()., 7, 19, 5));
    }
}
