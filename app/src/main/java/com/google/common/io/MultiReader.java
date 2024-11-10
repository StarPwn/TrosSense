package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
class MultiReader extends Reader {
    private Reader current;

    /* renamed from: it, reason: collision with root package name */
    private final Iterator<? extends CharSource> f6it;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiReader(Iterator<? extends CharSource> readers) throws IOException {
        this.f6it = readers;
        advance();
    }

    private void advance() throws IOException {
        close();
        if (this.f6it.hasNext()) {
            this.current = this.f6it.next().openStream();
        }
    }

    @Override // java.io.Reader
    public int read(@Nullable char[] cbuf, int off, int len) throws IOException {
        if (this.current == null) {
            return -1;
        }
        int result = this.current.read(cbuf, off, len);
        if (result == -1) {
            advance();
            return read(cbuf, off, len);
        }
        return result;
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        Preconditions.checkArgument(n >= 0, "n is negative");
        if (n > 0) {
            while (this.current != null) {
                long result = this.current.skip(n);
                if (result > 0) {
                    return result;
                }
                advance();
            }
        }
        return 0L;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return this.current != null && this.current.ready();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.current != null) {
            try {
                this.current.close();
            } finally {
                this.current = null;
            }
        }
    }
}
