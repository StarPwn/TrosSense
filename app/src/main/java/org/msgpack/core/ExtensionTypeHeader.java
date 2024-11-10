package org.msgpack.core;

import com.google.common.base.Ascii;

/* loaded from: classes5.dex */
public class ExtensionTypeHeader {
    private final int length;
    private final byte type;

    public ExtensionTypeHeader(byte b, int i) {
        Preconditions.checkArgument(i >= 0, "length must be >= 0");
        this.type = b;
        this.length = i;
    }

    public static byte checkedCastToByte(int i) {
        Preconditions.checkArgument(-128 <= i && i <= 127, "Extension type code must be within the range of byte");
        return (byte) i;
    }

    public byte getType() {
        return this.type;
    }

    public boolean isTimestampType() {
        return this.type == -1;
    }

    public int getLength() {
        return this.length;
    }

    public int hashCode() {
        return ((this.type + Ascii.US) * 31) + this.length;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExtensionTypeHeader)) {
            return false;
        }
        ExtensionTypeHeader extensionTypeHeader = (ExtensionTypeHeader) obj;
        return this.type == extensionTypeHeader.type && this.length == extensionTypeHeader.length;
    }

    public String toString() {
        return String.format("ExtensionTypeHeader(type:%d, length:%,d)", Byte.valueOf(this.type), Integer.valueOf(this.length));
    }
}
