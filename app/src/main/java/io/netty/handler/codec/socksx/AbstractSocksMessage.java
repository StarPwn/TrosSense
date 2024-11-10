package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class AbstractSocksMessage implements SocksMessage {
    private DecoderResult decoderResult = DecoderResult.SUCCESS;

    @Override // io.netty.handler.codec.DecoderResultProvider
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public void setDecoderResult(DecoderResult decoderResult) {
        this.decoderResult = (DecoderResult) ObjectUtil.checkNotNull(decoderResult, "decoderResult");
    }
}
