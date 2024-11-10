package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.Http2FrameWriter;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.collection.CharObjectMap;
import io.netty.util.internal.ObjectUtil;
import java.io.Closeable;

/* loaded from: classes4.dex */
public class DefaultHttp2FrameWriter implements Http2FrameWriter, Http2FrameSizePolicy, Http2FrameWriter.Configuration {
    private static final String STREAM_DEPENDENCY = "Stream Dependency";
    private static final String STREAM_ID = "Stream ID";
    private static final ByteBuf ZERO_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(255).writeZero(255)).asReadOnly();
    private final Http2HeadersEncoder headersEncoder;
    private int maxFrameSize;

    public DefaultHttp2FrameWriter() {
        this(new DefaultHttp2HeadersEncoder());
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector));
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector, boolean ignoreMaxHeaderListSize) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector, ignoreMaxHeaderListSize));
    }

    public DefaultHttp2FrameWriter(Http2HeadersEncoder headersEncoder) {
        this.headersEncoder = headersEncoder;
        this.maxFrameSize = 16384;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public Http2FrameWriter.Configuration configuration() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter.Configuration
    public Http2HeadersEncoder.Configuration headersConfiguration() {
        return this.headersEncoder.configuration();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter.Configuration
    public Http2FrameSizePolicy frameSizePolicy() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public void maxFrameSize(int max) throws Http2Exception {
        if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", Integer.valueOf(max));
        }
        this.maxFrameSize = max;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public int maxFrameSize() {
        return this.maxFrameSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.headersEncoder instanceof Closeable) {
            try {
                ((Closeable) this.headersEncoder).close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.http2.Http2DataWriter
    public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise) {
        ByteBuf lastFrame;
        ByteBuf lastFrame2;
        int padding2;
        boolean z;
        DefaultHttp2FrameWriter defaultHttp2FrameWriter = this;
        ByteBuf data2 = data;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        ByteBuf frameHeader = null;
        try {
            verifyStreamId(streamId, STREAM_ID);
            Http2CodecUtil.verifyPadding(padding);
            int remainingData = data.readableBytes();
            Http2Flags flags = new Http2Flags();
            byte b = 0;
            flags.endOfStream(false);
            flags.paddingPresent(false);
            if (remainingData > defaultHttp2FrameWriter.maxFrameSize) {
                frameHeader = ctx.alloc().buffer(9);
                Http2CodecUtil.writeFrameHeaderInternal(frameHeader, defaultHttp2FrameWriter.maxFrameSize, (byte) 0, flags, streamId);
                do {
                    ctx.write(frameHeader.retainedSlice(), promiseAggregator.newPromise());
                    ctx.write(data2.readRetainedSlice(defaultHttp2FrameWriter.maxFrameSize), promiseAggregator.newPromise());
                    remainingData -= defaultHttp2FrameWriter.maxFrameSize;
                } while (remainingData > defaultHttp2FrameWriter.maxFrameSize);
            }
            try {
                if (padding == 0) {
                    if (frameHeader != null) {
                        frameHeader.release();
                        frameHeader = null;
                    }
                    ByteBuf frameHeader2 = ctx.alloc().buffer(9);
                    flags.endOfStream(endStream);
                    Http2CodecUtil.writeFrameHeaderInternal(frameHeader2, remainingData, (byte) 0, flags, streamId);
                    ctx.write(frameHeader2, promiseAggregator.newPromise());
                    ByteBuf lastFrame3 = data2.readSlice(remainingData);
                    ctx.write(lastFrame3, promiseAggregator.newPromise());
                } else {
                    if (remainingData != defaultHttp2FrameWriter.maxFrameSize) {
                        if (frameHeader == null) {
                            lastFrame = frameHeader;
                            padding2 = padding;
                        } else {
                            frameHeader.release();
                            lastFrame = null;
                            padding2 = padding;
                        }
                    } else {
                        remainingData -= defaultHttp2FrameWriter.maxFrameSize;
                        if (frameHeader == null) {
                            lastFrame2 = ctx.alloc().buffer(9);
                            Http2CodecUtil.writeFrameHeaderInternal(lastFrame2, defaultHttp2FrameWriter.maxFrameSize, (byte) 0, flags, streamId);
                        } else {
                            lastFrame2 = frameHeader.slice();
                            frameHeader = null;
                        }
                        ctx.write(lastFrame2, promiseAggregator.newPromise());
                        ByteBuf lastFrame4 = data.readableBytes() != defaultHttp2FrameWriter.maxFrameSize ? data2.readSlice(defaultHttp2FrameWriter.maxFrameSize) : data2;
                        data2 = null;
                        ctx.write(lastFrame4, promiseAggregator.newPromise());
                        lastFrame = frameHeader;
                        padding2 = padding;
                    }
                    while (true) {
                        try {
                            int frameDataBytes = Math.min(remainingData, defaultHttp2FrameWriter.maxFrameSize);
                            int framePaddingBytes = Math.min(padding2, Math.max((int) b, (defaultHttp2FrameWriter.maxFrameSize - 1) - frameDataBytes));
                            padding2 -= framePaddingBytes;
                            remainingData -= frameDataBytes;
                            ByteBuf frameHeader22 = ctx.alloc().buffer(10);
                            flags.endOfStream((endStream && remainingData == 0 && padding2 == 0) ? true : b);
                            flags.paddingPresent(framePaddingBytes > 0 ? true : b);
                            Http2CodecUtil.writeFrameHeaderInternal(frameHeader22, framePaddingBytes + frameDataBytes, b, flags, streamId);
                            writePaddingLength(frameHeader22, framePaddingBytes);
                            ctx.write(frameHeader22, promiseAggregator.newPromise());
                            if (data2 != null) {
                                if (remainingData == 0) {
                                    ByteBuf lastFrame5 = data2.readSlice(frameDataBytes);
                                    data2 = null;
                                    ctx.write(lastFrame5, promiseAggregator.newPromise());
                                } else {
                                    ctx.write(data2.readRetainedSlice(frameDataBytes), promiseAggregator.newPromise());
                                }
                            }
                            if (paddingBytes(framePaddingBytes) <= 0) {
                                z = false;
                            } else {
                                z = false;
                                ctx.write(ZERO_BUFFER.slice(0, paddingBytes(framePaddingBytes)), promiseAggregator.newPromise());
                            }
                            if (remainingData == 0 && padding2 == 0) {
                                break;
                            }
                            b = z;
                            defaultHttp2FrameWriter = this;
                        } catch (Throwable th) {
                            th = th;
                            Throwable cause = th;
                            if (lastFrame != null) {
                                lastFrame.release();
                            }
                            if (data2 != null) {
                                try {
                                    data2.release();
                                } finally {
                                    promiseAggregator.setFailure(cause);
                                    promiseAggregator.doneAllocatingPromises();
                                }
                            }
                            return promiseAggregator;
                        }
                    }
                }
                return promiseAggregator.doneAllocatingPromises();
            } catch (Throwable th2) {
                th = th2;
                lastFrame = frameHeader;
            }
        } catch (Throwable th3) {
            th = th3;
            lastFrame = null;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short) 0, false, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
        try {
            verifyStreamId(streamId, STREAM_ID);
            verifyStreamOrConnectionId(streamDependency, STREAM_DEPENDENCY);
            verifyWeight(weight);
            ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte) 2, new Http2Flags(), streamId);
            buf.writeInt(exclusive ? (int) (2147483648L | streamDependency) : streamDependency);
            buf.writeByte(weight - 1);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        try {
            verifyStreamId(streamId, STREAM_ID);
            verifyErrorCode(errorCode);
            ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte) 3, new Http2Flags(), streamId);
            buf.writeInt((int) errorCode);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
        try {
            ObjectUtil.checkNotNull(settings, "settings");
            int payloadLength = settings.size() * 6;
            ByteBuf buf = ctx.alloc().buffer(payloadLength + 9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 4, new Http2Flags(), 0);
            for (CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                buf.writeChar(entry.key());
                buf.writeInt(entry.value().intValue());
            }
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
        try {
            ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte) 4, new Http2Flags().ack(true), 0);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
        Http2Flags flags = ack ? new Http2Flags().ack(true) : new Http2Flags();
        ByteBuf buf = ctx.alloc().buffer(17);
        Http2CodecUtil.writeFrameHeaderInternal(buf, 8, (byte) 6, flags, 0);
        buf.writeLong(data);
        return ctx.write(buf, promise);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00cb, code lost:            if (r6 == null) goto L35;     */
    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.channel.ChannelFuture writePushPromise(io.netty.channel.ChannelHandlerContext r17, int r18, int r19, io.netty.handler.codec.http2.Http2Headers r20, int r21, io.netty.channel.ChannelPromise r22) {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            r5 = r21
            r6 = 0
            io.netty.handler.codec.http2.Http2CodecUtil$SimpleChannelPromiseAggregator r0 = new io.netty.handler.codec.http2.Http2CodecUtil$SimpleChannelPromiseAggregator
            io.netty.channel.Channel r7 = r17.channel()
            io.netty.util.concurrent.EventExecutor r8 = r17.executor()
            r9 = r22
            r0.<init>(r9, r7, r8)
            r7 = r0
            java.lang.String r0 = "Stream ID"
            verifyStreamId(r3, r0)     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            java.lang.String r0 = "Promised Stream ID"
            verifyStreamId(r4, r0)     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            io.netty.handler.codec.http2.Http2CodecUtil.verifyPadding(r21)     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            io.netty.buffer.ByteBufAllocator r0 = r17.alloc()     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            io.netty.buffer.ByteBuf r0 = r0.buffer()     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            r6 = r0
            io.netty.handler.codec.http2.Http2HeadersEncoder r0 = r1.headersEncoder     // Catch: java.lang.Throwable -> Lb5 io.netty.handler.codec.http2.Http2Exception -> Lc4
            r8 = r20
            r0.encodeHeaders(r3, r8, r6)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            io.netty.handler.codec.http2.Http2Flags r0 = new io.netty.handler.codec.http2.Http2Flags     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r0.<init>()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r10 = 1
            if (r5 <= 0) goto L42
            r12 = r10
            goto L43
        L42:
            r12 = 0
        L43:
            io.netty.handler.codec.http2.Http2Flags r0 = r0.paddingPresent(r12)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r12 = r5 + 4
            int r13 = r1.maxFrameSize     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r13 = r13 - r12
            int r14 = r6.readableBytes()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r14 = java.lang.Math.min(r14, r13)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            io.netty.buffer.ByteBuf r14 = r6.readRetainedSlice(r14)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            boolean r15 = r6.isReadable()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            if (r15 != 0) goto L5f
            goto L60
        L5f:
            r10 = 0
        L60:
            r0.endOfHeaders(r10)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r10 = r14.readableBytes()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r10 = r10 + r12
            io.netty.buffer.ByteBufAllocator r15 = r17.alloc()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r11 = 14
            io.netty.buffer.ByteBuf r11 = r15.buffer(r11)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r15 = 5
            io.netty.handler.codec.http2.Http2CodecUtil.writeFrameHeaderInternal(r11, r10, r15, r0, r3)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            writePaddingLength(r11, r5)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r11.writeInt(r4)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            io.netty.channel.ChannelPromise r15 = r7.newPromise()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r2.write(r11, r15)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            io.netty.channel.ChannelPromise r15 = r7.newPromise()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r2.write(r14, r15)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r15 = paddingBytes(r21)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            if (r15 <= 0) goto La2
            io.netty.buffer.ByteBuf r15 = io.netty.handler.codec.http2.DefaultHttp2FrameWriter.ZERO_BUFFER     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            int r4 = paddingBytes(r21)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r5 = 0
            io.netty.buffer.ByteBuf r4 = r15.slice(r5, r4)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            io.netty.channel.ChannelPromise r5 = r7.newPromise()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            r2.write(r4, r5)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
        La2:
            boolean r4 = r0.endOfHeaders()     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
            if (r4 != 0) goto Lab
            r1.writeContinuationFrames(r2, r3, r6, r7)     // Catch: java.lang.Throwable -> Lb1 io.netty.handler.codec.http2.Http2Exception -> Lb3
        Lab:
            if (r6 == 0) goto Lce
        Lad:
            r6.release()
            goto Lce
        Lb1:
            r0 = move-exception
            goto Lb8
        Lb3:
            r0 = move-exception
            goto Lc7
        Lb5:
            r0 = move-exception
            r8 = r20
        Lb8:
            r7.setFailure(r0)     // Catch: java.lang.Throwable -> Ld3
            r7.doneAllocatingPromises()     // Catch: java.lang.Throwable -> Ld3
            io.netty.util.internal.PlatformDependent.throwException(r0)     // Catch: java.lang.Throwable -> Ld3
            if (r6 == 0) goto Lce
            goto Lad
        Lc4:
            r0 = move-exception
            r8 = r20
        Lc7:
            r7.setFailure(r0)     // Catch: java.lang.Throwable -> Ld3
            if (r6 == 0) goto Lce
            goto Lad
        Lce:
            io.netty.channel.ChannelPromise r0 = r7.doneAllocatingPromises()
            return r0
        Ld3:
            r0 = move-exception
            if (r6 == 0) goto Ld9
            r6.release()
        Ld9:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.DefaultHttp2FrameWriter.writePushPromise(io.netty.channel.ChannelHandlerContext, int, int, io.netty.handler.codec.http2.Http2Headers, int, io.netty.channel.ChannelPromise):io.netty.channel.ChannelFuture");
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
            verifyErrorCode(errorCode);
            int payloadLength = debugData.readableBytes() + 8;
            ByteBuf buf = ctx.alloc().buffer(17);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte) 7, new Http2Flags(), 0);
            buf.writeInt(lastStreamId);
            buf.writeInt((int) errorCode);
            ctx.write(buf, promiseAggregator.newPromise());
            try {
                ctx.write(debugData, promiseAggregator.newPromise());
            } catch (Throwable t) {
                promiseAggregator.setFailure(t);
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable t2) {
            try {
                debugData.release();
                return promiseAggregator;
            } finally {
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
            }
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
        try {
            verifyStreamOrConnectionId(streamId, STREAM_ID);
            verifyWindowSizeIncrement(windowSizeIncrement);
            ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte) 8, new Http2Flags(), streamId);
            buf.writeInt(windowSizeIncrement);
            return ctx.write(buf, promise);
        } catch (Throwable t) {
            return promise.setFailure(t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(streamId, STREAM_ID);
            ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
            ctx.write(buf, promiseAggregator.newPromise());
            try {
                ctx.write(payload, promiseAggregator.newPromise());
            } catch (Throwable t) {
                promiseAggregator.setFailure(t);
            }
            return promiseAggregator.doneAllocatingPromises();
        } catch (Throwable t2) {
            try {
                payload.release();
                return promiseAggregator;
            } finally {
                promiseAggregator.setFailure(t2);
                promiseAggregator.doneAllocatingPromises();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00f3, code lost:            if (r7 == null) goto L44;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private io.netty.channel.ChannelFuture writeHeadersInternal(io.netty.channel.ChannelHandlerContext r19, int r20, io.netty.handler.codec.http2.Http2Headers r21, int r22, boolean r23, boolean r24, int r25, short r26, boolean r27, io.netty.channel.ChannelPromise r28) {
        /*
            Method dump skipped, instructions count: 258
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.DefaultHttp2FrameWriter.writeHeadersInternal(io.netty.channel.ChannelHandlerContext, int, io.netty.handler.codec.http2.Http2Headers, int, boolean, boolean, int, short, boolean, io.netty.channel.ChannelPromise):io.netty.channel.ChannelFuture");
    }

    private ChannelFuture writeContinuationFrames(ChannelHandlerContext ctx, int streamId, ByteBuf headerBlock, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        Http2Flags flags = new Http2Flags();
        if (headerBlock.isReadable()) {
            int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), this.maxFrameSize);
            ByteBuf buf = ctx.alloc().buffer(10);
            Http2CodecUtil.writeFrameHeaderInternal(buf, fragmentReadableBytes, (byte) 9, flags, streamId);
            do {
                int fragmentReadableBytes2 = Math.min(headerBlock.readableBytes(), this.maxFrameSize);
                ByteBuf fragment = headerBlock.readRetainedSlice(fragmentReadableBytes2);
                if (headerBlock.isReadable()) {
                    ctx.write(buf.retainedSlice(), promiseAggregator.newPromise());
                } else {
                    flags = flags.endOfHeaders(true);
                    buf.release();
                    buf = ctx.alloc().buffer(10);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, fragmentReadableBytes2, (byte) 9, flags, streamId);
                    ctx.write(buf, promiseAggregator.newPromise());
                }
                ctx.write(fragment, promiseAggregator.newPromise());
            } while (headerBlock.isReadable());
        }
        return promiseAggregator;
    }

    private static int paddingBytes(int padding) {
        return padding - 1;
    }

    private static void writePaddingLength(ByteBuf buf, int padding) {
        if (padding > 0) {
            buf.writeByte(padding - 1);
        }
    }

    private static void verifyStreamId(int streamId, String argumentName) {
        ObjectUtil.checkPositive(streamId, argumentName);
    }

    private static void verifyStreamOrConnectionId(int streamId, String argumentName) {
        ObjectUtil.checkPositiveOrZero(streamId, argumentName);
    }

    private static void verifyWeight(short weight) {
        if (weight < 1 || weight > 256) {
            throw new IllegalArgumentException("Invalid weight: " + ((int) weight));
        }
    }

    private static void verifyErrorCode(long errorCode) {
        if (errorCode < 0 || errorCode > 4294967295L) {
            throw new IllegalArgumentException("Invalid errorCode: " + errorCode);
        }
    }

    private static void verifyWindowSizeIncrement(int windowSizeIncrement) {
        ObjectUtil.checkPositiveOrZero(windowSizeIncrement, "windowSizeIncrement");
    }
}
