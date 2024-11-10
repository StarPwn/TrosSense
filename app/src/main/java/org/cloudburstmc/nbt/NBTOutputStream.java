package org.cloudburstmc.nbt;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.jose4j.jwx.HeaderParameterNames;

/* loaded from: classes5.dex */
public class NBTOutputStream implements Closeable {
    private boolean closed = false;
    private final DataOutput output;

    public NBTOutputStream(DataOutput output) {
        this.output = (DataOutput) Objects.requireNonNull(output, "output");
    }

    public void writeTag(Object tag) throws IOException {
        writeTag(tag, 16);
    }

    public void writeTag(Object tag, int maxDepth) throws IOException {
        Objects.requireNonNull(tag, HeaderParameterNames.AUTHENTICATION_TAG);
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        NbtType<?> type = NbtType.byClass(tag.getClass());
        this.output.writeByte(type.getId());
        this.output.writeUTF("");
        serialize(tag, type, maxDepth);
    }

    public void writeValue(Object tag) throws IOException {
        writeValue(tag, 16);
    }

    public void writeValue(Object tag, int maxDepth) throws IOException {
        Objects.requireNonNull(tag, HeaderParameterNames.AUTHENTICATION_TAG);
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        NbtType<?> type = NbtType.byClass(tag.getClass());
        serialize(tag, type, maxDepth);
    }

    private void serialize(Object tag, NbtType<?> type, int maxDepth) throws IOException {
        if (maxDepth < 0) {
            throw new IllegalArgumentException("Reached depth limit");
        }
        int i = 0;
        switch (type.getEnum()) {
            case END:
            default:
                return;
            case BYTE:
                Byte byteVal = (Byte) tag;
                this.output.writeByte(byteVal.byteValue());
                return;
            case SHORT:
                Short shortVal = (Short) tag;
                this.output.writeShort(shortVal.shortValue());
                return;
            case INT:
                Integer intVal = (Integer) tag;
                this.output.writeInt(intVal.intValue());
                return;
            case LONG:
                Long longVal = (Long) tag;
                this.output.writeLong(longVal.longValue());
                return;
            case FLOAT:
                Float floatVal = (Float) tag;
                this.output.writeFloat(floatVal.floatValue());
                return;
            case DOUBLE:
                Double doubleVal = (Double) tag;
                this.output.writeDouble(doubleVal.doubleValue());
                return;
            case BYTE_ARRAY:
                byte[] byteArray = (byte[]) tag;
                this.output.writeInt(byteArray.length);
                this.output.write(byteArray);
                return;
            case STRING:
                String string = (String) tag;
                this.output.writeUTF(string);
                return;
            case LIST:
                NbtList<?> list = (NbtList) tag;
                NbtType<?> listType = list.getType();
                this.output.writeByte(listType.getId());
                this.output.writeInt(list.size());
                Iterator<?> it2 = list.iterator();
                while (it2.hasNext()) {
                    serialize(it2.next(), listType, maxDepth - 1);
                }
                return;
            case COMPOUND:
                NbtMap map = (NbtMap) tag;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    NbtType<?> entryType = NbtType.byClass(entry.getValue().getClass());
                    this.output.writeByte(entryType.getId());
                    this.output.writeUTF(entry.getKey());
                    serialize(entry.getValue(), entryType, maxDepth - 1);
                }
                this.output.writeByte(0);
                return;
            case INT_ARRAY:
                int[] intArray = (int[]) tag;
                this.output.writeInt(intArray.length);
                int length = intArray.length;
                while (i < length) {
                    int val = intArray[i];
                    this.output.writeInt(val);
                    i++;
                }
                return;
            case LONG_ARRAY:
                long[] longArray = (long[]) tag;
                this.output.writeInt(longArray.length);
                int length2 = longArray.length;
                while (i < length2) {
                    long val2 = longArray[i];
                    this.output.writeLong(val2);
                    i++;
                }
                return;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.output instanceof Closeable) {
            ((Closeable) this.output).close();
        }
    }
}
