package com.mycompany.app;

public class ElgamalRes {
    private final long r, e;

    public ElgamalRes(long r, long e) {
        this.r = r;
        this.e = e;
    }

    public long getR() {
        return r;
    }

    public long getE() {
        return e;
    }
}
