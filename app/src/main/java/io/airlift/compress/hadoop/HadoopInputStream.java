package io.airlift.compress.hadoop;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class HadoopInputStream extends InputStream {
    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public abstract void close() throws IOException;

    @Override // java.io.InputStream
    public abstract int read(byte[] b, int off, int len) throws IOException;

    public abstract void resetState();
}
