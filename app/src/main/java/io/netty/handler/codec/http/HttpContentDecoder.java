package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class HttpContentDecoder extends MessageToMessageDecoder<HttpObject> {
    static final String IDENTITY = HttpHeaderValues.IDENTITY.toString();
    private boolean continueResponse;
    protected ChannelHandlerContext ctx;
    private EmbeddedChannel decoder;
    private boolean needRead = true;

    protected abstract EmbeddedChannel newContentDecoder(String str) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List list) throws Exception {
        decode2(channelHandlerContext, httpObject, (List<Object>) list);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        String contentEncoding;
        HttpMessage copy;
        try {
            if ((msg instanceof HttpResponse) && ((HttpResponse) msg).status().code() == 100) {
                if (!(msg instanceof LastHttpContent)) {
                    this.continueResponse = true;
                }
                out.add(ReferenceCountUtil.retain(msg));
                return;
            }
            if (this.continueResponse) {
                if (msg instanceof LastHttpContent) {
                    this.continueResponse = false;
                }
                out.add(ReferenceCountUtil.retain(msg));
                return;
            }
            if (msg instanceof HttpMessage) {
                cleanup();
                HttpMessage message = (HttpMessage) msg;
                HttpHeaders headers = message.headers();
                String contentEncoding2 = headers.get(HttpHeaderNames.CONTENT_ENCODING);
                if (contentEncoding2 != null) {
                    contentEncoding = contentEncoding2.trim();
                } else {
                    String transferEncoding = headers.get(HttpHeaderNames.TRANSFER_ENCODING);
                    if (transferEncoding != null) {
                        int idx = transferEncoding.indexOf(",");
                        contentEncoding = idx != -1 ? transferEncoding.substring(0, idx).trim() : transferEncoding.trim();
                    } else {
                        contentEncoding = IDENTITY;
                    }
                }
                this.decoder = newContentDecoder(contentEncoding);
                if (this.decoder == null) {
                    if (message instanceof HttpContent) {
                        ((HttpContent) message).retain();
                    }
                    out.add(message);
                    return;
                }
                if (headers.contains(HttpHeaderNames.CONTENT_LENGTH)) {
                    headers.remove(HttpHeaderNames.CONTENT_LENGTH);
                    headers.set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                }
                CharSequence targetContentEncoding = getTargetContentEncoding(contentEncoding);
                if (HttpHeaderValues.IDENTITY.contentEquals(targetContentEncoding)) {
                    headers.remove(HttpHeaderNames.CONTENT_ENCODING);
                } else {
                    headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
                }
                if (message instanceof HttpContent) {
                    if (message instanceof HttpRequest) {
                        HttpRequest r = (HttpRequest) message;
                        copy = new DefaultHttpRequest(r.protocolVersion(), r.method(), r.uri());
                    } else {
                        if (!(message instanceof HttpResponse)) {
                            throw new CodecException("Object of class " + message.getClass().getName() + " is not an HttpRequest or HttpResponse");
                        }
                        HttpResponse r2 = (HttpResponse) message;
                        copy = new DefaultHttpResponse(r2.protocolVersion(), r2.status());
                    }
                    copy.headers().set(message.headers());
                    copy.setDecoderResult(message.decoderResult());
                    out.add(copy);
                } else {
                    out.add(message);
                }
            }
            if (msg instanceof HttpContent) {
                HttpContent c = (HttpContent) msg;
                if (this.decoder == null) {
                    out.add(c.retain());
                } else {
                    decodeContent(c, out);
                }
            }
        } finally {
            this.needRead = out.isEmpty();
        }
    }

    private void decodeContent(HttpContent c, List<Object> out) {
        ByteBuf content = c.content();
        decode(content, out);
        if (c instanceof LastHttpContent) {
            finishDecode(out);
            LastHttpContent last = (LastHttpContent) c;
            HttpHeaders headers = last.trailingHeaders();
            if (headers.isEmpty()) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            } else {
                out.add(new ComposedLastHttpContent(headers, DecoderResult.SUCCESS));
            }
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        boolean needRead = this.needRead;
        this.needRead = true;
        try {
            ctx.fireChannelReadComplete();
        } finally {
            if (needRead && !ctx.channel().config().isAutoRead()) {
                ctx.read();
            }
        }
    }

    protected String getTargetContentEncoding(String contentEncoding) throws Exception {
        return IDENTITY;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cleanupSafely(ctx);
        super.handlerRemoved(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cleanupSafely(ctx);
        super.channelInactive(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.handlerAdded(ctx);
    }

    private void cleanup() {
        if (this.decoder != null) {
            this.decoder.finishAndReleaseAll();
            this.decoder = null;
        }
    }

    private void cleanupSafely(ChannelHandlerContext ctx) {
        try {
            cleanup();
        } catch (Throwable cause) {
            ctx.fireExceptionCaught(cause);
        }
    }

    private void decode(ByteBuf in, List<Object> out) {
        this.decoder.writeInbound(in.retain());
        fetchDecoderOutput(out);
    }

    private void finishDecode(List<Object> out) {
        if (this.decoder.finish()) {
            fetchDecoderOutput(out);
        }
        this.decoder = null;
    }

    private void fetchDecoderOutput(List<Object> out) {
        while (true) {
            ByteBuf buf = (ByteBuf) this.decoder.readInbound();
            if (buf != null) {
                if (!buf.isReadable()) {
                    buf.release();
                } else {
                    out.add(new DefaultHttpContent(buf));
                }
            } else {
                return;
            }
        }
    }
}
