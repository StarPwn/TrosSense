package org.jose4j.jwt;

import java.text.DateFormat;
import java.util.Date;
import org.jose4j.lang.Maths;

/* loaded from: classes5.dex */
public class NumericDate {
    private static final long CONVERSION = 1000;
    private long value;

    private NumericDate(long value) {
        setValue(value);
    }

    public static NumericDate now() {
        return fromMilliseconds(System.currentTimeMillis());
    }

    public static NumericDate fromSeconds(long secondsFromEpoch) {
        return new NumericDate(secondsFromEpoch);
    }

    public static NumericDate fromMilliseconds(long millisecondsFromEpoch) {
        return fromSeconds(millisecondsFromEpoch / 1000);
    }

    public void addSeconds(long seconds) {
        setValue(Maths.add(this.value, seconds));
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValueInMillis() {
        long secs = getValue();
        long millis = 1000 * secs;
        if (!canConvertToMillis()) {
            throw new ArithmeticException("converting " + secs + " seconds to milliseconds (x1000) resulted in long integer overflow (" + millis + ")");
        }
        return millis;
    }

    private boolean canConvertToMillis() {
        long secs = getValue();
        long millis = 1000 * secs;
        return (secs <= 0 || millis >= secs) && (secs >= 0 || millis <= secs) && (secs != 0 || millis == 0);
    }

    public boolean isBefore(NumericDate when) {
        return this.value < when.getValue();
    }

    public boolean isOnOrAfter(NumericDate when) {
        return !isBefore(when);
    }

    public boolean isAfter(NumericDate when) {
        return this.value > when.getValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NumericDate").append("{").append(getValue());
        if (canConvertToMillis()) {
            DateFormat df = DateFormat.getDateTimeInstance(2, 1);
            Date date = new Date(getValueInMillis());
            sb.append(" -> ").append(df.format(date));
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object other) {
        return this == other || ((other instanceof NumericDate) && this.value == ((NumericDate) other).value);
    }

    public int hashCode() {
        return (int) (this.value ^ (this.value >>> 32));
    }
}
