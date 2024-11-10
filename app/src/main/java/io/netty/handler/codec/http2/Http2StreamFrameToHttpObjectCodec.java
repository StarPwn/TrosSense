package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class Http2StreamFrameToHttpObjectCodec extends MessageToMessageCodec<Http2StreamFrame, HttpObject> {
    private static final AttributeKey<HttpScheme> SCHEME_ATTR_KEY = AttributeKey.valueOf(HttpScheme.class, "STREAMFRAMECODEC_SCHEME");
    private final boolean isServer;
    private final boolean validateHeaders;

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, Http2StreamFrame http2StreamFrame, List list) throws Exception {
        decode2(channelHandlerContext, http2StreamFrame, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List list) throws Exception {
        encode2(channelHandlerContext, httpObject, (List<Object>) list);
    }

    public Http2StreamFrameToHttpObjectCodec(boolean isServer, boolean validateHeaders) {
        this.isServer = isServer;
        this.validateHeaders = validateHeaders;
    }

    public Http2StreamFrameToHttpObjectCodec(boolean isServer) {
        this(isServer, true);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return (msg instanceof Http2HeadersFrame) || (msg instanceof Http2DataFrame);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, Http2StreamFrame frame, List<Object> out) throws Exception {
        if (frame instanceof Http2HeadersFrame) {
            Http2HeadersFrame headersFrame = (Http2HeadersFrame) frame;
            Http2Headers headers = headersFrame.headers();
            Http2FrameStream stream = headersFrame.stream();
            int id = stream == null ? 0 : stream.id();
            CharSequence status = headers.status();
            if (status != null && isInformationalResponseHeaderFrame(status)) {
                FullHttpMessage fullMsg = newFullMessage(id, headers, ctx.alloc());
                out.add(fullMsg);
                return;
            }
            if (headersFrame.isEndStream()) {
                if (headers.method() == null && status == null) {
                    LastHttpContent last = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
                    HttpConversionUtil.addHttp2ToHttpHeaders(id, headers, last.trailingHeaders(), HttpVersion.HTTP_1_1, true, true);
                    out.add(last);
                    return;
                } else {
                    FullHttpMessage full = newFullMessage(id, headers, ctx.alloc());
                    out.add(full);
                    return;
                }
            }
            HttpMessage req = newMessage(id, headers);
            if ((status == null || !isContentAlwaysEmpty(status)) && !HttpUtil.isContentLengthSet(req)) {
                req.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            }
            out.add(req);
            return;
        }
        if (frame instanceof Http2DataFrame) {
            Http2DataFrame dataFrame = (Http2DataFrame) frame;
            if (dataFrame.isEndStream()) {
                out.add(new DefaultLastHttpContent(dataFrame.content().retain(), this.validateHeaders));
            } else {
                out.add(new DefaultHttpContent(dataFrame.content().retain()));
            }
        }
    }

    private void encodeLastContent(LastHttpContent last, List<Object> out) {
        boolean needFiller = !(last instanceof FullHttpMessage) && last.trailingHeaders().isEmpty();
        if (last.content().isReadable() || needFiller) {
            out.add(new DefaultHttp2DataFrame(last.content().retain(), last.trailingHeaders().isEmpty()));
        }
        if (!last.trailingHeaders().isEmpty()) {
            Http2Headers headers = HttpConversionUtil.toHttp2Headers(last.trailingHeaders(), this.validateHeaders);
            out.add(new DefaultHttp2HeadersFrame(headers, true));
        }
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, HttpObject obj, List<Object> out) throws Exception {
        if (obj instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) obj;
            HttpResponseStatus status = res.status();
            int code = status.code();
            HttpStatusClass statusClass = status.codeClass();
            if (statusClass == HttpStatusClass.INFORMATIONAL && code != 101) {
                if (res instanceof FullHttpResponse) {
                    Http2Headers headers = toHttp2Headers(ctx, res);
                    out.add(new DefaultHttp2HeadersFrame(headers, false));
                    return;
                }
                throw new EncoderException(status + " must be a FullHttpResponse");
            }
        }
        if (obj instanceof HttpMessage) {
            Http2Headers headers2 = toHttp2Headers(ctx, (HttpMessage) obj);
            boolean noMoreFrames = false;
            if (obj instanceof FullHttpMessage) {
                FullHttpMessage full = (FullHttpMessage) obj;
                noMoreFrames = !full.content().isReadable() && full.trailingHeaders().isEmpty();
            }
            out.add(new DefaultHttp2HeadersFrame(headers2, noMoreFrames));
        }
        if (obj instanceof LastHttpContent) {
            LastHttpContent last = (LastHttpContent) obj;
            encodeLastContent(last, out);
        } else if (obj instanceof HttpContent) {
            HttpContent cont = (HttpContent) obj;
            out.add(new DefaultHttp2DataFrame(cont.content().retain(), false));
        }
    }

    private Http2Headers toHttp2Headers(ChannelHandlerContext ctx, HttpMessage msg) {
        if (msg instanceof HttpRequest) {
            msg.headers().set(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), connectionScheme(ctx));
        }
        return HttpConversionUtil.toHttp2Headers(msg, this.validateHeaders);
    }

    private HttpMessage newMessage(int id, Http2Headers headers) throws Http2Exception {
        if (this.isServer) {
            return HttpConversionUtil.toHttpRequest(id, headers, this.validateHeaders);
        }
        return HttpConversionUtil.toHttpResponse(id, headers, this.validateHeaders);
    }

    private FullHttpMessage newFullMessage(int id, Http2Headers headers, ByteBufAllocator alloc) throws Http2Exception {
        if (this.isServer) {
            return HttpConversionUtil.toFullHttpRequest(id, headers, alloc, this.validateHeaders);
        }
        return HttpConversionUtil.toFullHttpResponse(id, headers, alloc, this.validateHeaders);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        Attribute<HttpScheme> schemeAttribute = connectionSchemeAttribute(ctx);
        if (schemeAttribute.get() == null) {
            HttpScheme scheme = isSsl(ctx) ? HttpScheme.HTTPS : HttpScheme.HTTP;
            schemeAttribute.set(scheme);
        }
    }

    protected boolean isSsl(ChannelHandlerContext ctx) {
        Channel connChannel = connectionChannel(ctx);
        return connChannel.pipeline().get(SslHandler.class) != null;
    }

    private static HttpScheme connectionScheme(ChannelHandlerContext ctx) {
        HttpScheme scheme = connectionSchemeAttribute(ctx).get();
        return scheme == null ? HttpScheme.HTTP : scheme;
    }

    private static Attribute<HttpScheme> connectionSchemeAttribute(ChannelHandlerContext ctx) {
        Channel ch = connectionChannel(ctx);
        return ch.attr(SCHEME_ATTR_KEY);
    }

    private static Channel connectionChannel(ChannelHandlerContext ctx) {
        Channel ch = ctx.channel();
        return ch instanceof Http2StreamChannel ? ch.parent() : ch;
    }

    private static boolean isInformationalResponseHeaderFrame(CharSequence status) {
        if (status.length() != 3) {
            return false;
        }
        char char0 = status.charAt(0);
        char char1 = status.charAt(1);
        char char2 = status.charAt(2);
        return char0 == '1' && char1 >= '0' && char1 <= '9' && char2 >= '0' && char2 <= '9' && char2 != '1';
    }

    private static boolean isContentAlwaysEmpty(CharSequence status) {
        if (status.length() != 3) {
            return false;
        }
        char char0 = status.charAt(0);
        char char1 = status.charAt(1);
        char char2 = status.charAt(2);
        return (char0 == '2' || char0 == '3') && char1 == '0' && char2 == '4';
    }
}
