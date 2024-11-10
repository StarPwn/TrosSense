package io.netty.handler.codec.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public class HttpClientUpgradeHandler extends HttpObjectAggregator implements ChannelOutboundHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final SourceCodec sourceCodec;
    private final UpgradeCodec upgradeCodec;
    private boolean upgradeRequested;

    /* loaded from: classes4.dex */
    public interface SourceCodec {
        void prepareUpgradeFrom(ChannelHandlerContext channelHandlerContext);

        void upgradeFrom(ChannelHandlerContext channelHandlerContext);
    }

    /* loaded from: classes4.dex */
    public interface UpgradeCodec {
        CharSequence protocol();

        Collection<CharSequence> setUpgradeHeaders(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest);

        void upgradeTo(ChannelHandlerContext channelHandlerContext, FullHttpResponse fullHttpResponse) throws Exception;
    }

    /* loaded from: classes4.dex */
    public enum UpgradeEvent {
        UPGRADE_ISSUED,
        UPGRADE_SUCCESSFUL,
        UPGRADE_REJECTED
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator, io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, Object obj, List list) throws Exception {
        decode(channelHandlerContext, (HttpObject) obj, (List<Object>) list);
    }

    public HttpClientUpgradeHandler(SourceCodec sourceCodec, UpgradeCodec upgradeCodec, int maxContentLength) {
        super(maxContentLength);
        this.sourceCodec = (SourceCodec) ObjectUtil.checkNotNull(sourceCodec, "sourceCodec");
        this.upgradeCodec = (UpgradeCodec) ObjectUtil.checkNotNull(upgradeCodec, "upgradeCodec");
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
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            ctx.write(msg, promise);
            return;
        }
        if (this.upgradeRequested) {
            promise.setFailure((Throwable) new IllegalStateException("Attempting to write HTTP request with upgrade in progress"));
            return;
        }
        this.upgradeRequested = true;
        setUpgradeRequestHeaders(ctx, (HttpRequest) msg);
        ctx.write(msg, promise);
        ctx.fireUserEventTriggered((Object) UpgradeEvent.UPGRADE_ISSUED);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        FullHttpResponse response;
        try {
            if (!this.upgradeRequested) {
                throw new IllegalStateException("Read HTTP response without requesting protocol switch");
            }
            if (msg instanceof HttpResponse) {
                HttpResponse rep = (HttpResponse) msg;
                if (!HttpResponseStatus.SWITCHING_PROTOCOLS.equals(rep.status())) {
                    ctx.fireUserEventTriggered((Object) UpgradeEvent.UPGRADE_REJECTED);
                    removeThisHandler(ctx);
                    ctx.fireChannelRead((Object) msg);
                    return;
                }
            }
            if (msg instanceof FullHttpResponse) {
                response = (FullHttpResponse) msg;
                response.retain();
                out.add(response);
            } else {
                super.decode(ctx, (ChannelHandlerContext) msg, out);
                if (out.isEmpty()) {
                    return;
                }
                if (out.size() != 1) {
                    throw new AssertionError();
                }
                response = (FullHttpResponse) out.get(0);
            }
            CharSequence upgradeHeader = response.headers().get(HttpHeaderNames.UPGRADE);
            if (upgradeHeader != null && !AsciiString.contentEqualsIgnoreCase(this.upgradeCodec.protocol(), upgradeHeader)) {
                throw new IllegalStateException("Switching Protocols response with unexpected UPGRADE protocol: " + ((Object) upgradeHeader));
            }
            this.sourceCodec.prepareUpgradeFrom(ctx);
            this.upgradeCodec.upgradeTo(ctx, response);
            ctx.fireUserEventTriggered((Object) UpgradeEvent.UPGRADE_SUCCESSFUL);
            this.sourceCodec.upgradeFrom(ctx);
            response.release();
            out.clear();
            removeThisHandler(ctx);
        } catch (Throwable t) {
            ReferenceCountUtil.release(null);
            ctx.fireExceptionCaught(t);
            removeThisHandler(ctx);
        }
    }

    private static void removeThisHandler(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(ctx.name());
    }

    private void setUpgradeRequestHeaders(ChannelHandlerContext ctx, HttpRequest request) {
        request.headers().set(HttpHeaderNames.UPGRADE, this.upgradeCodec.protocol());
        Set<CharSequence> connectionParts = new LinkedHashSet<>(2);
        connectionParts.addAll(this.upgradeCodec.setUpgradeHeaders(ctx, request));
        StringBuilder builder = new StringBuilder();
        for (CharSequence part : connectionParts) {
            builder.append(part);
            builder.append(StringUtil.COMMA);
        }
        builder.append((CharSequence) HttpHeaderValues.UPGRADE);
        request.headers().add(HttpHeaderNames.CONNECTION, builder.toString());
    }
}
