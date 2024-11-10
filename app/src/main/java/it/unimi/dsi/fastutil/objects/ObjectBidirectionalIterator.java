package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BidirectionalIterator;

/* loaded from: classes4.dex */
public interface ObjectBidirectionalIterator<K> extends ObjectIterator<K>, BidirectionalIterator<K> {
    default int back(int n) {
        int i;
        int i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasPrevious()) {
                break;
            }
            previous();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return super.skip(n);
    }
}
