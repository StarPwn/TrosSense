package io.netty.resolver.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.handler.codec.dns.TcpDnsQueryEncoder;
import io.netty.handler.codec.dns.TcpDnsResponseDecoder;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class DnsQueryContext {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final TcpDnsQueryEncoder TCP_ENCODER;
    private final DnsRecord[] additionals;
    private final Channel channel;
    private final Future<? extends Channel> channelReadyFuture;
    private int id = Integer.MIN_VALUE;
    private final InetSocketAddress nameServerAddr;
    private final DnsRecord optResource;
    private final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise;
    private final DnsQueryContextManager queryContextManager;
    private final long queryTimeoutMillis;
    private final DnsQuestion question;
    private final boolean recursionDesired;
    private final boolean retryWithTcpOnTimeout;
    private final Bootstrap socketBootstrap;
    private volatile Future<?> timeoutFuture;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DnsQueryContext.class);
    private static final long ID_REUSE_ON_TIMEOUT_DELAY_MILLIS = SystemPropertyUtil.getLong("io.netty.resolver.dns.idReuseOnTimeoutDelayMillis", 10000);

    protected abstract DnsQuery newQuery(int i, InetSocketAddress inetSocketAddress);

    protected abstract String protocol();

    static {
        logger.debug("-Dio.netty.resolver.dns.idReuseOnTimeoutDelayMillis: {}", Long.valueOf(ID_REUSE_ON_TIMEOUT_DELAY_MILLIS));
        TCP_ENCODER = new TcpDnsQueryEncoder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsQueryContext(Channel channel, Future<? extends Channel> channelReadyFuture, InetSocketAddress nameServerAddr, DnsQueryContextManager queryContextManager, int maxPayLoadSize, boolean recursionDesired, long queryTimeoutMillis, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise, Bootstrap socketBootstrap, boolean retryWithTcpOnTimeout) {
        this.channel = (Channel) ObjectUtil.checkNotNull(channel, "channel");
        this.queryContextManager = (DnsQueryContextManager) ObjectUtil.checkNotNull(queryContextManager, "queryContextManager");
        this.channelReadyFuture = (Future) ObjectUtil.checkNotNull(channelReadyFuture, "channelReadyFuture");
        this.nameServerAddr = (InetSocketAddress) ObjectUtil.checkNotNull(nameServerAddr, "nameServerAddr");
        this.question = (DnsQuestion) ObjectUtil.checkNotNull(question, "question");
        this.additionals = (DnsRecord[]) ObjectUtil.checkNotNull(additionals, "additionals");
        this.promise = (Promise) ObjectUtil.checkNotNull(promise, "promise");
        this.recursionDesired = recursionDesired;
        this.queryTimeoutMillis = queryTimeoutMillis;
        this.socketBootstrap = socketBootstrap;
        this.retryWithTcpOnTimeout = retryWithTcpOnTimeout;
        if (maxPayLoadSize > 0 && !hasOptRecord(additionals)) {
            int i = 0;
            this.optResource = new AbstractDnsOptPseudoRrRecord(maxPayLoadSize, i, i) { // from class: io.netty.resolver.dns.DnsQueryContext.1
            };
        } else {
            this.optResource = null;
        }
    }

    private static boolean hasOptRecord(DnsRecord[] additionals) {
        if (additionals != null && additionals.length > 0) {
            for (DnsRecord additional : additionals) {
                if (additional.type() == DnsRecordType.OPT) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isDone() {
        return this.promise.isDone();
    }

    final DnsQuestion question() {
        return this.question;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ChannelFuture writeQuery(boolean flush) {
        if (this.id != Integer.MIN_VALUE) {
            throw new AssertionError(getClass().getSimpleName() + ".writeQuery(...) can only be executed once.");
        }
        int add = this.queryContextManager.add(this.nameServerAddr, this);
        this.id = add;
        if (add == -1) {
            IllegalStateException e = new IllegalStateException("query ID space exhausted: " + question());
            finishFailure("failed to send a query via " + protocol(), e, false);
            return this.channel.newFailedFuture(e);
        }
        this.promise.addListener((GenericFutureListener<? extends Future<? super AddressedEnvelope<DnsResponse, InetSocketAddress>>>) new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() { // from class: io.netty.resolver.dns.DnsQueryContext.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                Future<?> timeoutFuture = DnsQueryContext.this.timeoutFuture;
                if (timeoutFuture != null) {
                    DnsQueryContext.this.timeoutFuture = null;
                    timeoutFuture.cancel(false);
                }
                Throwable cause = future.cause();
                if (!(cause instanceof DnsNameResolverTimeoutException) && !(cause instanceof CancellationException)) {
                    DnsQueryContext.this.removeFromContextManager(DnsQueryContext.this.nameServerAddr);
                } else {
                    DnsQueryContext.this.channel.eventLoop().schedule(new Runnable() { // from class: io.netty.resolver.dns.DnsQueryContext.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DnsQueryContext.this.removeFromContextManager(DnsQueryContext.this.nameServerAddr);
                        }
                    }, DnsQueryContext.ID_REUSE_ON_TIMEOUT_DELAY_MILLIS, TimeUnit.MILLISECONDS);
                }
            }
        });
        DnsQuestion question = question();
        DnsQuery query = newQuery(this.id, this.nameServerAddr);
        query.setRecursionDesired(this.recursionDesired);
        query.addRecord(DnsSection.QUESTION, (DnsRecord) question);
        for (DnsRecord record : this.additionals) {
            query.addRecord(DnsSection.ADDITIONAL, record);
        }
        if (this.optResource != null) {
            query.addRecord(DnsSection.ADDITIONAL, this.optResource);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("{} WRITE: {}, [{}: {}], {}", this.channel, protocol(), Integer.valueOf(this.id), this.nameServerAddr, question);
        }
        return sendQuery(query, flush);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromContextManager(InetSocketAddress nameServerAddr) {
        DnsQueryContext self = this.queryContextManager.remove(nameServerAddr, this.id);
        if (self != this) {
            throw new AssertionError("Removed DnsQueryContext is not the correct instance");
        }
    }

    private ChannelFuture sendQuery(final DnsQuery query, boolean flush) {
        final ChannelPromise writePromise = this.channel.newPromise();
        if (this.channelReadyFuture.isSuccess()) {
            writeQuery(query, flush, writePromise);
        } else {
            Throwable cause = this.channelReadyFuture.cause();
            if (cause != null) {
                failQuery(query, cause, writePromise);
            } else {
                this.channelReadyFuture.addListener(new GenericFutureListener<Future<? super Channel>>() { // from class: io.netty.resolver.dns.DnsQueryContext.3
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<? super Channel> future) {
                        if (future.isSuccess()) {
                            DnsQueryContext.this.writeQuery(query, true, writePromise);
                        } else {
                            Throwable cause2 = future.cause();
                            DnsQueryContext.this.failQuery(query, cause2, writePromise);
                        }
                    }
                });
            }
        }
        return writePromise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failQuery(DnsQuery query, Throwable cause, ChannelPromise writePromise) {
        try {
            this.promise.tryFailure(cause);
            writePromise.tryFailure(cause);
        } finally {
            ReferenceCountUtil.release(query);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeQuery(DnsQuery query, boolean flush, ChannelPromise promise) {
        Channel channel = this.channel;
        final ChannelFuture writeFuture = flush ? channel.writeAndFlush(query, promise) : channel.write(query, promise);
        if (writeFuture.isDone()) {
            onQueryWriteCompletion(this.queryTimeoutMillis, writeFuture);
        } else {
            writeFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsQueryContext.4
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) {
                    DnsQueryContext.this.onQueryWriteCompletion(DnsQueryContext.this.queryTimeoutMillis, writeFuture);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQueryWriteCompletion(final long queryTimeoutMillis, ChannelFuture writeFuture) {
        if (!writeFuture.isSuccess()) {
            finishFailure("failed to send a query '" + this.id + "' via " + protocol(), writeFuture.cause(), false);
        } else if (queryTimeoutMillis > 0) {
            this.timeoutFuture = this.channel.eventLoop().schedule(new Runnable() { // from class: io.netty.resolver.dns.DnsQueryContext.5
                @Override // java.lang.Runnable
                public void run() {
                    if (!DnsQueryContext.this.promise.isDone()) {
                        DnsQueryContext.this.finishFailure("query '" + DnsQueryContext.this.id + "' via " + DnsQueryContext.this.protocol() + " timed out after " + queryTimeoutMillis + " milliseconds", null, true);
                    }
                }
            }, queryTimeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishSuccess(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope, boolean truncated) {
        if (!truncated || !retryWithTcp(envelope)) {
            DnsResponse res = envelope.content();
            if (res.count(DnsSection.QUESTION) != 1) {
                logger.warn("{} Received a DNS response with invalid number of questions. Expected: 1, found: {}", this.channel, envelope);
            } else if (!question().equals(res.recordAt(DnsSection.QUESTION))) {
                logger.warn("{} Received a mismatching DNS response. Expected: [{}], found: {}", this.channel, question(), envelope);
            } else if (trySuccess(envelope)) {
                return;
            }
            envelope.release();
        }
    }

    private boolean trySuccess(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
        return this.promise.trySuccess(envelope);
    }

    final boolean finishFailure(String message, Throwable cause, boolean timeout) {
        DnsNameResolverException e;
        if (this.promise.isDone()) {
            return false;
        }
        DnsQuestion question = question();
        StringBuilder buf = new StringBuilder(message.length() + 128);
        buf.append('[').append(this.id).append(": ").append(this.nameServerAddr).append("] ").append(question).append(' ').append(message).append(" (no stack trace available)");
        if (timeout) {
            e = new DnsNameResolverTimeoutException(this.nameServerAddr, question, buf.toString());
            if (this.retryWithTcpOnTimeout && retryWithTcp(e)) {
                return false;
            }
        } else {
            e = new DnsNameResolverException(this.nameServerAddr, question, buf.toString(), cause);
        }
        return this.promise.tryFailure(e);
    }

    private boolean retryWithTcp(final Object originalResult) {
        if (this.socketBootstrap == null) {
            return false;
        }
        this.socketBootstrap.connect(this.nameServerAddr).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsQueryContext.6
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    DnsQueryContext.logger.debug("{} Unable to fallback to TCP [{}: {}]", future.channel(), Integer.valueOf(DnsQueryContext.this.id), DnsQueryContext.this.nameServerAddr, future.cause());
                    DnsQueryContext.this.finishOriginal(originalResult, future);
                    return;
                }
                final Channel tcpCh = future.channel();
                Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise = tcpCh.eventLoop().newPromise();
                final TcpDnsQueryContext tcpCtx = new TcpDnsQueryContext(tcpCh, DnsQueryContext.this.channelReadyFuture, (InetSocketAddress) tcpCh.remoteAddress(), DnsQueryContext.this.queryContextManager, 0, DnsQueryContext.this.recursionDesired, DnsQueryContext.this.queryTimeoutMillis, DnsQueryContext.this.question(), DnsQueryContext.this.additionals, promise);
                tcpCh.pipeline().addLast(DnsQueryContext.TCP_ENCODER);
                tcpCh.pipeline().addLast(new TcpDnsResponseDecoder());
                tcpCh.pipeline().addLast(new ChannelInboundHandlerAdapter() { // from class: io.netty.resolver.dns.DnsQueryContext.6.1
                    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        Channel tcpCh2 = ctx.channel();
                        DnsResponse response = (DnsResponse) msg;
                        int queryId = response.id();
                        if (DnsQueryContext.logger.isDebugEnabled()) {
                            DnsQueryContext.logger.debug("{} RECEIVED: TCP [{}: {}], {}", tcpCh2, Integer.valueOf(queryId), tcpCh2.remoteAddress(), response);
                        }
                        DnsQueryContext foundCtx = DnsQueryContext.this.queryContextManager.get(DnsQueryContext.this.nameServerAddr, queryId);
                        if (foundCtx != null && foundCtx.isDone()) {
                            DnsQueryContext.logger.debug("{} Received a DNS response for a query that was timed out or cancelled : TCP [{}: {}]", tcpCh2, Integer.valueOf(queryId), DnsQueryContext.this.nameServerAddr);
                            response.release();
                        } else {
                            if (foundCtx == tcpCtx) {
                                tcpCtx.finishSuccess(new AddressedEnvelopeAdapter((InetSocketAddress) ctx.channel().remoteAddress(), (InetSocketAddress) ctx.channel().localAddress(), response), false);
                                return;
                            }
                            response.release();
                            tcpCtx.finishFailure("Received TCP DNS response with unexpected ID", null, false);
                            if (DnsQueryContext.logger.isDebugEnabled()) {
                                DnsQueryContext.logger.debug("{} Received a DNS response with an unexpected ID: TCP [{}: {}]", tcpCh2, Integer.valueOf(queryId), tcpCh2.remoteAddress());
                            }
                        }
                    }

                    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        if (tcpCtx.finishFailure("TCP fallback error", cause, false) && DnsQueryContext.logger.isDebugEnabled()) {
                            DnsQueryContext.logger.debug("{} Error during processing response: TCP [{}: {}]", ctx.channel(), Integer.valueOf(DnsQueryContext.this.id), ctx.channel().remoteAddress(), cause);
                        }
                    }
                });
                promise.addListener(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() { // from class: io.netty.resolver.dns.DnsQueryContext.6.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future2) {
                        if (!future2.isSuccess()) {
                            DnsQueryContext.this.finishOriginal(originalResult, future2);
                        } else {
                            DnsQueryContext.this.finishSuccess(future2.getNow(), false);
                            ReferenceCountUtil.release(originalResult);
                        }
                        tcpCh.close();
                    }
                });
                tcpCtx.writeQuery(true);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOriginal(Object originalResult, Future<?> future) {
        if (originalResult instanceof Throwable) {
            Throwable error = (Throwable) originalResult;
            ThrowableUtil.addSuppressed(error, future.cause());
            this.promise.tryFailure(error);
            return;
        }
        finishSuccess((AddressedEnvelope) originalResult, false);
    }

    /* loaded from: classes4.dex */
    private static final class AddressedEnvelopeAdapter implements AddressedEnvelope<DnsResponse, InetSocketAddress> {
        private final InetSocketAddress recipient;
        private final DnsResponse response;
        private final InetSocketAddress sender;

        AddressedEnvelopeAdapter(InetSocketAddress sender, InetSocketAddress recipient, DnsResponse response) {
            this.sender = sender;
            this.recipient = recipient;
            this.response = response;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.channel.AddressedEnvelope
        public DnsResponse content() {
            return this.response;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.channel.AddressedEnvelope
        public InetSocketAddress sender() {
            return this.sender;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.channel.AddressedEnvelope
        public InetSocketAddress recipient() {
            return this.recipient;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> retain() {
            this.response.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> retain(int increment) {
            this.response.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> touch() {
            this.response.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public AddressedEnvelope<DnsResponse, InetSocketAddress> touch(Object hint) {
            this.response.touch(hint);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.response.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.response.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.response.release(decrement);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AddressedEnvelope)) {
                return false;
            }
            AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope) obj;
            if (sender() == null) {
                if (that.sender() != null) {
                    return false;
                }
            } else if (!sender().equals(that.sender())) {
                return false;
            }
            if (recipient() == null) {
                if (that.recipient() != null) {
                    return false;
                }
            } else if (!recipient().equals(that.recipient())) {
                return false;
            }
            return this.response.equals(obj);
        }

        public int hashCode() {
            int hashCode = this.response.hashCode();
            if (sender() != null) {
                hashCode = (hashCode * 31) + sender().hashCode();
            }
            if (recipient() != null) {
                return (hashCode * 31) + recipient().hashCode();
            }
            return hashCode;
        }
    }
}
