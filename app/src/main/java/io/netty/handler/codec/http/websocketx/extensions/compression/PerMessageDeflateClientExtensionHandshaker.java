package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionEncoder;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionFilterProvider;
import io.netty.util.internal.ObjectUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes4.dex */
public final class PerMessageDeflateClientExtensionHandshaker implements WebSocketClientExtensionHandshaker {
    private final boolean allowClientNoContext;
    private final boolean allowClientWindowSize;
    private final int compressionLevel;
    private final WebSocketExtensionFilterProvider extensionFilterProvider;
    private final boolean requestedServerNoContext;
    private final int requestedServerWindowSize;

    public PerMessageDeflateClientExtensionHandshaker() {
        this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), 15, false, false);
    }

    public PerMessageDeflateClientExtensionHandshaker(int compressionLevel, boolean allowClientWindowSize, int requestedServerWindowSize, boolean allowClientNoContext, boolean requestedServerNoContext) {
        this(compressionLevel, allowClientWindowSize, requestedServerWindowSize, allowClientNoContext, requestedServerNoContext, WebSocketExtensionFilterProvider.DEFAULT);
    }

    public PerMessageDeflateClientExtensionHandshaker(int compressionLevel, boolean allowClientWindowSize, int requestedServerWindowSize, boolean allowClientNoContext, boolean requestedServerNoContext, WebSocketExtensionFilterProvider extensionFilterProvider) {
        if (requestedServerWindowSize > 15 || requestedServerWindowSize < 8) {
            throw new IllegalArgumentException("requestedServerWindowSize: " + requestedServerWindowSize + " (expected: 8-15)");
        }
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressionLevel = compressionLevel;
        this.allowClientWindowSize = allowClientWindowSize;
        this.requestedServerWindowSize = requestedServerWindowSize;
        this.allowClientNoContext = allowClientNoContext;
        this.requestedServerNoContext = requestedServerNoContext;
        this.extensionFilterProvider = (WebSocketExtensionFilterProvider) ObjectUtil.checkNotNull(extensionFilterProvider, "extensionFilterProvider");
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker
    public WebSocketExtensionData newRequestData() {
        HashMap<String, String> parameters = new HashMap<>(4);
        if (this.requestedServerNoContext) {
            parameters.put("server_no_context_takeover", null);
        }
        if (this.allowClientNoContext) {
            parameters.put("client_no_context_takeover", null);
        }
        if (this.requestedServerWindowSize != 15) {
            parameters.put("server_max_window_bits", Integer.toString(this.requestedServerWindowSize));
        }
        if (this.allowClientWindowSize) {
            parameters.put("client_max_window_bits", null);
        }
        return new WebSocketExtensionData("permessage-deflate", parameters);
    }

    @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandshaker
    public WebSocketClientExtension handshakeExtension(WebSocketExtensionData extensionData) {
        if (!"permessage-deflate".equals(extensionData.name())) {
            return null;
        }
        boolean succeed = true;
        int clientWindowSize = 15;
        int serverWindowSize = 15;
        boolean serverNoContext = false;
        boolean clientNoContext = false;
        Iterator<Map.Entry<String, String>> parametersIterator = extensionData.parameters().entrySet().iterator();
        while (succeed && parametersIterator.hasNext()) {
            Map.Entry<String, String> parameter = parametersIterator.next();
            if ("client_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
                if (this.allowClientWindowSize) {
                    clientWindowSize = Integer.parseInt(parameter.getValue());
                    if (clientWindowSize > 15 || clientWindowSize < 8) {
                        succeed = false;
                    }
                } else {
                    succeed = false;
                }
            } else if ("server_max_window_bits".equalsIgnoreCase(parameter.getKey())) {
                serverWindowSize = Integer.parseInt(parameter.getValue());
                if (serverWindowSize > 15 || serverWindowSize < 8) {
                    succeed = false;
                }
            } else if ("client_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
                if (this.allowClientNoContext) {
                    clientNoContext = true;
                } else {
                    succeed = false;
                }
            } else if ("server_no_context_takeover".equalsIgnoreCase(parameter.getKey())) {
                serverNoContext = true;
            } else {
                succeed = false;
            }
        }
        if ((this.requestedServerNoContext && !serverNoContext) || this.requestedServerWindowSize < serverWindowSize) {
            succeed = false;
        }
        if (!succeed) {
            return null;
        }
        return new PermessageDeflateExtension(serverNoContext, serverWindowSize, clientNoContext, clientWindowSize, this.extensionFilterProvider);
    }

    /* loaded from: classes4.dex */
    private final class PermessageDeflateExtension implements WebSocketClientExtension {
        private final boolean clientNoContext;
        private final int clientWindowSize;
        private final WebSocketExtensionFilterProvider extensionFilterProvider;
        private final boolean serverNoContext;
        private final int serverWindowSize;

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public int rsv() {
            return 4;
        }

        PermessageDeflateExtension(boolean serverNoContext, int serverWindowSize, boolean clientNoContext, int clientWindowSize, WebSocketExtensionFilterProvider extensionFilterProvider) {
            this.serverNoContext = serverNoContext;
            this.serverWindowSize = serverWindowSize;
            this.clientNoContext = clientNoContext;
            this.clientWindowSize = clientWindowSize;
            this.extensionFilterProvider = extensionFilterProvider;
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new PerMessageDeflateEncoder(PerMessageDeflateClientExtensionHandshaker.this.compressionLevel, this.clientWindowSize, this.clientNoContext, this.extensionFilterProvider.encoderFilter());
        }

        @Override // io.netty.handler.codec.http.websocketx.extensions.WebSocketExtension
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new PerMessageDeflateDecoder(this.serverNoContext, this.extensionFilterProvider.decoderFilter());
        }
    }
}
