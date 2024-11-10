package net.jodah.expiringmap;

import java.util.concurrent.TimeUnit;

/* loaded from: classes5.dex */
public final class ExpiringValue<V> {
    private static final long UNSET_DURATION = -1;
    private final long duration;
    private final ExpirationPolicy expirationPolicy;
    private final TimeUnit timeUnit;
    private final V value;

    public ExpiringValue(V value) {
        this(value, -1L, (TimeUnit) null, (ExpirationPolicy) null);
    }

    public ExpiringValue(V value, ExpirationPolicy expirationPolicy) {
        this(value, -1L, (TimeUnit) null, expirationPolicy);
    }

    public ExpiringValue(V value, long duration, TimeUnit timeUnit) {
        this(value, duration, timeUnit, (ExpirationPolicy) null);
        if (timeUnit == null) {
            throw new NullPointerException();
        }
    }

    public ExpiringValue(V value, ExpirationPolicy expirationPolicy, long duration, TimeUnit timeUnit) {
        this(value, duration, timeUnit, expirationPolicy);
        if (timeUnit == null) {
            throw new NullPointerException();
        }
    }

    private ExpiringValue(V value, long duration, TimeUnit timeUnit, ExpirationPolicy expirationPolicy) {
        this.value = value;
        this.expirationPolicy = expirationPolicy;
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public V getValue() {
        return this.value;
    }

    public ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    public long getDuration() {
        return this.duration;
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }

    public int hashCode() {
        if (this.value != null) {
            return this.value.hashCode();
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpiringValue<?> that = (ExpiringValue) o;
        if (this.value == null ? that.value == null : this.value.equals(that.value)) {
            if (this.expirationPolicy == that.expirationPolicy && this.duration == that.duration && this.timeUnit == that.timeUnit) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "ExpiringValue{value=" + this.value + ", expirationPolicy=" + this.expirationPolicy + ", duration=" + this.duration + ", timeUnit=" + this.timeUnit + '}';
    }
}
