package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.compression.Brotli;
import io.netty.handler.codec.compression.BrotliEncoder;
import io.netty.handler.codec.compression.BrotliOptions;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.compression.DeflateOptions;
import io.netty.handler.codec.compression.GzipOptions;
import io.netty.handler.codec.compression.SnappyFrameEncoder;
import io.netty.handler.codec.compression.SnappyOptions;
import io.netty.handler.codec.compression.StandardCompressionOptions;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.compression.Zstd;
import io.netty.handler.codec.compression.ZstdEncoder;
import io.netty.handler.codec.compression.ZstdOptions;
import io.netty.handler.codec.http.HttpContentEncoder;
import io.netty.util.internal.ObjectUtil;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class HttpContentCompressor extends HttpContentEncoder {
    private final BrotliOptions brotliOptions;
    private final int compressionLevel;
    private final int contentSizeThreshold;
    private ChannelHandlerContext ctx;
    private final DeflateOptions deflateOptions;
    private final Map<String, CompressionEncoderFactory> factories;
    private final GzipOptions gzipOptions;
    private final int memLevel;
    private final SnappyOptions snappyOptions;
    private final boolean supportsCompressionOptions;
    private final int windowBits;
    private final ZstdOptions zstdOptions;

    public HttpContentCompressor() {
        this(6);
    }

    @Deprecated
    public HttpContentCompressor(int compressionLevel) {
        this(compressionLevel, 15, 8, 0);
    }

    @Deprecated
    public HttpContentCompressor(int compressionLevel, int windowBits, int memLevel) {
        this(compressionLevel, windowBits, memLevel, 0);
    }

    @Deprecated
    public HttpContentCompressor(int compressionLevel, int windowBits, int memLevel, int contentSizeThreshold) {
        this.compressionLevel = ObjectUtil.checkInRange(compressionLevel, 0, 9, "compressionLevel");
        this.windowBits = ObjectUtil.checkInRange(windowBits, 9, 15, "windowBits");
        this.memLevel = ObjectUtil.checkInRange(memLevel, 1, 9, "memLevel");
        this.contentSizeThreshold = ObjectUtil.checkPositiveOrZero(contentSizeThreshold, "contentSizeThreshold");
        this.brotliOptions = null;
        this.gzipOptions = null;
        this.deflateOptions = null;
        this.zstdOptions = null;
        this.snappyOptions = null;
        this.factories = null;
        this.supportsCompressionOptions = false;
    }

    public HttpContentCompressor(CompressionOptions... compressionOptions) {
        this(0, compressionOptions);
    }

    public HttpContentCompressor(int contentSizeThreshold, CompressionOptions... compressionOptions) {
        this.contentSizeThreshold = ObjectUtil.checkPositiveOrZero(contentSizeThreshold, "contentSizeThreshold");
        BrotliOptions brotliOptions = null;
        GzipOptions gzipOptions = null;
        DeflateOptions deflateOptions = null;
        ZstdOptions zstdOptions = null;
        SnappyOptions snappyOptions = null;
        if (compressionOptions == null || compressionOptions.length == 0) {
            brotliOptions = Brotli.isAvailable() ? StandardCompressionOptions.brotli() : null;
            gzipOptions = StandardCompressionOptions.gzip();
            deflateOptions = StandardCompressionOptions.deflate();
            zstdOptions = Zstd.isAvailable() ? StandardCompressionOptions.zstd() : null;
            snappyOptions = StandardCompressionOptions.snappy();
        } else {
            ObjectUtil.deepCheckNotNull("compressionOptions", compressionOptions);
            for (CompressionOptions compressionOption : compressionOptions) {
                if (Brotli.isAvailable() && (compressionOption instanceof BrotliOptions)) {
                    brotliOptions = (BrotliOptions) compressionOption;
                } else if (compressionOption instanceof GzipOptions) {
                    gzipOptions = (GzipOptions) compressionOption;
                } else if (compressionOption instanceof DeflateOptions) {
                    deflateOptions = (DeflateOptions) compressionOption;
                } else if (compressionOption instanceof ZstdOptions) {
                    zstdOptions = (ZstdOptions) compressionOption;
                } else if (compressionOption instanceof SnappyOptions) {
                    snappyOptions = (SnappyOptions) compressionOption;
                } else {
                    throw new IllegalArgumentException("Unsupported " + CompressionOptions.class.getSimpleName() + ": " + compressionOption);
                }
            }
        }
        this.gzipOptions = gzipOptions;
        this.deflateOptions = deflateOptions;
        this.brotliOptions = brotliOptions;
        this.zstdOptions = zstdOptions;
        this.snappyOptions = snappyOptions;
        this.factories = new HashMap();
        if (this.gzipOptions != null) {
            this.factories.put("gzip", new GzipEncoderFactory());
        }
        if (this.deflateOptions != null) {
            this.factories.put("deflate", new DeflateEncoderFactory());
        }
        if (Brotli.isAvailable() && this.brotliOptions != null) {
            this.factories.put("br", new BrEncoderFactory());
        }
        if (this.zstdOptions != null) {
            this.factories.put("zstd", new ZstdEncoderFactory());
        }
        if (this.snappyOptions != null) {
            this.factories.put("snappy", new SnappyEncoderFactory());
        }
        this.compressionLevel = -1;
        this.windowBits = -1;
        this.memLevel = -1;
        this.supportsCompressionOptions = true;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override // io.netty.handler.codec.http.HttpContentEncoder
    protected HttpContentEncoder.Result beginEncode(HttpResponse httpResponse, String acceptEncoding) throws Exception {
        String targetContentEncoding;
        if (this.contentSizeThreshold > 0 && (httpResponse instanceof HttpContent) && ((HttpContent) httpResponse).content().readableBytes() < this.contentSizeThreshold) {
            return null;
        }
        String contentEncoding = httpResponse.headers().get(HttpHeaderNames.CONTENT_ENCODING);
        if (contentEncoding != null) {
            return null;
        }
        if (this.supportsCompressionOptions) {
            String targetContentEncoding2 = determineEncoding(acceptEncoding);
            if (targetContentEncoding2 == null) {
                return null;
            }
            CompressionEncoderFactory encoderFactory = this.factories.get(targetContentEncoding2);
            if (encoderFactory == null) {
                throw new Error();
            }
            return new HttpContentEncoder.Result(targetContentEncoding2, new EmbeddedChannel(this.ctx.channel().id(), this.ctx.channel().metadata().hasDisconnect(), this.ctx.channel().config(), encoderFactory.createEncoder()));
        }
        ZlibWrapper wrapper = determineWrapper(acceptEncoding);
        if (wrapper == null) {
            return null;
        }
        switch (wrapper) {
            case GZIP:
                targetContentEncoding = "gzip";
                break;
            case ZLIB:
                targetContentEncoding = "deflate";
                break;
            default:
                throw new Error();
        }
        return new HttpContentEncoder.Result(targetContentEncoding, new EmbeddedChannel(this.ctx.channel().id(), this.ctx.channel().metadata().hasDisconnect(), this.ctx.channel().config(), ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel)));
    }

    protected String determineEncoding(String acceptEncoding) {
        String[] split = acceptEncoding.split(",");
        int length = split.length;
        int i = 0;
        float deflateQ = -1.0f;
        float deflateQ2 = -1.0f;
        float gzipQ = -1.0f;
        float snappyQ = -1.0f;
        float zstdQ = -1.0f;
        float starQ = -1.0f;
        while (i < length) {
            int i2 = length;
            String encoding = split[i];
            float q = 1.0f;
            String[] strArr = split;
            int equalsPos = encoding.indexOf(61);
            if (equalsPos != -1) {
                try {
                    q = Float.parseFloat(encoding.substring(equalsPos + 1));
                } catch (NumberFormatException e) {
                    q = 0.0f;
                }
            }
            if (encoding.contains("*")) {
                starQ = q;
            } else if (encoding.contains("br") && q > zstdQ) {
                zstdQ = q;
            } else if (encoding.contains("zstd") && q > snappyQ) {
                snappyQ = q;
            } else if (encoding.contains("snappy") && q > gzipQ) {
                gzipQ = q;
            } else if (encoding.contains("gzip") && q > deflateQ2) {
                deflateQ2 = q;
            } else if (encoding.contains("deflate") && q > deflateQ) {
                deflateQ = q;
            }
            i++;
            length = i2;
            split = strArr;
        }
        if (zstdQ > 0.0f || snappyQ > 0.0f || gzipQ > 0.0f || deflateQ2 > 0.0f || deflateQ > 0.0f) {
            if (zstdQ != -1.0f && zstdQ >= snappyQ && this.brotliOptions != null) {
                return "br";
            }
            if (snappyQ != -1.0f && snappyQ >= gzipQ && this.zstdOptions != null) {
                return "zstd";
            }
            if (gzipQ != -1.0f && gzipQ >= deflateQ2 && this.snappyOptions != null) {
                return "snappy";
            }
            if (deflateQ2 != -1.0f && deflateQ2 >= deflateQ && this.gzipOptions != null) {
                return "gzip";
            }
            if (deflateQ != -1.0f && this.deflateOptions != null) {
                return "deflate";
            }
        }
        if (starQ > 0.0f) {
            if (zstdQ == -1.0f && this.brotliOptions != null) {
                return "br";
            }
            if (snappyQ == -1.0f && this.zstdOptions != null) {
                return "zstd";
            }
            if (gzipQ == -1.0f && this.snappyOptions != null) {
                return "snappy";
            }
            if (deflateQ2 == -1.0f && this.gzipOptions != null) {
                return "gzip";
            }
            if (deflateQ != -1.0f || this.deflateOptions == null) {
                return null;
            }
            return "deflate";
        }
        return null;
    }

    @Deprecated
    protected ZlibWrapper determineWrapper(String acceptEncoding) {
        float starQ = -1.0f;
        float gzipQ = -1.0f;
        float deflateQ = -1.0f;
        for (String encoding : acceptEncoding.split(",")) {
            float q = 1.0f;
            int equalsPos = encoding.indexOf(61);
            if (equalsPos != -1) {
                try {
                    q = Float.parseFloat(encoding.substring(equalsPos + 1));
                } catch (NumberFormatException e) {
                    q = 0.0f;
                }
            }
            if (encoding.contains("*")) {
                starQ = q;
            } else if (encoding.contains("gzip") && q > gzipQ) {
                gzipQ = q;
            } else if (encoding.contains("deflate") && q > deflateQ) {
                deflateQ = q;
            }
        }
        if (gzipQ > 0.0f || deflateQ > 0.0f) {
            if (gzipQ >= deflateQ) {
                return ZlibWrapper.GZIP;
            }
            return ZlibWrapper.ZLIB;
        }
        if (starQ <= 0.0f) {
            return null;
        }
        if (gzipQ == -1.0f) {
            return ZlibWrapper.GZIP;
        }
        if (deflateQ == -1.0f) {
            return ZlibWrapper.ZLIB;
        }
        return null;
    }

    /* loaded from: classes4.dex */
    private final class GzipEncoderFactory implements CompressionEncoderFactory {
        private GzipEncoderFactory() {
        }

        @Override // io.netty.handler.codec.http.CompressionEncoderFactory
        public MessageToByteEncoder<ByteBuf> createEncoder() {
            return ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP, HttpContentCompressor.this.gzipOptions.compressionLevel(), HttpContentCompressor.this.gzipOptions.windowBits(), HttpContentCompressor.this.gzipOptions.memLevel());
        }
    }

    /* loaded from: classes4.dex */
    private final class DeflateEncoderFactory implements CompressionEncoderFactory {
        private DeflateEncoderFactory() {
        }

        @Override // io.netty.handler.codec.http.CompressionEncoderFactory
        public MessageToByteEncoder<ByteBuf> createEncoder() {
            return ZlibCodecFactory.newZlibEncoder(ZlibWrapper.ZLIB, HttpContentCompressor.this.deflateOptions.compressionLevel(), HttpContentCompressor.this.deflateOptions.windowBits(), HttpContentCompressor.this.deflateOptions.memLevel());
        }
    }

    /* loaded from: classes4.dex */
    private final class BrEncoderFactory implements CompressionEncoderFactory {
        private BrEncoderFactory() {
        }

        @Override // io.netty.handler.codec.http.CompressionEncoderFactory
        public MessageToByteEncoder<ByteBuf> createEncoder() {
            return new BrotliEncoder(HttpContentCompressor.this.brotliOptions.parameters());
        }
    }

    /* loaded from: classes4.dex */
    private final class ZstdEncoderFactory implements CompressionEncoderFactory {
        private ZstdEncoderFactory() {
        }

        @Override // io.netty.handler.codec.http.CompressionEncoderFactory
        public MessageToByteEncoder<ByteBuf> createEncoder() {
            return new ZstdEncoder(HttpContentCompressor.this.zstdOptions.compressionLevel(), HttpContentCompressor.this.zstdOptions.blockSize(), HttpContentCompressor.this.zstdOptions.maxEncodeSize());
        }
    }

    /* loaded from: classes4.dex */
    private static final class SnappyEncoderFactory implements CompressionEncoderFactory {
        private SnappyEncoderFactory() {
        }

        @Override // io.netty.handler.codec.http.CompressionEncoderFactory
        public MessageToByteEncoder<ByteBuf> createEncoder() {
            return new SnappyFrameEncoder();
        }
    }
}
