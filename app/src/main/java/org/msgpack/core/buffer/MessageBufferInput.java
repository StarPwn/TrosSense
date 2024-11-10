package org.msgpack.core.buffer;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: classes5.dex */
public interface MessageBufferInput extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    MessageBuffer next() throws IOException;
}
