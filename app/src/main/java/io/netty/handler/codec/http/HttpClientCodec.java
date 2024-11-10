package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.http.HttpClientUpgradeHandler;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public final class HttpClientCodec extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder> implements HttpClientUpgradeHandler.SourceCodec {
    public static final boolean DEFAULT_FAIL_ON_MISSING_RESPONSE = false;
    public static final boolean DEFAULT_PARSE_HTTP_AFTER_CONNECT_REQUEST = false;
    private boolean done;
    private final boolean failOnMissingResponse;
    private final boolean parseHttpAfterConnectRequest;
    private final Queue<HttpMethod> queue;
    private final AtomicLong requestResponseCounter;

    public HttpClientCodec() {
        this(new HttpDecoderConfig(), false, false);
    }

    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize), false, false);
    }

    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize), false, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders), false, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, boolean parseHttpAfterConnectRequest) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders), parseHttpAfterConnectRequest, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize), false, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize, boolean parseHttpAfterConnectRequest) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize), parseHttpAfterConnectRequest, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize, boolean parseHttpAfterConnectRequest, boolean allowDuplicateContentLengths) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths), parseHttpAfterConnectRequest, failOnMissingResponse);
    }

    @Deprecated
    public HttpClientCodec(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean failOnMissingResponse, boolean validateHeaders, int initialBufferSize, boolean parseHttpAfterConnectRequest, boolean allowDuplicateContentLengths, boolean allowPartialChunks) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths).setAllowPartialChunks(allowPartialChunks), parseHttpAfterConnectRequest, failOnMissingResponse);
    }

    public HttpClientCodec(HttpDecoderConfig config, boolean parseHttpAfterConnectRequest, boolean failOnMissingResponse) {
        this.queue = new ArrayDeque();
        this.requestResponseCounter = new AtomicLong();
        init(new Decoder(config), new Encoder());
        this.parseHttpAfterConnectRequest = parseHttpAfterConnectRequest;
        this.failOnMissingResponse = failOnMissingResponse;
    }

    @Override // io.netty.handler.codec.http.HttpClientUpgradeHandler.SourceCodec
    public void prepareUpgradeFrom(ChannelHandlerContext ctx) {
        ((Encoder) outboundHandler()).upgraded = true;
    }

    @Override // io.netty.handler.codec.http.HttpClientUpgradeHandler.SourceCodec
    public void upgradeFrom(ChannelHandlerContext ctx) {
        ChannelPipeline p = ctx.pipeline();
        p.remove(this);
    }

    public void setSingleDecode(boolean singleDecode) {
        inboundHandler().setSingleDecode(singleDecode);
    }

    public boolean isSingleDecode() {
        return inboundHandler().isSingleDecode();
    }

    /* loaded from: classes4.dex */
    private final class Encoder extends HttpRequestEncoder {
        boolean upgraded;

        private Encoder() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpObjectEncoder, io.netty.handler.codec.MessageToMessageEncoder
        public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
            if (this.upgraded) {
                out.add(msg);
                return;
            }
            if (msg instanceof HttpRequest) {
                HttpClientCodec.this.queue.offer(((HttpRequest) msg).method());
            }
            super.encode(ctx, msg, out);
            if (HttpClientCodec.this.failOnMissingResponse && !HttpClientCodec.this.done && (msg instanceof LastHttpContent)) {
                HttpClientCodec.this.requestResponseCounter.incrementAndGet();
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class Decoder extends HttpResponseDecoder {
        Decoder(HttpDecoderConfig config) {
            super(config);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpObjectDecoder, io.netty.handler.codec.ByteToMessageDecoder
        public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
            if (HttpClientCodec.this.done) {
                int readable = actualReadableBytes();
                if (readable == 0) {
                    return;
                }
                out.add(buffer.readBytes(readable));
                return;
            }
            int oldSize = out.size();
            super.decode(ctx, buffer, out);
            if (HttpClientCodec.this.failOnMissingResponse) {
                int size = out.size();
                for (int i = oldSize; i < size; i++) {
                    decrement(out.get(i));
                }
            }
        }

        private void decrement(Object msg) {
            if (msg != null && (msg instanceof LastHttpContent)) {
                HttpClientCodec.this.requestResponseCounter.decrementAndGet();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.http.HttpObjectDecoder
        public boolean isContentAlwaysEmpty(HttpMessage msg) {
            HttpMethod method = (HttpMethod) HttpClientCodec.this.queue.poll();
            HttpResponseStatus status = ((HttpResponse) msg).status();
            HttpStatusClass statusClass = status.codeClass();
            int statusCode = status.code();
            if (statusClass == HttpStatusClass.INFORMATIONAL) {
                return super.isContentAlwaysEmpty(msg);
            }
            if (method != null) {
                char firstChar = method.name().charAt(0);
                switch (firstChar) {
                    case 'C':
                        if (statusCode == 200 && HttpMethod.CONNECT.equals(method)) {
                            if (!HttpClientCodec.this.parseHttpAfterConnectRequest) {
                                HttpClientCodec.this.done = true;
                                HttpClientCodec.this.queue.clear();
                            }
                            return true;
                        }
                        break;
                    case 'H':
                        if (HttpMethod.HEAD.equals(method)) {
                            return true;
                        }
                        break;
                }
            }
            return super.isContentAlwaysEmpty(msg);
        }

        @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            if (HttpClientCodec.this.failOnMissingResponse) {
                long missingResponses = HttpClientCodec.this.requestResponseCounter.get();
                if (missingResponses > 0) {
                    ctx.fireExceptionCaught((Throwable) new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
                }
            }
        }
    }
}
