package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilter;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
abstract class DeflateEncoder extends WebSocketExtensionEncoder {
    private final int compressionLevel;
    private EmbeddedChannel encoder;
    private final WebSocketExtensionFilter extensionEncoderFilter;
    private final boolean noContext;
    private final int windowSize;

    protected abstract boolean removeFrameTail(WebSocketFrame webSocketFrame);

    protected abstract int rsv(WebSocketFrame webSocketFrame);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        encode2(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeflateEncoder(int compressionLevel, int windowSize, boolean noContext, WebSocketExtensionFilter extensionEncoderFilter) {
        this.compressionLevel = compressionLevel;
        this.windowSize = windowSize;
        this.noContext = noContext;
        this.extensionEncoderFilter = (WebSocketExtensionFilter) ObjectUtil.checkNotNull(extensionEncoderFilter, "extensionEncoderFilter");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WebSocketExtensionFilter extensionEncoderFilter() {
        return this.extensionEncoderFilter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: encode, reason: avoid collision after fix types in other method */
    public void encode2(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf compressedContent;
        WebSocketFrame outMsg;
        if (msg.content().isReadable()) {
            compressedContent = compressContent(ctx, msg);
        } else if (msg.isFinalFragment()) {
            compressedContent = PerMessageDeflateDecoder.EMPTY_DEFLATE_BLOCK.duplicate();
        } else {
            throw new CodecException("cannot compress content buffer");
        }
        if (msg instanceof TextWebSocketFrame) {
            outMsg = new TextWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent);
        } else if (msg instanceof BinaryWebSocketFrame) {
            outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent);
        } else if (msg instanceof ContinuationWebSocketFrame) {
            outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), rsv(msg), compressedContent);
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

    private ByteBuf compressContent(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (this.encoder == null) {
            this.encoder = new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.NONE, this.compressionLevel, this.windowSize, 8));
        }
        this.encoder.writeOutbound(msg.content().retain());
        CompositeByteBuf fullCompressedContent = ctx.alloc().compositeBuffer();
        while (true) {
            ByteBuf partCompressedContent = (ByteBuf) this.encoder.readOutbound();
            if (partCompressedContent == null) {
                break;
            }
            if (!partCompressedContent.isReadable()) {
                partCompressedContent.release();
            } else {
                fullCompressedContent.addComponent(true, partCompressedContent);
            }
        }
        if (fullCompressedContent.numComponents() <= 0) {
            fullCompressedContent.release();
            throw new CodecException("cannot read compressed buffer");
        }
        if (msg.isFinalFragment() && this.noContext) {
            cleanup();
        }
        if (removeFrameTail(msg)) {
            int realLength = fullCompressedContent.readableBytes() - PerMessageDeflateDecoder.FRAME_TAIL.readableBytes();
            ByteBuf compressedContent = fullCompressedContent.slice(0, realLength);
            return compressedContent;
        }
        return fullCompressedContent;
    }

    private void cleanup() {
        if (this.encoder != null) {
            this.encoder.finishAndReleaseAll();
            this.encoder = null;
        }
    }
}
