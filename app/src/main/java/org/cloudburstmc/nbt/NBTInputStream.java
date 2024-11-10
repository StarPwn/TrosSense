package org.cloudburstmc.nbt;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/* loaded from: classes5.dex */
public class NBTInputStream implements Closeable {
    private boolean closed;
    private final DataInput input;
    private final boolean internKeys;
    private final boolean internValues;

    public NBTInputStream(DataInput input, boolean internKeys, boolean internValues) {
        this.closed = false;
        this.input = (DataInput) Objects.requireNonNull(input, "input");
        this.internKeys = internKeys;
        this.internValues = internValues;
    }

    public NBTInputStream(DataInput input) {
        this(input, false, false);
    }

    public Object readTag() throws IOException {
        return readTag(16);
    }

    public Object readTag(int maxDepth) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("Trying to read from a closed reader!");
        }
        int typeId = this.input.readUnsignedByte();
        NbtType<?> type = NbtType.byId(typeId);
        this.input.readUTF();
        return deserialize(type, maxDepth);
    }

    public <T> T readValue(NbtType<T> nbtType) throws IOException {
        return (T) readValue(nbtType, 16);
    }

    public <T> T readValue(NbtType<T> nbtType, int i) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("Trying to read from a closed reader!");
        }
        return (T) deserialize(nbtType, i);
    }

    private Object deserialize(NbtType<?> type, int maxDepth) throws IOException {
        String name;
        if (maxDepth < 0) {
            throw new IllegalArgumentException("NBT compound is too deeply nested");
        }
        switch (type.getEnum()) {
            case END:
                return null;
            case BYTE:
                return Byte.valueOf(this.input.readByte());
            case SHORT:
                return Short.valueOf(this.input.readShort());
            case INT:
                if (this.internValues) {
                    return IntegerInternPool.intern(this.input.readInt());
                }
                return Integer.valueOf(this.input.readInt());
            case LONG:
                return Long.valueOf(this.input.readLong());
            case FLOAT:
                if (this.internValues) {
                    return FloatInternPool.intern(this.input.readFloat());
                }
                return Float.valueOf(this.input.readFloat());
            case DOUBLE:
                return Double.valueOf(this.input.readDouble());
            case BYTE_ARRAY:
                byte[] bytes = new byte[this.input.readInt()];
                this.input.readFully(bytes);
                return bytes;
            case STRING:
                if (this.internValues) {
                    return this.input.readUTF().intern();
                }
                return this.input.readUTF();
            case COMPOUND:
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                while (true) {
                    NbtType<?> nbtType = NbtType.byId(this.input.readUnsignedByte());
                    if (nbtType != NbtType.END) {
                        if (this.internKeys) {
                            name = this.input.readUTF().intern();
                        } else {
                            name = this.input.readUTF();
                        }
                        map.put(name, deserialize(nbtType, maxDepth - 1));
                    } else {
                        return new NbtMap(map);
                    }
                }
            case LIST:
                int typeId = this.input.readUnsignedByte();
                NbtType<?> listType = NbtType.byId(typeId);
                List<? super Object> list = new ArrayList<>();
                int listLength = this.input.readInt();
                for (int i = 0; i < listLength; i++) {
                    list.add(deserialize(listType, maxDepth - 1));
                }
                return new NbtList(listType, list);
            case INT_ARRAY:
                int arraySize = this.input.readInt();
                int[] ints = new int[arraySize];
                for (int i2 = 0; i2 < arraySize; i2++) {
                    ints[i2] = this.input.readInt();
                }
                return ints;
            case LONG_ARRAY:
                int arraySize2 = this.input.readInt();
                long[] longs = new long[arraySize2];
                for (int i3 = 0; i3 < arraySize2; i3++) {
                    longs[i3] = this.input.readLong();
                }
                return longs;
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.input instanceof Closeable) {
            ((Closeable) this.input).close();
        }
    }
}
