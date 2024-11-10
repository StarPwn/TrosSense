package org.jose4j.jwt;

import java.util.Date;

/* loaded from: classes5.dex */
public class IntDate {
    private static final long CONVERSION = 1000;
    private long value;

    private IntDate(long value) {
        this.value = value;
    }

    public static IntDate now() {
        return fromMillis(System.currentTimeMillis());
    }

    public static IntDate fromSeconds(long secondsFromEpoch) {
        return new IntDate(secondsFromEpoch);
    }

    public static IntDate fromMillis(long millisecondsFromEpoch) {
        return fromSeconds(millisecondsFromEpoch / 1000);
    }

    public void addSeconds(long seconds) {
        this.value += seconds;
    }

    public void addSeconds(int seconds) {
        addSeconds(seconds);
    }

    public long getValue() {
        return this.value;
    }

    public long getValueInMillis() {
        return getValue() * 1000;
    }

    public boolean before(IntDate when) {
        return this.value < when.getValue();
    }

    public boolean after(IntDate when) {
        return this.value > when.getValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IntDate");
        sb.append("{").append(getValue()).append(" --> ");
        sb.append(new Date(getValueInMillis()));
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object other) {
        return this == other || ((other instanceof IntDate) && this.value == ((IntDate) other).value);
    }

    public int hashCode() {
        return (int) (this.value ^ (this.value >>> 32));
    }
}
