package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

/* loaded from: classes4.dex */
public interface LongBidirectionalIterator extends LongIterator, ObjectBidirectionalIterator<Long> {
    long previousLong();

    @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
    @Deprecated
    default Long previous() {
        return Long.valueOf(previousLong());
    }

    default int back(int n) {
        int i;
        int i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasPrevious()) {
                break;
            }
            previousLong();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return super.skip(n);
    }
}
