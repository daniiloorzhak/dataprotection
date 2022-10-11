package com.mycompany.app;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppTest {

    @Test
    public void testModExp() {
        assertEquals(17, Crypto.modExp(5, 7, 23));
        assertEquals(1, Crypto.modExp(3, 2, 4));
        assertEquals(4, Crypto.modExp(2, 6, 10));
        assertEquals(3, Crypto.modExp(2, 3, 5));
        assertEquals(6, Crypto.modExp(2, 5, 13));
    }

    @Test
    public void testDhKeyExchange() {
        assertEquals(10, Crypto.dhke(7, 13, 23, 5));
    }

    @Test
    public void testEEA() {
        assertArrayEquals(new long[]{10, 1, -1}, Crypto.eea(30, 20).toIntArray());
        assertArrayEquals(new long[]{1, -2, 3}, Crypto.eea(28, 19).toIntArray());
        assertArrayEquals(new long[]{5, 1, -2}, Crypto.eea(35, 15).toIntArray());
    }

    @Test
    public void testDSGS() {
        assertEquals(5, Crypto.bsgs(2, 9, 23));
    }

    @Test
    public void testShamir() {
        long encodedInfo = Crypto.encodeShamir(10, 23, 7, 19, 5);
        assertEquals(19, encodedInfo);

        long decodedInfo = Crypto.decodeShamir(encodedInfo, 9, 23);
        assertEquals(10, decodedInfo);
    }

    @Test
    public void testElgamal() {
        ElgamalRes res = Crypto.encodeElgamal(15, 23, 5, 13, 7);
        assertEquals(12, res.getE());

        long decodedInfo = Crypto.decodeElgamal(res, 23, 13);
        assertEquals(15, decodedInfo);
    }

    @Test
    public void testVernam() {
        long encodedInfo = Crypto.encodeVernam(0b01001, 0b11010);
        assertEquals(0b10011, encodedInfo);

        long decodedInfo = Crypto.decodeVernam(encodedInfo, 0b11010);
        assertEquals(0b01001, decodedInfo);
    }

    @Test
    public void testRSA() {
        long encodedInfo = Crypto.encodeRSA(15, 33, 3);
        assertEquals(9, encodedInfo);

        long decodedInfo = Crypto.decodeRSA(3, 11, 33, 3, encodedInfo);
        assertEquals(15, decodedInfo);
    }

    @Test
    public void testIsPrime() {
        assertTrue(Crypto.isPrime(1));
        assertTrue(Crypto.isPrime(2));
        assertTrue(Crypto.isPrime(3));
        assertTrue(Crypto.isPrime(5));
        assertTrue(Crypto.isPrime(7));
        assertTrue(Crypto.isPrime(11));
        assertTrue(Crypto.isPrime(13));

        assertFalse(Crypto.isPrime(4));
        assertFalse(Crypto.isPrime(6));
        assertFalse(Crypto.isPrime(9));
        assertFalse(Crypto.isPrime(12));
        assertFalse(Crypto.isPrime(15));
        assertFalse(Crypto.isPrime(16));
    }

    @Test
    public void testIsCoPrime() {
        assertTrue(Crypto.isCoPrimeNums(15, 8));
        assertTrue(Crypto.isCoPrimeNums(5, 9));
        assertTrue(Crypto.isCoPrimeNums(8, 9));
        assertTrue(Crypto.isCoPrimeNums(18, 35));
        assertTrue(Crypto.isCoPrimeNums(12, 7));
        assertTrue(Crypto.isCoPrimeNums(35, 6));

        assertFalse(Crypto.isCoPrimeNums(35, 14));
        assertFalse(Crypto.isCoPrimeNums(60, 36));
        assertFalse(Crypto.isCoPrimeNums(81, 63));
        assertFalse(Crypto.isCoPrimeNums(144, 72));
        assertFalse(Crypto.isCoPrimeNums(64, 24));
        assertFalse(Crypto.isCoPrimeNums(2, 100));

    }

//    @Test
//    public void testEncodeAndDecodeFileShamir() {
//        long p, Ca, Da, Cb, Db;
//
//        p = Crypto.genRandomPrimeNum(1_000L, 10_000L);
//        Ca = Crypto.getRandomCoPrimeNum(p - 1, 3, p);
//        Da = p - 1 + Crypto.eea(p - 1, Ca).getY();
//        Cb = Crypto.getRandomCoPrimeNum(p - 1, 3, p);
//        Db = p - 1 + Crypto.eea(p - 1, Cb).getY();
//
//        Crypto.encodeFileShamir("origin", "encoded_file", p, Ca, Da, Cb);
//        Crypto.decodeFileShamir("encoded_file", "decoded_file", Db, p);
//        try {
//            assertTrue(FileUtils.contentEquals(
//                    new File("origin"),
//                    new File("decoded_file")
//            ));
//        } catch (IOException e) {
//
//        }
//    }

    @Test
    public void testEncodeAndDecodeFileRSA() {
        long N, P, Q, db;
        P = Crypto.genRandomPrimeNum(100, 1_000L);
        Q = Crypto.genRandomPrimeNum(100, 1_000L);
        N = P * Q;
        db = Crypto.getRandomCoPrimeNum((P - 1) * (Q - 1), 10, (P - 1) * (Q - 1) - 1);

        Crypto.encodeFileRSA("origin", "encoded_file", N, db);
        Crypto.decodeFileRSA("encoded_file", "decoded_file", P, Q, N, db);

        try {
            assertTrue(FileUtils.contentEquals(
                    new File("origin"),
                    new File("decoded_file")
            ));
        } catch (IOException e) {

        }

    }

//    public void testEncodeAndDecodeVernam() {
//        List<Long> keys = Crypto.encodeFileVernam("origin", "encoded_file");
//        Crypto.decodeFileVernam("encoded_file", "decoded_file", keys);
//    }
//
//    @Test
//    public void testEncodeAndDecodeElgamal() {
//        long p, g, c, k, q;
//        p = Crypto.genRandomPrimeNum(1_000, 10_000);
//        q = (p - 1) / 2;
//        g = 1;
//        for (long i = p - 2; i > 1; i--) {
//            if (Crypto.modExp(i, q, p) != 1L) {
//                g = i;
//                break;
//            }
//        }
//        c = Crypto.genRandomPrimeNum(3, p - 1);
//        k = Crypto.genRandomPrimeNum(3, p - 1);
//
//        ElgamalRes res = Crypto.encodeElgamal(15, p, g, c, k);
//
//        List<Long> rs = Crypto.encodeFileElgamal("origin", "encoded_file", p, g, c, k);
//        Crypto.decodeFileElgamal("encoded_file", "decoded_file", p, c, rs);
//
//        try {
//            assertTrue(FileUtils.contentEquals(
//                    new File("origin"),
//                    new File("decoded_file")
//            ));
//        } catch (IOException e) {
//
//        }
//    }
}
