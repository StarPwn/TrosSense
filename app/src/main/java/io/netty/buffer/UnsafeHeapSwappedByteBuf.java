package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
final class UnsafeHeapSwappedByteBuf extends AbstractUnsafeSwappedByteBuf {
    /* JADX INFO: Access modifiers changed from: package-private */
    public UnsafeHeapSwappedByteBuf(AbstractByteBuf buf) {
        super(buf);
    }

    private static int idx(ByteBuf wrapped, int index) {
        return wrapped.arrayOffset() + index;
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected long _getLong(AbstractByteBuf wrapped, int index) {
        return PlatformDependent.getLong(wrapped.array(), idx(wrapped, index));
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected int _getInt(AbstractByteBuf wrapped, int index) {
        return PlatformDependent.getInt(wrapped.array(), idx(wrapped, index));
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected short _getShort(AbstractByteBuf wrapped, int index) {
        return PlatformDependent.getShort(wrapped.array(), idx(wrapped, index));
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected void _setShort(AbstractByteBuf wrapped, int index, short value) {
        PlatformDependent.putShort(wrapped.array(), idx(wrapped, index), value);
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected void _setInt(AbstractByteBuf wrapped, int index, int value) {
        PlatformDependent.putInt(wrapped.array(), idx(wrapped, index), value);
    }

    @Override // io.netty.buffer.AbstractUnsafeSwappedByteBuf
    protected void _setLong(AbstractByteBuf wrapped, int index, long value) {
        PlatformDependent.putLong(wrapped.array(), idx(wrapped, index), value);
    }
}
