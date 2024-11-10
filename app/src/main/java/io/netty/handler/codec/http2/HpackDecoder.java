package io.netty.handler.codec.http2;

import com.google.common.base.Ascii;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaderValidationUtil;
import io.netty.handler.codec.http2.HpackUtil;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;
import okio.Utf8;
import org.msgpack.core.MessagePack;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class HpackDecoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final byte READ_HEADER_REPRESENTATION = 0;
    private static final byte READ_INDEXED_HEADER = 1;
    private static final byte READ_INDEXED_HEADER_NAME = 2;
    private static final byte READ_LITERAL_HEADER_NAME = 5;
    private static final byte READ_LITERAL_HEADER_NAME_LENGTH = 4;
    private static final byte READ_LITERAL_HEADER_NAME_LENGTH_PREFIX = 3;
    private static final byte READ_LITERAL_HEADER_VALUE = 8;
    private static final byte READ_LITERAL_HEADER_VALUE_LENGTH = 7;
    private static final byte READ_LITERAL_HEADER_VALUE_LENGTH_PREFIX = 6;
    private long encoderMaxDynamicTableSize;
    private final HpackDynamicTable hpackDynamicTable;
    private final HpackHuffmanDecoder huffmanDecoder;
    private long maxDynamicTableSize;
    private boolean maxDynamicTableSizeChangeRequired;
    private long maxHeaderListSize;
    private static final Http2Exception DECODE_ULE_128_DECOMPRESSION_EXCEPTION = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - decompression failure", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "decodeULE128(..)");
    private static final Http2Exception DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - long overflow", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "decodeULE128(..)");
    private static final Http2Exception DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - int overflow", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "decodeULE128ToInt(..)");
    private static final Http2Exception DECODE_ILLEGAL_INDEX_VALUE = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "decode(..)");
    private static final Http2Exception INDEX_HEADER_ILLEGAL_INDEX_VALUE = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "indexHeader(..)");
    private static final Http2Exception READ_NAME_ILLEGAL_INDEX_VALUE = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "readName(..)");
    private static final Http2Exception INVALID_MAX_DYNAMIC_TABLE_SIZE = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - invalid max dynamic table size", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "setDynamicTableSize(..)");
    private static final Http2Exception MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED = Http2Exception.newStatic(Http2Error.COMPRESSION_ERROR, "HPACK - max dynamic table size change required", Http2Exception.ShutdownHint.HARD_SHUTDOWN, HpackDecoder.class, "decode(..)");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum HeaderType {
        REGULAR_HEADER,
        REQUEST_PSEUDO_HEADER,
        RESPONSE_PSEUDO_HEADER
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackDecoder(long maxHeaderListSize) {
        this(maxHeaderListSize, 4096);
    }

    HpackDecoder(long maxHeaderListSize, int maxHeaderTableSize) {
        this.huffmanDecoder = new HpackHuffmanDecoder();
        this.maxHeaderListSize = ObjectUtil.checkPositive(maxHeaderListSize, "maxHeaderListSize");
        long j = maxHeaderTableSize;
        this.encoderMaxDynamicTableSize = j;
        this.maxDynamicTableSize = j;
        this.maxDynamicTableSizeChangeRequired = false;
        this.hpackDynamicTable = new HpackDynamicTable(maxHeaderTableSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decode(int streamId, ByteBuf in, Http2Headers headers, boolean validateHeaders) throws Http2Exception {
        Http2HeadersSink sink = new Http2HeadersSink(streamId, headers, this.maxHeaderListSize, validateHeaders);
        decodeDynamicTableSizeUpdates(in);
        decode(in, sink);
        sink.finish();
    }

    private void decodeDynamicTableSizeUpdates(ByteBuf in) throws Http2Exception {
        while (in.isReadable()) {
            byte b = in.getByte(in.readerIndex());
            if ((b & 32) == 32 && (b & MessagePack.Code.NIL) == 0) {
                in.readByte();
                int index = b & Ascii.US;
                if (index == 31) {
                    setDynamicTableSize(decodeULE128(in, index));
                } else {
                    setDynamicTableSize(index);
                }
            } else {
                return;
            }
        }
    }

    private void decode(ByteBuf in, Http2HeadersSink sink) throws Http2Exception {
        int index = 0;
        int nameLength = 0;
        int valueLength = 0;
        byte state = 0;
        boolean huffmanEncoded = false;
        AsciiString name = null;
        HpackUtil.IndexType indexType = HpackUtil.IndexType.NONE;
        while (true) {
            if (in.isReadable()) {
                switch (state) {
                    case 0:
                        byte b = in.readByte();
                        if (this.maxDynamicTableSizeChangeRequired && (b & MessagePack.Code.NEGFIXINT_PREFIX) != 32) {
                            throw MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED;
                        }
                        if (b < 0) {
                            index = b & Byte.MAX_VALUE;
                            switch (index) {
                                case 0:
                                    throw DECODE_ILLEGAL_INDEX_VALUE;
                                case 127:
                                    state = 1;
                                    break;
                                default:
                                    HpackHeaderField indexedHeader = getIndexedHeader(index);
                                    sink.appendToHeaderList((AsciiString) indexedHeader.name, (AsciiString) indexedHeader.value);
                                    break;
                            }
                        } else if ((b & 64) == 64) {
                            indexType = HpackUtil.IndexType.INCREMENTAL;
                            index = b & Utf8.REPLACEMENT_BYTE;
                            switch (index) {
                                case 0:
                                    state = 3;
                                    break;
                                case 63:
                                    state = 2;
                                    break;
                                default:
                                    name = readName(index);
                                    nameLength = name.length();
                                    state = 6;
                                    break;
                            }
                        } else {
                            if ((b & 32) == 32) {
                                throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "Dynamic table size update must happen at the beginning of the header block", new Object[0]);
                            }
                            indexType = (b & 16) == 16 ? HpackUtil.IndexType.NEVER : HpackUtil.IndexType.NONE;
                            index = b & 15;
                            switch (index) {
                                case 0:
                                    state = 3;
                                    break;
                                case 15:
                                    state = 2;
                                    break;
                                default:
                                    name = readName(index);
                                    nameLength = name.length();
                                    state = 6;
                                    break;
                            }
                        }
                    case 1:
                        HpackHeaderField indexedHeader2 = getIndexedHeader(decodeULE128(in, index));
                        sink.appendToHeaderList((AsciiString) indexedHeader2.name, (AsciiString) indexedHeader2.value);
                        state = 0;
                        break;
                    case 2:
                        name = readName(decodeULE128(in, index));
                        nameLength = name.length();
                        state = 6;
                        break;
                    case 3:
                        byte b2 = in.readByte();
                        huffmanEncoded = (b2 & 128) == 128;
                        index = b2 & Byte.MAX_VALUE;
                        if (index == 127) {
                            state = 4;
                            break;
                        } else {
                            nameLength = index;
                            state = 5;
                            break;
                        }
                    case 4:
                        nameLength = decodeULE128(in, index);
                        state = 5;
                        break;
                    case 5:
                        if (in.readableBytes() < nameLength) {
                            throw notEnoughDataException(in);
                        }
                        name = readStringLiteral(in, nameLength, huffmanEncoded);
                        state = 6;
                        break;
                    case 6:
                        byte b3 = in.readByte();
                        huffmanEncoded = (b3 & 128) == 128;
                        index = b3 & Byte.MAX_VALUE;
                        switch (index) {
                            case 0:
                                insertHeader(sink, name, AsciiString.EMPTY_STRING, indexType);
                                state = 0;
                                break;
                            case 127:
                                state = 7;
                                break;
                            default:
                                valueLength = index;
                                state = 8;
                                break;
                        }
                    case 7:
                        valueLength = decodeULE128(in, index);
                        state = 8;
                        break;
                    case 8:
                        if (in.readableBytes() < valueLength) {
                            throw notEnoughDataException(in);
                        }
                        AsciiString value = readStringLiteral(in, valueLength, huffmanEncoded);
                        insertHeader(sink, name, value, indexType);
                        state = 0;
                        break;
                    default:
                        throw new Error("should not reach here state: " + ((int) state));
                }
            } else {
                if (state != 0) {
                    throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "Incomplete header block fragment.", new Object[0]);
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMaxHeaderTableSize(long maxHeaderTableSize) throws Http2Exception {
        if (maxHeaderTableSize < 0 || maxHeaderTableSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderTableSize));
        }
        this.maxDynamicTableSize = maxHeaderTableSize;
        if (this.maxDynamicTableSize < this.encoderMaxDynamicTableSize) {
            this.maxDynamicTableSizeChangeRequired = true;
            this.hpackDynamicTable.setCapacity(this.maxDynamicTableSize);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMaxHeaderListSize(long maxHeaderListSize) throws Http2Exception {
        if (maxHeaderListSize < 0 || maxHeaderListSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", 0L, 4294967295L, Long.valueOf(maxHeaderListSize));
        }
        this.maxHeaderListSize = maxHeaderListSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getMaxHeaderListSize() {
        return this.maxHeaderListSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getMaxHeaderTableSize() {
        return this.hpackDynamicTable.capacity();
    }

    int length() {
        return this.hpackDynamicTable.length();
    }

    long size() {
        return this.hpackDynamicTable.size();
    }

    HpackHeaderField getHeaderField(int index) {
        return this.hpackDynamicTable.getEntry(index + 1);
    }

    private void setDynamicTableSize(long dynamicTableSize) throws Http2Exception {
        if (dynamicTableSize > this.maxDynamicTableSize) {
            throw INVALID_MAX_DYNAMIC_TABLE_SIZE;
        }
        this.encoderMaxDynamicTableSize = dynamicTableSize;
        this.maxDynamicTableSizeChangeRequired = false;
        this.hpackDynamicTable.setCapacity(dynamicTableSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HeaderType validateHeader(int streamId, AsciiString name, CharSequence value, HeaderType previousHeaderType) throws Http2Exception {
        if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
            if (previousHeaderType == HeaderType.REGULAR_HEADER) {
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Pseudo-header field '%s' found after regular header.", name);
            }
            Http2Headers.PseudoHeaderName pseudoHeader = Http2Headers.PseudoHeaderName.getPseudoHeader(name);
            HeaderType currentHeaderType = pseudoHeader.isRequestOnly() ? HeaderType.REQUEST_PSEUDO_HEADER : HeaderType.RESPONSE_PSEUDO_HEADER;
            if (previousHeaderType != null && currentHeaderType != previousHeaderType) {
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Mix of request and response pseudo-headers.", new Object[0]);
            }
            return currentHeaderType;
        }
        if (HttpHeaderValidationUtil.isConnectionHeader(name, true)) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Illegal connection-specific header '%s' encountered.", name);
        }
        if (HttpHeaderValidationUtil.isTeNotTrailers(name, value)) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Illegal value specified for the 'TE' header (only 'trailers' is allowed).", new Object[0]);
        }
        return HeaderType.REGULAR_HEADER;
    }

    private AsciiString readName(int index) throws Http2Exception {
        if (index <= HpackStaticTable.length) {
            HpackHeaderField hpackHeaderField = HpackStaticTable.getEntry(index);
            return (AsciiString) hpackHeaderField.name;
        }
        if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
            HpackHeaderField hpackHeaderField2 = this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
            return (AsciiString) hpackHeaderField2.name;
        }
        throw READ_NAME_ILLEGAL_INDEX_VALUE;
    }

    private HpackHeaderField getIndexedHeader(int index) throws Http2Exception {
        if (index <= HpackStaticTable.length) {
            return HpackStaticTable.getEntry(index);
        }
        if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
            return this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
        }
        throw INDEX_HEADER_ILLEGAL_INDEX_VALUE;
    }

    private void insertHeader(Http2HeadersSink sink, AsciiString name, AsciiString value, HpackUtil.IndexType indexType) {
        sink.appendToHeaderList(name, value);
        switch (indexType) {
            case NONE:
            case NEVER:
                return;
            case INCREMENTAL:
                this.hpackDynamicTable.add(new HpackHeaderField(name, value));
                return;
            default:
                throw new Error("should not reach here");
        }
    }

    private AsciiString readStringLiteral(ByteBuf in, int length, boolean huffmanEncoded) throws Http2Exception {
        if (huffmanEncoded) {
            return this.huffmanDecoder.decode(in, length);
        }
        byte[] buf = new byte[length];
        in.readBytes(buf);
        return new AsciiString(buf, false);
    }

    private static IllegalArgumentException notEnoughDataException(ByteBuf in) {
        return new IllegalArgumentException("decode only works with an entire header block! " + in);
    }

    static int decodeULE128(ByteBuf in, int result) throws Http2Exception {
        int readerIndex = in.readerIndex();
        long v = decodeULE128(in, result);
        if (v > 2147483647L) {
            in.readerIndex(readerIndex);
            throw DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION;
        }
        return (int) v;
    }

    static long decodeULE128(ByteBuf in, long result) throws Http2Exception {
        if (result > 127 || result < 0) {
            throw new AssertionError();
        }
        boolean resultStartedAtZero = result == 0;
        int writerIndex = in.writerIndex();
        int readerIndex = in.readerIndex();
        int shift = 0;
        while (readerIndex < writerIndex) {
            byte b = in.getByte(readerIndex);
            if (shift == 56 && ((b & 128) != 0 || (b == Byte.MAX_VALUE && !resultStartedAtZero))) {
                throw DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
            }
            if ((b & 128) != 0) {
                result += (b & 127) << shift;
                readerIndex++;
                shift += 7;
            } else {
                in.readerIndex(readerIndex + 1);
                return ((127 & b) << shift) + result;
            }
        }
        throw DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Http2HeadersSink {
        private boolean exceededMaxLength;
        private final Http2Headers headers;
        private long headersLength;
        private final long maxHeaderListSize;
        private HeaderType previousType;
        private final int streamId;
        private final boolean validateHeaders;
        private Http2Exception validationException;

        Http2HeadersSink(int streamId, Http2Headers headers, long maxHeaderListSize, boolean validateHeaders) {
            this.headers = headers;
            this.maxHeaderListSize = maxHeaderListSize;
            this.streamId = streamId;
            this.validateHeaders = validateHeaders;
        }

        void finish() throws Http2Exception {
            if (this.exceededMaxLength) {
                Http2CodecUtil.headerListSizeExceeded(this.streamId, this.maxHeaderListSize, true);
            } else if (this.validationException != null) {
                throw this.validationException;
            }
        }

        void appendToHeaderList(AsciiString name, AsciiString value) {
            this.headersLength += HpackHeaderField.sizeOf(name, value);
            this.exceededMaxLength |= this.headersLength > this.maxHeaderListSize;
            if (this.exceededMaxLength || this.validationException != null) {
                return;
            }
            try {
                this.headers.add((Http2Headers) name, value);
                if (this.validateHeaders) {
                    this.previousType = HpackDecoder.validateHeader(this.streamId, name, value, this.previousType);
                }
            } catch (Http2Exception ex) {
                this.validationException = Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, ex, ex.getMessage(), new Object[0]);
            } catch (IllegalArgumentException ex2) {
                this.validationException = Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, ex2, "Validation failed for header '%s': %s", name, ex2.getMessage());
            }
        }
    }
}
