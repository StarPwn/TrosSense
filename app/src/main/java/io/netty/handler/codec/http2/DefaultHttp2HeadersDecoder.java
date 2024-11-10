package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.Http2HeadersDecoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultHttp2HeadersDecoder implements Http2HeadersDecoder, Http2HeadersDecoder.Configuration {
    private static final float HEADERS_COUNT_WEIGHT_HISTORICAL = 0.8f;
    private static final float HEADERS_COUNT_WEIGHT_NEW = 0.2f;
    private float headerArraySizeAccumulator;
    private final HpackDecoder hpackDecoder;
    private long maxHeaderListSizeGoAway;
    private final boolean validateHeaderValues;
    private final boolean validateHeaders;

    public DefaultHttp2HeadersDecoder() {
        this(true);
    }

    public DefaultHttp2HeadersDecoder(boolean validateHeaders) {
        this(validateHeaders, Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE);
    }

    public DefaultHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues) {
        this(validateHeaders, validateHeaderValues, Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE);
    }

    public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize) {
        this(validateHeaders, false, new HpackDecoder(maxHeaderListSize));
    }

    public DefaultHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues, long maxHeaderListSize) {
        this(validateHeaders, validateHeaderValues, new HpackDecoder(maxHeaderListSize));
    }

    public DefaultHttp2HeadersDecoder(boolean validateHeaders, long maxHeaderListSize, @Deprecated int initialHuffmanDecodeCapacity) {
        this(validateHeaders, false, new HpackDecoder(maxHeaderListSize));
    }

    DefaultHttp2HeadersDecoder(boolean validateHeaders, boolean validateHeaderValues, HpackDecoder hpackDecoder) {
        this.headerArraySizeAccumulator = 8.0f;
        this.hpackDecoder = (HpackDecoder) ObjectUtil.checkNotNull(hpackDecoder, "hpackDecoder");
        this.validateHeaders = validateHeaders;
        this.validateHeaderValues = validateHeaderValues;
        this.maxHeaderListSizeGoAway = Http2CodecUtil.calculateMaxHeaderListSizeGoAway(hpackDecoder.getMaxHeaderListSize());
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder.Configuration
    public void maxHeaderTableSize(long max) throws Http2Exception {
        this.hpackDecoder.setMaxHeaderTableSize(max);
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder.Configuration
    public long maxHeaderTableSize() {
        return this.hpackDecoder.getMaxHeaderTableSize();
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder.Configuration
    public void maxHeaderListSize(long max, long goAwayMax) throws Http2Exception {
        if (goAwayMax < max || goAwayMax < 0) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Header List Size GO_AWAY %d must be non-negative and >= %d", Long.valueOf(goAwayMax), Long.valueOf(max));
        }
        this.hpackDecoder.setMaxHeaderListSize(max);
        this.maxHeaderListSizeGoAway = goAwayMax;
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder.Configuration
    public long maxHeaderListSize() {
        return this.hpackDecoder.getMaxHeaderListSize();
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder.Configuration
    public long maxHeaderListSizeGoAway() {
        return this.maxHeaderListSizeGoAway;
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder
    public Http2HeadersDecoder.Configuration configuration() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2HeadersDecoder
    public Http2Headers decodeHeaders(int streamId, ByteBuf headerBlock) throws Http2Exception {
        try {
            Http2Headers headers = newHeaders();
            this.hpackDecoder.decode(streamId, headerBlock, headers, this.validateHeaders);
            this.headerArraySizeAccumulator = (headers.size() * 0.2f) + (this.headerArraySizeAccumulator * HEADERS_COUNT_WEIGHT_HISTORICAL);
            return headers;
        } catch (Http2Exception e) {
            throw e;
        } catch (Throwable e2) {
            throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e2, "Error decoding headers: %s", e2.getMessage());
        }
    }

    protected final int numberOfHeadersGuess() {
        return (int) this.headerArraySizeAccumulator;
    }

    protected final boolean validateHeaders() {
        return this.validateHeaders;
    }

    protected boolean validateHeaderValues() {
        return this.validateHeaderValues;
    }

    protected Http2Headers newHeaders() {
        return new DefaultHttp2Headers(this.validateHeaders, this.validateHeaderValues, (int) this.headerArraySizeAccumulator);
    }
}
