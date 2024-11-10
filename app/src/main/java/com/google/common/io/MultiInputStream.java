package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class MultiInputStream extends InputStream {
    private InputStream in;

    /* renamed from: it, reason: collision with root package name */
    private Iterator<? extends ByteSource> f5it;

    public MultiInputStream(Iterator<? extends ByteSource> it2) throws IOException {
        this.f5it = (Iterator) Preconditions.checkNotNull(it2);
        advance();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.f5it.hasNext()) {
            this.in = this.f5it.next().openStream();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read();
        if (result == -1) {
            advance();
            return read();
        }
        return result;
    }

    @Override // java.io.InputStream
    public int read(@Nullable byte[] b, int off, int len) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read(b, off, len);
        if (result == -1) {
            advance();
            return read(b, off, len);
        }
        return result;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (this.in == null || n <= 0) {
            return 0L;
        }
        long result = this.in.skip(n);
        if (result != 0) {
            return result;
        }
        if (read() == -1) {
            return 0L;
        }
        return this.in.skip(n - 1) + 1;
    }
}
