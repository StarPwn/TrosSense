package io.netty.buffer;

import java.nio.ByteBuffer;

@Deprecated
/* loaded from: classes4.dex */
public abstract class AbstractDerivedByteBuf extends AbstractByteBuf {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDerivedByteBuf(int maxCapacity) {
        super(maxCapacity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.buffer.ByteBuf
    public final boolean isAccessible() {
        return isAccessible0();
    }

    boolean isAccessible0() {
        return unwrap().isAccessible();
    }

    @Override // io.netty.util.ReferenceCounted
    public final int refCnt() {
        return refCnt0();
    }

    int refCnt0() {
        return unwrap().refCnt();
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public final ByteBuf retain() {
        return retain0();
    }

    ByteBuf retain0() {
        unwrap().retain();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public final ByteBuf retain(int increment) {
        return retain0(increment);
    }

    ByteBuf retain0(int increment) {
        unwrap().retain(increment);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public final ByteBuf touch() {
        return touch0();
    }

    ByteBuf touch0() {
        unwrap().touch();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public final ByteBuf touch(Object hint) {
        return touch0(hint);
    }

    ByteBuf touch0(Object hint) {
        unwrap().touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release() {
        return release0();
    }

    boolean release0() {
        return unwrap().release();
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release(int decrement) {
        return release0(decrement);
    }

    boolean release0(int decrement) {
        return unwrap().release(decrement);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return unwrap().isReadOnly();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        return nioBuffer(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        return unwrap().nioBuffer(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isContiguous() {
        return unwrap().isContiguous();
    }
}
