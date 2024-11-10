package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class PemValue extends AbstractReferenceCounted implements PemEncoded {
    private final ByteBuf content;
    private final boolean sensitive;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PemValue(ByteBuf content, boolean sensitive) {
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
        this.sensitive = sensitive;
    }

    @Override // io.netty.handler.ssl.PemEncoded
    public boolean isSensitive() {
        return this.sensitive;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        int count = refCnt();
        if (count <= 0) {
            throw new IllegalReferenceCountException(count);
        }
        return this.content;
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemValue copy() {
        return replace(this.content.copy());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemValue duplicate() {
        return replace(this.content.duplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemValue retainedDuplicate() {
        return replace(this.content.retainedDuplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemValue replace(ByteBuf content) {
        return new PemValue(content, this.sensitive);
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemValue touch() {
        return (PemValue) super.touch();
    }

    @Override // io.netty.util.ReferenceCounted
    public PemValue touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemValue retain() {
        return (PemValue) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemValue retain(int increment) {
        return (PemValue) super.retain(increment);
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        if (this.sensitive) {
            SslUtils.zeroout(this.content);
        }
        this.content.release();
    }
}
