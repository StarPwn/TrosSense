package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;

/* loaded from: classes4.dex */
final class HttpPostBodyUtil {
    public static final String DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_TEXT_CONTENT_TYPE = "text/plain";
    public static final int chunkSize = 8096;

    /* loaded from: classes4.dex */
    public enum TransferEncodingMechanism {
        BIT7("7bit"),
        BIT8("8bit"),
        BINARY(HttpHeaders.Values.BINARY);

        private final String value;

        TransferEncodingMechanism(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.value;
        }
    }

    private HttpPostBodyUtil() {
    }

    /* loaded from: classes4.dex */
    static class SeekAheadOptimize {
        ByteBuf buffer;
        byte[] bytes;
        int limit;
        int origPos;
        int pos;
        int readerIndex;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SeekAheadOptimize(ByteBuf buffer) {
            if (!buffer.hasArray()) {
                throw new IllegalArgumentException("buffer hasn't backing byte array");
            }
            this.buffer = buffer;
            this.bytes = buffer.array();
            this.readerIndex = buffer.readerIndex();
            int arrayOffset = buffer.arrayOffset() + this.readerIndex;
            this.pos = arrayOffset;
            this.origPos = arrayOffset;
            this.limit = buffer.arrayOffset() + buffer.writerIndex();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setReadPosition(int minus) {
            this.pos -= minus;
            this.readerIndex = getReadPosition(this.pos);
            this.buffer.readerIndex(this.readerIndex);
        }

        int getReadPosition(int index) {
            return (index - this.origPos) + this.readerIndex;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int findNonWhitespace(String sb, int offset) {
        int result = offset;
        while (result < sb.length() && Character.isWhitespace(sb.charAt(result))) {
            result++;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int findEndOfString(String sb) {
        int result = sb.length();
        while (result > 0 && Character.isWhitespace(sb.charAt(result - 1))) {
            result--;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int findLineBreak(ByteBuf buffer, int index) {
        int toRead = buffer.readableBytes() - (index - buffer.readerIndex());
        int posFirstChar = buffer.bytesBefore(index, toRead, (byte) 10);
        if (posFirstChar == -1) {
            return -1;
        }
        if (posFirstChar > 0 && buffer.getByte((index + posFirstChar) - 1) == 13) {
            return posFirstChar - 1;
        }
        return posFirstChar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int findLastLineBreak(ByteBuf buffer, int index) {
        int candidate = findLineBreak(buffer, index);
        int findCRLF = 0;
        if (candidate >= 0) {
            if (buffer.getByte(index + candidate) == 13) {
                findCRLF = 2;
            } else {
                findCRLF = 1;
            }
            candidate += findCRLF;
        }
        while (candidate > 0) {
            int next = findLineBreak(buffer, index + candidate);
            if (next < 0) {
                break;
            }
            int candidate2 = candidate + next;
            if (buffer.getByte(index + candidate2) == 13) {
                findCRLF = 2;
            } else {
                findCRLF = 1;
            }
            candidate = candidate2 + findCRLF;
        }
        return candidate - findCRLF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int findDelimiter(ByteBuf buffer, int index, byte[] delimiter, boolean precededByLineBreak) {
        int posDelimiter;
        int delimiterLength = delimiter.length;
        int readerIndex = buffer.readerIndex();
        int writerIndex = buffer.writerIndex();
        int toRead = writerIndex - index;
        int newOffset = index;
        boolean delimiterNotFound = true;
        while (delimiterNotFound && delimiterLength <= toRead && (posDelimiter = buffer.bytesBefore(newOffset, toRead, delimiter[0])) >= 0) {
            newOffset += posDelimiter;
            toRead -= posDelimiter;
            if (toRead >= delimiterLength) {
                delimiterNotFound = false;
                int i = 0;
                while (true) {
                    if (i >= delimiterLength) {
                        break;
                    }
                    if (buffer.getByte(newOffset + i) == delimiter[i]) {
                        i++;
                    } else {
                        newOffset++;
                        toRead--;
                        delimiterNotFound = true;
                        break;
                    }
                }
            }
            if (!delimiterNotFound) {
                if (precededByLineBreak && newOffset > readerIndex) {
                    if (buffer.getByte(newOffset - 1) == 10) {
                        newOffset--;
                        if (newOffset > readerIndex && buffer.getByte(newOffset - 1) == 13) {
                            newOffset--;
                        }
                    } else {
                        newOffset++;
                        toRead--;
                        delimiterNotFound = true;
                    }
                }
                return newOffset - readerIndex;
            }
        }
        return -1;
    }
}
