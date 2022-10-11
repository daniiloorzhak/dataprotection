package com.mycompany.app;

public class EEARes {
    private final long x, y, gcd;

    EEARes(long[] nums) {
        gcd = nums[0];
        x = nums[1];
        y = nums[2];
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getGCD() {
        return gcd;
    }

    public long[] toIntArray() {
        return new long[] { gcd, x, y };
    }
}
