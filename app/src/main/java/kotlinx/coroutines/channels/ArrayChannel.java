package kotlinx.coroutines.channels;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;

/* compiled from: ArrayChannel.kt */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000BB9\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012 \u0010\t\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b¢\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u000e\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u001d\u0010\u0013\u001a\u00020\u00122\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0014¢\u0006\u0004\b\u0013\u0010\u0014J\u0019\u0010\u0018\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0016\u001a\u00020\u0015H\u0014¢\u0006\u0004\b\u0018\u0010\u0019J\u0017\u0010\u001a\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u0017\u0010\u001c\u001a\u00020\u00172\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u001c\u0010\u001dJ#\u0010 \u001a\u00020\u00172\u0006\u0010\r\u001a\u00028\u00002\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0014¢\u0006\u0004\b \u0010!J\u0017\u0010#\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0012H\u0014¢\u0006\u0004\b#\u0010$J\u0011\u0010%\u001a\u0004\u0018\u00010\u0017H\u0014¢\u0006\u0004\b%\u0010&J\u001d\u0010'\u001a\u0004\u0018\u00010\u00172\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0014¢\u0006\u0004\b'\u0010(J\u0019\u0010*\u001a\u0004\u0018\u00010)2\u0006\u0010\f\u001a\u00020\u0002H\u0002¢\u0006\u0004\b*\u0010+R\u001e\u0010-\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170,8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b-\u0010.R\u0014\u00102\u001a\u00020/8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u00103R\u0016\u00104\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b4\u00103R\u0014\u00105\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b5\u00106R\u0014\u00107\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b7\u00106R\u0014\u00108\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b8\u00106R\u0014\u00109\u001a\u00020\u00128DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b9\u00106R\u0014\u0010:\u001a\u00020\u00128VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b:\u00106R\u0014\u0010;\u001a\u00020\u00128VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b;\u00106R\u0018\u0010>\u001a\u00060<j\u0002`=8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b>\u0010?R\u0014\u0010\u0005\u001a\u00020\u00048\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010@¨\u0006A"}, d2 = {"Lkotlinx/coroutines/channels/ArrayChannel;", "E", "", "capacity", "Lkotlinx/coroutines/channels/BufferOverflow;", "onBufferOverflow", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(ILkotlinx/coroutines/channels/BufferOverflow;Lkotlin/jvm/functions/Function1;)V", "currentSize", "element", "enqueueElement", "(ILjava/lang/Object;)V", "Lkotlinx/coroutines/channels/Receive;", "receive", "", "enqueueReceiveInternal", "(Lkotlinx/coroutines/channels/Receive;)Z", "Lkotlinx/coroutines/channels/Send;", "send", "", "enqueueSend", "(Lkotlinx/coroutines/channels/Send;)Ljava/lang/Object;", "ensureCapacity", "(I)V", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "offerSelectInternal", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "wasClosed", "onCancelIdempotent", "(Z)V", "pollInternal", "()Ljava/lang/Object;", "pollSelectInternal", "(Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "Lkotlinx/coroutines/internal/Symbol;", "updateBufferSize", "(I)Lkotlinx/coroutines/internal/Symbol;", "", "buffer", "[Ljava/lang/Object;", "", "getBufferDebugString", "()Ljava/lang/String;", "bufferDebugString", "I", "head", "isBufferAlwaysEmpty", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "isClosedForReceive", "isEmpty", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/channels/BufferOverflow;", "kotlinx-coroutines-core", "Lkotlinx/coroutines/channels/AbstractChannel;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public class ArrayChannel<E> extends AbstractChannel<E> {
    private Object[] buffer;
    private final int capacity;
    private int head;
    private final ReentrantLock lock;
    private final BufferOverflow onBufferOverflow;
    private volatile /* synthetic */ int size;

    /* compiled from: ArrayChannel.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes5.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[BufferOverflow.values().length];
            iArr[BufferOverflow.SUSPEND.ordinal()] = 1;
            iArr[BufferOverflow.DROP_LATEST.ordinal()] = 2;
            iArr[BufferOverflow.DROP_OLDEST.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public ArrayChannel(int capacity, BufferOverflow onBufferOverflow, Function1<? super E, Unit> function1) {
        super(function1);
        this.capacity = capacity;
        this.onBufferOverflow = onBufferOverflow;
        if (!(this.capacity >= 1)) {
            throw new IllegalArgumentException(("ArrayChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
        }
        this.lock = new ReentrantLock();
        Object[] $this$buffer_u24lambda_u2d1 = new Object[Math.min(this.capacity, 8)];
        ArraysKt.fill$default($this$buffer_u24lambda_u2d1, AbstractChannelKt.EMPTY, 0, 0, 6, (Object) null);
        this.buffer = $this$buffer_u24lambda_u2d1;
        this.size = 0;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        return this.size == 0;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return this.size == this.capacity && this.onBufferOverflow == BufferOverflow.SUSPEND;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel, kotlinx.coroutines.channels.ReceiveChannel
    public boolean isEmpty() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return isEmptyImpl();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel, kotlinx.coroutines.channels.ReceiveChannel
    public boolean isClosedForReceive() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return super.isClosedForReceive();
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0025, code lost:            if (r5 == 0) goto L13;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:            r6 = takeFirstReceiveOrPeekClosed();     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:            if (r6 != null) goto L16;     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0033, code lost:            if ((r6 instanceof kotlinx.coroutines.channels.Closed) == false) goto L21;     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x003c, code lost:            kotlin.jvm.internal.Intrinsics.checkNotNull(r6);        r6 = r6.tryResumeReceive(r10, null);     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0044, code lost:            if (r6 == null) goto L46;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004a, code lost:            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() == false) goto L33;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004f, code lost:            if (r6 != kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN) goto L28;     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0051, code lost:            r8 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0054, code lost:            if (r8 == false) goto L31;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005c, code lost:            throw new java.lang.AssertionError();     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0053, code lost:            r8 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x005d, code lost:            r9.size = r5;        r4 = kotlin.Unit.INSTANCE;     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0062, code lost:            r3.unlock();        r6.completeResumeReceive(r10);     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006d, code lost:            return r6.getOfferResult();     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0035, code lost:            r9.size = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x003b, code lost:            return r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006e, code lost:            enqueueElement(r5, r10);     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0076, code lost:            return kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS;     */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object offerInternal(E r10) {
        /*
            r9 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r9.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            int r5 = r9.size     // Catch: java.lang.Throwable -> L77
            kotlinx.coroutines.channels.Closed r6 = r9.getClosedForSend()     // Catch: java.lang.Throwable -> L77
            if (r6 == 0) goto L19
            r7 = 0
            r3.unlock()
            return r6
        L19:
            kotlinx.coroutines.internal.Symbol r6 = r9.updateBufferSize(r5)     // Catch: java.lang.Throwable -> L77
            if (r6 == 0) goto L25
            r7 = 0
            r3.unlock()
            return r6
        L25:
            if (r5 != 0) goto L6e
        L27:
        L28:
            kotlinx.coroutines.channels.ReceiveOrClosed r6 = r9.takeFirstReceiveOrPeekClosed()     // Catch: java.lang.Throwable -> L77
            if (r6 != 0) goto L2f
            goto L6e
        L2f:
            r0 = r6
            boolean r6 = r0 instanceof kotlinx.coroutines.channels.Closed     // Catch: java.lang.Throwable -> L77
            if (r6 == 0) goto L3c
            r9.size = r5     // Catch: java.lang.Throwable -> L77
            r3.unlock()
            return r0
        L3c:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch: java.lang.Throwable -> L77
            r6 = 0
            kotlinx.coroutines.internal.Symbol r6 = r0.tryResumeReceive(r10, r6)     // Catch: java.lang.Throwable -> L77
            if (r6 == 0) goto L27
            boolean r7 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch: java.lang.Throwable -> L77
            if (r7 == 0) goto L5d
            r7 = 0
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN     // Catch: java.lang.Throwable -> L77
            if (r6 != r8) goto L53
            r8 = 1
            goto L54
        L53:
            r8 = 0
        L54:
            if (r8 == 0) goto L57
            goto L5d
        L57:
            java.lang.AssertionError r7 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> L77
            r7.<init>()     // Catch: java.lang.Throwable -> L77
            throw r7     // Catch: java.lang.Throwable -> L77
        L5d:
            r9.size = r5     // Catch: java.lang.Throwable -> L77
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L77
            r3.unlock()
            r0.completeResumeReceive(r10)
            java.lang.Object r1 = r0.getOfferResult()
            return r1
        L6e:
            r9.enqueueElement(r5, r10)     // Catch: java.lang.Throwable -> L77
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch: java.lang.Throwable -> L77
            r3.unlock()
            return r6
        L77:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.offerInternal(java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0025, code lost:            if (r5 == 0) goto L13;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:            r6 = describeTryOffer(r12);        r7 = r13.performAtomicTrySelect(r6);     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0034, code lost:            if (r7 != null) goto L18;     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0057, code lost:            if (r7 == kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED) goto L45;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:            if (r7 == kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC) goto L47;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0061, code lost:            if (r7 == kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L29;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0065, code lost:            if ((r7 instanceof kotlinx.coroutines.channels.Closed) == false) goto L27;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0084, code lost:            throw new java.lang.IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + r7).toString());     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0085, code lost:            r11.size = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x008b, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0036, code lost:            r11.size = r5;        r8 = r6.getResult();        r4 = kotlin.Unit.INSTANCE;     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0040, code lost:            r3.unlock();        kotlin.jvm.internal.Intrinsics.checkNotNull(r8);        ((kotlinx.coroutines.channels.ReceiveOrClosed) r8).completeResumeReceive(r12);     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0054, code lost:            return ((kotlinx.coroutines.channels.ReceiveOrClosed) r8).getOfferResult();     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0090, code lost:            if (r13.trySelect() != false) goto L37;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0092, code lost:            r11.size = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x009b, code lost:            return kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED();     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x009c, code lost:            enqueueElement(r5, r12);     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00a4, code lost:            return kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS;     */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object offerSelectInternal(E r12, kotlinx.coroutines.selects.SelectInstance<?> r13) {
        /*
            r11 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r11.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            int r5 = r11.size     // Catch: java.lang.Throwable -> La5
            kotlinx.coroutines.channels.Closed r6 = r11.getClosedForSend()     // Catch: java.lang.Throwable -> La5
            if (r6 == 0) goto L19
            r7 = 0
            r3.unlock()
            return r6
        L19:
            kotlinx.coroutines.internal.Symbol r6 = r11.updateBufferSize(r5)     // Catch: java.lang.Throwable -> La5
            if (r6 == 0) goto L25
            r7 = 0
            r3.unlock()
            return r6
        L25:
            if (r5 != 0) goto L8c
        L27:
        L28:
            kotlinx.coroutines.channels.AbstractSendChannel$TryOfferDesc r6 = r11.describeTryOffer(r12)     // Catch: java.lang.Throwable -> La5
            r7 = r6
            kotlinx.coroutines.internal.AtomicDesc r7 = (kotlinx.coroutines.internal.AtomicDesc) r7     // Catch: java.lang.Throwable -> La5
            java.lang.Object r7 = r13.performAtomicTrySelect(r7)     // Catch: java.lang.Throwable -> La5
            if (r7 != 0) goto L55
            r11.size = r5     // Catch: java.lang.Throwable -> La5
            java.lang.Object r8 = r6.getResult()     // Catch: java.lang.Throwable -> La5
            r0 = r8
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> La5
            r3.unlock()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            r1.completeResumeReceive(r12)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            java.lang.Object r1 = r1.getOfferResult()
            return r1
        L55:
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED     // Catch: java.lang.Throwable -> La5
            if (r7 == r8) goto L8c
            java.lang.Object r8 = kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC     // Catch: java.lang.Throwable -> La5
            if (r7 == r8) goto L27
            java.lang.Object r8 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch: java.lang.Throwable -> La5
            if (r7 == r8) goto L85
            boolean r8 = r7 instanceof kotlinx.coroutines.channels.Closed     // Catch: java.lang.Throwable -> La5
            if (r8 == 0) goto L68
            goto L85
        L68:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> La5
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La5
            r9.<init>()     // Catch: java.lang.Throwable -> La5
            java.lang.String r10 = "performAtomicTrySelect(describeTryOffer) returned "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch: java.lang.Throwable -> La5
            java.lang.StringBuilder r9 = r9.append(r7)     // Catch: java.lang.Throwable -> La5
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> La5
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> La5
            r8.<init>(r9)     // Catch: java.lang.Throwable -> La5
            throw r8     // Catch: java.lang.Throwable -> La5
        L85:
            r11.size = r5     // Catch: java.lang.Throwable -> La5
            r3.unlock()
            return r7
        L8c:
            boolean r6 = r13.trySelect()     // Catch: java.lang.Throwable -> La5
            if (r6 != 0) goto L9c
            r11.size = r5     // Catch: java.lang.Throwable -> La5
            java.lang.Object r6 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch: java.lang.Throwable -> La5
            r3.unlock()
            return r6
        L9c:
            r11.enqueueElement(r5, r12)     // Catch: java.lang.Throwable -> La5
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch: java.lang.Throwable -> La5
            r3.unlock()
            return r6
        La5:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.offerSelectInternal(java.lang.Object, kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public Object enqueueSend(Send send) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return super.enqueueSend(send);
        } finally {
            reentrantLock.unlock();
        }
    }

    private final Symbol updateBufferSize(int currentSize) {
        if (currentSize < this.capacity) {
            this.size = currentSize + 1;
            return null;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[this.onBufferOverflow.ordinal()]) {
            case 1:
                return AbstractChannelKt.OFFER_FAILED;
            case 2:
                return AbstractChannelKt.OFFER_SUCCESS;
            case 3:
                return null;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    private final void enqueueElement(int currentSize, E element) {
        if (currentSize < this.capacity) {
            ensureCapacity(currentSize);
            this.buffer[(this.head + currentSize) % this.buffer.length] = element;
            return;
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.onBufferOverflow == BufferOverflow.DROP_OLDEST)) {
                throw new AssertionError();
            }
        }
        this.buffer[this.head % this.buffer.length] = null;
        this.buffer[(this.head + currentSize) % this.buffer.length] = element;
        this.head = (this.head + 1) % this.buffer.length;
    }

    private final void ensureCapacity(int currentSize) {
        if (currentSize >= this.buffer.length) {
            int newSize = Math.min(this.buffer.length * 2, this.capacity);
            Object[] newBuffer = new Object[newSize];
            for (int i = 0; i < currentSize; i++) {
                newBuffer[i] = this.buffer[(this.head + i) % this.buffer.length];
            }
            ArraysKt.fill((Symbol[]) newBuffer, AbstractChannelKt.EMPTY, currentSize, newSize);
            this.buffer = newBuffer;
            this.head = 0;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected Object pollInternal() {
        Send send = null;
        boolean resumed = false;
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            int size = this.size;
            if (size == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object result = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = size - 1;
            Object replacement = AbstractChannelKt.POLL_FAILED;
            if (size == this.capacity) {
                while (true) {
                    Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    }
                    send = takeFirstSendOrPeekClosed;
                    Intrinsics.checkNotNull(send);
                    Symbol token = send.tryResumeSend(null);
                    if (token != null) {
                        if (DebugKt.getASSERTIONS_ENABLED()) {
                            if (!(token == CancellableContinuationImplKt.RESUME_TOKEN)) {
                                throw new AssertionError();
                            }
                        }
                        resumed = true;
                        replacement = send.getElement();
                    } else {
                        send.undeliveredElement();
                    }
                }
            }
            if (replacement != AbstractChannelKt.POLL_FAILED && !(replacement instanceof Closed)) {
                this.size = size;
                this.buffer[(this.head + size) % this.buffer.length] = replacement;
            }
            this.head = (this.head + 1) % this.buffer.length;
            Unit unit = Unit.INSTANCE;
            if (resumed) {
                Intrinsics.checkNotNull(send);
                send.completeResumeSend();
            }
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0033, code lost:            if (r7 == r14.capacity) goto L13;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:            r9 = describeTryPoll();        r10 = r15.performAtomicTrySelect(r9);     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0042, code lost:            if (r10 != null) goto L16;     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0058, code lost:            if (r10 == kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED) goto L50;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005c, code lost:            if (r10 == kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC) goto L52;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0062, code lost:            if (r10 != kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L25;     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0064, code lost:            r14.size = r7;        r14.buffer[r14.head] = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0070, code lost:            return r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0073, code lost:            if ((r10 instanceof kotlinx.coroutines.channels.Closed) == false) goto L28;     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0075, code lost:            r0 = r10;        r1 = true;        r8 = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0095, code lost:            throw new java.lang.IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + r10).toString());     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0044, code lost:            r0 = r9.getResult();        r1 = true;        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);        r8 = ((kotlinx.coroutines.channels.Send) r0).getElement();     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0098, code lost:            if (r8 == kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED) goto L35;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x009c, code lost:            if ((r8 instanceof kotlinx.coroutines.channels.Closed) != false) goto L35;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x009e, code lost:            r14.size = r7;        r14.buffer[(r14.head + r7) % r14.buffer.length] = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c2, code lost:            r14.head = (r14.head + 1) % r14.buffer.length;        r6 = kotlin.Unit.INSTANCE;     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d3, code lost:            if (r1 == false) goto L44;     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00d5, code lost:            kotlin.jvm.internal.Intrinsics.checkNotNull(r0);        ((kotlinx.coroutines.channels.Send) r0).completeResumeSend();     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00de, code lost:            return r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b0, code lost:            if (r15.trySelect() != false) goto L40;     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b2, code lost:            r14.size = r7;        r14.buffer[r14.head] = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c1, code lost:            return kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED();     */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.Object pollSelectInternal(kotlinx.coroutines.selects.SelectInstance<?> r15) {
        /*
            Method dump skipped, instructions count: 228
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.pollSelectInternal(kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public boolean enqueueReceiveInternal(Receive<? super E> receive) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return super.enqueueReceiveInternal(receive);
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public void onCancelIdempotent(boolean wasClosed) {
        Function1 onUndeliveredElement = this.onUndeliveredElement;
        UndeliveredElementException undeliveredElementException = null;
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            int i = this.size;
            for (int i2 = 0; i2 < i; i2++) {
                Object value = this.buffer[this.head];
                if (onUndeliveredElement != null && value != AbstractChannelKt.EMPTY) {
                    undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(onUndeliveredElement, value, undeliveredElementException);
                }
                this.buffer[this.head] = AbstractChannelKt.EMPTY;
                this.head = (this.head + 1) % this.buffer.length;
            }
            this.size = 0;
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            super.onCancelIdempotent(wasClosed);
            if (undeliveredElementException != null) {
                UndeliveredElementException it2 = undeliveredElementException;
                throw it2;
            }
        } catch (Throwable it3) {
            reentrantLock.unlock();
            throw it3;
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected String getBufferDebugString() {
        return "(buffer:capacity=" + this.capacity + ",size=" + this.size + ')';
    }
}
