package io.netty.buffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class UnpooledDuplicatedByteBuf extends DuplicatedByteBuf {
    /* JADX INFO: Access modifiers changed from: package-private */
    public UnpooledDuplicatedByteBuf(AbstractByteBuf buffer) {
        super(buffer);
    }

    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.ByteBuf
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf) super.unwrap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        return unwrap()._getByte(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        return unwrap()._getShort(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public short _getShortLE(int index) {
        return unwrap()._getShortLE(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        return unwrap()._getUnsignedMedium(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMediumLE(int index) {
        return unwrap()._getUnsignedMediumLE(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        return unwrap()._getInt(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public int _getIntLE(int index) {
        return unwrap()._getIntLE(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        return unwrap()._getLong(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public long _getLongLE(int index) {
        return unwrap()._getLongLE(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setByte(int index, int value) {
        unwrap()._setByte(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setShort(int index, int value) {
        unwrap()._setShort(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setShortLE(int index, int value) {
        unwrap()._setShortLE(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setMedium(int index, int value) {
        unwrap()._setMedium(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setMediumLE(int index, int value) {
        unwrap()._setMediumLE(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setInt(int index, int value) {
        unwrap()._setInt(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setIntLE(int index, int value) {
        unwrap()._setIntLE(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setLong(int index, long value) {
        unwrap()._setLong(index, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.DuplicatedByteBuf, io.netty.buffer.AbstractByteBuf
    public void _setLongLE(int index, long value) {
        unwrap()._setLongLE(index, value);
    }
}
