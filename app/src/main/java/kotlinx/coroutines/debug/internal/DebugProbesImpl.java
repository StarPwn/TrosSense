package kotlinx.coroutines.debug.internal;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineId;
import kotlinx.coroutines.CoroutineName;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.JobSupport;
import kotlinx.coroutines.debug.internal.DebugProbesImpl;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;

/* compiled from: DebugProbesImpl.kt */
@Metadata(d1 = {"\u0000Ö\u0001\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\bÀ\u0002\u0018\u00002\u00020\u0014:\u0002\u0095\u0001B\t\b\u0002¢\u0006\u0004\b\u0001\u0010\u0002J3\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0002¢\u0006\u0004\b\b\u0010\tJ\u0015\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\r\u0010\u000eJ\u0013\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0013\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013¢\u0006\u0004\b\u0015\u0010\u0016J@\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f\"\b\b\u0000\u0010\u0017*\u00020\u00142\u001e\b\u0004\u0010\u001b\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0019\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00028\u00000\u0018H\u0082\b¢\u0006\u0004\b\u001c\u0010\u001dJ\u0017\u0010\u001e\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\u001e\u0010\u000eJ\u0013\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f¢\u0006\u0004\b \u0010\u0012J)\u0010$\u001a\b\u0012\u0004\u0012\u00020\"0\u000f2\u0006\u0010!\u001a\u00020\u00102\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000f¢\u0006\u0004\b$\u0010%J\u0015\u0010'\u001a\u00020&2\u0006\u0010!\u001a\u00020\u0010¢\u0006\u0004\b'\u0010(J5\u0010,\u001a\b\u0012\u0004\u0012\u00020\"0\u000f2\u0006\u0010)\u001a\u00020&2\b\u0010+\u001a\u0004\u0018\u00010*2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b,\u0010-J?\u00102\u001a\u000e\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020.012\u0006\u0010/\u001a\u00020.2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\"0\u00132\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b2\u00103J3\u00105\u001a\u00020.2\u0006\u00104\u001a\u00020.2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\"0\u00132\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b5\u00106J\u001d\u00109\u001a\u0010\u0012\u0004\u0012\u000208\u0012\u0004\u0012\u00020\f\u0018\u000107H\u0002¢\u0006\u0004\b9\u0010:J\u0015\u0010=\u001a\u00020&2\u0006\u0010<\u001a\u00020;¢\u0006\u0004\b=\u0010>J\r\u0010?\u001a\u00020\f¢\u0006\u0004\b?\u0010\u0002J%\u0010A\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\bA\u0010BJ\u001b\u0010D\u001a\u00020\f2\n\u0010C\u001a\u0006\u0012\u0002\b\u00030\u0019H\u0002¢\u0006\u0004\bD\u0010EJ)\u0010H\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0000¢\u0006\u0004\bF\u0010GJ\u001b\u0010K\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bI\u0010JJ\u001b\u0010M\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bL\u0010JJ'\u0010P\u001a\b\u0012\u0004\u0012\u00020\"0\u000f\"\b\b\u0000\u0010\u0003*\u00020N2\u0006\u0010O\u001a\u00028\u0000H\u0002¢\u0006\u0004\bP\u0010QJ\u000f\u0010R\u001a\u00020\fH\u0002¢\u0006\u0004\bR\u0010\u0002J\u000f\u0010S\u001a\u00020\fH\u0002¢\u0006\u0004\bS\u0010\u0002J\r\u0010T\u001a\u00020\f¢\u0006\u0004\bT\u0010\u0002J\u001f\u0010V\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020U2\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bV\u0010WJ#\u0010X\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bX\u0010YJ/\u0010X\u001a\u00020\f2\n\u0010C\u001a\u0006\u0012\u0002\b\u00030\u00192\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bX\u0010ZJ;\u0010b\u001a\u00020\f*\u00020;2\u0012\u0010]\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020\\0[2\n\u0010`\u001a\u00060^j\u0002`_2\u0006\u0010a\u001a\u00020&H\u0002¢\u0006\u0004\bb\u0010cJ\u0017\u0010d\u001a\u000208*\u0006\u0012\u0002\b\u00030\u0019H\u0002¢\u0006\u0004\bd\u0010eJ\u001d\u0010C\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0019*\u0006\u0012\u0002\b\u00030\u0004H\u0002¢\u0006\u0004\bC\u0010fJ\u001a\u0010C\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0019*\u00020UH\u0082\u0010¢\u0006\u0004\bC\u0010gJ\u0016\u0010h\u001a\u0004\u0018\u00010U*\u00020UH\u0082\u0010¢\u0006\u0004\bh\u0010iJ\u001b\u0010j\u001a\u0004\u0018\u00010\u0006*\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020&*\u00020\u0014H\u0002¢\u0006\u0004\bl\u0010mR\u0014\u0010n\u001a\u00020&8\u0002X\u0082T¢\u0006\u0006\n\u0004\bn\u0010oR \u0010q\u001a\u000e\u0012\u0004\u0012\u00020U\u0012\u0004\u0012\u00020\\0p8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bq\u0010rR\u001e\u0010v\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00190s8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bt\u0010uR$\u0010w\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0019\u0012\u0004\u0012\u0002080p8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bw\u0010rR\u0014\u0010y\u001a\u00020x8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\by\u0010zR\u0014\u0010|\u001a\u00020{8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b|\u0010}R\"\u0010~\u001a\u0010\u0012\u0004\u0012\u000208\u0012\u0004\u0012\u00020\f\u0018\u0001078\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b~\u0010\u007fR)\u0010\u0080\u0001\u001a\u0002088\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u0080\u0001\u0010\u0081\u0001\u001a\u0006\b\u0082\u0001\u0010\u0083\u0001\"\u0006\b\u0084\u0001\u0010\u0085\u0001R\u0019\u0010\u0086\u0001\u001a\u00020.8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u0086\u0001\u0010\u0087\u0001R\u0017\u0010\u0089\u0001\u001a\u0002088@X\u0080\u0004¢\u0006\b\u001a\u0006\b\u0088\u0001\u0010\u0083\u0001R)\u0010\u008a\u0001\u001a\u0002088\u0006@\u0006X\u0086\u000e¢\u0006\u0018\n\u0006\b\u008a\u0001\u0010\u0081\u0001\u001a\u0006\b\u008b\u0001\u0010\u0083\u0001\"\u0006\b\u008c\u0001\u0010\u0085\u0001R\u001b\u0010\u008d\u0001\u001a\u0004\u0018\u00010*8\u0002@\u0002X\u0082\u000e¢\u0006\b\n\u0006\b\u008d\u0001\u0010\u008e\u0001R\"\u0010\u0092\u0001\u001a\u00020&*\u00020;8BX\u0082\u0004¢\u0006\u000f\u0012\u0006\b\u0090\u0001\u0010\u0091\u0001\u001a\u0005\b\u008f\u0001\u0010>R\u001b\u0010\u0093\u0001\u001a\u000208*\u00020\"8BX\u0082\u0004¢\u0006\b\u001a\u0006\b\u0093\u0001\u0010\u0094\u0001¨\u0006\u0096\u0001"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl;", "<init>", "()V", "T", "Lkotlin/coroutines/Continuation;", "completion", "Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "frame", "createOwner", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/StackTraceFrame;)Lkotlin/coroutines/Continuation;", "Ljava/io/PrintStream;", "out", "", "dumpCoroutines", "(Ljava/io/PrintStream;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;", "dumpCoroutinesInfo", "()Ljava/util/List;", "", "", "dumpCoroutinesInfoAsJsonAndReferences", "()[Ljava/lang/Object;", "R", "Lkotlin/Function2;", "Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "Lkotlin/coroutines/CoroutineContext;", "create", "dumpCoroutinesInfoImpl", "(Lkotlin/jvm/functions/Function2;)Ljava/util/List;", "dumpCoroutinesSynchronized", "Lkotlinx/coroutines/debug/internal/DebuggerInfo;", "dumpDebuggerInfo", "info", "Ljava/lang/StackTraceElement;", "coroutineTrace", "enhanceStackTraceWithThreadDump", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;Ljava/util/List;)Ljava/util/List;", "", "enhanceStackTraceWithThreadDumpAsJson", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;)Ljava/lang/String;", "state", "Ljava/lang/Thread;", "thread", "enhanceStackTraceWithThreadDumpImpl", "(Ljava/lang/String;Ljava/lang/Thread;Ljava/util/List;)Ljava/util/List;", "", "indexOfResumeWith", "actualTrace", "Lkotlin/Pair;", "findContinuationStartIndex", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)Lkotlin/Pair;", "frameIndex", "findIndexOfFrame", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)I", "Lkotlin/Function1;", "", "getDynamicAttach", "()Lkotlin/jvm/functions/Function1;", "Lkotlinx/coroutines/Job;", "job", "hierarchyToString", "(Lkotlinx/coroutines/Job;)Ljava/lang/String;", "install", "frames", "printStackTrace", "(Ljava/io/PrintStream;Ljava/util/List;)V", "owner", "probeCoroutineCompleted", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)V", "probeCoroutineCreated$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "probeCoroutineCreated", "probeCoroutineResumed$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)V", "probeCoroutineResumed", "probeCoroutineSuspended$kotlinx_coroutines_core", "probeCoroutineSuspended", "", "throwable", "sanitizeStackTrace", "(Ljava/lang/Throwable;)Ljava/util/List;", "startWeakRefCleanerThread", "stopWeakRefCleanerThread", "uninstall", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "updateRunningState", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;Ljava/lang/String;)V", "updateState", "(Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "map", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "builder", "indent", "build", "(Lkotlinx/coroutines/Job;Ljava/util/Map;Ljava/lang/StringBuilder;Ljava/lang/String;)V", "isFinished", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)Z", "(Lkotlin/coroutines/Continuation;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "realCaller", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "toStackTraceFrame", "(Ljava/util/List;)Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "toStringWithQuotes", "(Ljava/lang/Object;)Ljava/lang/String;", "ARTIFICIAL_FRAME_MESSAGE", "Ljava/lang/String;", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "callerInfoCache", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "", "getCapturedCoroutines", "()Ljava/util/Set;", "capturedCoroutines", "capturedCoroutinesMap", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "coroutineStateLock", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "Ljava/text/SimpleDateFormat;", "dateFormat", "Ljava/text/SimpleDateFormat;", "dynamicAttach", "Lkotlin/jvm/functions/Function1;", "enableCreationStackTraces", "Z", "getEnableCreationStackTraces", "()Z", "setEnableCreationStackTraces", "(Z)V", "installations", "I", "isInstalled$kotlinx_coroutines_core", "isInstalled", "sanitizeStackTraces", "getSanitizeStackTraces", "setSanitizeStackTraces", "weakRefCleanerThread", "Ljava/lang/Thread;", "getDebugString", "getDebugString$annotations", "(Lkotlinx/coroutines/Job;)V", "debugString", "isInternalMethod", "(Ljava/lang/StackTraceElement;)Z", "CoroutineOwner", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class DebugProbesImpl {
    private static final String ARTIFICIAL_FRAME_MESSAGE = "Coroutine creation stacktrace";
    private static volatile int installations;
    private static Thread weakRefCleanerThread;
    public static final DebugProbesImpl INSTANCE = new DebugProbesImpl();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final ConcurrentWeakMap<CoroutineOwner<?>, Boolean> capturedCoroutinesMap = new ConcurrentWeakMap<>(false, 1, null);
    private static final /* synthetic */ SequenceNumberRefVolatile debugProbesImpl$SequenceNumberRefVolatile = new Object(0) { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl.SequenceNumberRefVolatile
        volatile long sequenceNumber;

        {
            this.sequenceNumber = r1;
        }
    };
    private static final ReentrantReadWriteLock coroutineStateLock = new ReentrantReadWriteLock();
    private static boolean sanitizeStackTraces = true;
    private static boolean enableCreationStackTraces = true;
    private static final Function1<Boolean, Unit> dynamicAttach = INSTANCE.getDynamicAttach();
    private static final ConcurrentWeakMap<CoroutineStackFrame, DebugCoroutineInfo> callerInfoCache = new ConcurrentWeakMap<>(true);
    private static final /* synthetic */ AtomicLongFieldUpdater sequenceNumber$FU = AtomicLongFieldUpdater.newUpdater(SequenceNumberRefVolatile.class, "sequenceNumber");

    private static /* synthetic */ void getDebugString$annotations(Job job) {
    }

    private DebugProbesImpl() {
    }

    private final Set<CoroutineOwner<?>> getCapturedCoroutines() {
        return capturedCoroutinesMap.keySet();
    }

    public final boolean isInstalled$kotlinx_coroutines_core() {
        return installations > 0;
    }

    public final boolean getSanitizeStackTraces() {
        return sanitizeStackTraces;
    }

    public final void setSanitizeStackTraces(boolean z) {
        sanitizeStackTraces = z;
    }

    public final boolean getEnableCreationStackTraces() {
        return enableCreationStackTraces;
    }

    public final void setEnableCreationStackTraces(boolean z) {
        enableCreationStackTraces = z;
    }

    private final Function1<Boolean, Unit> getDynamicAttach() {
        Object m280constructorimpl;
        Object newInstance;
        try {
            Result.Companion companion = Result.INSTANCE;
            DebugProbesImpl debugProbesImpl = this;
            Class clz = Class.forName("kotlinx.coroutines.debug.internal.ByteBuddyDynamicAttach");
            Constructor ctor = clz.getConstructors()[0];
            newInstance = ctor.newInstance(new Object[0]);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m280constructorimpl = Result.m280constructorimpl(ResultKt.createFailure(th));
        }
        if (newInstance != null) {
            m280constructorimpl = Result.m280constructorimpl((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(newInstance, 1));
            if (Result.m286isFailureimpl(m280constructorimpl)) {
                m280constructorimpl = null;
            }
            return (Function1) m280constructorimpl;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Function1<kotlin.Boolean, kotlin.Unit>");
    }

    public final void install() {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int i = 0;
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i2 = 0; i2 < readHoldCount; i2++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            installations++;
            if (installations > 1) {
                return;
            }
            INSTANCE.startWeakRefCleanerThread();
            if (AgentInstallationType.INSTANCE.isInstalledStatically$kotlinx_coroutines_core()) {
                while (i < readHoldCount) {
                    readLock.lock();
                    i++;
                }
                writeLock.unlock();
                return;
            }
            Function1<Boolean, Unit> function1 = dynamicAttach;
            if (function1 != null) {
                function1.invoke(true);
            }
            Unit unit = Unit.INSTANCE;
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        } finally {
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        }
    }

    public final void uninstall() {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int i = 0;
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i2 = 0; i2 < readHoldCount; i2++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            if (!INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                throw new IllegalStateException("Agent was not installed".toString());
            }
            installations--;
            if (installations != 0) {
                return;
            }
            INSTANCE.stopWeakRefCleanerThread();
            capturedCoroutinesMap.clear();
            callerInfoCache.clear();
            if (AgentInstallationType.INSTANCE.isInstalledStatically$kotlinx_coroutines_core()) {
                while (i < readHoldCount) {
                    readLock.lock();
                    i++;
                }
                writeLock.unlock();
                return;
            }
            Function1<Boolean, Unit> function1 = dynamicAttach;
            if (function1 != null) {
                function1.invoke(false);
            }
            Unit unit = Unit.INSTANCE;
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        } finally {
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        }
    }

    private final void startWeakRefCleanerThread() {
        Thread thread;
        thread = ThreadsKt.thread((r12 & 1) != 0, (r12 & 2) != 0 ? false : true, (r12 & 4) != 0 ? null : null, (r12 & 8) != 0 ? null : "Coroutines Debugger Cleaner", (r12 & 16) != 0 ? -1 : 0, new Function0<Unit>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$startWeakRefCleanerThread$1
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ Unit invoke() {
                invoke2();
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                ConcurrentWeakMap concurrentWeakMap;
                concurrentWeakMap = DebugProbesImpl.callerInfoCache;
                concurrentWeakMap.runWeakRefQueueCleaningLoopUntilInterrupted();
            }
        });
        weakRefCleanerThread = thread;
    }

    private final void stopWeakRefCleanerThread() {
        Thread thread = weakRefCleanerThread;
        if (thread == null) {
            return;
        }
        weakRefCleanerThread = null;
        thread.interrupt();
        thread.join();
    }

    public final String hierarchyToString(Job job) {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i = 0; i < readHoldCount; i++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (!INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                throw new IllegalStateException("Debug probes are not installed".toString());
            }
            Iterable $this$filter$iv = INSTANCE.getCapturedCoroutines();
            Collection destination$iv$iv = new ArrayList();
            for (Object element$iv$iv : $this$filter$iv) {
                CoroutineOwner it2 = (CoroutineOwner) element$iv$iv;
                if (it2.delegate.getContext().get(Job.INSTANCE) != null) {
                    destination$iv$iv.add(element$iv$iv);
                }
            }
            Iterable $this$associateBy$iv = (List) destination$iv$iv;
            int capacity$iv = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
            Map destination$iv$iv2 = new LinkedHashMap(capacity$iv);
            for (Object element$iv$iv2 : $this$associateBy$iv) {
                CoroutineOwner it3 = (CoroutineOwner) element$iv$iv2;
                Job job2 = JobKt.getJob(it3.delegate.getContext());
                CoroutineOwner it4 = (CoroutineOwner) element$iv$iv2;
                destination$iv$iv2.put(job2, it4.info);
            }
            StringBuilder $this$hierarchyToString_u24lambda_u2d9_u24lambda_u2d8 = new StringBuilder();
            INSTANCE.build(job, destination$iv$iv2, $this$hierarchyToString_u24lambda_u2d9_u24lambda_u2d8, "");
            String sb = $this$hierarchyToString_u24lambda_u2d9_u24lambda_u2d8.toString();
            Intrinsics.checkNotNullExpressionValue(sb, "StringBuilder().apply(builderAction).toString()");
            for (int i2 = 0; i2 < readHoldCount; i2++) {
                readLock.lock();
            }
            writeLock.unlock();
            return sb;
        } catch (Throwable th2) {
            th = th2;
            for (int i3 = 0; i3 < readHoldCount; i3++) {
                readLock.lock();
            }
            writeLock.unlock();
            throw th;
        }
    }

    private final void build(Job $this$build, Map<Job, DebugCoroutineInfo> map, StringBuilder builder, String indent) {
        String newIndent;
        DebugCoroutineInfo info = map.get($this$build);
        if (info == null) {
            if (!($this$build instanceof ScopeCoroutine)) {
                builder.append(indent + getDebugString($this$build) + '\n');
                newIndent = indent + '\t';
            } else {
                newIndent = indent;
            }
        } else {
            StackTraceElement element = (StackTraceElement) CollectionsKt.firstOrNull((List) info.lastObservedStackTrace());
            String state = info.get_state();
            builder.append(indent + getDebugString($this$build) + ", continuation is " + state + " at line " + element + '\n');
            newIndent = indent + '\t';
        }
        for (Job child : $this$build.getChildren()) {
            build(child, map, builder, newIndent);
        }
    }

    private final String getDebugString(Job $this$debugString) {
        return $this$debugString instanceof JobSupport ? ((JobSupport) $this$debugString).toDebugString() : $this$debugString.toString();
    }

    private final <R> List<R> dumpCoroutinesInfoImpl(final Function2<? super CoroutineOwner<?>, ? super CoroutineContext, ? extends R> create) {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int i = 0;
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i2 = 0; i2 < readHoldCount; i2++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            if (!INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                throw new IllegalStateException("Debug probes are not installed".toString());
            }
            Sequence $this$sortedBy$iv = CollectionsKt.asSequence(INSTANCE.getCapturedCoroutines());
            return SequencesKt.toList(SequencesKt.mapNotNull(SequencesKt.sortedWith($this$sortedBy$iv, new DebugProbesImpl$dumpCoroutinesInfoImpl$lambda12$$inlined$sortedBy$1()), new Function1<CoroutineOwner<?>, R>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$1$3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public final R invoke(DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
                    boolean isFinished;
                    CoroutineContext context;
                    isFinished = DebugProbesImpl.INSTANCE.isFinished(coroutineOwner);
                    if (isFinished || (context = coroutineOwner.info.getContext()) == null) {
                        return null;
                    }
                    return create.invoke(coroutineOwner, context);
                }
            }));
        } finally {
            InlineMarker.finallyStart(1);
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
            InlineMarker.finallyEnd(1);
        }
    }

    public final Object[] dumpCoroutinesInfoAsJsonAndReferences() {
        String name;
        List coroutinesInfo = dumpCoroutinesInfo();
        int size = coroutinesInfo.size();
        ArrayList lastObservedThreads = new ArrayList(size);
        ArrayList lastObservedFrames = new ArrayList(size);
        ArrayList coroutinesInfoAsJson = new ArrayList(size);
        for (DebugCoroutineInfo info : coroutinesInfo) {
            CoroutineContext context = info.getContext();
            CoroutineName coroutineName = (CoroutineName) context.get(CoroutineName.INSTANCE);
            Long l = null;
            String name2 = (coroutineName == null || (name = coroutineName.getName()) == null) ? null : toStringWithQuotes(name);
            CoroutineDispatcher coroutineDispatcher = (CoroutineDispatcher) context.get(CoroutineDispatcher.INSTANCE);
            String dispatcher = coroutineDispatcher != null ? toStringWithQuotes(coroutineDispatcher) : null;
            StringBuilder append = new StringBuilder().append("\n                {\n                    \"name\": ").append(name2).append(",\n                    \"id\": ");
            CoroutineId coroutineId = (CoroutineId) context.get(CoroutineId.INSTANCE);
            if (coroutineId != null) {
                l = Long.valueOf(coroutineId.getId());
            }
            coroutinesInfoAsJson.add(StringsKt.trimIndent(append.append(l).append(",\n                    \"dispatcher\": ").append(dispatcher).append(",\n                    \"sequenceNumber\": ").append(info.getSequenceNumber()).append(",\n                    \"state\": \"").append(info.getState()).append("\"\n                } \n                ").toString()));
            lastObservedFrames.add(info.getLastObservedFrame());
            lastObservedThreads.add(info.getLastObservedThread());
        }
        Object[] objArr = new Object[4];
        objArr[0] = '[' + CollectionsKt.joinToString$default(coroutinesInfoAsJson, null, null, null, 0, null, null, 63, null) + ']';
        ArrayList $this$toTypedArray$iv = lastObservedThreads;
        Object[] array = $this$toTypedArray$iv.toArray(new Thread[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        objArr[1] = array;
        ArrayList $this$toTypedArray$iv2 = lastObservedFrames;
        Object[] array2 = $this$toTypedArray$iv2.toArray(new CoroutineStackFrame[0]);
        if (array2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        objArr[2] = array2;
        List $this$toTypedArray$iv3 = coroutinesInfo;
        Object[] array3 = $this$toTypedArray$iv3.toArray(new DebugCoroutineInfo[0]);
        if (array3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        objArr[3] = array3;
        return objArr;
    }

    public final String enhanceStackTraceWithThreadDumpAsJson(DebugCoroutineInfo info) {
        List stackTraceElements = enhanceStackTraceWithThreadDump(info, info.lastObservedStackTrace());
        List stackTraceElementsInfoAsJson = new ArrayList();
        for (StackTraceElement element : stackTraceElements) {
            StringBuilder append = new StringBuilder().append("\n                {\n                    \"declaringClass\": \"").append(element.getClassName()).append("\",\n                    \"methodName\": \"").append(element.getMethodName()).append("\",\n                    \"fileName\": ");
            String fileName = element.getFileName();
            stackTraceElementsInfoAsJson.add(StringsKt.trimIndent(append.append(fileName != null ? toStringWithQuotes(fileName) : null).append(",\n                    \"lineNumber\": ").append(element.getLineNumber()).append("\n                }\n                ").toString()));
        }
        return '[' + CollectionsKt.joinToString$default(stackTraceElementsInfoAsJson, null, null, null, 0, null, null, 63, null) + ']';
    }

    private final String toStringWithQuotes(Object $this$toStringWithQuotes) {
        return new StringBuilder().append('\"').append($this$toStringWithQuotes).append('\"').toString();
    }

    public final List<DebugCoroutineInfo> dumpCoroutinesInfo() {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int i = 0;
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i2 = 0; i2 < readHoldCount; i2++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            if (!INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                throw new IllegalStateException("Debug probes are not installed".toString());
            }
            Sequence $this$sortedBy$iv$iv = CollectionsKt.asSequence(INSTANCE.getCapturedCoroutines());
            return SequencesKt.toList(SequencesKt.mapNotNull(SequencesKt.sortedWith($this$sortedBy$iv$iv, new DebugProbesImpl$dumpCoroutinesInfoImpl$lambda12$$inlined$sortedBy$1()), new Function1<CoroutineOwner<?>, DebugCoroutineInfo>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfo$$inlined$dumpCoroutinesInfoImpl$1
                @Override // kotlin.jvm.functions.Function1
                public final DebugCoroutineInfo invoke(DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
                    boolean isFinished;
                    CoroutineContext context;
                    isFinished = DebugProbesImpl.INSTANCE.isFinished(coroutineOwner);
                    if (isFinished || (context = coroutineOwner.info.getContext()) == null) {
                        return null;
                    }
                    return new DebugCoroutineInfo(coroutineOwner.info, context);
                }
            }));
        } finally {
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        }
    }

    public final List<DebuggerInfo> dumpDebuggerInfo() {
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int i = 0;
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i2 = 0; i2 < readHoldCount; i2++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        try {
            if (!INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                throw new IllegalStateException("Debug probes are not installed".toString());
            }
            Sequence $this$sortedBy$iv$iv = CollectionsKt.asSequence(INSTANCE.getCapturedCoroutines());
            return SequencesKt.toList(SequencesKt.mapNotNull(SequencesKt.sortedWith($this$sortedBy$iv$iv, new DebugProbesImpl$dumpCoroutinesInfoImpl$lambda12$$inlined$sortedBy$1()), new Function1<CoroutineOwner<?>, DebuggerInfo>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpDebuggerInfo$$inlined$dumpCoroutinesInfoImpl$1
                @Override // kotlin.jvm.functions.Function1
                public final DebuggerInfo invoke(DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
                    boolean isFinished;
                    CoroutineContext context;
                    isFinished = DebugProbesImpl.INSTANCE.isFinished(coroutineOwner);
                    if (isFinished || (context = coroutineOwner.info.getContext()) == null) {
                        return null;
                    }
                    return new DebuggerInfo(coroutineOwner.info, context);
                }
            }));
        } finally {
            while (i < readHoldCount) {
                readLock.lock();
                i++;
            }
            writeLock.unlock();
        }
    }

    public final void dumpCoroutines(PrintStream out) {
        synchronized (out) {
            INSTANCE.dumpCoroutinesSynchronized(out);
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isFinished(CoroutineOwner<?> coroutineOwner) {
        Job job;
        CoroutineContext context = coroutineOwner.info.getContext();
        if (context == null || (job = (Job) context.get(Job.INSTANCE)) == null || !job.isCompleted()) {
            return false;
        }
        capturedCoroutinesMap.remove(coroutineOwner);
        return true;
    }

    private final void dumpCoroutinesSynchronized(PrintStream out) {
        String state;
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        for (int i = 0; i < readHoldCount; i++) {
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        int i2 = 0;
        try {
            if (INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                out.print("Coroutines dump " + dateFormat.format(Long.valueOf(System.currentTimeMillis())));
                Sequence $this$sortedBy$iv = SequencesKt.filter(CollectionsKt.asSequence(INSTANCE.getCapturedCoroutines()), new Function1<CoroutineOwner<?>, Boolean>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$1$2
                    @Override // kotlin.jvm.functions.Function1
                    public final Boolean invoke(DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
                        boolean isFinished;
                        isFinished = DebugProbesImpl.INSTANCE.isFinished(coroutineOwner);
                        return Boolean.valueOf(!isFinished);
                    }
                });
                Sequence $this$forEach$iv = SequencesKt.sortedWith($this$sortedBy$iv, new Comparator() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$lambda-19$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        DebugProbesImpl.CoroutineOwner it2 = (DebugProbesImpl.CoroutineOwner) t;
                        DebugProbesImpl.CoroutineOwner it3 = (DebugProbesImpl.CoroutineOwner) t2;
                        return ComparisonsKt.compareValues(Long.valueOf(it2.info.sequenceNumber), Long.valueOf(it3.info.sequenceNumber));
                    }
                });
                for (Object element$iv : $this$forEach$iv) {
                    CoroutineOwner owner = (CoroutineOwner) element$iv;
                    DebugCoroutineInfo info = owner.info;
                    List observedStackTrace = info.lastObservedStackTrace();
                    List enhancedStackTrace = INSTANCE.enhanceStackTraceWithThreadDumpImpl(info.get_state(), info.lastObservedThread, observedStackTrace);
                    if (Intrinsics.areEqual(info.get_state(), DebugCoroutineInfoImplKt.RUNNING) && enhancedStackTrace == observedStackTrace) {
                        state = info.get_state() + " (Last suspension stacktrace, not an actual stacktrace)";
                    } else {
                        state = info.get_state();
                    }
                    int i3 = i2;
                    out.print("\n\nCoroutine " + owner.delegate + ", state: " + state);
                    if (observedStackTrace.isEmpty()) {
                        out.print("\n\tat " + StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE));
                        INSTANCE.printStackTrace(out, info.getCreationStackTrace());
                    } else {
                        INSTANCE.printStackTrace(out, enhancedStackTrace);
                    }
                    i2 = i3;
                }
                Unit unit = Unit.INSTANCE;
                return;
            }
            throw new IllegalStateException("Debug probes are not installed".toString());
        } finally {
            for (int i4 = 0; i4 < readHoldCount; i4++) {
                readLock.lock();
            }
            writeLock.unlock();
        }
    }

    private final void printStackTrace(PrintStream out, List<StackTraceElement> frames) {
        List<StackTraceElement> $this$forEach$iv = frames;
        for (Object element$iv : $this$forEach$iv) {
            StackTraceElement frame = (StackTraceElement) element$iv;
            out.print("\n\tat " + frame);
        }
    }

    public final List<StackTraceElement> enhanceStackTraceWithThreadDump(DebugCoroutineInfo info, List<StackTraceElement> coroutineTrace) {
        return enhanceStackTraceWithThreadDumpImpl(info.getState(), info.getLastObservedThread(), coroutineTrace);
    }

    private final List<StackTraceElement> enhanceStackTraceWithThreadDumpImpl(String state, Thread thread, List<StackTraceElement> coroutineTrace) {
        Object m280constructorimpl;
        if (!Intrinsics.areEqual(state, DebugCoroutineInfoImplKt.RUNNING) || thread == null) {
            return coroutineTrace;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            DebugProbesImpl debugProbesImpl = this;
            m280constructorimpl = Result.m280constructorimpl(thread.getStackTrace());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            m280constructorimpl = Result.m280constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m286isFailureimpl(m280constructorimpl)) {
            m280constructorimpl = null;
        }
        StackTraceElement[] actualTrace = (StackTraceElement[]) m280constructorimpl;
        if (actualTrace == null) {
            return coroutineTrace;
        }
        int index$iv = 0;
        int length = actualTrace.length;
        while (true) {
            if (index$iv < length) {
                StackTraceElement it2 = actualTrace[index$iv];
                if (Intrinsics.areEqual(it2.getClassName(), "kotlin.coroutines.jvm.internal.BaseContinuationImpl") && Intrinsics.areEqual(it2.getMethodName(), "resumeWith") && Intrinsics.areEqual(it2.getFileName(), "ContinuationImpl.kt")) {
                    break;
                }
                index$iv++;
            } else {
                index$iv = -1;
                break;
            }
        }
        int indexOfResumeWith = index$iv;
        Pair<Integer, Integer> findContinuationStartIndex = findContinuationStartIndex(indexOfResumeWith, actualTrace, coroutineTrace);
        int continuationStartFrame = findContinuationStartIndex.component1().intValue();
        int delta = findContinuationStartIndex.component2().intValue();
        if (continuationStartFrame == -1) {
            return coroutineTrace;
        }
        int expectedSize = (((coroutineTrace.size() + indexOfResumeWith) - continuationStartFrame) - 1) - delta;
        ArrayList result = new ArrayList(expectedSize);
        int i = indexOfResumeWith - delta;
        for (int index = 0; index < i; index++) {
            result.add(actualTrace[index]);
        }
        int size = coroutineTrace.size();
        for (int index2 = continuationStartFrame + 1; index2 < size; index2++) {
            result.add(coroutineTrace.get(index2));
        }
        return result;
    }

    private final Pair<Integer, Integer> findContinuationStartIndex(int indexOfResumeWith, StackTraceElement[] actualTrace, List<StackTraceElement> coroutineTrace) {
        for (int i = 0; i < 3; i++) {
            int it2 = i;
            int result = INSTANCE.findIndexOfFrame((indexOfResumeWith - 1) - it2, actualTrace, coroutineTrace);
            if (result != -1) {
                return TuplesKt.to(Integer.valueOf(result), Integer.valueOf(it2));
            }
        }
        return TuplesKt.to(-1, 0);
    }

    private final int findIndexOfFrame(int frameIndex, StackTraceElement[] actualTrace, List<StackTraceElement> coroutineTrace) {
        StackTraceElement continuationFrame = (StackTraceElement) ArraysKt.getOrNull(actualTrace, frameIndex);
        if (continuationFrame == null) {
            return -1;
        }
        int index$iv = 0;
        for (Object item$iv : coroutineTrace) {
            StackTraceElement it2 = (StackTraceElement) item$iv;
            if (Intrinsics.areEqual(it2.getFileName(), continuationFrame.getFileName()) && Intrinsics.areEqual(it2.getClassName(), continuationFrame.getClassName()) && Intrinsics.areEqual(it2.getMethodName(), continuationFrame.getMethodName())) {
                return index$iv;
            }
            index$iv++;
        }
        return -1;
    }

    public final void probeCoroutineResumed$kotlinx_coroutines_core(Continuation<?> frame) {
        updateState(frame, DebugCoroutineInfoImplKt.RUNNING);
    }

    public final void probeCoroutineSuspended$kotlinx_coroutines_core(Continuation<?> frame) {
        updateState(frame, DebugCoroutineInfoImplKt.SUSPENDED);
    }

    private final void updateState(Continuation<?> frame, String state) {
        if (isInstalled$kotlinx_coroutines_core()) {
            if (Intrinsics.areEqual(state, DebugCoroutineInfoImplKt.RUNNING) && KotlinVersion.CURRENT.isAtLeast(1, 3, 30)) {
                CoroutineStackFrame stackFrame = frame instanceof CoroutineStackFrame ? (CoroutineStackFrame) frame : null;
                if (stackFrame == null) {
                    return;
                }
                updateRunningState(stackFrame, state);
                return;
            }
            CoroutineOwner owner = owner(frame);
            if (owner == null) {
                return;
            }
            updateState(owner, frame, state);
        }
    }

    private final void updateRunningState(CoroutineStackFrame frame, String state) {
        DebugCoroutineInfo debugCoroutineInfo;
        DebugCoroutineInfo info;
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            if (INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                DebugCoroutineInfo cached = callerInfoCache.remove(frame);
                if (cached != null) {
                    info = cached;
                } else {
                    CoroutineOwner<?> owner = INSTANCE.owner(frame);
                    if (owner != null && (debugCoroutineInfo = owner.info) != null) {
                        info = debugCoroutineInfo;
                        CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = info.getLastObservedFrame$kotlinx_coroutines_core();
                        CoroutineStackFrame realCaller = lastObservedFrame$kotlinx_coroutines_core != null ? INSTANCE.realCaller(lastObservedFrame$kotlinx_coroutines_core) : null;
                        if (realCaller != null) {
                            callerInfoCache.remove(realCaller);
                        }
                    }
                    return;
                }
                info.updateState$kotlinx_coroutines_core(state, (Continuation) frame);
                CoroutineStackFrame caller = INSTANCE.realCaller(frame);
                if (caller == null) {
                    return;
                }
                callerInfoCache.put(caller, info);
                Unit unit = Unit.INSTANCE;
            }
        } finally {
            readLock.unlock();
        }
    }

    private final CoroutineStackFrame realCaller(CoroutineStackFrame $this$realCaller) {
        CoroutineStackFrame caller = $this$realCaller;
        do {
            caller = caller.getCallerFrame();
            if (caller == null) {
                return null;
            }
        } while (caller.getStackTraceElement() == null);
        return caller;
    }

    private final void updateState(CoroutineOwner<?> owner, Continuation<?> frame, String state) {
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            if (INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                owner.info.updateState$kotlinx_coroutines_core(state, frame);
                Unit unit = Unit.INSTANCE;
            }
        } finally {
            readLock.unlock();
        }
    }

    private final CoroutineOwner<?> owner(Continuation<?> continuation) {
        CoroutineStackFrame coroutineStackFrame = continuation instanceof CoroutineStackFrame ? (CoroutineStackFrame) continuation : null;
        if (coroutineStackFrame != null) {
            return owner(coroutineStackFrame);
        }
        return null;
    }

    private final CoroutineOwner<?> owner(CoroutineStackFrame $this$owner) {
        CoroutineStackFrame coroutineStackFrame = $this$owner;
        while (!(coroutineStackFrame instanceof CoroutineOwner)) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return null;
            }
        }
        return (CoroutineOwner) coroutineStackFrame;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> Continuation<T> probeCoroutineCreated$kotlinx_coroutines_core(Continuation<? super T> completion) {
        StackTraceFrame frame;
        if (!isInstalled$kotlinx_coroutines_core()) {
            return completion;
        }
        CoroutineOwner owner = owner(completion);
        if (owner != null) {
            return completion;
        }
        if (enableCreationStackTraces) {
            frame = toStackTraceFrame(sanitizeStackTrace(new Exception()));
        } else {
            frame = null;
        }
        return createOwner(completion, frame);
    }

    private final StackTraceFrame toStackTraceFrame(List<StackTraceElement> list) {
        StackTraceFrame stackTraceFrame = null;
        if (!list.isEmpty()) {
            ListIterator iterator$iv = list.listIterator(list.size());
            while (iterator$iv.hasPrevious()) {
                StackTraceElement frame = iterator$iv.previous();
                StackTraceFrame acc = stackTraceFrame;
                stackTraceFrame = new StackTraceFrame(acc, frame);
            }
        }
        return stackTraceFrame;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final <T> Continuation<T> createOwner(Continuation<? super T> completion, StackTraceFrame frame) {
        if (!isInstalled$kotlinx_coroutines_core()) {
            return completion;
        }
        DebugCoroutineInfo info = new DebugCoroutineInfo(completion.getContext(), frame, sequenceNumber$FU.incrementAndGet(debugProbesImpl$SequenceNumberRefVolatile));
        CoroutineOwner owner = new CoroutineOwner(completion, info, frame);
        capturedCoroutinesMap.put(owner, true);
        if (!isInstalled$kotlinx_coroutines_core()) {
            capturedCoroutinesMap.clear();
        }
        return owner;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void probeCoroutineCompleted(CoroutineOwner<?> owner) {
        CoroutineStackFrame caller;
        capturedCoroutinesMap.remove(owner);
        CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = owner.info.getLastObservedFrame$kotlinx_coroutines_core();
        if (lastObservedFrame$kotlinx_coroutines_core == null || (caller = realCaller(lastObservedFrame$kotlinx_coroutines_core)) == null) {
            return;
        }
        callerInfoCache.remove(caller);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DebugProbesImpl.kt */
    @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B%\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bJ\n\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\u001e\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0016\u0010\t\u001a\u0004\u0018\u00010\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "T", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "delegate", "info", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "frame", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes5.dex */
    public static final class CoroutineOwner<T> implements Continuation<T>, CoroutineStackFrame {
        public final Continuation<T> delegate;
        private final CoroutineStackFrame frame;
        public final DebugCoroutineInfo info;

        @Override // kotlin.coroutines.Continuation
        public CoroutineContext getContext() {
            return this.delegate.getContext();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CoroutineOwner(Continuation<? super T> continuation, DebugCoroutineInfo info, CoroutineStackFrame frame) {
            this.delegate = continuation;
            this.info = info;
            this.frame = frame;
        }

        @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
        public CoroutineStackFrame getCallerFrame() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame != null) {
                return coroutineStackFrame.getCallerFrame();
            }
            return null;
        }

        @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
        public StackTraceElement getStackTraceElement() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame != null) {
                return coroutineStackFrame.getStackTraceElement();
            }
            return null;
        }

        @Override // kotlin.coroutines.Continuation
        public void resumeWith(Object result) {
            DebugProbesImpl.INSTANCE.probeCoroutineCompleted(this);
            this.delegate.resumeWith(result);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0022, code lost:            r2 = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0027, code lost:            if (kotlinx.coroutines.debug.internal.DebugProbesImpl.sanitizeStackTraces != false) goto L20;     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0029, code lost:            r3 = r1 - r2;        r5 = new java.util.ArrayList(r3);        r6 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0031, code lost:            if (r6 >= r3) goto L45;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:            r7 = r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0035, code lost:            if (r7 != 0) goto L16;     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0037, code lost:            r9 = kotlinx.coroutines.internal.StackTraceRecoveryKt.artificialFrame(kotlinx.coroutines.debug.internal.DebugProbesImpl.ARTIFICIAL_FRAME_MESSAGE);     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0040, code lost:            r5.add(r9);        r6 = r6 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003c, code lost:            r9 = r0[r7 + r2];     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0048, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0049, code lost:            r3 = new java.util.ArrayList((r1 - r2) + 1);        r3.add(kotlinx.coroutines.internal.StackTraceRecoveryKt.artificialFrame(kotlinx.coroutines.debug.internal.DebugProbesImpl.ARTIFICIAL_FRAME_MESSAGE));        r4 = r2 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005e, code lost:            if (r4 >= r1) goto L49;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0066, code lost:            if (isInternalMethod(r0[r4]) == false) goto L48;     */
    /* JADX WARN: Code restructure failed: missing block: B:2:0x000a, code lost:            if (r4 >= 0) goto L4;     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a8, code lost:            r3.add(r0[r4]);        r4 = r4 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0068, code lost:            r3.add(r0[r4]);        r5 = r4 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0072, code lost:            if (r5 >= r1) goto L54;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x007a, code lost:            if (isInternalMethod(r0[r5]) == false) goto L55;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x007c, code lost:            r5 = r5 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:3:0x000c, code lost:            r6 = r4;        r4 = r4 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x007f, code lost:            r6 = r5 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0081, code lost:            if (r6 <= r4) goto L56;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0089, code lost:            if (r0[r6].getFileName() != null) goto L57;     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x008b, code lost:            r6 = r6 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x008e, code lost:            if (r6 <= r4) goto L38;     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0092, code lost:            if (r6 >= (r5 - 1)) goto L38;     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0094, code lost:            r3.add(r0[r6]);     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001b, code lost:            if (kotlin.jvm.internal.Intrinsics.areEqual(r0[r6].getClassName(), "kotlin.coroutines.jvm.internal.DebugProbesKt") == false) goto L7;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x009c, code lost:            r3.add(r0[r5 - 1]);        r4 = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b6, code lost:            return r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x001f, code lost:            if (r4 >= 0) goto L44;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001d, code lost:            r5 = r6;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final <T extends java.lang.Throwable> java.util.List<java.lang.StackTraceElement> sanitizeStackTrace(T r12) {
        /*
            r11 = this;
            java.lang.StackTraceElement[] r0 = r12.getStackTrace()
            int r1 = r0.length
            r2 = r0
            r3 = 0
            int r4 = r2.length
            r5 = -1
            int r4 = r4 + r5
            if (r4 < 0) goto L21
        Lc:
            r6 = r4
            int r4 = r4 + r5
            r7 = r2[r6]
            r8 = 0
            java.lang.String r9 = r7.getClassName()
            java.lang.String r10 = "kotlin.coroutines.jvm.internal.DebugProbesKt"
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r9, r10)
            if (r7 == 0) goto L1f
            r5 = r6
            goto L22
        L1f:
            if (r4 >= 0) goto Lc
        L21:
        L22:
            r2 = r5
            boolean r3 = kotlinx.coroutines.debug.internal.DebugProbesImpl.sanitizeStackTraces
            java.lang.String r4 = "Coroutine creation stacktrace"
            if (r3 != 0) goto L49
            int r3 = r1 - r2
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>(r3)
            r6 = 0
        L31:
            if (r6 >= r3) goto L46
            r7 = r6
            r8 = 0
            if (r7 != 0) goto L3c
            java.lang.StackTraceElement r9 = kotlinx.coroutines.internal.StackTraceRecoveryKt.artificialFrame(r4)
            goto L40
        L3c:
            int r9 = r7 + r2
            r9 = r0[r9]
        L40:
            r5.add(r9)
            int r6 = r6 + 1
            goto L31
        L46:
            java.util.List r5 = (java.util.List) r5
            return r5
        L49:
            java.util.ArrayList r3 = new java.util.ArrayList
            int r5 = r1 - r2
            int r5 = r5 + 1
            r3.<init>(r5)
            r5 = r3
            java.util.Collection r5 = (java.util.Collection) r5
            java.lang.StackTraceElement r4 = kotlinx.coroutines.internal.StackTraceRecoveryKt.artificialFrame(r4)
            r5.add(r4)
            int r4 = r2 + 1
        L5e:
            if (r4 >= r1) goto Lb3
            r5 = r0[r4]
            boolean r5 = r11.isInternalMethod(r5)
            if (r5 == 0) goto La8
            r5 = r3
            java.util.Collection r5 = (java.util.Collection) r5
            r6 = r0[r4]
            r5.add(r6)
            int r5 = r4 + 1
        L72:
            if (r5 >= r1) goto L7f
            r6 = r0[r5]
            boolean r6 = r11.isInternalMethod(r6)
            if (r6 == 0) goto L7f
            int r5 = r5 + 1
            goto L72
        L7f:
            int r6 = r5 + (-1)
        L81:
            if (r6 <= r4) goto L8e
            r7 = r0[r6]
            java.lang.String r7 = r7.getFileName()
            if (r7 != 0) goto L8e
            int r6 = r6 + (-1)
            goto L81
        L8e:
            if (r6 <= r4) goto L9c
            int r7 = r5 + (-1)
            if (r6 >= r7) goto L9c
            r7 = r3
            java.util.Collection r7 = (java.util.Collection) r7
            r8 = r0[r6]
            r7.add(r8)
        L9c:
            r7 = r3
            java.util.Collection r7 = (java.util.Collection) r7
            int r8 = r5 + (-1)
            r8 = r0[r8]
            r7.add(r8)
            r4 = r5
            goto L5e
        La8:
            r5 = r3
            java.util.Collection r5 = (java.util.Collection) r5
            r6 = r0[r4]
            r5.add(r6)
            int r4 = r4 + 1
            goto L5e
        Lb3:
            r5 = r3
            java.util.List r5 = (java.util.List) r5
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.sanitizeStackTrace(java.lang.Throwable):java.util.List");
    }

    private final boolean isInternalMethod(StackTraceElement $this$isInternalMethod) {
        return StringsKt.startsWith$default($this$isInternalMethod.getClassName(), "kotlinx.coroutines", false, 2, (Object) null);
    }
}
