package com.mycompany.app;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
;

public class Crypto {

    public static long genRandomPrimeNum(long lb, long rb) {
        long p = (long) (Math.random() * (rb - lb) + lb);
        while (!isPrime(p)) p = (long) (Math.random() * (rb - lb) + lb);
        return p;
    }

    public static long getRandomCoPrimeNum(long num, long lb, long rb) {
        long res = genRandomPrimeNum(lb, rb);
        EEARes r = eea(res, num);
        while (r.getGCD() != 1) {
            res = genRandomPrimeNum(lb, rb);
            r = eea(res, num);
        }
        return res;

    }

    public static boolean isCoPrimeNums(long x, long y) {
        return eea(x, y).getGCD() == 1;
    }

    public static boolean isPrime(long num) {
        // if (num < 1)
        EEARes res;
        for (long i = 1; i <= (long) Math.ceil((long) Math.sqrt(num)); i++) {
            res = eea(num, i);
            if (res.getGCD() != 1) return false;
        }
        return true;
    }


    // a^x mod p
    public static long modExp(long a, long x, long p) {
        long y = 1L;
        long s = a;
        while (x > 0L) {
            if ((x & 1L) == 1L) y = (y * s) % p;
            s = (s * s) % p;
            x >>= 1;
        }
        return y;
    }

    // ax + by; x, y, gcd(a, b)
    public static EEARes eea(long a, long b) {
        long[] u = {a, 1, 0};
        long[] v = {b, 0, 1};
        long[] t = new long[3];
        long q;

        while (v[0] != 0) {
            q = u[0] / v[0];
            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            t[2] = u[2] - q * v[2];
            for (int i = 0; i < 3; i++) {
                u[i] = v[i];
                v[i] = t[i];
            }
        }
        EEARes res = new EEARes(u);
        u = null;
        v = null;
        t = null;
        return res;
    }

    public static long dhke(long Xa, long Xb, long p, long g) {
        long Ya = modExp(g, Xa, p);
        long Yb = modExp(g, Xb, p);
        long Zab = modExp(Yb, Xa, p);
        long Zba = modExp(Ya, Xb, p);
        if (Zab == Zba) return Zab;
        return 0;
    }

    // a^x mod p = y, x
    public static long bsgs(long a, long y, long p) {
        long k = (long) (Math.sqrt((double) p)) + 1;
        long m = k;
        HashMap<Long, Long> row = new HashMap<>();
        long row_element = y % p;
        row.put(y, 0L);

        for (long i = 1; i <= k; i++) {
            row_element = (modExp(a, i, p) * (y % p)) % p;
            if (row.get(row_element) == null) row.put(row_element, i);
        }

        for (long j = 1; j <= k; j++) {
            row_element = modExp(a, m * j, p);
            if (row.get(row_element) != null) return j * m - row.get(row_element);
        }
        return 0;
    }

    public static long encodeShamir(long m, long p, long Ca, long Da, long Cb) {
        long x1 = modExp(m, Ca, p);
        long x2 = modExp(x1, Cb, p);
        long x3 = modExp(x2, Da, p);
        return x3;
    }

    public static long decodeShamir(long e, long db, long p) {
        return modExp(e, db, p);
    }

    public static ElgamalRes encodeElgamal(long m, long p, long g, long c, long k) {
        long d = modExp(g, c, p);
        long r = modExp(g, k, p);
        long tmp = modExp(d, k, p);
        long e = (m % p);
        e = (e * tmp) % p;
        return new ElgamalRes(r, e);
    }

        public static long decodeElgamal(ElgamalRes elg, long p, long c) {
        long tmp1 = (elg.getE() % p);
        long tmp2 = modExp(elg.getR(), p - 1 - c, p);
        return (tmp1 * tmp2) % p;
    }

    public static long encodeVernam(long m, long k) {
        return m ^ k;
    }

    public static long decodeVernam(long e, long k) {
        return e ^ k;
    }

    public static long encodeRSA(long m, long Nb, long db) {
        return modExp(m, db, Nb);
    }

    public static long decodeRSA(long Pb, long Qb, long Nb, long db, long e) {
        long cb = eea((Pb - 1) * (Qb - 1), db).getY();
        return modExp(e, cb, Nb);
    }

    public static void encodeFileShamir(String inFilePath, String outFilePath, long p, long Ca, long Da, long Cb) {
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long num = 0;
            long encoded_num;
            while ((num = in.read()) != -1) {
                encoded_num = encodeShamir(num, p, Ca, Da, Cb);
                out.writeLong(encoded_num);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static void decodeFileShamir(String inFilePath, String outFilePath, long db, long p) {

        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long decoded_num, encoded_num;
            while ((encoded_num = in.readLong()) != -1) {
                decoded_num = decodeShamir(encoded_num, db, p);
                out.write((int) decoded_num);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }


    public static void encodeFileRSA(String inFilePath, String outFilePath, long N, long d) {
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long num, encoded_num;
            while ((num = in.read()) != -1) {
                encoded_num = encodeRSA(num, N, d);
                out.writeLong(encoded_num);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static void decodeFileRSA(String inFilePath, String outFilePath, long Pb, long Qb, long Nb, long db) {
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long decoded_num, encoded_num;
            while ((encoded_num = in.readLong()) != -1) {
                decoded_num = decodeRSA(Pb, Qb, Nb, db, encoded_num);
                out.write((int) decoded_num);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static List<Long> encodeFileVernam(String inFilePath, String outFilePath) {
        List<Long> keys = new ArrayList<>();
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long num, encoded_num, key;
            while ((num = in.read()) != -1) {
                key = (long) (Math.random() * ((long) Integer.MAX_VALUE));
                keys.add(key);
                encoded_num = encodeVernam(num, key);
                out.writeLong(encoded_num);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return keys;
    }

    public static void decodeFileVernam(String inFilePath, String outFilePath, List<Long> keys) {
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long decoded_num, encoded_num;
            for (long key : keys) {
                encoded_num = in.readLong();
                decoded_num = decodeVernam(encoded_num, key);
                out.write((int) decoded_num);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static List<Long> encodeFileElgamal(String inFilePath, String outFilePath, long p, long g, long c, long k) {
        List<Long> results = new ArrayList<>();
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long num, encoded_num;
            while ((num = in.read()) != -1) {
                ElgamalRes res = encodeElgamal(num, p, g, c, k);
                results.add(res.getR());
                encoded_num = res.getE();
                out.writeLong(encoded_num);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return results;
    }

    public static void decodeFileElgamal(String inFilePath, String outFilePath, long p, long c, List<Long> rs) {
        try {
            FileOutputStream outFile = new FileOutputStream(outFilePath);
            FileInputStream inFile = new FileInputStream(inFilePath);
            DataOutputStream out = new DataOutputStream(outFile);
            DataInputStream in = new DataInputStream(inFile);

            long decoded_num, encoded_num;
            for (long r : rs) {
                encoded_num = in.readLong();
                decoded_num = decodeElgamal(new ElgamalRes(r, encoded_num), p, c);
                out.write((int) decoded_num);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
