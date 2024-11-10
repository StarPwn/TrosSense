package it.unimi.dsi.fastutil.longs;

/* loaded from: classes4.dex */
public interface LongHash {

    /* loaded from: classes4.dex */
    public interface Strategy {
        boolean equals(long j, long j2);

        int hashCode(long j);
    }
}
