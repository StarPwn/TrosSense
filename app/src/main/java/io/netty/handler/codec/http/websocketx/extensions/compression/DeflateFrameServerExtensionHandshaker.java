package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilterProvider;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;

/* loaded from: classes4.dex */
public final class DeflateFrameServerExtensionHandshaker implements WebSocketServerExtensionHandshaker {
    static final String DEFLATE_FRAME_EXTENSION = "deflate-frame";
    static final String X_WEBKIT_DEFLATE_FRAME_EXTENSION = "x-webkit-deflate-frame";
    private final int compressionLevel;
    private final WebSocketExtensionFilterProvider extensionFilterProvider;

    public DeflateFrameServerExtensionHandshaker() {
        this(6);
    }

    public DeflateFrameServerExtensionHandshaker(int compressionLevel) {
        this(compressionLevel, WebSocketExtensionFilterProvider.DEFAULT);
    }

    public DeflateFrameServerExtensionHandshaker(int compressionLevel, WebSocketExtensionFilterProvider extensionFilterProvider) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressionLevel = compressionLevel;
        this.extensionFilterProvider = (WebSocketExtensionFilterProvider) ObjectUtil.checkNotNull(extensionFilterProvider, "extensionFilterProvider");
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker
    public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData) {
        if ((X_WEBKIT_DEFLATE_FRAME_EXTENSION.equals(extensionData.name()) || DEFLATE_FRAME_EXTENSION.equals(extensionData.name())) && extensionData.parameters().isEmpty()) {
            return new DeflateFrameServerExtension(this.compressionLevel, extensionData.name(), this.extensionFilterProvider);
        }
        return null;
    }

    /* loaded from: classes4.dex */
    private static class DeflateFrameServerExtension implements WebSocketServerExtension {
        private final int compressionLevel;
        private final WebSocketExtensionFilterProvider extensionFilterProvider;
        private final String extensionName;

        DeflateFrameServerExtension(int compressionLevel, String extensionName, WebSocketExtensionFilterProvider extensionFilterProvider) {
            this.extensionName = extensionName;
            this.compressionLevel = compressionLevel;
            this.extensionFilterProvider = extensionFilterProvider;
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public int rsv() {
            return 4;
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerFrameDeflateEncoder(this.compressionLevel, 15, false, this.extensionFilterProvider.encoderFilter());
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerFrameDeflateDecoder(false, this.extensionFilterProvider.decoderFilter());
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension
        public WebSocketExtensionData newReponseData() {
            return new WebSocketExtensionData(this.extensionName, Collections.emptyMap());
        }
    }
}
