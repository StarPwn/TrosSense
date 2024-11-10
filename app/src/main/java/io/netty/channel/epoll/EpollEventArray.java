package io.netty.channel.epoll;

import io.netty.channel.unix.Buffer;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

/* loaded from: classes4.dex */
public final class EpollEventArray {
    private int length;
    private ByteBuffer memory;
    private long memoryAddress;
    private static final int EPOLL_EVENT_SIZE = Native.sizeofEpollEvent();
    private static final int EPOLL_DATA_OFFSET = Native.offsetofEpollData();

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollEventArray(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be >= 1 but was " + length);
        }
        this.length = length;
        this.memory = Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(length));
        this.memoryAddress = Buffer.memoryAddress(this.memory);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long memoryAddress() {
        return this.memoryAddress;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int length() {
        return this.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void increase() {
        this.length <<= 1;
        ByteBuffer buffer = Buffer.allocateDirectWithNativeOrder(calculateBufferCapacity(this.length));
        Buffer.free(this.memory);
        this.memory = buffer;
        this.memoryAddress = Buffer.memoryAddress(buffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void free() {
        Buffer.free(this.memory);
        this.memoryAddress = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int events(int index) {
        return getInt(index, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int fd(int index) {
        return getInt(index, EPOLL_DATA_OFFSET);
    }

    private int getInt(int index, int offset) {
        if (PlatformDependent.hasUnsafe()) {
            long n = index * EPOLL_EVENT_SIZE;
            return PlatformDependent.getInt(this.memoryAddress + n + offset);
        }
        return this.memory.getInt((EPOLL_EVENT_SIZE * index) + offset);
    }

    private static int calculateBufferCapacity(int capacity) {
        return EPOLL_EVENT_SIZE * capacity;
    }
}
