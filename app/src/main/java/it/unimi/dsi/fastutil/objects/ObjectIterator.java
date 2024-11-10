package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

/* loaded from: classes4.dex */
public interface ObjectIterator<K> extends Iterator<K> {
    default int skip(int n) {
        int i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasNext()) {
                break;
            }
            next();
            i2 = i;
        }
        return (n - i) - 1;
    }
}
