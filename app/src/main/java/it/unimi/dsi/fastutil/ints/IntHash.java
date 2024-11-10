package it.unimi.dsi.fastutil.ints;

/* loaded from: classes4.dex */
public interface IntHash {

    /* loaded from: classes4.dex */
    public interface Strategy {
        boolean equals(int i, int i2);

        int hashCode(int i);
    }
}
