package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
abstract class DeflateDecoder extends WebSocketExtensionDecoder {
    private EmbeddedChannel decoder;
    private final WebSocketExtensionFilter extensionDecoderFilter;
    private final boolean noContext;
    static final ByteBuf FRAME_TAIL = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[]{0, 0, -1, -1})).asReadOnly();
    static final ByteBuf EMPTY_DEFLATE_BLOCK = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[]{0})).asReadOnly();

    protected abstract boolean appendFrameTail(WebSocketFrame webSocketFrame);

    protected abstract int newRsv(WebSocketFrame webSocketFrame);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        decode2(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeflateDecoder(boolean noContext, WebSocketExtensionFilter extensionDecoderFilter) {
        this.noContext = noContext;
        this.extensionDecoderFilter = (WebSocketExtensionFilter) ObjectUtil.checkNotNull(extensionDecoderFilter, "extensionDecoderFilter");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketExtensionFilter extensionDecoderFilter() {
        return this.extensionDecoderFilter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: decode, reason: avoid collision after fix types in other method */
    public void decode2(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        WebSocketFrame outMsg;
        ByteBuf decompressedContent = decompressContent(ctx, msg);
        if (msg instanceof TextWebSocketFrame) {
            outMsg = new TextWebSocketFrame(msg.isFinalFragment(), newRsv(msg), decompressedContent);
        } else if (msg instanceof BinaryWebSocketFrame) {
            outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), newRsv(msg), decompressedContent);
        } else if (msg instanceof ContinuationWebSocketFrame) {
            outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), newRsv(msg), decompressedContent);
        } else {
            throw new CodecException("unexpected frame type: " + msg.getClass().getName());
        }
        out.add(outMsg);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cleanup();
        super.handlerRemoved(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cleanup();
        super.channelInactive(ctx);
    }

    private ByteBuf decompressContent(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (this.decoder == null) {
            if (!(msg instanceof TextWebSocketFrame) && !(msg instanceof BinaryWebSocketFrame)) {
                throw new CodecException("unexpected initial frame type: " + msg.getClass().getName());
            }
            this.decoder = new EmbeddedChannel(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE));
        }
        boolean readable = msg.content().isReadable();
        boolean emptyDeflateBlock = EMPTY_DEFLATE_BLOCK.equals(msg.content());
        this.decoder.writeInbound(msg.content().retain());
        if (appendFrameTail(msg)) {
            this.decoder.writeInbound(FRAME_TAIL.duplicate());
        }
        CompositeByteBuf compositeDecompressedContent = ctx.alloc().compositeBuffer();
        while (true) {
            ByteBuf partUncompressedContent = (ByteBuf) this.decoder.readInbound();
            if (partUncompressedContent == null) {
                break;
            }
            if (!partUncompressedContent.isReadable()) {
                partUncompressedContent.release();
            } else {
                compositeDecompressedContent.addComponent(true, partUncompressedContent);
            }
        }
        if (!emptyDeflateBlock && readable && compositeDecompressedContent.numComponents() <= 0 && !(msg instanceof ContinuationWebSocketFrame)) {
            compositeDecompressedContent.release();
            throw new CodecException("cannot read uncompressed buffer");
        }
        if (msg.isFinalFragment() && this.noContext) {
            cleanup();
        }
        return compositeDecompressedContent;
    }

    private void cleanup() {
        if (this.decoder != null) {
            this.decoder.finishAndReleaseAll();
            this.decoder = null;
        }
    }
}
