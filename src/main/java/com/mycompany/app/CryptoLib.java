package com.mycompany.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class CryptoLib {

    public class ElgamalRes {
        private int r, e;
    }

    // a^x mod p
    public static int modExp(int a, int x, int p) {
        int y = 1;
        int s = a;
        while (x > 0) {
            if ((x & 1) == 1)
                y = (y * s) % p;
            s = (s * s) % p;
            x >>= 1;
        }
        return y;
    }

    public static int[] eea(int a, int b) {
        int[] u = { a, 1, 0 };
        int[] v = { b, 0, 1 };
        int[] t = new int[3];
        int q;

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

        return u;
    }

    public static int dhke(int Xa, int Xb, int p, int g) {
        int Ya = modExp(g, Xa, p);
        int Yb = modExp(g, Xb, p);
        int Zab = modExp(Yb, Xa, p);
        int Zba = modExp(Ya, Xb, p);
        if (Zab == Zba)
            return Zab;
        return 0;
    }

    // a^x mod p = y, x
    public static int bsgs(int a, int y, int p) {
        int k = (int) (Math.sqrt((double) p)) + 1;
        int m = k;
        HashMap<Integer, Integer> row = new HashMap<>();
        int row_element = y % p;
        row.put(y, 0);

        for (int i = 1; i <= k; i++) {
            row_element = (modExp(a, i, p) * (y % p)) % p;
            if (row.get(row_element) == null)
                row.put(row_element, i);
        }

        for (int j = 1; j <= k; j++) {
            row_element = modExp(a, m * j, p);
            if (row.get(row_element) != null)
                return j * m - row.get(row_element);
        }
        return 0;
    }

    public static int encodeShamir(int m, int p, int Ca, int Da, int Cb) {
        int x1 = modExp(m, Ca, p);
        int x2 = modExp(x1, Cb, p);
        int x3 = modExp(x2, Da, p);
        return x3;
    }

    public static int decodeShamir(int e, int db, int p) {
        return modExp(e, db, p);
    }

    public static int encodeElgamal(int m, int p, int g, int Cb, int k) {
        int Db = modExp(g, Cb, p);
        return ((m % p) * modExp(Db, k, p)) % p;
    }

    public static int decodeElgamal(int e, int g, int k, int p, int Cb) {
        int r = modExp(g, k, p);
        return ((e % p) * modExp(r, p - 1 - Cb, p)) % p;
    }

    public static int encodeVernam(int m, int k) {
        return m ^ k;
    }

    public static int decodeVernam(int e, int k) {
        return e ^ k;
    }

    public static int encodeRSA(int m, int Pb, int Qb, int Nb, int db) {
        return modExp(m, db, Nb);
    }

    public static int decodeRSA(int Pb, int Qb, int Nb, int db, int e) {
        int cb = eea((Pb - 1) * (Qb - 1), db)[2];
        return modExp(e, cb, Nb);
    }

    public static void encodeFileShamir(String inFilePath, String outFilePath, int p, int Ca, int Da, int Cb) {
        try {
            FileOutputStream out = new FileOutputStream(outFilePath);
            FileInputStream in = new FileInputStream(inFilePath);
            int num = 0;
            int encoded_num;
            while ((num=in.read()) != -1) {
                encoded_num = encodeShamir(num, p, Ca, Da, Cb);
                out.write(encoded_num);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            
        }
    }

    public static void decodeFileShamir(String inFileName, String outFileName, int db, int p) {
        try {
            FileInputStream in = new FileInputStream(inFileName);
            FileOutputStream out = new FileOutputStream(outFileName);
            int decoded_num, encoded_num; 
            while ((encoded_num=in.read()) != -1) {
                decoded_num = decodeShamir(encoded_num, db, p);
                out.write(decoded_num);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        }
    }

    // encodefileshamir
    // decodefileshamir

}
