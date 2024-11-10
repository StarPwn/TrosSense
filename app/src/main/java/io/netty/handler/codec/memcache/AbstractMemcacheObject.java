package io.netty.handler.codec.memcache;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class AbstractMemcacheObject extends AbstractReferenceCounted implements MemcacheObject {
    private DecoderResult decoderResult = DecoderResult.SUCCESS;

    @Override // io.netty.handler.codec.DecoderResultProvider
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public void setDecoderResult(DecoderResult result) {
        this.decoderResult = (DecoderResult) ObjectUtil.checkNotNull(result, "DecoderResult should not be null.");
    }
}
