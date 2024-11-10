package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class SpdyHttpEncoder extends MessageToMessageEncoder<HttpObject> {
    private int currentStreamId;
    private final boolean headersToLowerCase;
    private final boolean validateHeaders;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List list) throws Exception {
        encode2(channelHandlerContext, httpObject, (List<Object>) list);
    }

    public SpdyHttpEncoder(SpdyVersion version) {
        this(version, true, true);
    }

    public SpdyHttpEncoder(SpdyVersion version, boolean headersToLowerCase, boolean validateHeaders) {
        ObjectUtil.checkNotNull(version, "version");
        this.headersToLowerCase = headersToLowerCase;
        this.validateHeaders = validateHeaders;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        boolean valid = false;
        boolean last = false;
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            SpdySynStreamFrame spdySynStreamFrame = createSynStreamFrame(httpRequest);
            out.add(spdySynStreamFrame);
            last = spdySynStreamFrame.isLast() || spdySynStreamFrame.isUnidirectional();
            valid = true;
        }
        if (msg instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) msg;
            SpdyHeadersFrame spdyHeadersFrame = createHeadersFrame(httpResponse);
            out.add(spdyHeadersFrame);
            last = spdyHeadersFrame.isLast();
            valid = true;
        }
        if ((msg instanceof HttpContent) && !last) {
            HttpContent chunk = (HttpContent) msg;
            chunk.content().retain();
            SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
            if (chunk instanceof LastHttpContent) {
                LastHttpContent trailer = (LastHttpContent) chunk;
                HttpHeaders trailers = trailer.trailingHeaders();
                if (trailers.isEmpty()) {
                    spdyDataFrame.setLast(true);
                    out.add(spdyDataFrame);
                } else {
                    SpdyHeadersFrame spdyHeadersFrame2 = new DefaultSpdyHeadersFrame(this.currentStreamId, this.validateHeaders);
                    spdyHeadersFrame2.setLast(true);
                    Iterator<Map.Entry<CharSequence, CharSequence>> itr = trailers.iteratorCharSequence();
                    while (itr.hasNext()) {
                        Map.Entry<CharSequence, CharSequence> entry = itr.next();
                        CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
                        spdyHeadersFrame2.headers().add((SpdyHeaders) headerName, entry.getValue());
                    }
                    out.add(spdyDataFrame);
                    out.add(spdyHeadersFrame2);
                }
            } else {
                out.add(spdyDataFrame);
            }
            valid = true;
        }
        if (!valid) {
            throw new UnsupportedMessageTypeException(msg, (Class<?>[]) new Class[0]);
        }
    }

    private SpdySynStreamFrame createSynStreamFrame(HttpRequest httpRequest) throws Exception {
        HttpHeaders httpHeaders = httpRequest.headers();
        int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID).intValue();
        int associatedToStreamId = httpHeaders.getInt(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID, 0);
        byte priority = (byte) httpHeaders.getInt(SpdyHttpHeaders.Names.PRIORITY, 0);
        String str = httpHeaders.get(SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove(SpdyHttpHeaders.Names.ASSOCIATED_TO_STREAM_ID);
        httpHeaders.remove(SpdyHttpHeaders.Names.PRIORITY);
        httpHeaders.remove(SpdyHttpHeaders.Names.SCHEME);
        httpHeaders.remove(HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
        SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority, this.validateHeaders);
        SpdyHeaders frameHeaders = spdySynStreamFrame.headers();
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.METHOD, (AsciiString) httpRequest.method().name());
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.PATH, (AsciiString) httpRequest.uri());
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.VERSION, (AsciiString) httpRequest.protocolVersion().text());
        String str2 = httpHeaders.get(HttpHeaderNames.HOST);
        httpHeaders.remove(HttpHeaderNames.HOST);
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.HOST, (AsciiString) str2);
        if (str == null) {
            str = "https";
        }
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.SCHEME, (AsciiString) str);
        Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            Map.Entry<CharSequence, CharSequence> entry = itr.next();
            CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
            frameHeaders.add((SpdyHeaders) headerName, entry.getValue());
        }
        this.currentStreamId = spdySynStreamFrame.streamId();
        if (associatedToStreamId == 0) {
            spdySynStreamFrame.setLast(isLast(httpRequest));
        } else {
            spdySynStreamFrame.setUnidirectional(true);
        }
        return spdySynStreamFrame;
    }

    private SpdyHeadersFrame createHeadersFrame(HttpResponse httpResponse) throws Exception {
        SpdyHeadersFrame spdyHeadersFrame;
        HttpHeaders httpHeaders = httpResponse.headers();
        int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID).intValue();
        httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
        httpHeaders.remove(HttpHeaderNames.CONNECTION);
        httpHeaders.remove("Keep-Alive");
        httpHeaders.remove("Proxy-Connection");
        httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
        if (SpdyCodecUtil.isServerId(streamId)) {
            spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, this.validateHeaders);
        } else {
            spdyHeadersFrame = new DefaultSpdySynReplyFrame(streamId, this.validateHeaders);
        }
        SpdyHeaders frameHeaders = spdyHeadersFrame.headers();
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.STATUS, httpResponse.status().codeAsText());
        frameHeaders.set((SpdyHeaders) SpdyHeaders.HttpNames.VERSION, (AsciiString) httpResponse.protocolVersion().text());
        Iterator<Map.Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
        while (itr.hasNext()) {
            Map.Entry<CharSequence, CharSequence> entry = itr.next();
            CharSequence headerName = this.headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
            spdyHeadersFrame.headers().add((SpdyHeaders) headerName, entry.getValue());
        }
        this.currentStreamId = streamId;
        spdyHeadersFrame.setLast(isLast(httpResponse));
        return spdyHeadersFrame;
    }

    private static boolean isLast(HttpMessage httpMessage) {
        if (httpMessage instanceof FullHttpMessage) {
            FullHttpMessage fullMessage = (FullHttpMessage) httpMessage;
            if (fullMessage.trailingHeaders().isEmpty() && !fullMessage.content().isReadable()) {
                return true;
            }
            return false;
        }
        return false;
    }
}
