package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.List;

/* loaded from: classes4.dex */
public class SpdyFrameCodec extends ByteToMessageDecoder implements SpdyFrameDecoderDelegate, ChannelOutboundHandler {
    private static final SpdyProtocolException INVALID_FRAME = new SpdyProtocolException("Received invalid frame");
    private ChannelHandlerContext ctx;
    private boolean read;
    private final SpdyFrameDecoder spdyFrameDecoder;
    private final SpdyFrameEncoder spdyFrameEncoder;
    private final SpdyHeaderBlockDecoder spdyHeaderBlockDecoder;
    private final SpdyHeaderBlockEncoder spdyHeaderBlockEncoder;
    private SpdyHeadersFrame spdyHeadersFrame;
    private SpdySettingsFrame spdySettingsFrame;
    private final boolean validateHeaders;

    public SpdyFrameCodec(SpdyVersion version) {
        this(version, true);
    }

    public SpdyFrameCodec(SpdyVersion version, boolean validateHeaders) {
        this(version, 8192, 16384, 6, 15, 8, validateHeaders);
    }

    public SpdyFrameCodec(SpdyVersion version, int maxChunkSize, int maxHeaderSize, int compressionLevel, int windowBits, int memLevel) {
        this(version, maxChunkSize, maxHeaderSize, compressionLevel, windowBits, memLevel, true);
    }

    public SpdyFrameCodec(SpdyVersion version, int maxChunkSize, int maxHeaderSize, int compressionLevel, int windowBits, int memLevel, boolean validateHeaders) {
        this(version, maxChunkSize, SpdyHeaderBlockDecoder.newInstance(version, maxHeaderSize), SpdyHeaderBlockEncoder.newInstance(version, compressionLevel, windowBits, memLevel), validateHeaders);
    }

    protected SpdyFrameCodec(SpdyVersion version, int maxChunkSize, SpdyHeaderBlockDecoder spdyHeaderBlockDecoder, SpdyHeaderBlockEncoder spdyHeaderBlockEncoder, boolean validateHeaders) {
        this.spdyFrameDecoder = new SpdyFrameDecoder(version, this, maxChunkSize);
        this.spdyFrameEncoder = new SpdyFrameEncoder(version);
        this.spdyHeaderBlockDecoder = spdyHeaderBlockDecoder;
        this.spdyHeaderBlockEncoder = spdyHeaderBlockEncoder;
        this.validateHeaders = validateHeaders;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.ctx = ctx;
        ctx.channel().closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.spdy.SpdyFrameCodec.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                SpdyFrameCodec.this.spdyHeaderBlockDecoder.end();
                SpdyFrameCodec.this.spdyHeaderBlockEncoder.end();
            }
        });
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        this.spdyFrameDecoder.decode(in);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (!this.read && !ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
        this.read = false;
        super.channelReadComplete(ctx);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf headerBlock;
        if (msg instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
            ByteBuf frame = this.spdyFrameEncoder.encodeDataFrame(ctx.alloc(), spdyDataFrame.streamId(), spdyDataFrame.isLast(), spdyDataFrame.content());
            spdyDataFrame.release();
            ctx.write(frame, promise);
            return;
        }
        if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynStreamFrame);
            try {
                ByteBuf frame2 = this.spdyFrameEncoder.encodeSynStreamFrame(ctx.alloc(), spdySynStreamFrame.streamId(), spdySynStreamFrame.associatedStreamId(), spdySynStreamFrame.priority(), spdySynStreamFrame.isLast(), spdySynStreamFrame.isUnidirectional(), headerBlock);
                headerBlock.release();
                ctx.write(frame2, promise);
                return;
            } finally {
            }
        }
        if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
            headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynReplyFrame);
            try {
                ByteBuf frame3 = this.spdyFrameEncoder.encodeSynReplyFrame(ctx.alloc(), spdySynReplyFrame.streamId(), spdySynReplyFrame.isLast(), headerBlock);
                headerBlock.release();
                ctx.write(frame3, promise);
                return;
            } finally {
            }
        }
        if (msg instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
            ByteBuf frame4 = this.spdyFrameEncoder.encodeRstStreamFrame(ctx.alloc(), spdyRstStreamFrame.streamId(), spdyRstStreamFrame.status().code());
            ctx.write(frame4, promise);
            return;
        }
        if (msg instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
            ByteBuf frame5 = this.spdyFrameEncoder.encodeSettingsFrame(ctx.alloc(), spdySettingsFrame);
            ctx.write(frame5, promise);
            return;
        }
        if (msg instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
            ByteBuf frame6 = this.spdyFrameEncoder.encodePingFrame(ctx.alloc(), spdyPingFrame.id());
            ctx.write(frame6, promise);
            return;
        }
        if (msg instanceof SpdyGoAwayFrame) {
            SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame) msg;
            ByteBuf frame7 = this.spdyFrameEncoder.encodeGoAwayFrame(ctx.alloc(), spdyGoAwayFrame.lastGoodStreamId(), spdyGoAwayFrame.status().code());
            ctx.write(frame7, promise);
        } else {
            if (msg instanceof SpdyHeadersFrame) {
                SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
                headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdyHeadersFrame);
                try {
                    ByteBuf frame8 = this.spdyFrameEncoder.encodeHeadersFrame(ctx.alloc(), spdyHeadersFrame.streamId(), spdyHeadersFrame.isLast(), headerBlock);
                    headerBlock.release();
                    ctx.write(frame8, promise);
                    return;
                } finally {
                }
            }
            if (msg instanceof SpdyWindowUpdateFrame) {
                SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame) msg;
                ByteBuf frame9 = this.spdyFrameEncoder.encodeWindowUpdateFrame(ctx.alloc(), spdyWindowUpdateFrame.streamId(), spdyWindowUpdateFrame.deltaWindowSize());
                ctx.write(frame9, promise);
                return;
            }
            throw new UnsupportedMessageTypeException(msg, (Class<?>[]) new Class[0]);
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readDataFrame(int streamId, boolean last, ByteBuf data) {
        this.read = true;
        SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamId, data);
        spdyDataFrame.setLast(last);
        this.ctx.fireChannelRead((Object) spdyDataFrame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readSynStreamFrame(int streamId, int associatedToStreamId, byte priority, boolean last, boolean unidirectional) {
        SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority, this.validateHeaders);
        spdySynStreamFrame.setLast(last);
        spdySynStreamFrame.setUnidirectional(unidirectional);
        this.spdyHeadersFrame = spdySynStreamFrame;
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readSynReplyFrame(int streamId, boolean last) {
        SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId, this.validateHeaders);
        spdySynReplyFrame.setLast(last);
        this.spdyHeadersFrame = spdySynReplyFrame;
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readRstStreamFrame(int streamId, int statusCode) {
        this.read = true;
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, statusCode);
        this.ctx.fireChannelRead((Object) spdyRstStreamFrame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readSettingsFrame(boolean clearPersisted) {
        this.read = true;
        this.spdySettingsFrame = new DefaultSpdySettingsFrame();
        this.spdySettingsFrame.setClearPreviouslyPersistedSettings(clearPersisted);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readSetting(int id, int value, boolean persistValue, boolean persisted) {
        this.spdySettingsFrame.setValue(id, value, persistValue, persisted);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readSettingsEnd() {
        this.read = true;
        Object frame = this.spdySettingsFrame;
        this.spdySettingsFrame = null;
        this.ctx.fireChannelRead(frame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readPingFrame(int id) {
        this.read = true;
        SpdyPingFrame spdyPingFrame = new DefaultSpdyPingFrame(id);
        this.ctx.fireChannelRead((Object) spdyPingFrame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readGoAwayFrame(int lastGoodStreamId, int statusCode) {
        this.read = true;
        SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(lastGoodStreamId, statusCode);
        this.ctx.fireChannelRead((Object) spdyGoAwayFrame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readHeadersFrame(int streamId, boolean last) {
        this.spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, this.validateHeaders);
        this.spdyHeadersFrame.setLast(last);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readWindowUpdateFrame(int streamId, int deltaWindowSize) {
        this.read = true;
        SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(streamId, deltaWindowSize);
        this.ctx.fireChannelRead((Object) spdyWindowUpdateFrame);
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readHeaderBlock(ByteBuf headerBlock) {
        try {
            try {
                this.spdyHeaderBlockDecoder.decode(this.ctx.alloc(), headerBlock, this.spdyHeadersFrame);
            } catch (Exception e) {
                this.ctx.fireExceptionCaught((Throwable) e);
            }
        } finally {
            headerBlock.release();
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readHeaderBlockEnd() {
        Object frame = null;
        try {
            this.spdyHeaderBlockDecoder.endHeaderBlock(this.spdyHeadersFrame);
            frame = this.spdyHeadersFrame;
            this.spdyHeadersFrame = null;
        } catch (Exception e) {
            this.ctx.fireExceptionCaught((Throwable) e);
        }
        if (frame != null) {
            this.read = true;
            this.ctx.fireChannelRead(frame);
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyFrameDecoderDelegate
    public void readFrameError(String message) {
        this.ctx.fireExceptionCaught((Throwable) INVALID_FRAME);
    }
}
