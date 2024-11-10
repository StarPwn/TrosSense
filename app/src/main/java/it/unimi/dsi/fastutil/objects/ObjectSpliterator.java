package it.unimi.dsi.fastutil.objects;

import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public interface ObjectSpliterator<K> extends Spliterator<K> {
    @Override // java.util.Spliterator
    ObjectSpliterator<K> trySplit();

    default long skip(long n) {
        long i;
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        long i2 = n;
        while (true) {
            i = i2 - 1;
            if (i2 == 0 || !tryAdvance(new Consumer() { // from class: it.unimi.dsi.fastutil.objects.ObjectSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ObjectSpliterator.lambda$skip$0(obj);
                }
            })) {
                break;
            }
            i2 = i;
        }
        return (n - i) - 1;
    }

    static /* synthetic */ void lambda$skip$0(Object unused) {
    }
}
