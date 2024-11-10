package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public final class CleartextHttp2ServerUpgradeHandler extends ByteToMessageDecoder {
    private static final ByteBuf CONNECTION_PREFACE = Unpooled.unreleasableBuffer(Http2CodecUtil.connectionPrefaceBuf()).asReadOnly();
    private final ChannelHandler http2ServerHandler;
    private final HttpServerCodec httpServerCodec;
    private final HttpServerUpgradeHandler httpServerUpgradeHandler;

    public CleartextHttp2ServerUpgradeHandler(HttpServerCodec httpServerCodec, HttpServerUpgradeHandler httpServerUpgradeHandler, ChannelHandler http2ServerHandler) {
        this.httpServerCodec = (HttpServerCodec) ObjectUtil.checkNotNull(httpServerCodec, "httpServerCodec");
        this.httpServerUpgradeHandler = (HttpServerUpgradeHandler) ObjectUtil.checkNotNull(httpServerUpgradeHandler, "httpServerUpgradeHandler");
        this.http2ServerHandler = (ChannelHandler) ObjectUtil.checkNotNull(http2ServerHandler, "http2ServerHandler");
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addAfter(ctx.name(), null, this.httpServerUpgradeHandler).addAfter(ctx.name(), null, this.httpServerCodec);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int prefaceLength = CONNECTION_PREFACE.readableBytes();
        int bytesRead = Math.min(in.readableBytes(), prefaceLength);
        if (!ByteBufUtil.equals(CONNECTION_PREFACE, CONNECTION_PREFACE.readerIndex(), in, in.readerIndex(), bytesRead)) {
            ctx.pipeline().remove(this);
        } else if (bytesRead == prefaceLength) {
            ctx.pipeline().remove(this.httpServerCodec).remove(this.httpServerUpgradeHandler);
            ctx.pipeline().addAfter(ctx.name(), null, this.http2ServerHandler);
            ctx.pipeline().remove(this);
            ctx.fireUserEventTriggered((Object) PriorKnowledgeUpgradeEvent.INSTANCE);
        }
    }

    /* loaded from: classes4.dex */
    public static final class PriorKnowledgeUpgradeEvent {
        private static final PriorKnowledgeUpgradeEvent INSTANCE = new PriorKnowledgeUpgradeEvent();

        private PriorKnowledgeUpgradeEvent() {
        }
    }
}
