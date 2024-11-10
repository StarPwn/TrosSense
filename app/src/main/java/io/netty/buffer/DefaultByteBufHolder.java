package io.netty.buffer;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultByteBufHolder implements ByteBufHolder {
    private final ByteBuf data;

    public DefaultByteBufHolder(ByteBuf data) {
        this.data = (ByteBuf) ObjectUtil.checkNotNull(data, "data");
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(this.data);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBufHolder copy() {
        return replace(this.data.copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBufHolder duplicate() {
        return replace(this.data.duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBufHolder retainedDuplicate() {
        return replace(this.data.retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBufHolder replace(ByteBuf content) {
        return new DefaultByteBufHolder(content);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.data.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public ByteBufHolder retain() {
        this.data.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public ByteBufHolder retain(int increment) {
        this.data.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public ByteBufHolder touch() {
        this.data.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public ByteBufHolder touch(Object hint) {
        this.data.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.data.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.data.release(decrement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String contentToString() {
        return this.data.toString();
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + contentToString() + ')';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && getClass() == o.getClass()) {
            return this.data.equals(((DefaultByteBufHolder) o).data);
        }
        return false;
    }

    public int hashCode() {
        return this.data.hashCode();
    }
}
