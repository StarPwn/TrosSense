package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/* loaded from: classes4.dex */
public final class HttpServerCodec extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder> implements HttpServerUpgradeHandler.SourceCodec {
    private final Queue<HttpMethod> queue;

    public HttpServerCodec() {
        this(4096, 8192, 8192);
    }

    public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize));
    }

    @Deprecated
    public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders));
    }

    @Deprecated
    public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize));
    }

    @Deprecated
    public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths));
    }

    @Deprecated
    public HttpServerCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths, boolean allowPartialChunks) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths).setAllowPartialChunks(allowPartialChunks));
    }

    public HttpServerCodec(HttpDecoderConfig config) {
        this.queue = new ArrayDeque();
        init(new HttpServerRequestDecoder(config), new HttpServerResponseEncoder());
    }

    @Override // io.netty.handler.codec.http.HttpServerUpgradeHandler.SourceCodec
    public void upgradeFrom(ChannelHandlerContext ctx) {
        ctx.pipeline().remove(this);
    }

    /* loaded from: classes4.dex */
    private final class HttpServerRequestDecoder extends HttpRequestDecoder {
        HttpServerRequestDecoder(HttpDecoderConfig config) {
            super(config);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpObjectDecoder, io.netty.handler.codec.ByteToMessageDecoder
        public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
            int oldSize = out.size();
            super.decode(ctx, buffer, out);
            int size = out.size();
            for (int i = oldSize; i < size; i++) {
                Object obj = out.get(i);
                if (obj instanceof HttpRequest) {
                    HttpServerCodec.this.queue.add(((HttpRequest) obj).method());
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class HttpServerResponseEncoder extends HttpResponseEncoder {
        private HttpMethod method;

        private HttpServerResponseEncoder() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpResponseEncoder, io.netty.handler.codec.http.HttpObjectEncoder
        public void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty) {
            if (!isAlwaysEmpty && HttpMethod.CONNECT.equals(this.method) && msg.status().codeClass() == HttpStatusClass.SUCCESS) {
                msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
            } else {
                super.sanitizeHeadersBeforeEncode(msg, isAlwaysEmpty);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpResponseEncoder, io.netty.handler.codec.http.HttpObjectEncoder
        public boolean isContentAlwaysEmpty(HttpResponse msg) {
            this.method = (HttpMethod) HttpServerCodec.this.queue.poll();
            return HttpMethod.HEAD.equals(this.method) || super.isContentAlwaysEmpty(msg);
        }
    }
}
