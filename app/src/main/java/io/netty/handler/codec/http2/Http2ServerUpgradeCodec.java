package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Dialect;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class Http2ServerUpgradeCodec implements HttpServerUpgradeHandler.UpgradeCodec {
    private final Http2ConnectionHandler connectionHandler;
    private final Http2FrameReader frameReader;
    private final String handlerName;
    private final ChannelHandler[] handlers;
    private Http2Settings settings;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Http2ServerUpgradeCodec.class);
    private static final List<CharSequence> REQUIRED_UPGRADE_HEADERS = Collections.singletonList(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
    private static final ChannelHandler[] EMPTY_HANDLERS = new ChannelHandler[0];

    public Http2ServerUpgradeCodec(Http2ConnectionHandler connectionHandler) {
        this(null, connectionHandler, EMPTY_HANDLERS);
    }

    public Http2ServerUpgradeCodec(Http2MultiplexCodec http2Codec) {
        this(null, http2Codec, EMPTY_HANDLERS);
    }

    public Http2ServerUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler) {
        this(handlerName, connectionHandler, EMPTY_HANDLERS);
    }

    public Http2ServerUpgradeCodec(String handlerName, Http2MultiplexCodec http2Codec) {
        this(handlerName, http2Codec, EMPTY_HANDLERS);
    }

    public Http2ServerUpgradeCodec(Http2FrameCodec http2Codec, ChannelHandler... handlers) {
        this(null, http2Codec, handlers);
    }

    private Http2ServerUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler, ChannelHandler... handlers) {
        this.handlerName = handlerName;
        this.connectionHandler = connectionHandler;
        this.handlers = handlers;
        this.frameReader = new DefaultHttp2FrameReader();
    }

    @Override // io.netty.handler.codec.http.HttpServerUpgradeHandler.UpgradeCodec
    public Collection<CharSequence> requiredUpgradeHeaders() {
        return REQUIRED_UPGRADE_HEADERS;
    }

    @Override // io.netty.handler.codec.http.HttpServerUpgradeHandler.UpgradeCodec
    public boolean prepareUpgradeResponse(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest, HttpHeaders headers) {
        try {
            List<String> upgradeHeaders = upgradeRequest.headers().getAll(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
            if (upgradeHeaders.size() != 1) {
                throw new IllegalArgumentException("There must be 1 and only 1 " + ((Object) Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER) + " header.");
            }
            this.settings = decodeSettingsHeader(ctx, upgradeHeaders.get(0));
            return true;
        } catch (Throwable cause) {
            logger.info("Error during upgrade to HTTP/2", cause);
            return false;
        }
    }

    @Override // io.netty.handler.codec.http.HttpServerUpgradeHandler.UpgradeCodec
    public void upgradeTo(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest) {
        try {
            ctx.pipeline().addAfter(ctx.name(), this.handlerName, this.connectionHandler);
            if (this.handlers != null) {
                String name = ctx.pipeline().context(this.connectionHandler).name();
                for (int i = this.handlers.length - 1; i >= 0; i--) {
                    ctx.pipeline().addAfter(name, null, this.handlers[i]);
                }
            }
            this.connectionHandler.onHttpServerUpgrade(this.settings);
        } catch (Http2Exception e) {
            ctx.fireExceptionCaught((Throwable) e);
            ctx.close();
        }
    }

    private Http2Settings decodeSettingsHeader(ChannelHandlerContext ctx, CharSequence settingsHeader) throws Http2Exception {
        ByteBuf header = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(settingsHeader), CharsetUtil.UTF_8);
        try {
            ByteBuf payload = Base64.decode(header, Base64Dialect.URL_SAFE);
            ByteBuf frame = createSettingsFrame(ctx, payload);
            return decodeSettings(ctx, frame);
        } finally {
            header.release();
        }
    }

    private Http2Settings decodeSettings(ChannelHandlerContext ctx, ByteBuf frame) throws Http2Exception {
        try {
            final Http2Settings decodedSettings = new Http2Settings();
            this.frameReader.readFrame(ctx, frame, new Http2FrameAdapter() { // from class: io.netty.handler.codec.http2.Http2ServerUpgradeCodec.1
                @Override // io.netty.handler.codec.http2.Http2FrameAdapter, io.netty.handler.codec.http2.Http2FrameListener
                public void onSettingsRead(ChannelHandlerContext ctx2, Http2Settings settings) {
                    decodedSettings.copyFrom(settings);
                }
            });
            return decodedSettings;
        } finally {
            frame.release();
        }
    }

    private static ByteBuf createSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload) {
        ByteBuf frame = ctx.alloc().buffer(payload.readableBytes() + 9);
        Http2CodecUtil.writeFrameHeader(frame, payload.readableBytes(), (byte) 4, new Http2Flags(), 0);
        frame.writeBytes(payload);
        payload.release();
        return frame;
    }
}
