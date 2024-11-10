package org.msgpack.value;

import java.nio.ByteBuffer;

/* loaded from: classes5.dex */
public interface RawValue extends Value {
    byte[] asByteArray();

    ByteBuffer asByteBuffer();

    String asString();

    String toString();
}
