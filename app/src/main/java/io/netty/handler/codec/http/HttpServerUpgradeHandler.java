package io.netty.handler.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes4.dex */
public class HttpServerUpgradeHandler extends HttpObjectAggregator {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean handlingUpgrade;
    private final HttpHeadersFactory headersFactory;
    private final SourceCodec sourceCodec;
    private final HttpHeadersFactory trailersFactory;
    private final UpgradeCodecFactory upgradeCodecFactory;

    /* loaded from: classes4.dex */
    public interface SourceCodec {
        void upgradeFrom(ChannelHandlerContext channelHandlerContext);
    }

    /* loaded from: classes4.dex */
    public interface UpgradeCodec {
        boolean prepareUpgradeResponse(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders);

        Collection<CharSequence> requiredUpgradeHeaders();

        void upgradeTo(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest);
    }

    /* loaded from: classes4.dex */
    public interface UpgradeCodecFactory {
        UpgradeCodec newUpgradeCodec(CharSequence charSequence);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator, io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, Object obj, List list) throws Exception {
        decode(channelHandlerContext, (HttpObject) obj, (List<Object>) list);
    }

    /* loaded from: classes4.dex */
    public static final class UpgradeEvent implements ReferenceCounted {
        private final CharSequence protocol;
        private final FullHttpRequest upgradeRequest;

        UpgradeEvent(CharSequence protocol, FullHttpRequest upgradeRequest) {
            this.protocol = protocol;
            this.upgradeRequest = upgradeRequest;
        }

        public CharSequence protocol() {
            return this.protocol;
        }

        public FullHttpRequest upgradeRequest() {
            return this.upgradeRequest;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.upgradeRequest.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent retain() {
            this.upgradeRequest.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent retain(int increment) {
            this.upgradeRequest.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent touch() {
            this.upgradeRequest.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent touch(Object hint) {
            this.upgradeRequest.touch(hint);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.upgradeRequest.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.upgradeRequest.release(decrement);
        }

        public String toString() {
            return "UpgradeEvent [protocol=" + ((Object) this.protocol) + ", upgradeRequest=" + this.upgradeRequest + ']';
        }
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory) {
        this(sourceCodec, upgradeCodecFactory, 0, DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory, int maxContentLength) {
        this(sourceCodec, upgradeCodecFactory, maxContentLength, DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory, int maxContentLength, boolean validateHeaders) {
        this(sourceCodec, upgradeCodecFactory, maxContentLength, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory, int maxContentLength, HttpHeadersFactory headersFactory, HttpHeadersFactory trailersFactory) {
        super(maxContentLength);
        this.sourceCodec = (SourceCodec) ObjectUtil.checkNotNull(sourceCodec, "sourceCodec");
        this.upgradeCodecFactory = (UpgradeCodecFactory) ObjectUtil.checkNotNull(upgradeCodecFactory, "upgradeCodecFactory");
        this.headersFactory = (HttpHeadersFactory) ObjectUtil.checkNotNull(headersFactory, "headersFactory");
        this.trailersFactory = (HttpHeadersFactory) ObjectUtil.checkNotNull(trailersFactory, "trailersFactory");
    }

    protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        FullHttpRequest fullRequest;
        if (!this.handlingUpgrade) {
            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;
                if (req.headers().contains(HttpHeaderNames.UPGRADE) && shouldHandleUpgradeRequest(req)) {
                    this.handlingUpgrade = true;
                } else {
                    ReferenceCountUtil.retain(msg);
                    ctx.fireChannelRead((Object) msg);
                    return;
                }
            } else {
                ReferenceCountUtil.retain(msg);
                ctx.fireChannelRead((Object) msg);
                return;
            }
        }
        if (msg instanceof FullHttpRequest) {
            fullRequest = (FullHttpRequest) msg;
            ReferenceCountUtil.retain(msg);
            out.add(msg);
        } else {
            super.decode(ctx, (ChannelHandlerContext) msg, out);
            if (out.isEmpty()) {
                return;
            }
            if (out.size() != 1) {
                throw new AssertionError();
            }
            this.handlingUpgrade = false;
            fullRequest = (FullHttpRequest) out.get(0);
        }
        if (upgrade(ctx, fullRequest)) {
            out.clear();
        }
    }

    protected boolean shouldHandleUpgradeRequest(HttpRequest req) {
        return true;
    }

    private boolean upgrade(ChannelHandlerContext ctx, FullHttpRequest request) {
        List<String> connectionHeaderValues;
        List<CharSequence> requestedProtocols = splitHeader(request.headers().get(HttpHeaderNames.UPGRADE));
        int numRequestedProtocols = requestedProtocols.size();
        UpgradeCodec upgradeCodec = null;
        CharSequence upgradeProtocol = null;
        int i = 0;
        while (true) {
            if (i >= numRequestedProtocols) {
                break;
            }
            CharSequence p = requestedProtocols.get(i);
            UpgradeCodec c = this.upgradeCodecFactory.newUpgradeCodec(p);
            if (c == null) {
                i++;
            } else {
                upgradeProtocol = p;
                upgradeCodec = c;
                break;
            }
        }
        if (upgradeCodec == null || (connectionHeaderValues = request.headers().getAll(HttpHeaderNames.CONNECTION)) == null || connectionHeaderValues.isEmpty()) {
            return false;
        }
        StringBuilder concatenatedConnectionValue = new StringBuilder(connectionHeaderValues.size() * 10);
        for (CharSequence connectionHeaderValue : connectionHeaderValues) {
            concatenatedConnectionValue.append(connectionHeaderValue).append(StringUtil.COMMA);
        }
        concatenatedConnectionValue.setLength(concatenatedConnectionValue.length() - 1);
        Collection<CharSequence> requiredHeaders = upgradeCodec.requiredUpgradeHeaders();
        List<CharSequence> values = splitHeader(concatenatedConnectionValue);
        if (!AsciiString.containsContentEqualsIgnoreCase(values, HttpHeaderNames.UPGRADE) || !AsciiString.containsAllContentEqualsIgnoreCase(values, requiredHeaders)) {
            return false;
        }
        for (CharSequence requiredHeader : requiredHeaders) {
            if (!request.headers().contains(requiredHeader)) {
                return false;
            }
        }
        FullHttpResponse upgradeResponse = createUpgradeResponse(upgradeProtocol);
        if (!upgradeCodec.prepareUpgradeResponse(ctx, request, upgradeResponse.headers())) {
            return false;
        }
        UpgradeEvent event = new UpgradeEvent(upgradeProtocol, request);
        try {
            ChannelFuture writeComplete = ctx.writeAndFlush(upgradeResponse);
            this.sourceCodec.upgradeFrom(ctx);
            upgradeCodec.upgradeTo(ctx, request);
            ctx.pipeline().remove(this);
            ctx.fireUserEventTriggered((Object) event.retain());
            writeComplete.addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
            return true;
        } finally {
            event.release();
        }
    }

    private FullHttpResponse createUpgradeResponse(CharSequence upgradeProtocol) {
        DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS, Unpooled.EMPTY_BUFFER, this.headersFactory, this.trailersFactory);
        res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
        res.headers().add(HttpHeaderNames.UPGRADE, upgradeProtocol);
        return res;
    }

    private static List<CharSequence> splitHeader(CharSequence header) {
        StringBuilder builder = new StringBuilder(header.length());
        List<CharSequence> protocols = new ArrayList<>(4);
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAt(i);
            if (!Character.isWhitespace(c)) {
                if (c == ',') {
                    protocols.add(builder.toString());
                    builder.setLength(0);
                } else {
                    builder.append(c);
                }
            }
        }
        int i2 = builder.length();
        if (i2 > 0) {
            protocols.add(builder.toString());
        }
        return protocols;
    }
}
