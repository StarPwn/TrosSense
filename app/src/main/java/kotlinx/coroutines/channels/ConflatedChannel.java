package kotlinx.coroutines.channels;

import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.UndeliveredElementException;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

/* compiled from: ConflatedChannel.kt */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012 \u0010\u0003\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0018\u001a\u00020\r2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aH\u0014J\u0015\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u001dJ!\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00028\u00002\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030 H\u0014¢\u0006\u0002\u0010!J\u0010\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\rH\u0014J\n\u0010$\u001a\u0004\u0018\u00010\u0017H\u0014J\u0016\u0010%\u001a\u0004\u0018\u00010\u00172\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030 H\u0014J\u0014\u0010&\u001a\u0004\u0018\u00010'2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0017H\u0002R\u0014\u0010\b\u001a\u00020\t8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\r8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000eR\u0014\u0010\u0010\u001a\u00020\r8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0014\u0010\u0011\u001a\u00020\r8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000eR\u0014\u0010\u0012\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000eR\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006("}, d2 = {"Lkotlinx/coroutines/channels/ConflatedChannel;", "E", "Lkotlinx/coroutines/channels/AbstractChannel;", "onUndeliveredElement", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "(Lkotlin/jvm/functions/Function1;)V", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "isEmpty", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "value", "", "enqueueReceiveInternal", "receive", "Lkotlinx/coroutines/channels/Receive;", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "onCancelIdempotent", "wasClosed", "pollInternal", "pollSelectInternal", "updateValueLocked", "Lkotlinx/coroutines/internal/UndeliveredElementException;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public class ConflatedChannel<E> extends AbstractChannel<E> {
    private final ReentrantLock lock;
    private Object value;

    public ConflatedChannel(Function1<? super E, Unit> function1) {
        super(function1);
        this.lock = new ReentrantLock();
        this.value = AbstractChannelKt.EMPTY;
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return this.value == AbstractChannelKt.EMPTY;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return false;
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x001e, code lost:            r5 = takeFirstReceiveOrPeekClosed();     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0022, code lost:            if (r5 != null) goto L13;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0029, code lost:            if ((r5 instanceof kotlinx.coroutines.channels.Closed) == false) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0030, code lost:            kotlin.jvm.internal.Intrinsics.checkNotNull(r5);        r5 = r5.tryResumeReceive(r9, null);     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0038, code lost:            if (r5 == null) goto L46;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003e, code lost:            if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() == false) goto L29;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0043, code lost:            if (r5 != kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN) goto L24;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0045, code lost:            r7 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0048, code lost:            if (r7 == false) goto L27;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0050, code lost:            throw new java.lang.AssertionError();     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0047, code lost:            r7 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0051, code lost:            r4 = kotlin.Unit.INSTANCE;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0054, code lost:            r3.unlock();        r5.completeResumeReceive(r9);     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x005f, code lost:            return r5.getOfferResult();     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x002f, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0060, code lost:            r5 = updateValueLocked(r9);     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0064, code lost:            if (r5 != null) goto L37;     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:            return kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS;     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006e, code lost:            throw r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:            if (r8.value == kotlinx.coroutines.channels.AbstractChannelKt.EMPTY) goto L10;     */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object offerInternal(E r9) {
        /*
            r8 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r8.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            kotlinx.coroutines.channels.Closed r5 = r8.getClosedForSend()     // Catch: java.lang.Throwable -> L6f
            if (r5 == 0) goto L17
            r6 = 0
            r3.unlock()
            return r5
        L17:
            java.lang.Object r5 = r8.value     // Catch: java.lang.Throwable -> L6f
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.EMPTY     // Catch: java.lang.Throwable -> L6f
            if (r5 != r6) goto L60
        L1d:
        L1e:
            kotlinx.coroutines.channels.ReceiveOrClosed r5 = r8.takeFirstReceiveOrPeekClosed()     // Catch: java.lang.Throwable -> L6f
            if (r5 != 0) goto L25
            goto L60
        L25:
            r0 = r5
            boolean r5 = r0 instanceof kotlinx.coroutines.channels.Closed     // Catch: java.lang.Throwable -> L6f
            if (r5 == 0) goto L30
        L2c:
            r3.unlock()
            return r0
        L30:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch: java.lang.Throwable -> L6f
            r5 = 0
            kotlinx.coroutines.internal.Symbol r5 = r0.tryResumeReceive(r9, r5)     // Catch: java.lang.Throwable -> L6f
            if (r5 == 0) goto L1d
            boolean r6 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch: java.lang.Throwable -> L6f
            if (r6 == 0) goto L51
            r6 = 0
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN     // Catch: java.lang.Throwable -> L6f
            if (r5 != r7) goto L47
            r7 = 1
            goto L48
        L47:
            r7 = 0
        L48:
            if (r7 == 0) goto L4b
            goto L51
        L4b:
            java.lang.AssertionError r6 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> L6f
            r6.<init>()     // Catch: java.lang.Throwable -> L6f
            throw r6     // Catch: java.lang.Throwable -> L6f
        L51:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L6f
            r3.unlock()
            r0.completeResumeReceive(r9)
            java.lang.Object r1 = r0.getOfferResult()
            return r1
        L60:
            kotlinx.coroutines.internal.UndeliveredElementException r5 = r8.updateValueLocked(r9)     // Catch: java.lang.Throwable -> L6f
            if (r5 != 0) goto L6c
            kotlinx.coroutines.internal.Symbol r5 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch: java.lang.Throwable -> L6f
            r3.unlock()
            return r5
        L6c:
            r6 = 0
            throw r5     // Catch: java.lang.Throwable -> L6f
        L6f:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ConflatedChannel.offerInternal(java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x001e, code lost:            r5 = describeTryOffer(r11);        r6 = r12.performAtomicTrySelect(r5);     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x002a, code lost:            if (r6 != null) goto L15;     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004b, code lost:            if (r6 == kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED) goto L45;     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004f, code lost:            if (r6 == kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC) goto L47;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0055, code lost:            if (r6 == kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L26;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0059, code lost:            if ((r6 instanceof kotlinx.coroutines.channels.Closed) == false) goto L24;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0078, code lost:            throw new java.lang.IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + r6).toString());     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x007c, code lost:            return r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x002c, code lost:            r7 = r5.getResult();        r4 = kotlin.Unit.INSTANCE;     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0034, code lost:            r3.unlock();        kotlin.jvm.internal.Intrinsics.checkNotNull(r7);        ((kotlinx.coroutines.channels.ReceiveOrClosed) r7).completeResumeReceive(r11);     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0048, code lost:            return ((kotlinx.coroutines.channels.ReceiveOrClosed) r7).getOfferResult();     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0081, code lost:            if (r12.trySelect() != false) goto L33;     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x008a, code lost:            return kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED();     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x008b, code lost:            r5 = updateValueLocked(r11);     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008f, code lost:            if (r5 != null) goto L38;     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0096, code lost:            return kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0099, code lost:            throw r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:            if (r10.value == kotlinx.coroutines.channels.AbstractChannelKt.EMPTY) goto L10;     */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object offerSelectInternal(E r11, kotlinx.coroutines.selects.SelectInstance<?> r12) {
        /*
            r10 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r10.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            kotlinx.coroutines.channels.Closed r5 = r10.getClosedForSend()     // Catch: java.lang.Throwable -> L9a
            if (r5 == 0) goto L17
            r6 = 0
            r3.unlock()
            return r5
        L17:
            java.lang.Object r5 = r10.value     // Catch: java.lang.Throwable -> L9a
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.EMPTY     // Catch: java.lang.Throwable -> L9a
            if (r5 != r6) goto L7d
        L1d:
        L1e:
            kotlinx.coroutines.channels.AbstractSendChannel$TryOfferDesc r5 = r10.describeTryOffer(r11)     // Catch: java.lang.Throwable -> L9a
            r6 = r5
            kotlinx.coroutines.internal.AtomicDesc r6 = (kotlinx.coroutines.internal.AtomicDesc) r6     // Catch: java.lang.Throwable -> L9a
            java.lang.Object r6 = r12.performAtomicTrySelect(r6)     // Catch: java.lang.Throwable -> L9a
            if (r6 != 0) goto L49
            java.lang.Object r7 = r5.getResult()     // Catch: java.lang.Throwable -> L9a
            r0 = r7
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L9a
            r3.unlock()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            r1.completeResumeReceive(r11)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            java.lang.Object r1 = r1.getOfferResult()
            return r1
        L49:
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED     // Catch: java.lang.Throwable -> L9a
            if (r6 == r7) goto L7d
            java.lang.Object r7 = kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC     // Catch: java.lang.Throwable -> L9a
            if (r6 == r7) goto L1d
            java.lang.Object r7 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch: java.lang.Throwable -> L9a
            if (r6 == r7) goto L79
            boolean r7 = r6 instanceof kotlinx.coroutines.channels.Closed     // Catch: java.lang.Throwable -> L9a
            if (r7 == 0) goto L5c
            goto L79
        L5c:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L9a
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L9a
            r8.<init>()     // Catch: java.lang.Throwable -> L9a
            java.lang.String r9 = "performAtomicTrySelect(describeTryOffer) returned "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch: java.lang.Throwable -> L9a
            java.lang.StringBuilder r8 = r8.append(r6)     // Catch: java.lang.Throwable -> L9a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L9a
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L9a
            r7.<init>(r8)     // Catch: java.lang.Throwable -> L9a
            throw r7     // Catch: java.lang.Throwable -> L9a
        L79:
            r3.unlock()
            return r6
        L7d:
            boolean r5 = r12.trySelect()     // Catch: java.lang.Throwable -> L9a
            if (r5 != 0) goto L8b
            java.lang.Object r5 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch: java.lang.Throwable -> L9a
            r3.unlock()
            return r5
        L8b:
            kotlinx.coroutines.internal.UndeliveredElementException r5 = r10.updateValueLocked(r11)     // Catch: java.lang.Throwable -> L9a
            if (r5 != 0) goto L97
            kotlinx.coroutines.internal.Symbol r5 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch: java.lang.Throwable -> L9a
            r3.unlock()
            return r5
        L97:
            r6 = 0
            throw r5     // Catch: java.lang.Throwable -> L9a
        L9a:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ConflatedChannel.offerSelectInternal(java.lang.Object, kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected Object pollInternal() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            if (this.value == AbstractChannelKt.EMPTY) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object result = this.value;
            this.value = AbstractChannelKt.EMPTY;
            Unit unit = Unit.INSTANCE;
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected Object pollSelectInternal(SelectInstance<?> select) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            if (this.value == AbstractChannelKt.EMPTY) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            if (!select.trySelect()) {
                return SelectKt.getALREADY_SELECTED();
            }
            Object result = this.value;
            this.value = AbstractChannelKt.EMPTY;
            Unit unit = Unit.INSTANCE;
            return result;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public void onCancelIdempotent(boolean wasClosed) {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            UndeliveredElementException updateValueLocked = updateValueLocked(AbstractChannelKt.EMPTY);
            Unit unit = Unit.INSTANCE;
            reentrantLock.unlock();
            super.onCancelIdempotent(wasClosed);
            if (updateValueLocked != null) {
                throw updateValueLocked;
            }
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    private final UndeliveredElementException updateValueLocked(Object element) {
        Function1<E, Unit> function1;
        Object old = this.value;
        UndeliveredElementException undeliveredElementException = null;
        if (old != AbstractChannelKt.EMPTY && (function1 = this.onUndeliveredElement) != null) {
            undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, old, null, 2, null);
        }
        UndeliveredElementException undeliveredElementException2 = undeliveredElementException;
        this.value = element;
        return undeliveredElementException2;
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

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected String getBufferDebugString() {
        ReentrantLock $this$withLock$iv = this.lock;
        ReentrantLock reentrantLock = $this$withLock$iv;
        reentrantLock.lock();
        try {
            return "(value=" + this.value + ')';
        } finally {
            reentrantLock.unlock();
        }
    }
}
