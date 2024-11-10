package io.airlift.compress.hadoop;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public abstract class HadoopOutputStream extends OutputStream {
    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public abstract void finish() throws IOException;

    @Override // java.io.OutputStream, java.io.Flushable
    public abstract void flush() throws IOException;

    @Override // java.io.OutputStream
    public abstract void write(byte[] b, int off, int len) throws IOException;
}
