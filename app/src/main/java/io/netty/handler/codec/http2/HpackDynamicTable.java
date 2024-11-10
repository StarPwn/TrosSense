package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
final class HpackDynamicTable {
    private long capacity = -1;
    int head;
    HpackHeaderField[] hpackHeaderFields;
    private long size;
    int tail;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackDynamicTable(long initialCapacity) {
        setCapacity(initialCapacity);
    }

    public int length() {
        if (this.head < this.tail) {
            int length = (this.hpackHeaderFields.length - this.tail) + this.head;
            return length;
        }
        int length2 = this.head;
        return length2 - this.tail;
    }

    public long size() {
        return this.size;
    }

    public long capacity() {
        return this.capacity;
    }

    public HpackHeaderField getEntry(int index) {
        if (index <= 0 || index > length()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + length());
        }
        int i = this.head - index;
        if (i < 0) {
            return this.hpackHeaderFields[this.hpackHeaderFields.length + i];
        }
        return this.hpackHeaderFields[i];
    }

    public void add(HpackHeaderField header) {
        int headerSize = header.size();
        if (headerSize > this.capacity) {
            clear();
            return;
        }
        while (this.capacity - this.size < headerSize) {
            remove();
        }
        HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
        int i = this.head;
        this.head = i + 1;
        hpackHeaderFieldArr[i] = header;
        this.size += headerSize;
        if (this.head == this.hpackHeaderFields.length) {
            this.head = 0;
        }
    }

    public HpackHeaderField remove() {
        HpackHeaderField removed = this.hpackHeaderFields[this.tail];
        if (removed == null) {
            return null;
        }
        this.size -= removed.size();
        HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
        int i = this.tail;
        this.tail = i + 1;
        hpackHeaderFieldArr[i] = null;
        if (this.tail == this.hpackHeaderFields.length) {
            this.tail = 0;
        }
        return removed;
    }

    public void clear() {
        while (this.tail != this.head) {
            HpackHeaderField[] hpackHeaderFieldArr = this.hpackHeaderFields;
            int i = this.tail;
            this.tail = i + 1;
            hpackHeaderFieldArr[i] = null;
            if (this.tail == this.hpackHeaderFields.length) {
                this.tail = 0;
            }
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0L;
    }

    public void setCapacity(long capacity) {
        if (capacity < 0 || capacity > 4294967295L) {
            throw new IllegalArgumentException("capacity is invalid: " + capacity);
        }
        if (this.capacity == capacity) {
            return;
        }
        this.capacity = capacity;
        if (capacity == 0) {
            clear();
        } else {
            while (this.size > capacity) {
                remove();
            }
        }
        int maxEntries = (int) (capacity / 32);
        if (capacity % 32 != 0) {
            maxEntries++;
        }
        if (this.hpackHeaderFields != null && this.hpackHeaderFields.length == maxEntries) {
            return;
        }
        HpackHeaderField[] tmp = new HpackHeaderField[maxEntries];
        int len = length();
        if (this.hpackHeaderFields != null) {
            int cursor = this.tail;
            for (int i = 0; i < len; i++) {
                int cursor2 = cursor + 1;
                HpackHeaderField entry = this.hpackHeaderFields[cursor];
                tmp[i] = entry;
                if (cursor2 != this.hpackHeaderFields.length) {
                    cursor = cursor2;
                } else {
                    cursor = 0;
                }
            }
        }
        this.tail = 0;
        this.head = this.tail + len;
        this.hpackHeaderFields = tmp;
    }
}
