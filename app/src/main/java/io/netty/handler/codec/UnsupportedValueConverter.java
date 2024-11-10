package io.netty.handler.codec;

/* loaded from: classes4.dex */
public final class UnsupportedValueConverter<V> implements ValueConverter<V> {
    private static final UnsupportedValueConverter INSTANCE = new UnsupportedValueConverter();

    private UnsupportedValueConverter() {
    }

    public static <V> UnsupportedValueConverter<V> instance() {
        return INSTANCE;
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertObject(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertBoolean(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public boolean convertToBoolean(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertByte(byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public byte convertToByte(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertChar(char value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public char convertToChar(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertShort(short value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public short convertToShort(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertInt(int value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public int convertToInt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertLong(long value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public long convertToLong(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertTimeMillis(long value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public long convertToTimeMillis(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertFloat(float value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public float convertToFloat(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public V convertDouble(double value) {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public double convertToDouble(V value) {
        throw new UnsupportedOperationException();
    }
}
