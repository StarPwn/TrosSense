package org.cloudburstmc.netty.handler.codec.raknet.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;

/* loaded from: classes5.dex */
public class RakServerRateLimiter extends SimpleChannelInboundHandler<DatagramPacket> {
    public static final String NAME = "rak-server-rate-limiter";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakServerRateLimiter.class);
    private ScheduledFuture<?> blockedTickFuture;
    private final RakServerChannel channel;
    private ScheduledFuture<?> tickFuture;
    private final ConcurrentHashMap<InetAddress, AtomicInteger> rateLimitMap = new ConcurrentHashMap<>();
    private final Map<InetAddress, Long> blockedConnections = new ConcurrentHashMap();
    private final Collection<InetAddress> exceptions = Collections.synchronizedCollection(new HashSet());
    private final AtomicLong globalCounter = new AtomicLong(0);

    public RakServerRateLimiter(RakServerChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.tickFuture = ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerRateLimiter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RakServerRateLimiter.this.onRakTick();
            }
        }, 10L, 10L, TimeUnit.MILLISECONDS);
        this.blockedTickFuture = ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerRateLimiter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RakServerRateLimiter.this.onBlockedTick();
            }
        }, 100L, 100L, TimeUnit.MILLISECONDS);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.tickFuture.cancel(false);
        this.blockedTickFuture.cancel(true);
        this.rateLimitMap.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRakTick() {
        this.rateLimitMap.clear();
        this.globalCounter.set(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBlockedTick() {
        long currTime = System.currentTimeMillis();
        Iterator<Map.Entry<InetAddress, Long>> iterator = this.blockedConnections.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<InetAddress, Long> entry = iterator.next();
            if (entry.getValue().longValue() != 0 && currTime > entry.getValue().longValue()) {
                iterator.remove();
                log.info("Unblocked address {}", entry.getKey());
            }
        }
    }

    public boolean blockAddress(InetAddress address, long time, TimeUnit unit) {
        if (this.exceptions.contains(address)) {
            return false;
        }
        long millis = unit.toMillis(time);
        this.blockedConnections.put(address, Long.valueOf(System.currentTimeMillis() + millis));
        return true;
    }

    public void unblockAddress(InetAddress address) {
        if (this.blockedConnections.remove(address) != null) {
            log.info("Unblocked address {}", address);
        }
    }

    public boolean isAddressBlocked(InetAddress address) {
        return this.blockedConnections.containsKey(address);
    }

    public void addException(InetAddress address) {
        this.exceptions.add(address);
    }

    public void removeException(InetAddress address) {
        this.exceptions.remove(address);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagram) throws Exception {
        if (this.globalCounter.incrementAndGet() > this.channel.config().getGlobalPacketLimit()) {
            if (log.isTraceEnabled()) {
                log.trace("[{}] Dropped incoming packet because global packet limit was reached: {}", datagram.sender(), Long.valueOf(this.globalCounter.get()));
                return;
            }
            return;
        }
        InetAddress address = datagram.sender().getAddress();
        if (this.blockedConnections.containsKey(address)) {
            return;
        }
        AtomicInteger counter = this.rateLimitMap.computeIfAbsent(address, new Function() { // from class: org.cloudburstmc.netty.handler.codec.raknet.server.RakServerRateLimiter$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RakServerRateLimiter.lambda$channelRead0$0((InetAddress) obj);
            }
        });
        if (counter.incrementAndGet() > this.channel.config().getPacketLimit() && blockAddress(address, 10L, TimeUnit.SECONDS)) {
            log.warn("[{}] Blocked because packet limit was reached");
        } else {
            ctx.fireChannelRead((Object) datagram.retain());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ AtomicInteger lambda$channelRead0$0(InetAddress a) {
        return new AtomicInteger();
    }
}
