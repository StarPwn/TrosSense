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
public final class IntegerInternPool {
    private static final Map<Object, WeakInteger> CACHE = new ConcurrentSkipListMap(new Comparator() { // from class: org.cloudburstmc.nbt.IntegerInternPool$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return IntegerInternPool.lambda$static$0(obj, obj2);
        }
    });
    private static final ReferenceQueue<?>[] REFERENCE_QUEUES = (ReferenceQueue[]) IntStream.range(0, Runtime.getRuntime().availableProcessors()).mapToObj(new IntFunction() { // from class: org.cloudburstmc.nbt.IntegerInternPool$$ExternalSyntheticLambda1
        @Override // java.util.function.IntFunction
        public final Object apply(int i) {
            return IntegerInternPool.lambda$static$1(i);
        }
    }).toArray(new IntFunction() { // from class: org.cloudburstmc.nbt.IntegerInternPool$$ExternalSyntheticLambda2
        @Override // java.util.function.IntFunction
        public final Object apply(int i) {
            return IntegerInternPool.lambda$static$2(i);
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$static$0(Object a, Object b) {
        return a instanceof Integer ? Integer.compare(((Integer) a).intValue(), ((WeakInteger) b).value) : Integer.compare(((WeakInteger) a).value, ((WeakInteger) b).value);
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
            WeakInteger entry = (WeakInteger) referenceQueue.poll();
            if (entry != null) {
                CACHE.remove(entry, entry);
            } else {
                return;
            }
        }
    }

    public static Integer intern(int value) {
        Integer interned;
        if (((byte) value) == value) {
            return Integer.valueOf(value);
        }
        Integer boxed = Integer.valueOf(value);
        WeakInteger entry = CACHE.get(boxed);
        if (entry != null) {
            expungeStaleEntries(entry.referenceQueue);
            Integer interned2 = (Integer) entry.get();
            if (interned2 != null) {
                return interned2;
            }
        }
        WeakInteger newEntry = new WeakInteger(boxed, REFERENCE_QUEUES[ThreadLocalRandom.current().nextInt(REFERENCE_QUEUES.length)]);
        do {
            WeakInteger entry2 = CACHE.putIfAbsent(newEntry, newEntry);
            if (entry2 == null) {
                interned = boxed;
            } else {
                expungeStaleEntries(entry2.referenceQueue);
                interned = (Integer) entry2.get();
            }
        } while (interned == null);
        return interned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static final class WeakInteger extends WeakReference<Integer> {
        private final ReferenceQueue<?> referenceQueue;
        private final int value;

        public WeakInteger(Integer value, ReferenceQueue referenceQueue) {
            super(value, referenceQueue);
            this.value = value.intValue();
            this.referenceQueue = referenceQueue;
        }
    }
}
