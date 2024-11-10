package org.cloudburstmc.nbt;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/* loaded from: classes5.dex */
public final class FloatInternPool {
    private static final Map<Object, WeakFloat> CACHE = new ConcurrentSkipListMap(new Comparator() { // from class: org.cloudburstmc.nbt.FloatInternPool$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return FloatInternPool.lambda$static$0(obj, obj2);
        }
    });
    private static final ReferenceQueue<?>[] REFERENCE_QUEUES = (ReferenceQueue[]) IntStream.range(0, Runtime.getRuntime().availableProcessors()).mapToObj(new IntFunction() { // from class: org.cloudburstmc.nbt.FloatInternPool$$ExternalSyntheticLambda1
        @Override // java.util.function.IntFunction
        public final Object apply(int i) {
            return FloatInternPool.lambda$static$1(i);
        }
    }).toArray(new IntFunction() { // from class: org.cloudburstmc.nbt.FloatInternPool$$ExternalSyntheticLambda2
        @Override // java.util.function.IntFunction
        public final Object apply(int i) {
            return FloatInternPool.lambda$static$2(i);
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$static$0(Object a, Object b) {
        return a instanceof Float ? Float.compare(((Float) a).floatValue(), ((WeakFloat) b).value) : Float.compare(((WeakFloat) a).value, ((WeakFloat) b).value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ReferenceQueue lambda$static$1(int i) {
        return new ReferenceQueue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ReferenceQueue[] lambda$static$2(int x$0) {
        return new ReferenceQueue[x$0];
    }

    private static void expungeStaleEntries(ReferenceQueue<?> referenceQueue) {
        while (true) {
            WeakFloat entry = (WeakFloat) referenceQueue.poll();
            if (entry != null) {
                CACHE.remove(entry, entry);
            } else {
                return;
            }
        }
    }

    public static Float intern(float value) {
        Float interned;
        Float boxed = Float.valueOf(value);
        WeakFloat entry = CACHE.get(boxed);
        if (entry != null) {
            expungeStaleEntries(entry.referenceQueue);
            Float interned2 = (Float) entry.get();
            if (interned2 != null) {
                return interned2;
            }
        }
        WeakFloat newEntry = new WeakFloat(boxed, REFERENCE_QUEUES[ThreadLocalRandom.current().nextInt(REFERENCE_QUEUES.length)]);
        do {
            WeakFloat entry2 = CACHE.putIfAbsent(newEntry, newEntry);
            if (entry2 == null) {
                interned = boxed;
            } else {
                expungeStaleEntries(entry2.referenceQueue);
                interned = (Float) entry2.get();
            }
        } while (interned == null);
        return interned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static final class WeakFloat extends WeakReference<Float> {
        private final ReferenceQueue<?> referenceQueue;
        private final float value;

        public WeakFloat(Float value, ReferenceQueue referenceQueue) {
            super(value, referenceQueue);
            this.value = value.floatValue();
            this.referenceQueue = referenceQueue;
        }
    }
}
