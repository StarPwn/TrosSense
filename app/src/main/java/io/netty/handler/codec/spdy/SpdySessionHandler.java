package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.spdy.SpdySession;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes4.dex */
public class SpdySessionHandler extends ChannelDuplexHandler {
    private static final int DEFAULT_MAX_CONCURRENT_STREAMS = Integer.MAX_VALUE;
    private static final int DEFAULT_WINDOW_SIZE = 65536;
    private static final SpdyProtocolException PROTOCOL_EXCEPTION = SpdyProtocolException.newStatic(null, SpdySessionHandler.class, "handleOutboundMessage(...)");
    private static final SpdyProtocolException STREAM_CLOSED = SpdyProtocolException.newStatic("Stream closed", SpdySessionHandler.class, "removeStream(...)");
    private ChannelFutureListener closeSessionFutureListener;
    private int lastGoodStreamId;
    private final int minorVersion;
    private boolean receivedGoAwayFrame;
    private boolean sentGoAwayFrame;
    private final boolean server;
    private int initialSendWindowSize = 65536;
    private int initialReceiveWindowSize = 65536;
    private volatile int initialSessionReceiveWindowSize = 65536;
    private final SpdySession spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
    private int remoteConcurrentStreams = Integer.MAX_VALUE;
    private int localConcurrentStreams = Integer.MAX_VALUE;
    private final AtomicInteger pings = new AtomicInteger();

    public SpdySessionHandler(SpdyVersion version, boolean server) {
        this.minorVersion = ((SpdyVersion) ObjectUtil.checkNotNull(version, "version")).getMinorVersion();
        this.server = server;
    }

    public void setSessionReceiveWindowSize(int sessionReceiveWindowSize) {
        ObjectUtil.checkPositiveOrZero(sessionReceiveWindowSize, "sessionReceiveWindowSize");
        this.initialSessionReceiveWindowSize = sessionReceiveWindowSize;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
            int streamId = spdyDataFrame.streamId();
            int deltaWindowSize = spdyDataFrame.content().readableBytes() * (-1);
            int newSessionWindowSize = this.spdySession.updateReceiveWindowSize(0, deltaWindowSize);
            if (newSessionWindowSize < 0) {
                issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            if (newSessionWindowSize <= this.initialSessionReceiveWindowSize / 2) {
                int sessionDeltaWindowSize = this.initialSessionReceiveWindowSize - newSessionWindowSize;
                this.spdySession.updateReceiveWindowSize(0, sessionDeltaWindowSize);
                ctx.writeAndFlush(new DefaultSpdyWindowUpdateFrame(0, sessionDeltaWindowSize));
            }
            if (!this.spdySession.isActiveStream(streamId)) {
                spdyDataFrame.release();
                if (streamId <= this.lastGoodStreamId) {
                    issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    return;
                } else {
                    if (!this.sentGoAwayFrame) {
                        issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
                        return;
                    }
                    return;
                }
            }
            if (this.spdySession.isRemoteSideClosed(streamId)) {
                spdyDataFrame.release();
                issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
                return;
            }
            if (!isRemoteInitiatedId(streamId) && !this.spdySession.hasReceivedReply(streamId)) {
                spdyDataFrame.release();
                issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            int newWindowSize = this.spdySession.updateReceiveWindowSize(streamId, deltaWindowSize);
            if (newWindowSize < this.spdySession.getReceiveWindowSizeLowerBound(streamId)) {
                spdyDataFrame.release();
                issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                return;
            }
            if (newWindowSize < 0) {
                while (spdyDataFrame.content().readableBytes() > this.initialReceiveWindowSize) {
                    SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readRetainedSlice(this.initialReceiveWindowSize));
                    ctx.writeAndFlush(partialDataFrame);
                }
            }
            if (newWindowSize <= this.initialReceiveWindowSize / 2 && !spdyDataFrame.isLast()) {
                int streamDeltaWindowSize = this.initialReceiveWindowSize - newWindowSize;
                this.spdySession.updateReceiveWindowSize(streamId, streamDeltaWindowSize);
                ctx.writeAndFlush(new DefaultSpdyWindowUpdateFrame(streamId, streamDeltaWindowSize));
            }
            if (spdyDataFrame.isLast()) {
                halfCloseStream(streamId, true, ctx.newSucceededFuture());
            }
        } else if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            int streamId2 = spdySynStreamFrame.streamId();
            if (spdySynStreamFrame.isInvalid() || !isRemoteInitiatedId(streamId2) || this.spdySession.isActiveStream(streamId2)) {
                issueStreamError(ctx, streamId2, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            if (streamId2 <= this.lastGoodStreamId) {
                issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            byte priority = spdySynStreamFrame.priority();
            boolean remoteSideClosed = spdySynStreamFrame.isLast();
            boolean localSideClosed = spdySynStreamFrame.isUnidirectional();
            if (!acceptStream(streamId2, priority, remoteSideClosed, localSideClosed)) {
                issueStreamError(ctx, streamId2, SpdyStreamStatus.REFUSED_STREAM);
                return;
            }
        } else if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
            int streamId3 = spdySynReplyFrame.streamId();
            if (spdySynReplyFrame.isInvalid() || isRemoteInitiatedId(streamId3) || this.spdySession.isRemoteSideClosed(streamId3)) {
                issueStreamError(ctx, streamId3, SpdyStreamStatus.INVALID_STREAM);
                return;
            } else if (this.spdySession.hasReceivedReply(streamId3)) {
                issueStreamError(ctx, streamId3, SpdyStreamStatus.STREAM_IN_USE);
                return;
            } else {
                this.spdySession.receivedReply(streamId3);
                if (spdySynReplyFrame.isLast()) {
                    halfCloseStream(streamId3, true, ctx.newSucceededFuture());
                }
            }
        } else if (msg instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
            removeStream(spdyRstStreamFrame.streamId(), ctx.newSucceededFuture());
        } else if (msg instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
            int settingsMinorVersion = spdySettingsFrame.getValue(0);
            if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
                issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            int newConcurrentStreams = spdySettingsFrame.getValue(4);
            if (newConcurrentStreams >= 0) {
                this.remoteConcurrentStreams = newConcurrentStreams;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            int newInitialWindowSize = spdySettingsFrame.getValue(7);
            if (newInitialWindowSize >= 0) {
                updateInitialSendWindowSize(newInitialWindowSize);
            }
        } else if (msg instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
            if (isRemoteInitiatedId(spdyPingFrame.id())) {
                ctx.writeAndFlush(spdyPingFrame);
                return;
            } else if (this.pings.get() == 0) {
                return;
            } else {
                this.pings.getAndDecrement();
            }
        } else if (msg instanceof SpdyGoAwayFrame) {
            this.receivedGoAwayFrame = true;
        } else if (msg instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
            int streamId4 = spdyHeadersFrame.streamId();
            if (spdyHeadersFrame.isInvalid()) {
                issueStreamError(ctx, streamId4, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            } else if (this.spdySession.isRemoteSideClosed(streamId4)) {
                issueStreamError(ctx, streamId4, SpdyStreamStatus.INVALID_STREAM);
                return;
            } else if (spdyHeadersFrame.isLast()) {
                halfCloseStream(streamId4, true, ctx.newSucceededFuture());
            }
        } else if (msg instanceof SpdyWindowUpdateFrame) {
            SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame) msg;
            int streamId5 = spdyWindowUpdateFrame.streamId();
            int deltaWindowSize2 = spdyWindowUpdateFrame.deltaWindowSize();
            if (streamId5 != 0 && this.spdySession.isLocalSideClosed(streamId5)) {
                return;
            }
            if (this.spdySession.getSendWindowSize(streamId5) > Integer.MAX_VALUE - deltaWindowSize2) {
                if (streamId5 == 0) {
                    issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                    return;
                } else {
                    issueStreamError(ctx, streamId5, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                    return;
                }
            }
            updateSendWindowSize(ctx, streamId5, deltaWindowSize2);
        }
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        for (Integer streamId : this.spdySession.activeStreams().keySet()) {
            removeStream(streamId.intValue(), ctx.newSucceededFuture());
        }
        ctx.fireChannelInactive();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SpdyProtocolException) {
            issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        sendGoAwayFrame(ctx, promise);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ((msg instanceof SpdyDataFrame) || (msg instanceof SpdySynStreamFrame) || (msg instanceof SpdySynReplyFrame) || (msg instanceof SpdyRstStreamFrame) || (msg instanceof SpdySettingsFrame) || (msg instanceof SpdyPingFrame) || (msg instanceof SpdyGoAwayFrame) || (msg instanceof SpdyHeadersFrame) || (msg instanceof SpdyWindowUpdateFrame)) {
            handleOutboundMessage(ctx, msg, promise);
        } else {
            ctx.write(msg, promise);
        }
    }

    private void handleOutboundMessage(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
            int streamId = spdyDataFrame.streamId();
            if (this.spdySession.isLocalSideClosed(streamId)) {
                spdyDataFrame.release();
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
            int dataLength = spdyDataFrame.content().readableBytes();
            int sendWindowSize = this.spdySession.getSendWindowSize(streamId);
            int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
            int sendWindowSize2 = Math.min(sendWindowSize, sessionSendWindowSize);
            if (sendWindowSize2 <= 0) {
                this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
                return;
            }
            if (sendWindowSize2 < dataLength) {
                this.spdySession.updateSendWindowSize(streamId, sendWindowSize2 * (-1));
                this.spdySession.updateSendWindowSize(0, sendWindowSize2 * (-1));
                SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readRetainedSlice(sendWindowSize2));
                this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
                ctx.write(partialDataFrame).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.spdy.SpdySessionHandler.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
                return;
            }
            this.spdySession.updateSendWindowSize(streamId, dataLength * (-1));
            this.spdySession.updateSendWindowSize(0, dataLength * (-1));
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.spdy.SpdySessionHandler.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                    }
                }
            });
            if (spdyDataFrame.isLast()) {
                halfCloseStream(streamId, false, promise);
            }
        } else if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            int streamId2 = spdySynStreamFrame.streamId();
            if (isRemoteInitiatedId(streamId2)) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
            byte priority = spdySynStreamFrame.priority();
            boolean remoteSideClosed = spdySynStreamFrame.isUnidirectional();
            boolean localSideClosed = spdySynStreamFrame.isLast();
            if (!acceptStream(streamId2, priority, remoteSideClosed, localSideClosed)) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
        } else if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
            int streamId3 = spdySynReplyFrame.streamId();
            if (!isRemoteInitiatedId(streamId3) || this.spdySession.isLocalSideClosed(streamId3)) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            } else if (spdySynReplyFrame.isLast()) {
                halfCloseStream(streamId3, false, promise);
            }
        } else if (msg instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
            removeStream(spdyRstStreamFrame.streamId(), promise);
        } else if (msg instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
            int settingsMinorVersion = spdySettingsFrame.getValue(0);
            if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
            int newConcurrentStreams = spdySettingsFrame.getValue(4);
            if (newConcurrentStreams >= 0) {
                this.localConcurrentStreams = newConcurrentStreams;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            int newInitialWindowSize = spdySettingsFrame.getValue(7);
            if (newInitialWindowSize >= 0) {
                updateInitialReceiveWindowSize(newInitialWindowSize);
            }
        } else if (msg instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
            if (isRemoteInitiatedId(spdyPingFrame.id())) {
                ctx.fireExceptionCaught((Throwable) new IllegalArgumentException("invalid PING ID: " + spdyPingFrame.id()));
                return;
            }
            this.pings.getAndIncrement();
        } else {
            if (msg instanceof SpdyGoAwayFrame) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
            if (msg instanceof SpdyHeadersFrame) {
                SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
                int streamId4 = spdyHeadersFrame.streamId();
                if (this.spdySession.isLocalSideClosed(streamId4)) {
                    promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                    return;
                } else if (spdyHeadersFrame.isLast()) {
                    halfCloseStream(streamId4, false, promise);
                }
            } else if (msg instanceof SpdyWindowUpdateFrame) {
                promise.setFailure((Throwable) PROTOCOL_EXCEPTION);
                return;
            }
        }
        ctx.write(msg, promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void issueSessionError(ChannelHandlerContext ctx, SpdySessionStatus status) {
        sendGoAwayFrame(ctx, status).addListener((GenericFutureListener<? extends Future<? super Void>>) new ClosingChannelFutureListener(ctx, ctx.newPromise()));
    }

    private void issueStreamError(ChannelHandlerContext ctx, int streamId, SpdyStreamStatus status) {
        boolean fireChannelRead = !this.spdySession.isRemoteSideClosed(streamId);
        ChannelPromise promise = ctx.newPromise();
        removeStream(streamId, promise);
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, status);
        ctx.writeAndFlush(spdyRstStreamFrame, promise);
        if (fireChannelRead) {
            ctx.fireChannelRead((Object) spdyRstStreamFrame);
        }
    }

    private boolean isRemoteInitiatedId(int id) {
        boolean serverId = SpdyCodecUtil.isServerId(id);
        return (this.server && !serverId) || (!this.server && serverId);
    }

    private void updateInitialSendWindowSize(int newInitialWindowSize) {
        int deltaWindowSize = newInitialWindowSize - this.initialSendWindowSize;
        this.initialSendWindowSize = newInitialWindowSize;
        this.spdySession.updateAllSendWindowSizes(deltaWindowSize);
    }

    private void updateInitialReceiveWindowSize(int newInitialWindowSize) {
        int deltaWindowSize = newInitialWindowSize - this.initialReceiveWindowSize;
        this.initialReceiveWindowSize = newInitialWindowSize;
        this.spdySession.updateAllReceiveWindowSizes(deltaWindowSize);
    }

    private boolean acceptStream(int streamId, byte priority, boolean remoteSideClosed, boolean localSideClosed) {
        if (this.receivedGoAwayFrame || this.sentGoAwayFrame) {
            return false;
        }
        boolean remote = isRemoteInitiatedId(streamId);
        int maxConcurrentStreams = remote ? this.localConcurrentStreams : this.remoteConcurrentStreams;
        if (this.spdySession.numActiveStreams(remote) >= maxConcurrentStreams) {
            return false;
        }
        this.spdySession.acceptStream(streamId, priority, remoteSideClosed, localSideClosed, this.initialSendWindowSize, this.initialReceiveWindowSize, remote);
        if (remote) {
            this.lastGoodStreamId = streamId;
            return true;
        }
        return true;
    }

    private void halfCloseStream(int streamId, boolean remote, ChannelFuture future) {
        if (remote) {
            this.spdySession.closeRemoteSide(streamId, isRemoteInitiatedId(streamId));
        } else {
            this.spdySession.closeLocalSide(streamId, isRemoteInitiatedId(streamId));
        }
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) this.closeSessionFutureListener);
        }
    }

    private void removeStream(int streamId, ChannelFuture future) {
        this.spdySession.removeStream(streamId, STREAM_CLOSED, isRemoteInitiatedId(streamId));
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) this.closeSessionFutureListener);
        }
    }

    private void updateSendWindowSize(final ChannelHandlerContext ctx, int streamId, int deltaWindowSize) {
        this.spdySession.updateSendWindowSize(streamId, deltaWindowSize);
        while (true) {
            SpdySession.PendingWrite pendingWrite = this.spdySession.getPendingWrite(streamId);
            if (pendingWrite == null) {
                return;
            }
            SpdyDataFrame spdyDataFrame = pendingWrite.spdyDataFrame;
            int dataFrameSize = spdyDataFrame.content().readableBytes();
            int writeStreamId = spdyDataFrame.streamId();
            int sendWindowSize = this.spdySession.getSendWindowSize(writeStreamId);
            int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
            int sendWindowSize2 = Math.min(sendWindowSize, sessionSendWindowSize);
            if (sendWindowSize2 <= 0) {
                return;
            }
            if (sendWindowSize2 < dataFrameSize) {
                this.spdySession.updateSendWindowSize(writeStreamId, sendWindowSize2 * (-1));
                this.spdySession.updateSendWindowSize(0, sendWindowSize2 * (-1));
                SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(writeStreamId, spdyDataFrame.content().readRetainedSlice(sendWindowSize2));
                ctx.writeAndFlush(partialDataFrame).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.spdy.SpdySessionHandler.3
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
            } else {
                this.spdySession.removePendingWrite(writeStreamId);
                this.spdySession.updateSendWindowSize(writeStreamId, dataFrameSize * (-1));
                this.spdySession.updateSendWindowSize(0, dataFrameSize * (-1));
                if (spdyDataFrame.isLast()) {
                    halfCloseStream(writeStreamId, false, pendingWrite.promise);
                }
                ctx.writeAndFlush(spdyDataFrame, pendingWrite.promise).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.spdy.SpdySessionHandler.4
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
            }
        }
    }

    private void sendGoAwayFrame(ChannelHandlerContext ctx, ChannelPromise future) {
        if (!ctx.channel().isActive()) {
            ctx.close(future);
            return;
        }
        ChannelFuture f = sendGoAwayFrame(ctx, SpdySessionStatus.OK);
        if (this.spdySession.noActiveStreams()) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ClosingChannelFutureListener(ctx, future));
        } else {
            this.closeSessionFutureListener = new ClosingChannelFutureListener(ctx, future);
        }
    }

    private ChannelFuture sendGoAwayFrame(ChannelHandlerContext ctx, SpdySessionStatus status) {
        if (!this.sentGoAwayFrame) {
            this.sentGoAwayFrame = true;
            SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, status);
            return ctx.writeAndFlush(spdyGoAwayFrame);
        }
        return ctx.newSucceededFuture();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ClosingChannelFutureListener implements ChannelFutureListener {
        private final ChannelHandlerContext ctx;
        private final ChannelPromise promise;

        ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.ctx = ctx;
            this.promise = promise;
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture sentGoAwayFuture) throws Exception {
            this.ctx.close(this.promise);
        }
    }
}
