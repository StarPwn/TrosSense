package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import org.cloudburstmc.netty.channel.raknet.RakChannel;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.RakPriority;
import org.cloudburstmc.netty.channel.raknet.RakReliability;
import org.cloudburstmc.netty.channel.raknet.RakSlidingWindow;
import org.cloudburstmc.netty.channel.raknet.RakState;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.channel.raknet.config.RakMetrics;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.channel.raknet.packet.RakDatagramPacket;
import org.cloudburstmc.netty.channel.raknet.packet.RakMessage;
import org.cloudburstmc.netty.util.BitQueue;
import org.cloudburstmc.netty.util.FastBinaryMinHeap;
import org.cloudburstmc.netty.util.IntRange;
import org.cloudburstmc.netty.util.RakUtils;
import org.cloudburstmc.netty.util.RoundRobinArray;
import org.cloudburstmc.netty.util.SplitPacketHelper;

/* loaded from: classes5.dex */
public class RakSessionCodec extends ChannelDuplexHandler {
    public static final String NAME = "rak-session-codec";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakSessionCodec.class);
    private final RakChannel channel;
    private int datagramReadIndex;
    private int datagramWriteIndex;
    private Queue<IntRange> incomingAcks;
    private Queue<IntRange> incomingNaks;
    private volatile long lastFlush;
    private long lastMinWeight;
    private int[] orderReadIndex;
    private int[] orderWriteIndex;
    private FastBinaryMinHeap<EncapsulatedPacket>[] orderingHeaps;
    private Queue<IntRange> outgoingAcks;
    private Queue<IntRange> outgoingNaks;
    private long[] outgoingPacketNextWeights;
    private FastBinaryMinHeap<EncapsulatedPacket> outgoingPackets;
    private int reliabilityReadIndex;
    private int reliabilityWriteIndex;
    private BitQueue reliableDatagramQueue;
    private IntObjectMap<RakDatagramPacket> sentDatagrams;
    private RakSlidingWindow slidingWindow;
    private int splitIndex;
    private RoundRobinArray<SplitPacketHelper> splitPackets;
    private ScheduledFuture<?> tickFuture;
    private volatile RakState state = RakState.UNCONNECTED;
    private volatile long lastTouched = System.currentTimeMillis();
    private long currentPingTime = -1;
    private long lastPingTime = -1;
    private long lastPongTime = -1;

    public RakSessionCodec(RakChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.state = RakState.CONNECTED;
        int mtu = getMtu();
        this.slidingWindow = new RakSlidingWindow(mtu);
        this.outgoingPacketNextWeights = new long[4];
        initHeapWeights();
        int maxChannels = ((Integer) this.channel.config().getOption(RakChannelOption.RAK_ORDERING_CHANNELS)).intValue();
        this.orderReadIndex = new int[maxChannels];
        this.orderWriteIndex = new int[maxChannels];
        this.orderingHeaps = new FastBinaryMinHeap[maxChannels];
        for (int i = 0; i < maxChannels; i++) {
            this.orderingHeaps[i] = new FastBinaryMinHeap<>(64);
        }
        this.outgoingPackets = new FastBinaryMinHeap<>(8);
        this.sentDatagrams = new IntObjectHashMap();
        this.incomingAcks = new ArrayDeque();
        this.incomingNaks = new ArrayDeque();
        this.outgoingAcks = new ArrayDeque();
        this.outgoingNaks = new ArrayDeque();
        this.reliableDatagramQueue = new BitQueue(512);
        this.splitPackets = new RoundRobinArray<>(256);
        boolean autoFlush = this.channel.config().isAutoFlush();
        int flushInterval = autoFlush ? this.channel.config().getFlushInterval() : 10;
        this.tickFuture = ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                RakSessionCodec.this.tryTick();
            }
        }, 0L, flushInterval, TimeUnit.MILLISECONDS);
        ctx.fireChannelActive();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.state == RakState.UNCONNECTED && this.tickFuture == null) {
            return;
        }
        this.state = RakState.UNCONNECTED;
        this.tickFuture.cancel(false);
        this.tickFuture = null;
        Iterator<SplitPacketHelper> it2 = this.splitPackets.iterator();
        while (it2.hasNext()) {
            SplitPacketHelper helper = it2.next();
            if (helper != null) {
                helper.release();
            }
        }
        this.splitPackets = null;
        Iterator<RakDatagramPacket> it3 = this.sentDatagrams.values().iterator();
        while (it3.hasNext()) {
            it3.next().release();
        }
        this.sentDatagrams = null;
        FastBinaryMinHeap<EncapsulatedPacket>[] orderingHeaps = this.orderingHeaps;
        this.orderingHeaps = null;
        if (orderingHeaps != null) {
            for (FastBinaryMinHeap<EncapsulatedPacket> orderingHeap : orderingHeaps) {
                while (true) {
                    EncapsulatedPacket packet = orderingHeap.poll();
                    if (packet != null) {
                        packet.release();
                    }
                }
                orderingHeap.release();
            }
        }
        FastBinaryMinHeap<EncapsulatedPacket> outgoingPackets = this.outgoingPackets;
        this.outgoingPackets = null;
        if (outgoingPackets != null) {
            while (true) {
                EncapsulatedPacket packet2 = outgoingPackets.poll();
                if (packet2 == null) {
                    break;
                } else {
                    packet2.release();
                }
            }
            outgoingPackets.release();
        }
        if (log.isTraceEnabled()) {
            log.trace("RakNet Session ({} => {}) closed!", this.channel.localAddress(), getRemoteAddress());
        }
    }

    private void initHeapWeights() {
        for (int priorityLevel = 0; priorityLevel < 4; priorityLevel++) {
            this.outgoingPacketNextWeights[priorityLevel] = ((1 << priorityLevel) * priorityLevel) + priorityLevel;
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof ByteBuf) {
            msg = new RakMessage((ByteBuf) msg);
        } else if (!(msg instanceof RakMessage)) {
            throw new IllegalArgumentException("Message must be a ByteBuf or RakMessage");
        }
        try {
            send(ctx, (RakMessage) msg);
            promise.setSuccess((Void) null);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (!this.channel.config().isAutoFlush()) {
            internalFlush(ctx);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (!(msg instanceof RakDatagramPacket)) {
                return;
            }
            RakDatagramPacket packet = (RakDatagramPacket) msg;
            if (this.state == RakState.UNCONNECTED) {
                log.debug("{} received message from inactive channel: {}", getRemoteAddress(), packet);
            } else {
                handleDatagram(ctx, packet);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        m2045x1effc1bb(RakDisconnectReason.DISCONNECTED).addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec$$ExternalSyntheticLambda1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakSessionCodec.lambda$disconnect$0(ChannelPromise.this, future);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$disconnect$0(ChannelPromise promise, Future future) throws Exception {
        if (future.cause() == null) {
            promise.trySuccess();
        } else {
            promise.tryFailure(future.cause());
        }
    }

    private void send(ChannelHandlerContext ctx, RakMessage message) {
        if (this.state == RakState.UNCONNECTED) {
            throw new IllegalStateException("Can not send RakMessage to inactive channel");
        }
        if (message.content().getUnsignedByte(message.content().readerIndex()) == 192) {
            throw new IllegalArgumentException();
        }
        EncapsulatedPacket[] packets = createEncapsulated(message);
        if (message.priority() == RakPriority.IMMEDIATE) {
            sendImmediate(ctx, packets);
            return;
        }
        long weight = getNextWeight(message.priority());
        if (packets.length == 1) {
            this.outgoingPackets.insert(weight, packets[0]);
        } else {
            this.outgoingPackets.insertSeries(weight, packets);
        }
    }

    private void handleDatagram(ChannelHandlerContext ctx, RakDatagramPacket packet) {
        touch();
        RakMetrics metrics = getMetrics();
        if (metrics != null) {
            metrics.rakDatagramsIn(1);
        }
        this.slidingWindow.onPacketReceived(packet.getSendTime());
        int prevSequenceIndex = this.datagramReadIndex;
        if (prevSequenceIndex <= packet.getSequenceIndex()) {
            this.datagramReadIndex = packet.getSequenceIndex() + 1;
        }
        int missedDatagrams = packet.getSequenceIndex() - prevSequenceIndex;
        if (missedDatagrams > 0) {
            this.outgoingNaks.offer(new IntRange(packet.getSequenceIndex() - missedDatagrams, packet.getSequenceIndex() - 1));
        }
        this.outgoingAcks.offer(new IntRange(packet.getSequenceIndex(), packet.getSequenceIndex()));
        for (EncapsulatedPacket encapsulated : packet.getPackets()) {
            if (encapsulated.getReliability().isReliable()) {
                int missed = encapsulated.getReliabilityIndex() - this.reliabilityReadIndex;
                if (missed > 0) {
                    if (missed < this.reliableDatagramQueue.size()) {
                        if (this.reliableDatagramQueue.get(missed)) {
                            this.reliableDatagramQueue.set(missed, false);
                        } else {
                            continue;
                        }
                    } else {
                        int count = missed - this.reliableDatagramQueue.size();
                        for (int i = 0; i < count; i++) {
                            this.reliableDatagramQueue.add(true);
                        }
                        this.reliableDatagramQueue.add(false);
                    }
                    while (!this.reliableDatagramQueue.isEmpty() && !this.reliableDatagramQueue.peek()) {
                        this.reliableDatagramQueue.poll();
                        this.reliabilityReadIndex++;
                    }
                } else if (missed == 0) {
                    this.reliabilityReadIndex++;
                    if (!this.reliableDatagramQueue.isEmpty()) {
                        this.reliableDatagramQueue.poll();
                    }
                    while (!this.reliableDatagramQueue.isEmpty()) {
                        this.reliableDatagramQueue.poll();
                        this.reliabilityReadIndex++;
                    }
                } else {
                    continue;
                }
            }
            if (encapsulated.isSplit()) {
                EncapsulatedPacket reassembled = getReassembledPacket(encapsulated, ctx.alloc());
                if (reassembled == null) {
                    continue;
                } else {
                    try {
                        checkForOrdered(ctx, reassembled);
                    } finally {
                        reassembled.release();
                    }
                }
            } else {
                checkForOrdered(ctx, encapsulated);
            }
        }
    }

    private void checkForOrdered(ChannelHandlerContext ctx, EncapsulatedPacket packet) {
        if (packet.getReliability().isOrdered()) {
            onOrderedReceived(ctx, packet);
        } else {
            ctx.fireChannelRead((Object) packet.retain());
        }
    }

    private void onOrderedReceived(ChannelHandlerContext ctx, EncapsulatedPacket packet) {
        FastBinaryMinHeap<EncapsulatedPacket> binaryHeap = this.orderingHeaps[packet.getOrderingChannel()];
        if (this.orderReadIndex[packet.getOrderingChannel()] < packet.getOrderingIndex()) {
            binaryHeap.insert(packet.getOrderingIndex(), packet.retain());
            return;
        }
        if (this.orderReadIndex[packet.getOrderingChannel()] > packet.getOrderingIndex()) {
            return;
        }
        int[] iArr = this.orderReadIndex;
        short orderingChannel = packet.getOrderingChannel();
        iArr[orderingChannel] = iArr[orderingChannel] + 1;
        ctx.fireChannelRead((Object) packet.retain());
        while (true) {
            EncapsulatedPacket queuedPacket = binaryHeap.peek();
            if (queuedPacket != null && queuedPacket.getOrderingIndex() == this.orderReadIndex[packet.getOrderingChannel()]) {
                try {
                    binaryHeap.remove();
                    int[] iArr2 = this.orderReadIndex;
                    short orderingChannel2 = packet.getOrderingChannel();
                    iArr2[orderingChannel2] = iArr2[orderingChannel2] + 1;
                    ctx.fireChannelRead((Object) queuedPacket.retain());
                } finally {
                    queuedPacket.release();
                }
            } else {
                return;
            }
        }
    }

    private EncapsulatedPacket getReassembledPacket(EncapsulatedPacket splitPacket, ByteBufAllocator alloc) {
        checkForClosed();
        SplitPacketHelper helper = this.splitPackets.get(splitPacket.getPartId());
        if (helper == null) {
            RoundRobinArray<SplitPacketHelper> roundRobinArray = this.splitPackets;
            int partId = splitPacket.getPartId();
            SplitPacketHelper splitPacketHelper = new SplitPacketHelper(splitPacket.getPartCount());
            helper = splitPacketHelper;
            roundRobinArray.set(partId, splitPacketHelper);
        }
        EncapsulatedPacket result = helper.add(splitPacket, alloc);
        if (result != null) {
            this.splitPackets.remove(splitPacket.getPartId(), helper);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryTick() {
        try {
            onTick();
        } catch (Throwable t) {
            log.error("[{}] Error while ticking RakSessionCodec state={} channelActive={}", getRemoteAddress(), this.state, Boolean.valueOf(this.channel.isActive()), t);
            this.channel.close();
        }
    }

    private void onTick() {
        long curTime = System.currentTimeMillis();
        if (this.state == RakState.UNCONNECTED) {
            if (isTimedOut(curTime)) {
                close(RakDisconnectReason.TIMED_OUT);
            }
        } else {
            if (isTimedOut(curTime)) {
                disconnect(RakDisconnectReason.TIMED_OUT);
                return;
            }
            ChannelHandlerContext ctx = ctx();
            if (this.currentPingTime + RakConstants.CC_MAXIMUM_THRESHOLD < curTime) {
                ByteBuf buffer = ctx.alloc().ioBuffer(9);
                buffer.writeByte(0);
                buffer.writeLong(curTime);
                this.currentPingTime = curTime;
                write(ctx, new RakMessage(buffer, RakReliability.UNRELIABLE, RakPriority.IMMEDIATE), ctx.voidPromise());
            }
            internalFlush(ctx);
        }
    }

    private void internalFlush(ChannelHandlerContext ctx) {
        long curTime = System.currentTimeMillis();
        if (this.lastFlush == curTime) {
            return;
        }
        this.lastFlush = curTime;
        handleIncomingAcknowledge(ctx, curTime, this.incomingAcks, false);
        handleIncomingAcknowledge(ctx, curTime, this.incomingNaks, true);
        int mtuSize = getMtu();
        int ackMtu = mtuSize - 4;
        int writtenAcks = 0;
        int writtenNacks = 0;
        while (!this.outgoingAcks.isEmpty()) {
            ByteBuf buffer = ctx.alloc().ioBuffer(ackMtu);
            buffer.writeByte(-64);
            writtenAcks += RakUtils.writeAckEntries(buffer, this.outgoingAcks, ackMtu - 1);
            ctx.write(buffer);
            this.slidingWindow.onSendAck();
        }
        while (!this.outgoingNaks.isEmpty()) {
            ByteBuf buffer2 = ctx.alloc().ioBuffer(ackMtu);
            buffer2.writeByte(-96);
            writtenNacks += RakUtils.writeAckEntries(buffer2, this.outgoingNaks, ackMtu - 1);
            ctx.write(buffer2);
        }
        int resendCount = sendStaleDatagrams(ctx, curTime);
        sendDatagrams(ctx, curTime, mtuSize);
        ctx.flush();
        RakMetrics metrics = getMetrics();
        if (metrics != null) {
            metrics.nackOut(writtenNacks);
            metrics.ackOut(writtenAcks);
            metrics.rakStaleDatagrams(resendCount);
        }
    }

    private void handleIncomingAcknowledge(ChannelHandlerContext ctx, long curTime, Queue<IntRange> queue, boolean nack) {
        if (queue.isEmpty()) {
            return;
        }
        while (true) {
            IntRange range = queue.poll();
            if (range != null) {
                for (int i = range.start; i <= range.end; i++) {
                    RakDatagramPacket datagram = this.sentDatagrams.remove(i);
                    if (datagram != null) {
                        if (nack) {
                            onIncomingNack(ctx, datagram, curTime);
                        } else {
                            onIncomingAck(datagram, curTime);
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private void onIncomingAck(RakDatagramPacket datagram, long curTime) {
        try {
            this.slidingWindow.onAck(curTime, datagram, this.datagramReadIndex);
        } finally {
            datagram.release();
        }
    }

    private void onIncomingNack(ChannelHandlerContext ctx, RakDatagramPacket datagram, long curTime) {
        if (log.isTraceEnabled()) {
            log.trace("NAK'ed datagram {} from {}", Integer.valueOf(datagram.getSequenceIndex()), getRemoteAddress());
        }
        this.slidingWindow.onNak();
        m2047xca6188af(ctx, datagram, curTime);
    }

    private int sendStaleDatagrams(ChannelHandlerContext ctx, long curTime) {
        if (this.sentDatagrams.isEmpty()) {
            return 0;
        }
        boolean hasResent = false;
        int resendCount = 0;
        int transmissionBandwidth = this.slidingWindow.getRetransmissionBandwidth();
        for (RakDatagramPacket datagram : this.sentDatagrams.values()) {
            if (datagram.getNextSend() <= curTime) {
                int size = datagram.getSize();
                if (transmissionBandwidth < size) {
                    break;
                }
                transmissionBandwidth -= size;
                if (!hasResent) {
                    hasResent = true;
                }
                if (log.isTraceEnabled()) {
                    log.trace("Stale datagram {} from {}", Integer.valueOf(datagram.getSequenceIndex()), getRemoteAddress());
                }
                resendCount++;
                m2047xca6188af(ctx, datagram, curTime);
            }
        }
        if (hasResent) {
            this.slidingWindow.onResend(curTime);
        }
        return resendCount;
    }

    private void sendDatagrams(ChannelHandlerContext ctx, long curTime, int mtuSize) {
        int size;
        if (this.outgoingPackets.isEmpty()) {
            return;
        }
        int transmissionBandwidth = this.slidingWindow.getTransmissionBandwidth();
        RakDatagramPacket datagram = RakDatagramPacket.newInstance();
        datagram.setSendTime(curTime);
        while (true) {
            EncapsulatedPacket packet = this.outgoingPackets.peek();
            if (packet == null || transmissionBandwidth < (size = packet.getSize())) {
                break;
            }
            transmissionBandwidth -= size;
            this.outgoingPackets.remove();
            if (!datagram.tryAddPacket(packet, mtuSize)) {
                m2047xca6188af(ctx, datagram, curTime);
                datagram = RakDatagramPacket.newInstance();
                datagram.setSendTime(curTime);
                if (!datagram.tryAddPacket(packet, mtuSize)) {
                    throw new IllegalArgumentException("Packet too large to fit in MTU (size: " + packet.getSize() + ", MTU: " + mtuSize + ")");
                }
            }
        }
        if (!datagram.getPackets().isEmpty()) {
            m2047xca6188af(ctx, datagram, curTime);
        }
    }

    private void sendImmediate(ChannelHandlerContext ctx, EncapsulatedPacket[] packets) {
        long curTime = System.currentTimeMillis();
        for (EncapsulatedPacket packet : packets) {
            RakDatagramPacket datagram = RakDatagramPacket.newInstance();
            datagram.setSendTime(curTime);
            if (!datagram.tryAddPacket(packet, getMtu())) {
                throw new IllegalArgumentException("Packet too large to fit in MTU (size: " + packet.getSize() + ", MTU: " + getMtu() + ")");
            }
            m2047xca6188af(ctx, datagram, curTime);
        }
        ctx.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: sendDatagram, reason: merged with bridge method [inline-methods] */
    public void m2047xca6188af(final ChannelHandlerContext ctx, final RakDatagramPacket datagram, final long time) {
        if (!this.channel.parent().eventLoop().inEventLoop()) {
            log.error("Tried to send datagrams from wrong thread: {}", Thread.currentThread().getName(), new Throwable());
            this.channel.parent().eventLoop().execute(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    RakSessionCodec.this.m2047xca6188af(ctx, datagram, time);
                }
            });
            return;
        }
        if (datagram.getPackets().isEmpty()) {
            throw new IllegalArgumentException("RakNetDatagram with no packets");
        }
        RakMetrics metrics = getMetrics();
        if (metrics != null) {
            metrics.rakDatagramsOut(1);
        }
        int oldIndex = datagram.getSequenceIndex();
        int i = this.datagramWriteIndex;
        this.datagramWriteIndex = i + 1;
        datagram.setSequenceIndex(i);
        Iterator<EncapsulatedPacket> it2 = datagram.getPackets().iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            EncapsulatedPacket packet = it2.next();
            if (packet.getReliability().isReliable()) {
                datagram.setNextSend(this.slidingWindow.getRtoForRetransmission() + time);
                if (oldIndex == -1) {
                    this.slidingWindow.onReliableSend(datagram);
                } else {
                    this.sentDatagrams.remove(Integer.valueOf(oldIndex), datagram);
                }
                this.sentDatagrams.put(datagram.getSequenceIndex(), (int) datagram.retain());
            }
        }
        ctx.write(datagram);
    }

    private ChannelHandlerContext ctx() {
        return this.channel.rakPipeline().context(NAME);
    }

    private EncapsulatedPacket[] createEncapsulated(RakMessage rakMessage) {
        ByteBuf[] buffers;
        int maxLength = (getMtu() - 28) - 4;
        int splitId = 0;
        RakReliability reliability = rakMessage.reliability();
        ByteBuf buffer = rakMessage.content();
        int orderingChannel = rakMessage.channel();
        if (buffer.readableBytes() > maxLength) {
            switch (reliability) {
                case UNRELIABLE:
                    reliability = RakReliability.RELIABLE;
                    break;
                case UNRELIABLE_SEQUENCED:
                    reliability = RakReliability.RELIABLE_SEQUENCED;
                    break;
                case UNRELIABLE_WITH_ACK_RECEIPT:
                    reliability = RakReliability.RELIABLE_WITH_ACK_RECEIPT;
                    break;
            }
            int split = ((buffer.readableBytes() - 1) / maxLength) + 1;
            buffer.retain(split);
            buffers = new ByteBuf[split];
            for (int i = 0; i < split; i++) {
                buffers[i] = buffer.readSlice(Math.min(maxLength, buffer.readableBytes()));
            }
            if (buffer.isReadable()) {
                throw new IllegalStateException("Buffer still has bytes to read!");
            }
            int i2 = this.splitIndex;
            this.splitIndex = i2 + 1;
            splitId = i2;
        } else {
            buffers = new ByteBuf[]{buffer.readRetainedSlice(buffer.readableBytes())};
        }
        int orderingIndex = 0;
        if (reliability.isOrdered()) {
            int[] iArr = this.orderWriteIndex;
            int i3 = iArr[orderingChannel];
            iArr[orderingChannel] = i3 + 1;
            orderingIndex = i3;
        }
        EncapsulatedPacket[] packets = new EncapsulatedPacket[buffers.length];
        int parts = buffers.length;
        for (int i4 = 0; i4 < parts; i4++) {
            EncapsulatedPacket packet = EncapsulatedPacket.newInstance();
            packet.setBuffer(buffers[i4]);
            packet.setNeedsBAS(true);
            packet.setOrderingChannel((short) orderingChannel);
            packet.setOrderingIndex(orderingIndex);
            packet.setReliability(reliability);
            if (reliability.isReliable()) {
                int i5 = this.reliabilityWriteIndex;
                this.reliabilityWriteIndex = i5 + 1;
                packet.setReliabilityIndex(i5);
            }
            if (parts > 1) {
                packet.setSplit(true);
                packet.setPartIndex(i4);
                packet.setPartCount(parts);
                packet.setPartId(splitId);
            }
            packets[i4] = packet;
        }
        return packets;
    }

    private long getNextWeight(RakPriority priority) {
        int priorityLevel = priority.ordinal();
        long next = this.outgoingPacketNextWeights[priorityLevel];
        if (this.outgoingPackets.isEmpty()) {
            initHeapWeights();
        } else if (next >= this.lastMinWeight) {
            next = this.lastMinWeight + ((1 << priorityLevel) * priorityLevel) + priorityLevel;
            this.outgoingPacketNextWeights[priorityLevel] = ((1 << priorityLevel) * (priorityLevel + 1)) + next + priorityLevel;
        }
        this.lastMinWeight = (next - ((1 << priorityLevel) * priorityLevel)) + priorityLevel;
        return next;
    }

    public void disconnect() {
        disconnect(RakDisconnectReason.DISCONNECTED);
    }

    public void disconnect(final RakDisconnectReason reason) {
        if (this.channel.parent().eventLoop().inEventLoop()) {
            m2045x1effc1bb(reason);
        } else {
            this.channel.parent().eventLoop().execute(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    RakSessionCodec.this.m2045x1effc1bb(reason);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: disconnect0, reason: merged with bridge method [inline-methods] */
    public ChannelPromise m2045x1effc1bb(final RakDisconnectReason reason) {
        if (this.state == RakState.UNCONNECTED || this.state == RakState.DISCONNECTING) {
            return this.channel.voidPromise();
        }
        this.state = RakState.DISCONNECTING;
        if (log.isDebugEnabled()) {
            log.debug("Disconnecting RakNet Session ({} => {}) due to {}", this.channel.localAddress(), getRemoteAddress(), reason);
        }
        ChannelHandlerContext ctx = ctx();
        ByteBuf buffer = ctx.alloc().ioBuffer(1);
        buffer.writeByte(21);
        RakMessage rakMessage = new RakMessage(buffer, RakReliability.RELIABLE, RakPriority.IMMEDIATE);
        ChannelPromise promise = ctx.newPromise();
        promise.addListener(new GenericFutureListener() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec$$ExternalSyntheticLambda0
            @Override // io.netty.util.concurrent.GenericFutureListener
            public final void operationComplete(Future future) {
                RakSessionCodec.this.m2046x951108bc(reason, (ChannelFuture) future);
            }
        });
        write(ctx, rakMessage, promise);
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$disconnect0$3$org-cloudburstmc-netty-handler-codec-raknet-common-RakSessionCodec, reason: not valid java name */
    public /* synthetic */ void m2046x951108bc(RakDisconnectReason reason, ChannelFuture future) throws Exception {
        this.channel.pipeline().fireUserEventTriggered((Object) reason).close();
    }

    public void close(RakDisconnectReason reason) {
        if (this.state == RakState.DISCONNECTING) {
            return;
        }
        this.state = RakState.DISCONNECTING;
        if (log.isDebugEnabled()) {
            log.debug("Closing RakNet Session ({} => {}) due to {}", this.channel.localAddress(), getRemoteAddress(), reason);
        }
        this.channel.pipeline().fireUserEventTriggered((Object) reason).close();
    }

    public boolean isClosed() {
        return this.state == RakState.UNCONNECTED;
    }

    private void checkForClosed() {
        if (this.state == RakState.UNCONNECTED) {
            throw new IllegalStateException("RakSession is closed!");
        }
    }

    public void recalculatePongTime(long pingTime) {
        if (this.currentPingTime == pingTime) {
            this.lastPingTime = this.currentPingTime;
            this.lastPongTime = System.currentTimeMillis();
        }
    }

    private void touch() {
        checkForClosed();
        this.lastTouched = System.currentTimeMillis();
    }

    public boolean isStale(long curTime) {
        return curTime - this.lastTouched >= 5000;
    }

    public boolean isStale() {
        return isStale(System.currentTimeMillis());
    }

    public boolean isTimedOut(long curTime) {
        return curTime - this.lastTouched >= ((Long) this.channel.config().getOption(RakChannelOption.RAK_SESSION_TIMEOUT)).longValue();
    }

    public boolean isTimedOut() {
        return isTimedOut(System.currentTimeMillis());
    }

    public long getPing() {
        return this.lastPongTime - this.lastPingTime;
    }

    public double getRTT() {
        return this.slidingWindow.getRTT();
    }

    public int getMtu() {
        return (((Integer) this.channel.config().getOption(RakChannelOption.RAK_MTU)).intValue() - 8) - (getRemoteAddress().getAddress() instanceof Inet6Address ? 40 : 20);
    }

    public RakMetrics getMetrics() {
        return (RakMetrics) this.channel.config().getOption(RakChannelOption.RAK_METRICS);
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) this.channel.remoteAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Queue<IntRange> getAcknowledgeQueue(boolean nack) {
        return nack ? this.incomingNaks : this.incomingAcks;
    }

    public Channel getChannel() {
        return this.channel;
    }
}
