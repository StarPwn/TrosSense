package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultHttpObject implements HttpObject {
    private static final int HASH_CODE_PRIME = 31;
    private DecoderResult decoderResult = DecoderResult.SUCCESS;

    @Override // io.netty.handler.codec.DecoderResultProvider
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    @Override // io.netty.handler.codec.http.HttpObject
    @Deprecated
    public DecoderResult getDecoderResult() {
        return decoderResult();
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public void setDecoderResult(DecoderResult decoderResult) {
        this.decoderResult = (DecoderResult) ObjectUtil.checkNotNull(decoderResult, "decoderResult");
    }

    public int hashCode() {
        int result = (1 * 31) + this.decoderResult.hashCode();
        return result;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpObject)) {
            return false;
        }
        DefaultHttpObject other = (DefaultHttpObject) o;
        return decoderResult().equals(other.decoderResult());
    }
}
