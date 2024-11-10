package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

/* loaded from: classes4.dex */
public interface IntBidirectionalIterator extends IntIterator, ObjectBidirectionalIterator<Integer> {
    int previousInt();

    @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
    @Deprecated
    default Integer previous() {
        return Integer.valueOf(previousInt());
    }

    default int back(int n) {
        int i;
        int i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !hasPrevious()) {
                break;
            }
            previousInt();
            i2 = i;
        }
        return (n - i) - 1;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
    default int skip(int n) {
        return super.skip(n);
    }
}
