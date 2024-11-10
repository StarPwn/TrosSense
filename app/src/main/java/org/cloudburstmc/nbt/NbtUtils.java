package org.cloudburstmc.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.cloudburstmc.nbt.util.stream.LittleEndianDataInputStream;
import org.cloudburstmc.nbt.util.stream.LittleEndianDataOutputStream;
import org.cloudburstmc.nbt.util.stream.NetworkDataInputStream;
import org.cloudburstmc.nbt.util.stream.NetworkDataOutputStream;

/* loaded from: classes5.dex */
public class NbtUtils {
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();
    public static final int MAX_DEPTH = 16;

    private NbtUtils() {
    }

    public static NBTInputStream createReader(InputStream stream, boolean internKeys, boolean internValues) {
        Objects.requireNonNull(stream, "stream");
        return new NBTInputStream(new DataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createReaderLE(InputStream stream, boolean internKeys, boolean internValues) {
        Objects.requireNonNull(stream, "stream");
        return new NBTInputStream(new LittleEndianDataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createGZIPReader(InputStream stream, boolean internKeys, boolean internValues) throws IOException {
        return createReader(new GZIPInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createNetworkReader(InputStream stream, boolean internKeys, boolean internValues) {
        Objects.requireNonNull(stream, "stream");
        return new NBTInputStream(new NetworkDataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createReader(InputStream stream) {
        return createReader(stream, false, false);
    }

    public static NBTInputStream createReaderLE(InputStream stream) {
        return createReaderLE(stream, false, false);
    }

    public static NBTInputStream createGZIPReader(InputStream stream) throws IOException {
        return createGZIPReader(stream, false, false);
    }

    public static NBTInputStream createNetworkReader(InputStream stream) {
        return createNetworkReader(stream, false, false);
    }

    public static NBTOutputStream createWriter(OutputStream stream) {
        Objects.requireNonNull(stream, "stream");
        return new NBTOutputStream(new DataOutputStream(stream));
    }

    public static NBTOutputStream createWriterLE(OutputStream stream) {
        Objects.requireNonNull(stream, "stream");
        return new NBTOutputStream(new LittleEndianDataOutputStream(stream));
    }

    public static NBTOutputStream createGZIPWriter(OutputStream stream) throws IOException {
        return createWriter(new GZIPOutputStream(stream));
    }

    public static NBTOutputStream createNetworkWriter(OutputStream stream) {
        return new NBTOutputStream(new NetworkDataOutputStream(stream));
    }

    public static String toString(Object o) {
        if (o instanceof Byte) {
            return ((int) ((Byte) o).byteValue()) + "b";
        }
        if (o instanceof Short) {
            return ((int) ((Short) o).shortValue()) + "s";
        }
        if (o instanceof Integer) {
            return ((Integer) o).intValue() + "i";
        }
        if (o instanceof Long) {
            return ((Long) o).longValue() + "l";
        }
        if (o instanceof Float) {
            return ((Float) o).floatValue() + "f";
        }
        if (o instanceof Double) {
            return ((Double) o).doubleValue() + "d";
        }
        if (o instanceof byte[]) {
            return "0x" + printHexBinary((byte[]) o);
        }
        if (o instanceof String) {
            return "\"" + o + "\"";
        }
        int i = 0;
        if (o instanceof int[]) {
            StringJoiner joiner = new StringJoiner(", ");
            int[] iArr = (int[]) o;
            int length = iArr.length;
            while (i < length) {
                int i2 = iArr[i];
                joiner.add(i2 + "i");
                i++;
            }
            return "[ " + joiner + " ]";
        }
        if (o instanceof long[]) {
            StringJoiner joiner2 = new StringJoiner(", ");
            long[] jArr = (long[]) o;
            int length2 = jArr.length;
            while (i < length2) {
                long l = jArr[i];
                joiner2.add(l + "l");
                i++;
            }
            return "[ " + joiner2 + " ]";
        }
        return o.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T copy(T t) {
        if (t instanceof byte[]) {
            byte[] bArr = (byte[]) t;
            return (T) Arrays.copyOf(bArr, bArr.length);
        }
        if (t instanceof int[]) {
            int[] iArr = (int[]) t;
            return (T) Arrays.copyOf(iArr, iArr.length);
        }
        if (t instanceof long[]) {
            long[] jArr = (long[]) t;
            return (T) Arrays.copyOf(jArr, jArr.length);
        }
        return t;
    }

    public static String indent(String string) {
        StringBuilder builder = new StringBuilder("  " + string);
        int i = 2;
        while (i < builder.length()) {
            if (builder.charAt(i) == '\n') {
                builder.insert(i + 1, "  ");
                i += 2;
            }
            i++;
        }
        return builder.toString();
    }

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length << 1);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 15]);
            r.append(HEX_CODE[b & 15]);
        }
        return r.toString();
    }
}
