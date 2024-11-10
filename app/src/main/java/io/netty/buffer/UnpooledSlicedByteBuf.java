package io.netty.buffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class UnpooledSlicedByteBuf extends AbstractUnpooledSlicedByteBuf {
    /* JADX INFO: Access modifiers changed from: package-private */
    public UnpooledSlicedByteBuf(AbstractByteBuf buffer, int index, int length) {
        super(buffer, index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return maxCapacity();
    }

    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.ByteBuf
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf) super.unwrap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        return unwrap()._getByte(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        return unwrap()._getShort(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public short _getShortLE(int index) {
        return unwrap()._getShortLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        return unwrap()._getUnsignedMedium(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMediumLE(int index) {
        return unwrap()._getUnsignedMediumLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        return unwrap()._getInt(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getIntLE(int index) {
        return unwrap()._getIntLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        return unwrap()._getLong(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public long _getLongLE(int index) {
        return unwrap()._getLongLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setByte(int index, int value) {
        unwrap()._setByte(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setShort(int index, int value) {
        unwrap()._setShort(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setShortLE(int index, int value) {
        unwrap()._setShortLE(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setMedium(int index, int value) {
        unwrap()._setMedium(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setMediumLE(int index, int value) {
        unwrap()._setMediumLE(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setInt(int index, int value) {
        unwrap()._setInt(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setIntLE(int index, int value) {
        unwrap()._setIntLE(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setLong(int index, long value) {
        unwrap()._setLong(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractUnpooledSlicedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setLongLE(int index, long value) {
        unwrap()._setLongLE(idx(index), value);
    }
}
