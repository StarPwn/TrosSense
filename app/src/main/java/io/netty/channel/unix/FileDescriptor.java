package io.netty.channel.unix;

import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: classes4.dex */
public class FileDescriptor {
    private static final int STATE_ALL_MASK = 7;
    private static final int STATE_CLOSED_MASK = 1;
    private static final int STATE_INPUT_SHUTDOWN_MASK = 2;
    private static final int STATE_OUTPUT_SHUTDOWN_MASK = 4;
    private static final AtomicIntegerFieldUpdater<FileDescriptor> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(FileDescriptor.class, "state");
    final int fd;
    volatile int state;

    private static native int close(int i);

    private static native long newPipe();

    private static native int open(String str);

    private static native int read(int i, ByteBuffer byteBuffer, int i2, int i3);

    private static native int readAddress(int i, long j, int i2, int i3);

    private static native int write(int i, ByteBuffer byteBuffer, int i2, int i3);

    private static native int writeAddress(int i, long j, int i2, int i3);

    private static native long writev(int i, ByteBuffer[] byteBufferArr, int i2, int i3, long j);

    private static native long writevAddresses(int i, long j, int i2);

    public FileDescriptor(int fd) {
        ObjectUtil.checkPositiveOrZero(fd, "fd");
        this.fd = fd;
    }

    public final int intValue() {
        return this.fd;
    }

    protected boolean markClosed() {
        int state;
        do {
            state = this.state;
            if (isClosed(state)) {
                return false;
            }
        } while (!casState(state, state | 7));
        return true;
    }

    public void close() throws IOException {
        int res;
        if (markClosed() && (res = close(this.fd)) < 0) {
            throw Errors.newIOException("close", res);
        }
    }

    public boolean isOpen() {
        return !isClosed(this.state);
    }

    public final int write(ByteBuffer buf, int pos, int limit) throws IOException {
        int res = write(this.fd, buf, pos, limit);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("write", res);
    }

    public final int writeAddress(long address, int pos, int limit) throws IOException {
        int res = writeAddress(this.fd, address, pos, limit);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("writeAddress", res);
    }

    public final long writev(ByteBuffer[] buffers, int offset, int length, long maxBytesToWrite) throws IOException {
        long res = writev(this.fd, buffers, offset, Math.min(Limits.IOV_MAX, length), maxBytesToWrite);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("writev", (int) res);
    }

    public final long writevAddresses(long memoryAddress, int length) throws IOException {
        long res = writevAddresses(this.fd, memoryAddress, length);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("writevAddresses", (int) res);
    }

    public final int read(ByteBuffer buf, int pos, int limit) throws IOException {
        int res = read(this.fd, buf, pos, limit);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        return Errors.ioResult("read", res);
    }

    public final int readAddress(long address, int pos, int limit) throws IOException {
        int res = readAddress(this.fd, address, pos, limit);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        return Errors.ioResult("readAddress", res);
    }

    public String toString() {
        return "FileDescriptor{fd=" + this.fd + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof FileDescriptor) && this.fd == ((FileDescriptor) o).fd;
    }

    public int hashCode() {
        return this.fd;
    }

    public static FileDescriptor from(String path) throws IOException {
        int res = open((String) ObjectUtil.checkNotNull(path, "path"));
        if (res < 0) {
            throw Errors.newIOException("open", res);
        }
        return new FileDescriptor(res);
    }

    public static FileDescriptor from(File file) throws IOException {
        return from(((File) ObjectUtil.checkNotNull(file, "file")).getPath());
    }

    public static FileDescriptor[] pipe() throws IOException {
        long res = newPipe();
        if (res < 0) {
            throw Errors.newIOException("newPipe", (int) res);
        }
        return new FileDescriptor[]{new FileDescriptor((int) (res >>> 32)), new FileDescriptor((int) res)};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean casState(int expected, int update) {
        return stateUpdater.compareAndSet(this, expected, update);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isClosed(int state) {
        return (state & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInputShutdown(int state) {
        return (state & 2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isOutputShutdown(int state) {
        return (state & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int inputShutdown(int state) {
        return state | 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int outputShutdown(int state) {
        return state | 4;
    }
}
