package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public final class InboundHttp2ToHttpAdapterBuilder extends AbstractInboundHttp2ToHttpAdapterBuilder<InboundHttp2ToHttpAdapter, InboundHttp2ToHttpAdapterBuilder> {
    public InboundHttp2ToHttpAdapterBuilder(Http2Connection connection) {
        super(connection);
    }

    @Override // io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder
    public InboundHttp2ToHttpAdapterBuilder maxContentLength(int maxContentLength) {
        return (InboundHttp2ToHttpAdapterBuilder) super.maxContentLength(maxContentLength);
    }

    @Override // io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder
    public InboundHttp2ToHttpAdapterBuilder validateHttpHeaders(boolean validate) {
        return (InboundHttp2ToHttpAdapterBuilder) super.validateHttpHeaders(validate);
    }

    @Override // io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder
    public InboundHttp2ToHttpAdapterBuilder propagateSettings(boolean propagate) {
        return (InboundHttp2ToHttpAdapterBuilder) super.propagateSettings(propagate);
    }

    @Override // io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder
    public InboundHttp2ToHttpAdapter build() {
        return super.build();
    }

    @Override // io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder
    protected InboundHttp2ToHttpAdapter build(Http2Connection connection, int maxContentLength, boolean validateHttpHeaders, boolean propagateSettings) throws Exception {
        return new InboundHttp2ToHttpAdapter(connection, maxContentLength, validateHttpHeaders, propagateSettings);
    }
}
