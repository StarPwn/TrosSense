package io.netty.util;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import androidx.lifecycle.LifecycleKt$$ExternalSyntheticBackportWithForwarding0;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public class ResourceLeakDetector<T> {
    private static final int DEFAULT_SAMPLING_INTERVAL = 128;
    private static final int DEFAULT_TARGET_RECORDS = 4;
    private static final String PROP_LEVEL = "io.netty.leakDetection.level";
    private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
    private static final String PROP_SAMPLING_INTERVAL = "io.netty.leakDetection.samplingInterval";
    private static final String PROP_TARGET_RECORDS = "io.netty.leakDetection.targetRecords";
    static final int SAMPLING_INTERVAL;
    private static final int TARGET_RECORDS;
    private static final AtomicReference<String[]> excludedMethods;
    private static Level level;
    private final Set<DefaultResourceLeak<?>> allLeaks;
    private volatile LeakListener leakListener;
    private final ReferenceQueue<Object> refQueue;
    private final Set<String> reportedLeaks;
    private final String resourceType;
    private final int samplingInterval;
    private static final Level DEFAULT_LEVEL = Level.SIMPLE;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ResourceLeakDetector.class);

    /* loaded from: classes4.dex */
    public interface LeakListener {
        void onLeak(String str, String str2);
    }

    static {
        boolean disabled;
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            logger.debug("-Dio.netty.noResourceLeakDetection: {}", Boolean.valueOf(disabled));
            logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", PROP_LEVEL, Level.DISABLED.name().toLowerCase());
        } else {
            disabled = false;
        }
        Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;
        String levelStr = SystemPropertyUtil.get(PROP_LEVEL_OLD, defaultLevel.name());
        Level level2 = Level.parseLevel(SystemPropertyUtil.get(PROP_LEVEL, levelStr));
        TARGET_RECORDS = SystemPropertyUtil.getInt(PROP_TARGET_RECORDS, 4);
        SAMPLING_INTERVAL = SystemPropertyUtil.getInt(PROP_SAMPLING_INTERVAL, 128);
        level = level2;
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", PROP_LEVEL, level2.name().toLowerCase());
            logger.debug("-D{}: {}", PROP_TARGET_RECORDS, Integer.valueOf(TARGET_RECORDS));
        }
        excludedMethods = new AtomicReference<>(EmptyArrays.EMPTY_STRINGS);
    }

    /* loaded from: classes4.dex */
    public enum Level {
        DISABLED,
        SIMPLE,
        ADVANCED,
        PARANOID;

        static Level parseLevel(String levelStr) {
            String trimmedLevelStr = levelStr.trim();
            for (Level l : values()) {
                if (trimmedLevelStr.equalsIgnoreCase(l.name()) || trimmedLevelStr.equals(String.valueOf(l.ordinal()))) {
                    return l;
                }
            }
            return ResourceLeakDetector.DEFAULT_LEVEL;
        }
    }

    @Deprecated
    public static void setEnabled(boolean enabled) {
        setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
    }

    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }

    public static void setLevel(Level level2) {
        level = (Level) ObjectUtil.checkNotNull(level2, "level");
    }

    public static Level getLevel() {
        return level;
    }

    @Deprecated
    public ResourceLeakDetector(Class<?> resourceType) {
        this(StringUtil.simpleClassName(resourceType));
    }

    @Deprecated
    public ResourceLeakDetector(String resourceType) {
        this(resourceType, 128, Long.MAX_VALUE);
    }

    @Deprecated
    public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive) {
        this(resourceType, samplingInterval);
    }

    public ResourceLeakDetector(Class<?> resourceType, int samplingInterval) {
        this(StringUtil.simpleClassName(resourceType), samplingInterval, Long.MAX_VALUE);
    }

    @Deprecated
    public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive) {
        this.allLeaks = Collections.newSetFromMap(new ConcurrentHashMap());
        this.refQueue = new ReferenceQueue<>();
        this.reportedLeaks = Collections.newSetFromMap(new ConcurrentHashMap());
        this.resourceType = (String) ObjectUtil.checkNotNull(resourceType, "resourceType");
        this.samplingInterval = samplingInterval;
    }

    @Deprecated
    public final ResourceLeak open(T obj) {
        return track0(obj, false);
    }

    public final ResourceLeakTracker<T> track(T obj) {
        return track0(obj, false);
    }

    public ResourceLeakTracker<T> trackForcibly(T obj) {
        return track0(obj, true);
    }

    private DefaultResourceLeak track0(T obj, boolean force) {
        Level level2 = level;
        if (force || level2 == Level.PARANOID || (level2 != Level.DISABLED && PlatformDependent.threadLocalRandom().nextInt(this.samplingInterval) == 0)) {
            reportLeak();
            return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks, getInitialHint(this.resourceType));
        }
        return null;
    }

    private void clearRefQueue() {
        while (true) {
            DefaultResourceLeak ref = (DefaultResourceLeak) this.refQueue.poll();
            if (ref != null) {
                ref.dispose();
            } else {
                return;
            }
        }
    }

    protected boolean needReport() {
        return logger.isErrorEnabled();
    }

    private void reportLeak() {
        if (!needReport()) {
            clearRefQueue();
            return;
        }
        while (true) {
            DefaultResourceLeak ref = (DefaultResourceLeak) this.refQueue.poll();
            if (ref != null) {
                if (ref.dispose()) {
                    String records = ref.getReportAndClearRecords();
                    if (this.reportedLeaks.add(records)) {
                        if (records.isEmpty()) {
                            reportUntracedLeak(this.resourceType);
                        } else {
                            reportTracedLeak(this.resourceType, records);
                        }
                        LeakListener listener = this.leakListener;
                        if (listener != null) {
                            listener.onLeak(this.resourceType, records);
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    protected void reportTracedLeak(String resourceType, String records) {
        logger.error("LEAK: {}.release() was not called before it's garbage-collected. See https://netty.io/wiki/reference-counted-objects.html for more information.{}", resourceType, records);
    }

    protected void reportUntracedLeak(String resourceType) {
        logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See https://netty.io/wiki/reference-counted-objects.html for more information.", resourceType, PROP_LEVEL, Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this));
    }

    @Deprecated
    protected void reportInstancesLeak(String resourceType) {
    }

    protected Object getInitialHint(String resourceType) {
        return null;
    }

    public void setLeakListener(LeakListener leakListener) {
        this.leakListener = leakListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultResourceLeak<T> extends WeakReference<Object> implements ResourceLeakTracker<T>, ResourceLeak {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Set<DefaultResourceLeak<?>> allLeaks;
        private volatile int droppedRecords;
        private volatile TraceRecord head;
        private final int trackedHash;
        private static final AtomicReferenceFieldUpdater<DefaultResourceLeak<?>, TraceRecord> headUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultResourceLeak.class, TraceRecord.class, "head");
        private static final AtomicIntegerFieldUpdater<DefaultResourceLeak<?>> droppedRecordsUpdater = AtomicIntegerFieldUpdater.newUpdater(DefaultResourceLeak.class, "droppedRecords");

        DefaultResourceLeak(Object referent, ReferenceQueue<Object> refQueue, Set<DefaultResourceLeak<?>> allLeaks, Object initialHint) {
            super(referent, refQueue);
            if (referent == null) {
                throw new AssertionError();
            }
            this.trackedHash = System.identityHashCode(referent);
            allLeaks.add(this);
            headUpdater.set(this, initialHint == null ? new TraceRecord(TraceRecord.BOTTOM) : new TraceRecord(TraceRecord.BOTTOM, initialHint));
            this.allLeaks = allLeaks;
        }

        @Override // io.netty.util.ResourceLeakTracker, io.netty.util.ResourceLeak
        public void record() {
            record0(null);
        }

        @Override // io.netty.util.ResourceLeakTracker, io.netty.util.ResourceLeak
        public void record(Object hint) {
            record0(hint);
        }

        /* JADX WARN: Type inference failed for: r0v5, types: [io.netty.util.ResourceLeakDetector$TraceRecord, java.lang.Object, int] */
        private void record0(Object hint) {
            ?? r0;
            boolean dropped;
            AtomicReferenceFieldUpdater<DefaultResourceLeak<?>, TraceRecord> atomicReferenceFieldUpdater;
            if (ResourceLeakDetector.TARGET_RECORDS <= 0) {
                return;
            }
            do {
                TraceRecord oldHead = headUpdater.get(this);
                TraceRecord prevHead = oldHead;
                if (oldHead == null) {
                    return;
                }
                r0 = oldHead.pos + 1;
                if (r0 >= ResourceLeakDetector.TARGET_RECORDS) {
                    int backOffFactor = Math.min(r0 - ResourceLeakDetector.TARGET_RECORDS, 30);
                    boolean z = PlatformDependent.threadLocalRandom().nextInt(1 << backOffFactor) != 0;
                    dropped = z;
                    if (z) {
                        prevHead = ((TraceRecord) r0).next;
                    }
                } else {
                    dropped = false;
                }
                if (hint != null) {
                    new TraceRecord(prevHead, hint);
                } else {
                    new TraceRecord(prevHead);
                }
                atomicReferenceFieldUpdater = headUpdater;
            } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(atomicReferenceFieldUpdater, this, r0, atomicReferenceFieldUpdater));
            if (dropped) {
                droppedRecordsUpdater.incrementAndGet(this);
            }
        }

        boolean dispose() {
            clear();
            return this.allLeaks.remove(this);
        }

        @Override // io.netty.util.ResourceLeak
        public boolean close() {
            if (this.allLeaks.remove(this)) {
                clear();
                headUpdater.set(this, null);
                return true;
            }
            return false;
        }

        @Override // io.netty.util.ResourceLeakTracker
        public boolean close(T trackedObject) {
            if (this.trackedHash != System.identityHashCode(trackedObject)) {
                throw new AssertionError();
            }
            try {
                return close();
            } finally {
                reachabilityFence0(trackedObject);
            }
        }

        private static void reachabilityFence0(Object ref) {
            if (ref != null) {
                synchronized (ref) {
                }
            }
        }

        public String toString() {
            TraceRecord oldHead = headUpdater.get(this);
            return generateReport(oldHead);
        }

        String getReportAndClearRecords() {
            TraceRecord oldHead = headUpdater.getAndSet(this, null);
            return generateReport(oldHead);
        }

        private String generateReport(TraceRecord oldHead) {
            if (oldHead == null) {
                return "";
            }
            int dropped = droppedRecordsUpdater.get(this);
            int duped = 0;
            int present = oldHead.pos + 1;
            StringBuilder buf = new StringBuilder(present * 2048).append(StringUtil.NEWLINE);
            buf.append("Recent access records: ").append(StringUtil.NEWLINE);
            int i = 1;
            Set<String> seen = new HashSet<>(present);
            while (oldHead != TraceRecord.BOTTOM) {
                String s = oldHead.toString();
                if (!seen.add(s)) {
                    duped++;
                } else if (oldHead.next == TraceRecord.BOTTOM) {
                    buf.append("Created at:").append(StringUtil.NEWLINE).append(s);
                } else {
                    buf.append('#').append(i).append(':').append(StringUtil.NEWLINE).append(s);
                    i++;
                }
                oldHead = oldHead.next;
            }
            if (duped > 0) {
                buf.append(": ").append(duped).append(" leak records were discarded because they were duplicates").append(StringUtil.NEWLINE);
            }
            if (dropped > 0) {
                buf.append(": ").append(dropped).append(" leak records were discarded because the leak record count is targeted to ").append(ResourceLeakDetector.TARGET_RECORDS).append(". Use system property ").append(ResourceLeakDetector.PROP_TARGET_RECORDS).append(" to increase the limit.").append(StringUtil.NEWLINE);
            }
            buf.setLength(buf.length() - StringUtil.NEWLINE.length());
            return buf.toString();
        }
    }

    public static void addExclusions(Class clz, String... methodNames) {
        String[] oldMethods;
        String[] newMethods;
        Set<String> nameSet = new HashSet<>(Arrays.asList(methodNames));
        for (Method method : clz.getDeclaredMethods()) {
            if (nameSet.remove(method.getName()) && nameSet.isEmpty()) {
                break;
            }
        }
        if (!nameSet.isEmpty()) {
            throw new IllegalArgumentException("Can't find '" + nameSet + "' in " + clz.getName());
        }
        do {
            oldMethods = excludedMethods.get();
            newMethods = (String[]) Arrays.copyOf(oldMethods, oldMethods.length + (methodNames.length * 2));
            for (int i = 0; i < methodNames.length; i++) {
                newMethods[oldMethods.length + (i * 2)] = clz.getName();
                newMethods[oldMethods.length + (i * 2) + 1] = methodNames[i];
            }
        } while (!LifecycleKt$$ExternalSyntheticBackportWithForwarding0.m(excludedMethods, oldMethods, newMethods));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class TraceRecord extends Throwable {
        private static final TraceRecord BOTTOM = new TraceRecord() { // from class: io.netty.util.ResourceLeakDetector.TraceRecord.1
            private static final long serialVersionUID = 7396077602074694571L;

            @Override // java.lang.Throwable
            public Throwable fillInStackTrace() {
                return this;
            }
        };
        private static final long serialVersionUID = 6065153674892850720L;
        private final String hintString;
        private final TraceRecord next;
        private final int pos;

        TraceRecord(TraceRecord next, Object hint) {
            this.hintString = hint instanceof ResourceLeakHint ? ((ResourceLeakHint) hint).toHintString() : hint.toString();
            this.next = next;
            this.pos = next.pos + 1;
        }

        TraceRecord(TraceRecord next) {
            this.hintString = null;
            this.next = next;
            this.pos = next.pos + 1;
        }

        private TraceRecord() {
            this.hintString = null;
            this.next = null;
            this.pos = -1;
        }

        @Override // java.lang.Throwable
        public String toString() {
            int k;
            StringBuilder buf = new StringBuilder(2048);
            if (this.hintString != null) {
                buf.append("\tHint: ").append(this.hintString).append(StringUtil.NEWLINE);
            }
            StackTraceElement[] array = getStackTrace();
            for (int i = 3; i < array.length; i++) {
                StackTraceElement element = array[i];
                String[] exclusions = (String[]) ResourceLeakDetector.excludedMethods.get();
                while (true) {
                    if (k < exclusions.length) {
                        k = (exclusions[k].equals(element.getClassName()) && exclusions[k + 1].equals(element.getMethodName())) ? 0 : k + 2;
                    } else {
                        buf.append('\t');
                        buf.append(element.toString());
                        buf.append(StringUtil.NEWLINE);
                        break;
                    }
                }
            }
            return buf.toString();
        }
    }
}
