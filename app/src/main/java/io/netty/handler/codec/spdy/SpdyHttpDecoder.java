package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeadersFactory;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeadersFactory;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class SpdyHttpDecoder extends MessageToMessageDecoder<SpdyFrame> {
    private final HttpHeadersFactory headersFactory;
    private final int maxContentLength;
    private final Map<Integer, FullHttpMessage> messageMap;
    private final int spdyVersion;
    private final HttpHeadersFactory trailersFactory;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, SpdyFrame spdyFrame, List list) throws Exception {
        decode2(channelHandlerContext, spdyFrame, (List<Object>) list);
    }

    public SpdyHttpDecoder(SpdyVersion version, int maxContentLength) {
        this(version, maxContentLength, new HashMap(), DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    @Deprecated
    public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, boolean validateHeaders) {
        this(version, maxContentLength, new HashMap(), validateHeaders);
    }

    protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap) {
        this(version, maxContentLength, messageMap, DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    @Deprecated
    protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, boolean validateHeaders) {
        this(version, maxContentLength, messageMap, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, HttpHeadersFactory headersFactory, HttpHeadersFactory trailersFactory) {
        this.spdyVersion = ((SpdyVersion) ObjectUtil.checkNotNull(version, "version")).getVersion();
        this.maxContentLength = ObjectUtil.checkPositive(maxContentLength, "maxContentLength");
        this.messageMap = messageMap;
        this.headersFactory = headersFactory;
        this.trailersFactory = trailersFactory;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        for (Map.Entry<Integer, FullHttpMessage> entry : this.messageMap.entrySet()) {
            ReferenceCountUtil.safeRelease(entry.getValue());
        }
        this.messageMap.clear();
        super.channelInactive(ctx);
    }

    protected FullHttpMessage putMessage(int streamId, FullHttpMessage message) {
        return this.messageMap.put(Integer.valueOf(streamId), message);
    }

    protected FullHttpMessage getMessage(int streamId) {
        return this.messageMap.get(Integer.valueOf(streamId));
    }

    protected FullHttpMessage removeMessage(int streamId) {
        return this.messageMap.remove(Integer.valueOf(streamId));
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out) throws Exception {
        if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            int streamId = spdySynStreamFrame.streamId();
            if (SpdyCodecUtil.isServerId(streamId)) {
                int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
                if (associatedToStreamId == 0) {
                    SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                if (spdySynStreamFrame.isLast()) {
                    SpdyRstStreamFrame spdyRstStreamFrame2 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame2);
                    return;
                }
                if (spdySynStreamFrame.isTruncated()) {
                    SpdyRstStreamFrame spdyRstStreamFrame3 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame3);
                    return;
                }
                try {
                    FullHttpRequest httpRequestWithEntity = createHttpRequest(spdySynStreamFrame, ctx.alloc());
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, associatedToStreamId);
                    httpRequestWithEntity.headers().setInt(SpdyHttpHeaders.Names.PRIORITY, spdySynStreamFrame.priority());
                    out.add(httpRequestWithEntity);
                    return;
                } catch (Throwable th) {
                    SpdyRstStreamFrame spdyRstStreamFrame4 = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame4);
                    return;
                }
            }
            if (spdySynStreamFrame.isTruncated()) {
                SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                spdySynReplyFrame.setLast(true);
                SpdyHeaders frameHeaders = spdySynReplyFrame.headers();
                frameHeaders.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.code());
                frameHeaders.setObject((SpdyHeaders) SpdyHeaders.HttpNames.VERSION, (Object) HttpVersion.HTTP_1_0);
                ctx.writeAndFlush(spdySynReplyFrame);
                return;
            }
            try {
                FullHttpRequest httpRequestWithEntity2 = createHttpRequest(spdySynStreamFrame, ctx.alloc());
                httpRequestWithEntity2.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId);
                if (spdySynStreamFrame.isLast()) {
                    out.add(httpRequestWithEntity2);
                } else {
                    putMessage(streamId, httpRequestWithEntity2);
                }
                return;
            } catch (Throwable th2) {
                SpdySynReplyFrame spdySynReplyFrame2 = new DefaultSpdySynReplyFrame(streamId);
                spdySynReplyFrame2.setLast(true);
                SpdyHeaders frameHeaders2 = spdySynReplyFrame2.headers();
                frameHeaders2.setInt(SpdyHeaders.HttpNames.STATUS, HttpResponseStatus.BAD_REQUEST.code());
                frameHeaders2.setObject((SpdyHeaders) SpdyHeaders.HttpNames.VERSION, (Object) HttpVersion.HTTP_1_0);
                ctx.writeAndFlush(spdySynReplyFrame2);
                return;
            }
        }
        if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame3 = (SpdySynReplyFrame) msg;
            int streamId2 = spdySynReplyFrame3.streamId();
            if (spdySynReplyFrame3.isTruncated()) {
                SpdyRstStreamFrame spdyRstStreamFrame5 = new DefaultSpdyRstStreamFrame(streamId2, SpdyStreamStatus.INTERNAL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame5);
                return;
            }
            try {
                FullHttpResponse httpResponseWithEntity = createHttpResponse(spdySynReplyFrame3, ctx.alloc());
                httpResponseWithEntity.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId2);
                if (spdySynReplyFrame3.isLast()) {
                    HttpUtil.setContentLength(httpResponseWithEntity, 0L);
                    out.add(httpResponseWithEntity);
                } else {
                    putMessage(streamId2, httpResponseWithEntity);
                }
                return;
            } catch (Throwable th3) {
                SpdyRstStreamFrame spdyRstStreamFrame6 = new DefaultSpdyRstStreamFrame(streamId2, SpdyStreamStatus.PROTOCOL_ERROR);
                ctx.writeAndFlush(spdyRstStreamFrame6);
                return;
            }
        }
        if (!(msg instanceof SpdyHeadersFrame)) {
            if (msg instanceof SpdyDataFrame) {
                SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
                int streamId3 = spdyDataFrame.streamId();
                FullHttpMessage fullHttpMessage = getMessage(streamId3);
                if (fullHttpMessage == null) {
                    return;
                }
                ByteBuf content = fullHttpMessage.content();
                if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
                    removeMessage(streamId3);
                    throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
                }
                ByteBuf spdyDataFrameData = spdyDataFrame.content();
                int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
                content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
                if (spdyDataFrame.isLast()) {
                    HttpUtil.setContentLength(fullHttpMessage, content.readableBytes());
                    removeMessage(streamId3);
                    out.add(fullHttpMessage);
                    return;
                }
                return;
            }
            if (msg instanceof SpdyRstStreamFrame) {
                SpdyRstStreamFrame spdyRstStreamFrame7 = (SpdyRstStreamFrame) msg;
                removeMessage(spdyRstStreamFrame7.streamId());
                return;
            }
            return;
        }
        SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
        int streamId4 = spdyHeadersFrame.streamId();
        FullHttpMessage fullHttpMessage2 = getMessage(streamId4);
        if (fullHttpMessage2 == null) {
            if (SpdyCodecUtil.isServerId(streamId4)) {
                if (spdyHeadersFrame.isTruncated()) {
                    SpdyRstStreamFrame spdyRstStreamFrame8 = new DefaultSpdyRstStreamFrame(streamId4, SpdyStreamStatus.INTERNAL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame8);
                    return;
                }
                try {
                    FullHttpMessage fullHttpMessage3 = createHttpResponse(spdyHeadersFrame, ctx.alloc());
                    fullHttpMessage3.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, streamId4);
                    if (spdyHeadersFrame.isLast()) {
                        HttpUtil.setContentLength(fullHttpMessage3, 0L);
                        out.add(fullHttpMessage3);
                    } else {
                        putMessage(streamId4, fullHttpMessage3);
                    }
                    return;
                } catch (Throwable th4) {
                    SpdyRstStreamFrame spdyRstStreamFrame9 = new DefaultSpdyRstStreamFrame(streamId4, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame9);
                    return;
                }
            }
            return;
        }
        if (!spdyHeadersFrame.isTruncated()) {
            for (Map.Entry<CharSequence, CharSequence> e : spdyHeadersFrame.headers()) {
                fullHttpMessage2.headers().add(e.getKey(), e.getValue());
            }
        }
        if (spdyHeadersFrame.isLast()) {
            HttpUtil.setContentLength(fullHttpMessage2, fullHttpMessage2.content().readableBytes());
            removeMessage(streamId4);
            out.add(fullHttpMessage2);
        }
    }

    private static FullHttpRequest createHttpRequest(SpdyHeadersFrame requestFrame, ByteBufAllocator alloc) throws Exception {
        SpdyHeaders headers = requestFrame.headers();
        HttpMethod method = HttpMethod.valueOf(headers.getAsString(SpdyHeaders.HttpNames.METHOD));
        String url = headers.getAsString(SpdyHeaders.HttpNames.PATH);
        HttpVersion httpVersion = HttpVersion.valueOf(headers.getAsString(SpdyHeaders.HttpNames.VERSION));
        headers.remove(SpdyHeaders.HttpNames.METHOD);
        headers.remove(SpdyHeaders.HttpNames.PATH);
        headers.remove(SpdyHeaders.HttpNames.VERSION);
        boolean release = true;
        ByteBuf buffer = alloc.buffer();
        try {
            FullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url, buffer);
            headers.remove(SpdyHeaders.HttpNames.SCHEME);
            CharSequence host = headers.get(SpdyHeaders.HttpNames.HOST);
            headers.remove(SpdyHeaders.HttpNames.HOST);
            req.headers().set(HttpHeaderNames.HOST, host);
            for (Map.Entry<CharSequence, CharSequence> e : requestFrame.headers()) {
                req.headers().add(e.getKey(), e.getValue());
            }
            HttpUtil.setKeepAlive(req, true);
            req.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            release = false;
            return req;
        } finally {
            if (release) {
                buffer.release();
            }
        }
    }

    private FullHttpResponse createHttpResponse(SpdyHeadersFrame responseFrame, ByteBufAllocator alloc) throws Exception {
        SpdyHeaders headers = responseFrame.headers();
        HttpResponseStatus status = HttpResponseStatus.parseLine(headers.get(SpdyHeaders.HttpNames.STATUS));
        HttpVersion version = HttpVersion.valueOf(headers.getAsString(SpdyHeaders.HttpNames.VERSION));
        headers.remove(SpdyHeaders.HttpNames.STATUS);
        headers.remove(SpdyHeaders.HttpNames.VERSION);
        boolean release = true;
        ByteBuf buffer = alloc.buffer();
        try {
            FullHttpResponse res = new DefaultFullHttpResponse(version, status, buffer, this.headersFactory, this.trailersFactory);
            for (Map.Entry<CharSequence, CharSequence> e : responseFrame.headers()) {
                res.headers().add(e.getKey(), e.getValue());
            }
            HttpUtil.setKeepAlive(res, true);
            res.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            res.headers().remove(HttpHeaderNames.TRAILER);
            release = false;
            return res;
        } finally {
            if (release) {
                buffer.release();
            }
        }
    }
}
