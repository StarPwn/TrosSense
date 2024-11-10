package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.HpackUtil;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.MathUtil;
import java.util.Map;

/* loaded from: classes4.dex */
final class HpackEncoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int HUFF_CODE_THRESHOLD = 512;
    static final int NOT_FOUND = -1;
    private final byte hashMask;
    private final NameValueEntry head;
    private final HpackHuffmanEncoder hpackHuffmanEncoder;
    private final int huffCodeThreshold;
    private final boolean ignoreMaxHeaderListSize;
    private NameValueEntry latest;
    private long maxHeaderListSize;
    private long maxHeaderTableSize;
    private final NameEntry[] nameEntries;
    private final NameValueEntry[] nameValueEntries;
    private long size;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackEncoder() {
        this(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackEncoder(boolean ignoreMaxHeaderListSize) {
        this(ignoreMaxHeaderListSize, 64, 512);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackEncoder(boolean ignoreMaxHeaderListSize, int arraySizeHint, int huffCodeThreshold) {
        this.head = new NameValueEntry(-1, AsciiString.EMPTY_STRING, AsciiString.EMPTY_STRING, Integer.MAX_VALUE, null);
        this.latest = this.head;
        this.hpackHuffmanEncoder = new HpackHuffmanEncoder();
        this.ignoreMaxHeaderListSize = ignoreMaxHeaderListSize;
        this.maxHeaderTableSize = 4096L;
        this.maxHeaderListSize = 4294967295L;
        this.nameEntries = new NameEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.nameValueEntries = new NameValueEntry[this.nameEntries.length];
        this.hashMask = (byte) (this.nameEntries.length - 1);
        this.huffCodeThreshold = huffCodeThreshold;
    }

    public void encodeHeaders(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        if (this.ignoreMaxHeaderListSize) {
            encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
        } else {
            encodeHeadersEnforceMaxHeaderListSize(streamId, out, headers, sensitivityDetector);
        }
    }

    private void encodeHeadersEnforceMaxHeaderListSize(int streamId, ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        long headerSize = 0;
        for (Map.Entry<CharSequence, CharSequence> header : headers) {
            CharSequence name = header.getKey();
            CharSequence value = header.getValue();
            headerSize += HpackHeaderField.sizeOf(name, value);
            if (headerSize > this.maxHeaderListSize) {
                Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, false);
            }
        }
        encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
    }

    private void encodeHeadersIgnoreMaxHeaderListSize(ByteBuf out, Http2Headers headers, Http2HeadersEncoder.SensitivityDetector sensitivityDetector) {
        for (Map.Entry<CharSequence, CharSequence> header : headers) {
            CharSequence name = header.getKey();
            CharSequence value = header.getValue();
            encodeHeader(out, name, value, sensitivityDetector.isSensitive(name, value), HpackHeaderField.sizeOf(name, value));
        }
    }

    private void encodeHeader(ByteBuf out, CharSequence name, CharSequence value, boolean sensitive, long headerSize) {
        if (sensitive) {
            int nameIndex = getNameIndex(name);
            encodeLiteral(out, name, value, HpackUtil.IndexType.NEVER, nameIndex);
            return;
        }
        if (this.maxHeaderTableSize == 0) {
            int staticTableIndex = HpackStaticTable.getIndexInsensitive(name, value);
            if (staticTableIndex == -1) {
                int nameIndex2 = HpackStaticTable.getIndex(name);
                encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex2);
                return;
            } else {
                encodeInteger(out, 128, 7, staticTableIndex);
                return;
            }
        }
        if (headerSize > this.maxHeaderTableSize) {
            int nameIndex3 = getNameIndex(name);
            encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex3);
            return;
        }
        int nameHash = AsciiString.hashCode(name);
        int valueHash = AsciiString.hashCode(value);
        NameValueEntry headerField = getEntryInsensitive(name, nameHash, value, valueHash);
        if (headerField != null) {
            encodeInteger(out, 128, 7, getIndexPlusOffset(headerField.counter));
            return;
        }
        int staticTableIndex2 = HpackStaticTable.getIndexInsensitive(name, value);
        if (staticTableIndex2 != -1) {
            encodeInteger(out, 128, 7, staticTableIndex2);
            return;
        }
        ensureCapacity(headerSize);
        encodeAndAddEntries(out, name, nameHash, value, valueHash);
        this.size += headerSize;
    }

    private void encodeAndAddEntries(ByteBuf out, CharSequence name, int nameHash, CharSequence value, int valueHash) {
        int staticTableIndex = HpackStaticTable.getIndex(name);
        int nextCounter = latestCounter() - 1;
        if (staticTableIndex == -1) {
            NameEntry e = getEntry(name, nameHash);
            if (e == null) {
                encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, -1);
                addNameEntry(name, nameHash, nextCounter);
                addNameValueEntry(name, value, nameHash, valueHash, nextCounter);
                return;
            } else {
                encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, getIndexPlusOffset(e.counter));
                addNameValueEntry(e.name, value, nameHash, valueHash, nextCounter);
                e.counter = nextCounter;
                return;
            }
        }
        encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, staticTableIndex);
        addNameValueEntry(HpackStaticTable.getEntry(staticTableIndex).name, value, nameHash, valueHash, nextCounter);
    }

    public void setMaxHeaderTableSize(ByteBuf out, long maxHeaderTableSize) throws Http2Exception {
        if (maxHeaderTableSize < 0 || maxHeaderTableSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderTableSize));
        }
        if (this.maxHeaderTableSize == maxHeaderTableSize) {
            return;
        }
        this.maxHeaderTableSize = maxHeaderTableSize;
        ensureCapacity(0L);
        encodeInteger(out, 32, 5, maxHeaderTableSize);
    }

    public long getMaxHeaderTableSize() {
        return this.maxHeaderTableSize;
    }

    public void setMaxHeaderListSize(long maxHeaderListSize) throws Http2Exception {
        if (maxHeaderListSize < 0 || maxHeaderListSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderListSize));
        }
        this.maxHeaderListSize = maxHeaderListSize;
    }

    public long getMaxHeaderListSize() {
        return this.maxHeaderListSize;
    }

    private static void encodeInteger(ByteBuf out, int mask, int n, int i) {
        encodeInteger(out, mask, n, i);
    }

    private static void encodeInteger(ByteBuf out, int mask, int n, long i) {
        if (n < 0 || n > 8) {
            throw new AssertionError("N: " + n);
        }
        int nbits = 255 >>> (8 - n);
        if (i < nbits) {
            out.writeByte((int) (mask | i));
            return;
        }
        out.writeByte(mask | nbits);
        long length = i - nbits;
        while (((-128) & length) != 0) {
            out.writeByte((int) ((127 & length) | 128));
            length >>>= 7;
        }
        out.writeByte((int) length);
    }

    private void encodeStringLiteral(ByteBuf out, CharSequence string) {
        int huffmanLength;
        if (string.length() >= this.huffCodeThreshold && (huffmanLength = this.hpackHuffmanEncoder.getEncodedLength(string)) < string.length()) {
            encodeInteger(out, 128, 7, huffmanLength);
            this.hpackHuffmanEncoder.encode(out, string);
            return;
        }
        encodeInteger(out, 0, 7, string.length());
        if (string instanceof AsciiString) {
            AsciiString asciiString = (AsciiString) string;
            out.writeBytes(asciiString.array(), asciiString.arrayOffset(), asciiString.length());
        } else {
            out.writeCharSequence(string, CharsetUtil.ISO_8859_1);
        }
    }

    private void encodeLiteral(ByteBuf out, CharSequence name, CharSequence value, HpackUtil.IndexType indexType, int nameIndex) {
        boolean nameIndexValid = nameIndex != -1;
        switch (indexType) {
            case INCREMENTAL:
                encodeInteger(out, 64, 6, nameIndexValid ? nameIndex : 0);
                break;
            case NONE:
                encodeInteger(out, 0, 4, nameIndexValid ? nameIndex : 0);
                break;
            case NEVER:
                encodeInteger(out, 16, 4, nameIndexValid ? nameIndex : 0);
                break;
            default:
                throw new Error("should not reach here");
        }
        if (!nameIndexValid) {
            encodeStringLiteral(out, name);
        }
        encodeStringLiteral(out, value);
    }

    private int getNameIndex(CharSequence name) {
        int index = HpackStaticTable.getIndex(name);
        if (index != -1) {
            return index;
        }
        NameEntry e = getEntry(name, AsciiString.hashCode(name));
        if (e == null) {
            return -1;
        }
        return getIndexPlusOffset(e.counter);
    }

    private void ensureCapacity(long headerSize) {
        while (this.maxHeaderTableSize - this.size < headerSize) {
            remove();
        }
    }

    int length() {
        if (isEmpty()) {
            return 0;
        }
        return getIndex(this.head.after.counter);
    }

    long size() {
        return this.size;
    }

    HpackHeaderField getHeaderField(int index) {
        NameValueEntry entry = this.head;
        while (true) {
            int index2 = index + 1;
            if (index < length()) {
                entry = entry.after;
                index = index2;
            } else {
                return entry;
            }
        }
    }

    private NameValueEntry getEntryInsensitive(CharSequence name, int nameHash, CharSequence value, int valueHash) {
        int h = hash(nameHash, valueHash);
        for (NameValueEntry e = this.nameValueEntries[bucket(h)]; e != null; e = e.next) {
            if (e.hash == h && HpackUtil.equalsVariableTime(value, e.value) && HpackUtil.equalsVariableTime(name, e.name)) {
                return e;
            }
        }
        return null;
    }

    private NameEntry getEntry(CharSequence name, int nameHash) {
        for (NameEntry e = this.nameEntries[bucket(nameHash)]; e != null; e = e.next) {
            if (e.hash == nameHash && HpackUtil.equalsConstantTime(name, e.name) != 0) {
                return e;
            }
        }
        return null;
    }

    private int getIndexPlusOffset(int counter) {
        return getIndex(counter) + HpackStaticTable.length;
    }

    private int getIndex(int counter) {
        return (counter - latestCounter()) + 1;
    }

    private int latestCounter() {
        return this.latest.counter;
    }

    private void addNameEntry(CharSequence name, int nameHash, int nextCounter) {
        int bucket = bucket(nameHash);
        this.nameEntries[bucket] = new NameEntry(nameHash, name, nextCounter, this.nameEntries[bucket]);
    }

    private void addNameValueEntry(CharSequence name, CharSequence value, int nameHash, int valueHash, int nextCounter) {
        int hash = hash(nameHash, valueHash);
        int bucket = bucket(hash);
        NameValueEntry e = new NameValueEntry(hash, name, value, nextCounter, this.nameValueEntries[bucket]);
        this.nameValueEntries[bucket] = e;
        this.latest.after = e;
        this.latest = e;
    }

    private void remove() {
        NameValueEntry eldest = this.head.after;
        removeNameValueEntry(eldest);
        removeNameEntryMatchingCounter(eldest.name, eldest.counter);
        this.head.after = eldest.after;
        eldest.unlink();
        this.size -= eldest.size();
        if (isEmpty()) {
            this.latest = this.head;
        }
    }

    private boolean isEmpty() {
        return this.size == 0;
    }

    private void removeNameValueEntry(NameValueEntry eldest) {
        int bucket = bucket(eldest.hash);
        NameValueEntry e = this.nameValueEntries[bucket];
        if (e == eldest) {
            this.nameValueEntries[bucket] = eldest.next;
            return;
        }
        while (e.next != eldest) {
            e = e.next;
        }
        e.next = eldest.next;
    }

    private void removeNameEntryMatchingCounter(CharSequence name, int counter) {
        int hash = AsciiString.hashCode(name);
        int bucket = bucket(hash);
        NameEntry e = this.nameEntries[bucket];
        if (e == null) {
            return;
        }
        if (counter == e.counter) {
            this.nameEntries[bucket] = e.next;
            e.unlink();
            return;
        }
        NameEntry prev = e;
        for (NameEntry e2 = e.next; e2 != null; e2 = e2.next) {
            if (counter == e2.counter) {
                prev.next = e2.next;
                e2.unlink();
                return;
            }
            prev = e2;
        }
    }

    private int bucket(int h) {
        return this.hashMask & h;
    }

    private static int hash(int nameHash, int valueHash) {
        return (nameHash * 31) + valueHash;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class NameEntry {
        int counter;
        final int hash;
        final CharSequence name;
        NameEntry next;

        NameEntry(int hash, CharSequence name, int counter, NameEntry next) {
            this.hash = hash;
            this.name = name;
            this.counter = counter;
            this.next = next;
        }

        void unlink() {
            this.next = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class NameValueEntry extends HpackHeaderField {
        NameValueEntry after;
        final int counter;
        final int hash;
        NameValueEntry next;

        NameValueEntry(int hash, CharSequence name, CharSequence value, int counter, NameValueEntry next) {
            super(name, value);
            this.next = next;
            this.hash = hash;
            this.counter = counter;
        }

        void unlink() {
            this.after = null;
            this.next = null;
        }
    }
}
