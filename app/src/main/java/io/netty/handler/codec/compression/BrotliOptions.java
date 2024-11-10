package io.netty.handler.codec.compression;

import com.aayushatharva.brotli4j.encoder.Encoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class BrotliOptions implements CompressionOptions {
    static final BrotliOptions DEFAULT = new BrotliOptions(new Encoder.Parameters().setQuality(4).setMode(Encoder.Mode.TEXT));
    private final Encoder.Parameters parameters;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrotliOptions(Encoder.Parameters parameters) {
        if (!Brotli.isAvailable()) {
            throw new IllegalStateException("Brotli is not available", Brotli.cause());
        }
        this.parameters = (Encoder.Parameters) ObjectUtil.checkNotNull(parameters, "Parameters");
    }

    public Encoder.Parameters parameters() {
        return this.parameters;
    }
}
