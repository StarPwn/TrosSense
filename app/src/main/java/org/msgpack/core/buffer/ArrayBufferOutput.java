package org.msgpack.core.buffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes5.dex */
public class ArrayBufferOutput implements MessageBufferOutput {
    private final int bufferSize;
    private MessageBuffer lastBuffer;
    private final List<MessageBuffer> list;

    public ArrayBufferOutput() {
        this(8192);
    }

    public ArrayBufferOutput(int i) {
        this.bufferSize = i;
        this.list = new ArrayList();
    }

    public int getSize() {
        Iterator<MessageBuffer> it2 = this.list.iterator();
        int i = 0;
        while (it2.hasNext()) {
            i += it2.next().size();
        }
        return i;
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[getSize()];
        int i = 0;
        for (MessageBuffer messageBuffer : this.list) {
            messageBuffer.getBytes(0, bArr, i, messageBuffer.size());
            i += messageBuffer.size();
        }
        return bArr;
    }

    public MessageBuffer toMessageBuffer() {
        if (this.list.size() == 1) {
            return this.list.get(0);
        }
        if (this.list.isEmpty()) {
            return MessageBuffer.allocate(0);
        }
        return MessageBuffer.wrap(toByteArray());
    }

    public List<MessageBuffer> toBufferList() {
        return new ArrayList(this.list);
    }

    public void clear() {
        this.list.clear();
    }

    @Override // org.msgpack.core.buffer.MessageBufferOutput
    public MessageBuffer next(int i) {
        if (this.lastBuffer != null && this.lastBuffer.size() > i) {
            return this.lastBuffer;
        }
        MessageBuffer allocate = MessageBuffer.allocate(Math.max(this.bufferSize, i));
        this.lastBuffer = allocate;
        return allocate;
    }

    @Override // org.msgpack.core.buffer.MessageBufferOutput
    public void writeBuffer(int i) {
        this.list.add(this.lastBuffer.slice(0, i));
        if (this.lastBuffer.size() - i > this.bufferSize / 4) {
            this.lastBuffer = this.lastBuffer.slice(i, this.lastBuffer.size() - i);
        } else {
            this.lastBuffer = null;
        }
    }

    @Override // org.msgpack.core.buffer.MessageBufferOutput
    public void write(byte[] bArr, int i, int i2) {
        MessageBuffer allocate = MessageBuffer.allocate(i2);
        allocate.putBytes(0, bArr, i, i2);
        this.list.add(allocate);
    }

    @Override // org.msgpack.core.buffer.MessageBufferOutput
    public void add(byte[] bArr, int i, int i2) {
        this.list.add(MessageBuffer.wrap(bArr, i, i2));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.Flushable
    public void flush() {
    }
}
