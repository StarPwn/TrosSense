package io.netty.buffer;

import androidx.core.view.ViewCompat;
import com.trossense.bl;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.Recycler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SWARUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Locale;
import kotlin.UShort;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
public final class ByteBufUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;
    private static final ByteProcessor FIND_NON_ASCII;
    private static final int MAX_CHAR_BUFFER_SIZE;
    static final int MAX_TL_ARRAY_LEN = 1024;
    private static final int THREAD_LOCAL_BUFFER_SIZE;
    static final int WRITE_CHUNK_SIZE = 8192;
    private static final byte WRITE_UTF_UNKNOWN = 63;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ByteBufUtil.class);
    private static final FastThreadLocal<byte[]> BYTE_ARRAYS = new FastThreadLocal<byte[]>() { // from class: io.netty.buffer.ByteBufUtil.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public byte[] initialValue() throws Exception {
            return PlatformDependent.allocateUninitializedArray(1024);
        }
    };
    private static final int MAX_BYTES_PER_CHAR_UTF8 = (int) CharsetUtil.encoder(CharsetUtil.UTF_8).maxBytesPerChar();

    static {
        ByteBufAllocator alloc;
        String allocType = SystemPropertyUtil.get("io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled").toLowerCase(Locale.US).trim();
        if ("unpooled".equals(allocType)) {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else if ("pooled".equals(allocType)) {
            alloc = PooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else {
            alloc = PooledByteBufAllocator.DEFAULT;
            logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
        }
        DEFAULT_ALLOCATOR = alloc;
        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 0);
        logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", Integer.valueOf(THREAD_LOCAL_BUFFER_SIZE));
        MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16384);
        logger.debug("-Dio.netty.maxThreadLocalCharBufferSize: {}", Integer.valueOf(MAX_CHAR_BUFFER_SIZE));
        FIND_NON_ASCII = new ByteProcessor() { // from class: io.netty.buffer.ByteBufUtil.2
            @Override // io.netty.util.ByteProcessor
            public boolean process(byte value) {
                return value >= 0;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] threadLocalTempArray(int minLength) {
        return minLength <= 1024 ? BYTE_ARRAYS.get() : PlatformDependent.allocateUninitializedArray(minLength);
    }

    public static boolean isAccessible(ByteBuf buffer) {
        return buffer.isAccessible();
    }

    public static ByteBuf ensureAccessible(ByteBuf buffer) {
        if (!buffer.isAccessible()) {
            throw new IllegalReferenceCountException(buffer.refCnt());
        }
        return buffer;
    }

    public static String hexDump(ByteBuf buffer) {
        return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    public static String hexDump(ByteBuf buffer, int fromIndex, int length) {
        return HexUtil.hexDump(buffer, fromIndex, length);
    }

    public static String hexDump(byte[] array) {
        return hexDump(array, 0, array.length);
    }

    public static String hexDump(byte[] array, int fromIndex, int length) {
        return HexUtil.hexDump(array, fromIndex, length);
    }

    public static byte decodeHexByte(CharSequence s, int pos) {
        return StringUtil.decodeHexByte(s, pos);
    }

    public static byte[] decodeHexDump(CharSequence hexDump) {
        return StringUtil.decodeHexDump(hexDump, 0, hexDump.length());
    }

    public static byte[] decodeHexDump(CharSequence hexDump, int fromIndex, int length) {
        return StringUtil.decodeHexDump(hexDump, fromIndex, length);
    }

    public static boolean ensureWritableSuccess(int ensureWritableResult) {
        return ensureWritableResult == 0 || ensureWritableResult == 2;
    }

    public static int hashCode(ByteBuf buffer) {
        int aLen = buffer.readableBytes();
        int intCount = aLen >>> 2;
        int byteCount = aLen & 3;
        int hashCode = 1;
        int arrayIndex = buffer.readerIndex();
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = intCount; i > 0; i--) {
                hashCode = (hashCode * 31) + buffer.getInt(arrayIndex);
                arrayIndex += 4;
            }
        } else {
            for (int i2 = intCount; i2 > 0; i2--) {
                hashCode = (hashCode * 31) + swapInt(buffer.getInt(arrayIndex));
                arrayIndex += 4;
            }
        }
        int i3 = byteCount;
        while (i3 > 0) {
            hashCode = (hashCode * 31) + buffer.getByte(arrayIndex);
            i3--;
            arrayIndex++;
        }
        if (hashCode == 0) {
            return 1;
        }
        return hashCode;
    }

    public static int indexOf(ByteBuf needle, ByteBuf haystack) {
        int length;
        long suffixes;
        if (haystack == null || needle == null || needle.readableBytes() > haystack.readableBytes()) {
            return -1;
        }
        int n = haystack.readableBytes();
        int m = needle.readableBytes();
        if (m == 0) {
            return 0;
        }
        if (m == 1) {
            return haystack.indexOf(haystack.readerIndex(), haystack.writerIndex(), needle.getByte(needle.readerIndex()));
        }
        int aStartIndex = needle.readerIndex();
        int bStartIndex = haystack.readerIndex();
        long suffixes2 = maxSuf(needle, m, aStartIndex, true);
        long prefixes = maxSuf(needle, m, aStartIndex, false);
        int ell = Math.max((int) (suffixes2 >> 32), (int) (prefixes >> 32));
        int per = Math.max((int) suffixes2, (int) prefixes);
        int length2 = Math.min(m - per, ell + 1);
        if (!equals(needle, aStartIndex, needle, aStartIndex + per, length2)) {
            int per2 = Math.max(ell + 1, (m - ell) - 1) + 1;
            int j = 0;
            while (j <= n - m) {
                int i = ell + 1;
                while (i < m && needle.getByte(i + aStartIndex) == haystack.getByte(i + j + bStartIndex)) {
                    i++;
                }
                if (i > n) {
                    return -1;
                }
                if (i >= m) {
                    int i2 = ell;
                    while (i2 >= 0 && needle.getByte(i2 + aStartIndex) == haystack.getByte(i2 + j + bStartIndex)) {
                        i2--;
                    }
                    if (i2 < 0) {
                        return j + bStartIndex;
                    }
                    j += per2;
                } else {
                    j += i - ell;
                }
            }
            return -1;
        }
        int memory = -1;
        int j2 = 0;
        while (j2 <= n - m) {
            int i3 = Math.max(ell, memory) + 1;
            while (true) {
                if (i3 < m) {
                    length = length2;
                    suffixes = suffixes2;
                    if (needle.getByte(i3 + aStartIndex) != haystack.getByte(i3 + j2 + bStartIndex)) {
                        break;
                    }
                    i3++;
                    length2 = length;
                    suffixes2 = suffixes;
                } else {
                    length = length2;
                    suffixes = suffixes2;
                    break;
                }
            }
            if (i3 > n) {
                return -1;
            }
            if (i3 >= m) {
                int i4 = ell;
                while (i4 > memory && needle.getByte(i4 + aStartIndex) == haystack.getByte(i4 + j2 + bStartIndex)) {
                    i4--;
                }
                if (i4 <= memory) {
                    return j2 + bStartIndex;
                }
                j2 += per;
                memory = (m - per) - 1;
                length2 = length;
                suffixes2 = suffixes;
            } else {
                j2 += i3 - ell;
                memory = -1;
                length2 = length;
                suffixes2 = suffixes;
            }
        }
        return -1;
    }

    private static long maxSuf(ByteBuf x, int m, int start, boolean isSuffix) {
        int p = 1;
        int ms = -1;
        int j = start;
        int k = 1;
        while (j + k < m) {
            byte a = x.getByte(j + k);
            byte b = x.getByte(ms + k);
            boolean suffix = false;
            if (!isSuffix ? a > b : a < b) {
                suffix = true;
            }
            if (suffix) {
                j += k;
                k = 1;
                p = j - ms;
            } else if (a == b) {
                if (k != p) {
                    k++;
                } else {
                    j += p;
                    k = 1;
                }
            } else {
                ms = j;
                j = ms + 1;
                p = 1;
                k = 1;
            }
        }
        return (ms << 32) + p;
    }

    public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length) {
        ObjectUtil.checkNotNull(a, "a");
        ObjectUtil.checkNotNull(b, "b");
        ObjectUtil.checkPositiveOrZero(aStartIndex, "aStartIndex");
        ObjectUtil.checkPositiveOrZero(bStartIndex, "bStartIndex");
        ObjectUtil.checkPositiveOrZero(length, "length");
        if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
            return false;
        }
        int longCount = length >>> 3;
        int byteCount = length & 7;
        if (a.order() == b.order()) {
            for (int i = longCount; i > 0; i--) {
                if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        } else {
            for (int i2 = longCount; i2 > 0; i2--) {
                if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        }
        for (int i3 = byteCount; i3 > 0; i3--) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            aStartIndex++;
            bStartIndex++;
        }
        return true;
    }

    public static boolean equals(ByteBuf bufferA, ByteBuf bufferB) {
        if (bufferA == bufferB) {
            return true;
        }
        int aLen = bufferA.readableBytes();
        if (aLen != bufferB.readableBytes()) {
            return false;
        }
        return equals(bufferA, bufferA.readerIndex(), bufferB, bufferB.readerIndex(), aLen);
    }

    public static int compare(ByteBuf bufferA, ByteBuf bufferB) {
        long res;
        if (bufferA == bufferB) {
            return 0;
        }
        int aLen = bufferA.readableBytes();
        int bLen = bufferB.readableBytes();
        int minLength = Math.min(aLen, bLen);
        int uintCount = minLength >>> 2;
        int byteCount = minLength & 3;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();
        if (uintCount > 0) {
            boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
            int uintCountIncrement = uintCount << 2;
            if (bufferA.order() == bufferB.order()) {
                res = bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) : compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            } else if (bufferAIsBigEndian) {
                res = compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            } else {
                res = compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            }
            if (res != 0) {
                return (int) Math.min(2147483647L, Math.max(-2147483648L, res));
            }
            aIndex += uintCountIncrement;
            bIndex += uintCountIncrement;
        }
        int aEnd = aIndex + byteCount;
        while (aIndex < aEnd) {
            int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
            if (comp == 0) {
                aIndex++;
                bIndex++;
            } else {
                return comp;
            }
        }
        int aEnd2 = aLen - bLen;
        return aEnd2;
    }

    private static long compareUintBigEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintLittleEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long comp = uintFromLE(bufferA.getUnsignedIntLE(aIndex)) - uintFromLE(bufferB.getUnsignedIntLE(bIndex));
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintBigEndianA(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long a = bufferA.getUnsignedInt(aIndex);
            long b = uintFromLE(bufferB.getUnsignedIntLE(bIndex));
            long comp = a - b;
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long compareUintBigEndianB(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        int aEnd = aIndex + uintCountIncrement;
        while (aIndex < aEnd) {
            long a = uintFromLE(bufferA.getUnsignedIntLE(aIndex));
            long b = bufferB.getUnsignedInt(bIndex);
            long comp = a - b;
            if (comp == 0) {
                aIndex += 4;
                bIndex += 4;
            } else {
                return comp;
            }
        }
        return 0L;
    }

    private static long uintFromLE(long value) {
        return Long.reverseBytes(value) >>> 32;
    }

    private static int unrolledFirstIndexOf(AbstractByteBuf buffer, int fromIndex, int byteCount, byte value) {
        if (byteCount <= 0 || byteCount >= 8) {
            throw new AssertionError();
        }
        if (buffer._getByte(fromIndex) == value) {
            return fromIndex;
        }
        if (byteCount == 1) {
            return -1;
        }
        if (buffer._getByte(fromIndex + 1) == value) {
            return fromIndex + 1;
        }
        if (byteCount == 2) {
            return -1;
        }
        if (buffer._getByte(fromIndex + 2) == value) {
            return fromIndex + 2;
        }
        if (byteCount == 3) {
            return -1;
        }
        if (buffer._getByte(fromIndex + 3) == value) {
            return fromIndex + 3;
        }
        if (byteCount == 4) {
            return -1;
        }
        if (buffer._getByte(fromIndex + 4) == value) {
            return fromIndex + 4;
        }
        if (byteCount == 5) {
            return -1;
        }
        if (buffer._getByte(fromIndex + 5) == value) {
            return fromIndex + 5;
        }
        if (byteCount != 6 && buffer._getByte(fromIndex + 6) == value) {
            return fromIndex + 6;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int firstIndexOf(AbstractByteBuf buffer, int fromIndex, int toIndex, byte value) {
        AbstractByteBuf abstractByteBuf = buffer;
        int fromIndex2 = Math.max(fromIndex, 0);
        if (fromIndex2 < toIndex && buffer.capacity() != 0) {
            int length = toIndex - fromIndex2;
            abstractByteBuf.checkIndex(fromIndex2, length);
            if (!PlatformDependent.isUnaligned()) {
                return linearFirstIndexOf(abstractByteBuf, fromIndex2, toIndex, value);
            }
            if (!PlatformDependent.isUnaligned()) {
                throw new AssertionError();
            }
            int offset = fromIndex2;
            int byteCount = length & 7;
            if (byteCount > 0) {
                int index = unrolledFirstIndexOf(abstractByteBuf, fromIndex2, byteCount, value);
                if (index != -1) {
                    return index;
                }
                offset += byteCount;
                if (offset == toIndex) {
                    return -1;
                }
            }
            int index2 = length >>> 3;
            ByteOrder nativeOrder = ByteOrder.nativeOrder();
            boolean isNative = nativeOrder == buffer.order();
            boolean useLE = nativeOrder == ByteOrder.LITTLE_ENDIAN;
            long pattern = SWARUtil.compilePattern(value);
            int i = 0;
            while (i < index2) {
                long word = useLE ? abstractByteBuf._getLongLE(offset) : abstractByteBuf._getLong(offset);
                int length2 = length;
                long result = SWARUtil.applyPattern(word, pattern);
                if (result == 0) {
                    offset += 8;
                    i++;
                    abstractByteBuf = buffer;
                    length = length2;
                } else {
                    return SWARUtil.getIndex(result, isNative) + offset;
                }
            }
            return -1;
        }
        return -1;
    }

    private static int linearFirstIndexOf(AbstractByteBuf buffer, int fromIndex, int toIndex, byte value) {
        for (int i = fromIndex; i < toIndex; i++) {
            if (buffer._getByte(i) == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
        return buffer.indexOf(fromIndex, toIndex, value);
    }

    public static short swapShort(short value) {
        return Short.reverseBytes(value);
    }

    public static int swapMedium(int value) {
        int swapped = ((value << 16) & 16711680) | (65280 & value) | ((value >>> 16) & 255);
        if ((8388608 & swapped) != 0) {
            return swapped | ViewCompat.MEASURED_STATE_MASK;
        }
        return swapped;
    }

    public static int swapInt(int value) {
        return Integer.reverseBytes(value);
    }

    public static long swapLong(long value) {
        return Long.reverseBytes(value);
    }

    public static ByteBuf writeShortBE(ByteBuf buf, int shortValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeShort(shortValue) : buf.writeShort(swapShort((short) shortValue));
    }

    public static ByteBuf setShortBE(ByteBuf buf, int index, int shortValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.setShort(index, shortValue) : buf.setShort(index, swapShort((short) shortValue));
    }

    public static ByteBuf writeMediumBE(ByteBuf buf, int mediumValue) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.writeMedium(mediumValue) : buf.writeMedium(swapMedium(mediumValue));
    }

    public static int readUnsignedShortBE(ByteBuf buf) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.readUnsignedShort() : swapShort((short) buf.readUnsignedShort()) & UShort.MAX_VALUE;
    }

    public static int readIntBE(ByteBuf buf) {
        return buf.order() == ByteOrder.BIG_ENDIAN ? buf.readInt() : swapInt(buf.readInt());
    }

    public static ByteBuf readBytes(ByteBufAllocator alloc, ByteBuf buffer, int length) {
        boolean release = true;
        ByteBuf dst = alloc.buffer(length);
        try {
            buffer.readBytes(dst);
            release = false;
            return dst;
        } finally {
            if (release) {
                dst.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int lastIndexOf(AbstractByteBuf buffer, int fromIndex, int toIndex, byte value) {
        if (fromIndex <= toIndex) {
            throw new AssertionError();
        }
        int capacity = buffer.capacity();
        int fromIndex2 = Math.min(fromIndex, capacity);
        if (fromIndex2 <= 0) {
            return -1;
        }
        int length = fromIndex2 - toIndex;
        buffer.checkIndex(toIndex, length);
        if (!PlatformDependent.isUnaligned()) {
            return linearLastIndexOf(buffer, fromIndex2, toIndex, value);
        }
        int longCount = length >>> 3;
        if (longCount > 0) {
            ByteOrder nativeOrder = ByteOrder.nativeOrder();
            boolean isNative = nativeOrder == buffer.order();
            boolean useLE = nativeOrder == ByteOrder.LITTLE_ENDIAN;
            long pattern = SWARUtil.compilePattern(value);
            int i = 0;
            int offset = fromIndex2 - 8;
            while (i < longCount) {
                long word = useLE ? buffer._getLongLE(offset) : buffer._getLong(offset);
                long result = SWARUtil.applyPattern(word, pattern);
                if (result == 0) {
                    i++;
                    offset -= 8;
                } else {
                    int i2 = (offset + 8) - 1;
                    int capacity2 = SWARUtil.getIndex(result, !isNative);
                    return i2 - capacity2;
                }
            }
        }
        return unrolledLastIndexOf(buffer, fromIndex2 - (longCount << 3), length & 7, value);
    }

    private static int linearLastIndexOf(AbstractByteBuf buffer, int fromIndex, int toIndex, byte value) {
        for (int i = fromIndex - 1; i >= toIndex; i--) {
            if (buffer._getByte(i) == value) {
                return i;
            }
        }
        return -1;
    }

    private static int unrolledLastIndexOf(AbstractByteBuf buffer, int fromIndex, int byteCount, byte value) {
        if (byteCount < 0 || byteCount >= 8) {
            throw new AssertionError();
        }
        if (byteCount == 0) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 1) == value) {
            return fromIndex - 1;
        }
        if (byteCount == 1) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 2) == value) {
            return fromIndex - 2;
        }
        if (byteCount == 2) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 3) == value) {
            return fromIndex - 3;
        }
        if (byteCount == 3) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 4) == value) {
            return fromIndex - 4;
        }
        if (byteCount == 4) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 5) == value) {
            return fromIndex - 5;
        }
        if (byteCount == 5) {
            return -1;
        }
        if (buffer._getByte(fromIndex - 6) == value) {
            return fromIndex - 6;
        }
        if (byteCount == 6 || buffer._getByte(fromIndex - 7) != value) {
            return -1;
        }
        return fromIndex - 7;
    }

    private static CharSequence checkCharSequenceBounds(CharSequence seq, int start, int end) {
        if (MathUtil.isOutOfBounds(start, end - start, seq.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= seq.length(" + seq.length() + ')');
        }
        return seq;
    }

    public static ByteBuf writeUtf8(ByteBufAllocator alloc, CharSequence seq) {
        ByteBuf buf = alloc.buffer(utf8MaxBytes(seq));
        writeUtf8(buf, seq);
        return buf;
    }

    public static int writeUtf8(ByteBuf buf, CharSequence seq) {
        int seqLength = seq.length();
        return reserveAndWriteUtf8Seq(buf, seq, 0, seqLength, utf8MaxBytes(seqLength));
    }

    public static int writeUtf8(ByteBuf buf, CharSequence seq, int start, int end) {
        checkCharSequenceBounds(seq, start, end);
        return reserveAndWriteUtf8Seq(buf, seq, start, end, utf8MaxBytes(end - start));
    }

    public static int reserveAndWriteUtf8(ByteBuf buf, CharSequence seq, int reserveBytes) {
        return reserveAndWriteUtf8Seq(buf, seq, 0, seq.length(), reserveBytes);
    }

    public static int reserveAndWriteUtf8(ByteBuf buf, CharSequence seq, int start, int end, int reserveBytes) {
        return reserveAndWriteUtf8Seq(buf, checkCharSequenceBounds(seq, start, end), start, end, reserveBytes);
    }

    private static int reserveAndWriteUtf8Seq(ByteBuf buf, CharSequence seq, int start, int end, int reserveBytes) {
        while (true) {
            if (buf instanceof WrappedCompositeByteBuf) {
                buf = buf.unwrap();
            } else {
                if (buf instanceof AbstractByteBuf) {
                    AbstractByteBuf byteBuf = (AbstractByteBuf) buf;
                    byteBuf.ensureWritable0(reserveBytes);
                    int written = writeUtf8(byteBuf, byteBuf.writerIndex, reserveBytes, seq, start, end);
                    byteBuf.writerIndex += written;
                    return written;
                }
                if (buf instanceof WrappedByteBuf) {
                    buf = buf.unwrap();
                } else {
                    byte[] bytes = seq.subSequence(start, end).toString().getBytes(CharsetUtil.UTF_8);
                    buf.writeBytes(bytes);
                    return bytes.length;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int writeUtf8(AbstractByteBuf buffer, int writerIndex, int reservedBytes, CharSequence seq, int len) {
        return writeUtf8(buffer, writerIndex, reservedBytes, seq, 0, len);
    }

    static int writeUtf8(AbstractByteBuf buffer, int writerIndex, int reservedBytes, CharSequence seq, int start, int end) {
        if (seq instanceof AsciiString) {
            writeAsciiString(buffer, writerIndex, (AsciiString) seq, start, end);
            return end - start;
        }
        if (PlatformDependent.hasUnsafe()) {
            if (buffer.hasArray()) {
                return unsafeWriteUtf8(buffer.array(), PlatformDependent.byteArrayBaseOffset(), buffer.arrayOffset() + writerIndex, seq, start, end);
            }
            if (buffer.hasMemoryAddress()) {
                return unsafeWriteUtf8(null, buffer.memoryAddress(), writerIndex, seq, start, end);
            }
        } else {
            if (buffer.hasArray()) {
                return safeArrayWriteUtf8(buffer.array(), buffer.arrayOffset() + writerIndex, seq, start, end);
            }
            if (buffer.isDirect()) {
                if (buffer.nioBufferCount() != 1) {
                    throw new AssertionError();
                }
                ByteBuffer internalDirectBuffer = buffer.internalNioBuffer(writerIndex, reservedBytes);
                int bufferPosition = internalDirectBuffer.position();
                return safeDirectWriteUtf8(internalDirectBuffer, bufferPosition, seq, start, end);
            }
        }
        return safeWriteUtf8(buffer, writerIndex, seq, start, end);
    }

    static void writeAsciiString(AbstractByteBuf buffer, int writerIndex, AsciiString seq, int start, int end) {
        int begin = seq.arrayOffset() + start;
        int length = end - start;
        if (PlatformDependent.hasUnsafe()) {
            if (buffer.hasArray()) {
                PlatformDependent.copyMemory(seq.array(), begin, buffer.array(), buffer.arrayOffset() + writerIndex, length);
                return;
            } else if (buffer.hasMemoryAddress()) {
                PlatformDependent.copyMemory(seq.array(), begin, buffer.memoryAddress() + writerIndex, length);
                return;
            }
        }
        if (buffer.hasArray()) {
            System.arraycopy(seq.array(), begin, buffer.array(), buffer.arrayOffset() + writerIndex, length);
        } else {
            buffer.setBytes(writerIndex, seq.array(), begin, length);
        }
    }

    private static int safeDirectWriteUtf8(ByteBuffer buffer, int writerIndex, CharSequence seq, int start, int end) {
        if (seq instanceof AsciiString) {
            throw new AssertionError();
        }
        int i = start;
        while (true) {
            if (i >= end) {
                break;
            }
            char c = seq.charAt(i);
            if (c < 128) {
                buffer.put(writerIndex, (byte) c);
                writerIndex++;
            } else if (c < 2048) {
                int writerIndex2 = writerIndex + 1;
                buffer.put(writerIndex, (byte) ((c >> 6) | bl.cq));
                writerIndex = writerIndex2 + 1;
                buffer.put(writerIndex2, (byte) (128 | (c & '?')));
            } else {
                if (StringUtil.isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        buffer.put(writerIndex, (byte) 63);
                        writerIndex++;
                    } else {
                        i++;
                        if (i == end) {
                            buffer.put(writerIndex, (byte) 63);
                            writerIndex++;
                            break;
                        }
                        char c2 = seq.charAt(i);
                        if (!Character.isLowSurrogate(c2)) {
                            int writerIndex3 = writerIndex + 1;
                            buffer.put(writerIndex, (byte) 63);
                            writerIndex = writerIndex3 + 1;
                            buffer.put(writerIndex3, Character.isHighSurrogate(c2) ? (byte) 63 : (byte) c2);
                        } else {
                            int codePoint = Character.toCodePoint(c, c2);
                            int writerIndex4 = writerIndex + 1;
                            buffer.put(writerIndex, (byte) ((codePoint >> 18) | bl.db));
                            int writerIndex5 = writerIndex4 + 1;
                            buffer.put(writerIndex4, (byte) (((codePoint >> 12) & 63) | 128));
                            int writerIndex6 = writerIndex5 + 1;
                            buffer.put(writerIndex5, (byte) ((63 & (codePoint >> 6)) | 128));
                            writerIndex = writerIndex6 + 1;
                            buffer.put(writerIndex6, (byte) (128 | (codePoint & 63)));
                        }
                    }
                } else {
                    int writerIndex7 = writerIndex + 1;
                    buffer.put(writerIndex, (byte) ((c >> '\f') | bl.cW));
                    int writerIndex8 = writerIndex7 + 1;
                    buffer.put(writerIndex7, (byte) ((63 & (c >> 6)) | 128));
                    buffer.put(writerIndex8, (byte) (128 | (c & '?')));
                    writerIndex = writerIndex8 + 1;
                }
            }
            i++;
        }
        int i2 = writerIndex - writerIndex;
        return i2;
    }

    private static int safeWriteUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int start, int end) {
        if (seq instanceof AsciiString) {
            throw new AssertionError();
        }
        int i = start;
        while (true) {
            if (i >= end) {
                break;
            }
            char c = seq.charAt(i);
            if (c < 128) {
                buffer._setByte(writerIndex, (byte) c);
                writerIndex++;
            } else if (c < 2048) {
                int writerIndex2 = writerIndex + 1;
                buffer._setByte(writerIndex, (byte) ((c >> 6) | bl.cq));
                writerIndex = writerIndex2 + 1;
                buffer._setByte(writerIndex2, (byte) (128 | (c & '?')));
            } else {
                if (StringUtil.isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        buffer._setByte(writerIndex, 63);
                        writerIndex++;
                    } else {
                        i++;
                        if (i == end) {
                            buffer._setByte(writerIndex, 63);
                            writerIndex++;
                            break;
                        }
                        char c2 = seq.charAt(i);
                        if (!Character.isLowSurrogate(c2)) {
                            int writerIndex3 = writerIndex + 1;
                            buffer._setByte(writerIndex, 63);
                            writerIndex = writerIndex3 + 1;
                            buffer._setByte(writerIndex3, Character.isHighSurrogate(c2) ? '?' : c2);
                        } else {
                            int codePoint = Character.toCodePoint(c, c2);
                            int writerIndex4 = writerIndex + 1;
                            buffer._setByte(writerIndex, (byte) ((codePoint >> 18) | bl.db));
                            int writerIndex5 = writerIndex4 + 1;
                            buffer._setByte(writerIndex4, (byte) (((codePoint >> 12) & 63) | 128));
                            int writerIndex6 = writerIndex5 + 1;
                            buffer._setByte(writerIndex5, (byte) ((63 & (codePoint >> 6)) | 128));
                            writerIndex = writerIndex6 + 1;
                            buffer._setByte(writerIndex6, (byte) (128 | (codePoint & 63)));
                        }
                    }
                } else {
                    int writerIndex7 = writerIndex + 1;
                    buffer._setByte(writerIndex, (byte) ((c >> '\f') | bl.cW));
                    int writerIndex8 = writerIndex7 + 1;
                    buffer._setByte(writerIndex7, (byte) ((63 & (c >> 6)) | 128));
                    buffer._setByte(writerIndex8, (byte) (128 | (c & '?')));
                    writerIndex = writerIndex8 + 1;
                }
            }
            i++;
        }
        int i2 = writerIndex - writerIndex;
        return i2;
    }

    private static int safeArrayWriteUtf8(byte[] buffer, int writerIndex, CharSequence seq, int start, int end) {
        int i = start;
        while (true) {
            if (i >= end) {
                break;
            }
            char c = seq.charAt(i);
            if (c < 128) {
                buffer[writerIndex] = (byte) c;
                writerIndex++;
            } else if (c < 2048) {
                int writerIndex2 = writerIndex + 1;
                buffer[writerIndex] = (byte) ((c >> 6) | bl.cq);
                writerIndex = writerIndex2 + 1;
                buffer[writerIndex2] = (byte) (128 | (c & '?'));
            } else {
                if (StringUtil.isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        buffer[writerIndex] = 63;
                        writerIndex++;
                    } else {
                        i++;
                        if (i == end) {
                            buffer[writerIndex] = 63;
                            writerIndex++;
                            break;
                        }
                        char c2 = seq.charAt(i);
                        if (!Character.isLowSurrogate(c2)) {
                            int writerIndex3 = writerIndex + 1;
                            buffer[writerIndex] = 63;
                            writerIndex = writerIndex3 + 1;
                            buffer[writerIndex3] = (byte) (Character.isHighSurrogate(c2) ? '?' : c2);
                        } else {
                            int codePoint = Character.toCodePoint(c, c2);
                            int writerIndex4 = writerIndex + 1;
                            buffer[writerIndex] = (byte) ((codePoint >> 18) | bl.db);
                            int writerIndex5 = writerIndex4 + 1;
                            buffer[writerIndex4] = (byte) (((codePoint >> 12) & 63) | 128);
                            int writerIndex6 = writerIndex5 + 1;
                            buffer[writerIndex5] = (byte) ((63 & (codePoint >> 6)) | 128);
                            writerIndex = writerIndex6 + 1;
                            buffer[writerIndex6] = (byte) (128 | (codePoint & 63));
                        }
                    }
                } else {
                    int writerIndex7 = writerIndex + 1;
                    buffer[writerIndex] = (byte) ((c >> '\f') | bl.cW);
                    int writerIndex8 = writerIndex7 + 1;
                    buffer[writerIndex7] = (byte) ((63 & (c >> 6)) | 128);
                    buffer[writerIndex8] = (byte) (128 | (c & '?'));
                    writerIndex = writerIndex8 + 1;
                }
            }
            i++;
        }
        int i2 = writerIndex - writerIndex;
        return i2;
    }

    private static int unsafeWriteUtf8(byte[] buffer, long memoryOffset, int writerIndex, CharSequence seq, int start, int end) {
        long writerOffset;
        CharSequence charSequence = seq;
        int i = end;
        if (charSequence instanceof AsciiString) {
            throw new AssertionError();
        }
        long writerOffset2 = memoryOffset + writerIndex;
        int i2 = start;
        while (true) {
            if (i2 >= i) {
                break;
            }
            char c = charSequence.charAt(i2);
            if (c < 128) {
                writerOffset = 1 + writerOffset2;
                PlatformDependent.putByte(buffer, writerOffset2, (byte) c);
            } else if (c < 2048) {
                long writerOffset3 = writerOffset2 + 1;
                PlatformDependent.putByte(buffer, writerOffset2, (byte) ((c >> 6) | bl.cq));
                writerOffset = 1 + writerOffset3;
                PlatformDependent.putByte(buffer, writerOffset3, (byte) ((c & '?') | 128));
            } else {
                if (StringUtil.isSurrogate(c)) {
                    if (!Character.isHighSurrogate(c)) {
                        writerOffset = 1 + writerOffset2;
                        PlatformDependent.putByte(buffer, writerOffset2, (byte) 63);
                    } else {
                        i2++;
                        if (i2 == i) {
                            PlatformDependent.putByte(buffer, writerOffset2, (byte) 63);
                            writerOffset2 = 1 + writerOffset2;
                            break;
                        }
                        char c2 = charSequence.charAt(i2);
                        if (!Character.isLowSurrogate(c2)) {
                            long writerOffset4 = writerOffset2 + 1;
                            PlatformDependent.putByte(buffer, writerOffset2, (byte) 63);
                            writerOffset = 1 + writerOffset4;
                            PlatformDependent.putByte(buffer, writerOffset4, (byte) (Character.isHighSurrogate(c2) ? '?' : c2));
                        } else {
                            int codePoint = Character.toCodePoint(c, c2);
                            long writerOffset5 = writerOffset2 + 1;
                            PlatformDependent.putByte(buffer, writerOffset2, (byte) ((codePoint >> 18) | bl.db));
                            long writerOffset6 = writerOffset5 + 1;
                            PlatformDependent.putByte(buffer, writerOffset5, (byte) (((codePoint >> 12) & 63) | 128));
                            long writerOffset7 = writerOffset6 + 1;
                            PlatformDependent.putByte(buffer, writerOffset6, (byte) (((codePoint >> 6) & 63) | 128));
                            writerOffset = 1 + writerOffset7;
                            PlatformDependent.putByte(buffer, writerOffset7, (byte) ((codePoint & 63) | 128));
                        }
                    }
                } else {
                    long writerOffset8 = writerOffset2 + 1;
                    PlatformDependent.putByte(buffer, writerOffset2, (byte) ((c >> '\f') | bl.cW));
                    long writerOffset9 = writerOffset8 + 1;
                    PlatformDependent.putByte(buffer, writerOffset8, (byte) (((c >> 6) & 63) | 128));
                    writerOffset = 1 + writerOffset9;
                    PlatformDependent.putByte(buffer, writerOffset9, (byte) ((c & '?') | 128));
                }
            }
            writerOffset2 = writerOffset;
            i2++;
            charSequence = seq;
            i = end;
        }
        return (int) (writerOffset2 - writerOffset2);
    }

    public static int utf8MaxBytes(int seqLength) {
        return MAX_BYTES_PER_CHAR_UTF8 * seqLength;
    }

    public static int utf8MaxBytes(CharSequence seq) {
        if (seq instanceof AsciiString) {
            return seq.length();
        }
        return utf8MaxBytes(seq.length());
    }

    public static int utf8Bytes(CharSequence seq) {
        return utf8ByteCount(seq, 0, seq.length());
    }

    public static int utf8Bytes(CharSequence seq, int start, int end) {
        return utf8ByteCount(checkCharSequenceBounds(seq, start, end), start, end);
    }

    private static int utf8ByteCount(CharSequence seq, int start, int end) {
        if (seq instanceof AsciiString) {
            return end - start;
        }
        int i = start;
        while (i < end && seq.charAt(i) < 128) {
            i++;
        }
        int i2 = i - start;
        return i < end ? i2 + utf8BytesNonAscii(seq, i, end) : i2;
    }

    private static int utf8BytesNonAscii(CharSequence seq, int start, int end) {
        int encodedLength = 0;
        int i = start;
        while (i < end) {
            char c = seq.charAt(i);
            if (c < 2048) {
                encodedLength += ((127 - c) >>> 31) + 1;
            } else if (StringUtil.isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    encodedLength++;
                } else {
                    i++;
                    if (i == end) {
                        return encodedLength + 1;
                    }
                    if (!Character.isLowSurrogate(seq.charAt(i))) {
                        encodedLength += 2;
                    } else {
                        encodedLength += 4;
                    }
                }
            } else {
                encodedLength += 3;
            }
            i++;
        }
        return encodedLength;
    }

    public static ByteBuf writeAscii(ByteBufAllocator alloc, CharSequence seq) {
        ByteBuf buf = alloc.buffer(seq.length());
        writeAscii(buf, seq);
        return buf;
    }

    public static int writeAscii(ByteBuf buf, CharSequence seq) {
        while (true) {
            if (buf instanceof WrappedCompositeByteBuf) {
                buf = buf.unwrap();
            } else {
                if (buf instanceof AbstractByteBuf) {
                    int len = seq.length();
                    AbstractByteBuf byteBuf = (AbstractByteBuf) buf;
                    byteBuf.ensureWritable0(len);
                    if (seq instanceof AsciiString) {
                        writeAsciiString(byteBuf, byteBuf.writerIndex, (AsciiString) seq, 0, len);
                    } else {
                        int written = writeAscii(byteBuf, byteBuf.writerIndex, seq, len);
                        if (written != len) {
                            throw new AssertionError();
                        }
                    }
                    int written2 = byteBuf.writerIndex;
                    byteBuf.writerIndex = written2 + len;
                    return len;
                }
                if (buf instanceof WrappedByteBuf) {
                    buf = buf.unwrap();
                } else {
                    byte[] bytes = seq.toString().getBytes(CharsetUtil.US_ASCII);
                    buf.writeBytes(bytes);
                    return bytes.length;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int writeAscii(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {
        if (seq instanceof AsciiString) {
            writeAsciiString(buffer, writerIndex, (AsciiString) seq, 0, len);
        } else {
            writeAsciiCharSequence(buffer, writerIndex, seq, len);
        }
        return len;
    }

    private static int writeAsciiCharSequence(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {
        int i = 0;
        while (i < len) {
            buffer._setByte(writerIndex, AsciiString.c2b(seq.charAt(i)));
            i++;
            writerIndex++;
        }
        return len;
    }

    public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset) {
        return encodeString0(alloc, false, src, charset, 0);
    }

    public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset, int extraCapacity) {
        return encodeString0(alloc, false, src, charset, extraCapacity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf encodeString0(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset, int extraCapacity) {
        ByteBuf dst;
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        int length = ((int) (src.remaining() * encoder.maxBytesPerChar())) + extraCapacity;
        boolean release = true;
        if (enforceHeap) {
            dst = alloc.heapBuffer(length);
        } else {
            dst = alloc.buffer(length);
        }
        try {
            try {
                ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
                int pos = dstBuf.position();
                CoderResult cr = encoder.encode(src, dstBuf, true);
                if (!cr.isUnderflow()) {
                    cr.throwException();
                }
                CoderResult cr2 = encoder.flush(dstBuf);
                if (!cr2.isUnderflow()) {
                    cr2.throwException();
                }
                dst.writerIndex((dst.writerIndex() + dstBuf.position()) - pos);
                release = false;
                return dst;
            } catch (CharacterCodingException x) {
                throw new IllegalStateException(x);
            }
        } finally {
            if (release) {
                dst.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeString(ByteBuf src, int readerIndex, int len, Charset charset) {
        byte[] array;
        int offset;
        if (len == 0) {
            return "";
        }
        if (src.hasArray()) {
            array = src.array();
            offset = src.arrayOffset() + readerIndex;
        } else {
            array = threadLocalTempArray(len);
            offset = 0;
            src.getBytes(readerIndex, array, 0, len);
        }
        if (CharsetUtil.US_ASCII.equals(charset)) {
            return new String(array, 0, offset, len);
        }
        return new String(array, offset, len, charset);
    }

    public static ByteBuf threadLocalDirectBuffer() {
        if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }
        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        }
        return ThreadLocalDirectByteBuf.newInstance();
    }

    public static byte[] getBytes(ByteBuf buf) {
        return getBytes(buf, buf.readerIndex(), buf.readableBytes());
    }

    public static byte[] getBytes(ByteBuf buf, int start, int length) {
        return getBytes(buf, start, length, true);
    }

    public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy) {
        int capacity = buf.capacity();
        if (MathUtil.isOutOfBounds(start, length, capacity)) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= buf.capacity(" + capacity + ')');
        }
        if (buf.hasArray()) {
            int baseOffset = buf.arrayOffset() + start;
            byte[] bytes = buf.array();
            if (copy || baseOffset != 0 || length != bytes.length) {
                return Arrays.copyOfRange(bytes, baseOffset, baseOffset + length);
            }
            return bytes;
        }
        byte[] bytes2 = PlatformDependent.allocateUninitializedArray(length);
        buf.getBytes(start, bytes2);
        return bytes2;
    }

    public static void copy(AsciiString src, ByteBuf dst) {
        copy(src, 0, dst, src.length());
    }

    public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int dstIdx, int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ((ByteBuf) ObjectUtil.checkNotNull(dst, "dst")).setBytes(dstIdx, src.array(), src.arrayOffset() + srcIdx, length);
    }

    public static void copy(AsciiString src, int srcIdx, ByteBuf dst, int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, src.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + src.length() + ')');
        }
        ((ByteBuf) ObjectUtil.checkNotNull(dst, "dst")).writeBytes(src.array(), src.arrayOffset() + srcIdx, length);
    }

    public static String prettyHexDump(ByteBuf buffer) {
        return prettyHexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    public static String prettyHexDump(ByteBuf buffer, int offset, int length) {
        return HexUtil.prettyHexDump(buffer, offset, length);
    }

    public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf) {
        appendPrettyHexDump(dump, buf, buf.readerIndex(), buf.readableBytes());
    }

    public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length) {
        HexUtil.appendPrettyHexDump(dump, buf, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class HexUtil {
        private static final char[] BYTE2CHAR = new char[256];
        private static final char[] HEXDUMP_TABLE = new char[1024];
        private static final String[] HEXPADDING = new String[16];
        private static final String[] HEXDUMP_ROWPREFIXES = new String[4096];
        private static final String[] BYTE2HEX = new String[256];
        private static final String[] BYTEPADDING = new String[16];

        private HexUtil() {
        }

        static {
            char[] DIGITS = "0123456789abcdef".toCharArray();
            for (int i = 0; i < 256; i++) {
                HEXDUMP_TABLE[i << 1] = DIGITS[(i >>> 4) & 15];
                HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i & 15];
            }
            for (int i2 = 0; i2 < HEXPADDING.length; i2++) {
                int padding = HEXPADDING.length - i2;
                StringBuilder buf = new StringBuilder(padding * 3);
                for (int j = 0; j < padding; j++) {
                    buf.append("   ");
                }
                HEXPADDING[i2] = buf.toString();
            }
            for (int i3 = 0; i3 < HEXDUMP_ROWPREFIXES.length; i3++) {
                StringBuilder buf2 = new StringBuilder(12);
                buf2.append(StringUtil.NEWLINE);
                buf2.append(Long.toHexString(((i3 << 4) & 4294967295L) | 4294967296L));
                buf2.setCharAt(buf2.length() - 9, '|');
                buf2.append('|');
                HEXDUMP_ROWPREFIXES[i3] = buf2.toString();
            }
            for (int i4 = 0; i4 < BYTE2HEX.length; i4++) {
                BYTE2HEX[i4] = ' ' + StringUtil.byteToHexStringPadded(i4);
            }
            for (int i5 = 0; i5 < BYTEPADDING.length; i5++) {
                int padding2 = BYTEPADDING.length - i5;
                StringBuilder buf3 = new StringBuilder(padding2);
                for (int j2 = 0; j2 < padding2; j2++) {
                    buf3.append(' ');
                }
                BYTEPADDING[i5] = buf3.toString();
            }
            for (int i6 = 0; i6 < BYTE2CHAR.length; i6++) {
                if (i6 <= 31 || i6 >= 127) {
                    BYTE2CHAR[i6] = '.';
                } else {
                    BYTE2CHAR[i6] = (char) i6;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String hexDump(ByteBuf buffer, int fromIndex, int length) {
            ObjectUtil.checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }
            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];
            int srcIdx = fromIndex;
            int dstIdx = 0;
            while (srcIdx < endIndex) {
                System.arraycopy(HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1, buf, dstIdx, 2);
                srcIdx++;
                dstIdx += 2;
            }
            return new String(buf);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String hexDump(byte[] array, int fromIndex, int length) {
            ObjectUtil.checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }
            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];
            int srcIdx = fromIndex;
            int dstIdx = 0;
            while (srcIdx < endIndex) {
                System.arraycopy(HEXDUMP_TABLE, (array[srcIdx] & 255) << 1, buf, dstIdx, 2);
                srcIdx++;
                dstIdx += 2;
            }
            return new String(buf);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String prettyHexDump(ByteBuf buffer, int offset, int length) {
            if (length == 0) {
                return "";
            }
            int rows = (length / 16) + ((length & 15) == 0 ? 0 : 1) + 4;
            StringBuilder buf = new StringBuilder(rows * 80);
            appendPrettyHexDump(buf, buffer, offset, length);
            return buf.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length) {
            if (MathUtil.isOutOfBounds(offset, length, buf.capacity())) {
                throw new IndexOutOfBoundsException("expected: 0 <= offset(" + offset + ") <= offset + length(" + length + ") <= buf.capacity(" + buf.capacity() + ')');
            }
            if (length == 0) {
                return;
            }
            dump.append("         +-------------------------------------------------+" + StringUtil.NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
            int fullRows = length >>> 4;
            int remainder = length & 15;
            for (int row = 0; row < fullRows; row++) {
                int rowStartIndex = (row << 4) + offset;
                appendHexDumpRowPrefix(dump, row, rowStartIndex);
                int rowEndIndex = rowStartIndex + 16;
                for (int j = rowStartIndex; j < rowEndIndex; j++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
                }
                dump.append(" |");
                for (int j2 = rowStartIndex; j2 < rowEndIndex; j2++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j2)]);
                }
                dump.append('|');
            }
            if (remainder != 0) {
                int rowStartIndex2 = (fullRows << 4) + offset;
                appendHexDumpRowPrefix(dump, fullRows, rowStartIndex2);
                int rowEndIndex2 = rowStartIndex2 + remainder;
                for (int j3 = rowStartIndex2; j3 < rowEndIndex2; j3++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j3)]);
                }
                dump.append(HEXPADDING[remainder]);
                dump.append(" |");
                for (int j4 = rowStartIndex2; j4 < rowEndIndex2; j4++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j4)]);
                }
                dump.append(BYTEPADDING[remainder]);
                dump.append('|');
            }
            dump.append(StringUtil.NEWLINE + "+--------+-------------------------------------------------+----------------+");
        }

        private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
            if (row < HEXDUMP_ROWPREFIXES.length) {
                dump.append(HEXDUMP_ROWPREFIXES[row]);
                return;
            }
            dump.append(StringUtil.NEWLINE);
            dump.append(Long.toHexString((rowStartIndex & 4294967295L) | 4294967296L));
            dump.setCharAt(dump.length() - 9, '|');
            dump.append('|');
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf {
        private static final ObjectPool<ThreadLocalUnsafeDirectByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<ThreadLocalUnsafeDirectByteBuf>() { // from class: io.netty.buffer.ByteBufUtil.ThreadLocalUnsafeDirectByteBuf.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public ThreadLocalUnsafeDirectByteBuf newObject(ObjectPool.Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
                return new ThreadLocalUnsafeDirectByteBuf(handle);
            }
        });
        private final Recycler.EnhancedHandle<ThreadLocalUnsafeDirectByteBuf> handle;

        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            ThreadLocalUnsafeDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private ThreadLocalUnsafeDirectByteBuf(ObjectPool.Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = (Recycler.EnhancedHandle) handle;
        }

        @Override // io.netty.buffer.UnpooledDirectByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf
        protected void deallocate() {
            if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                this.handle.unguardedRecycle(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf {
        private static final ObjectPool<ThreadLocalDirectByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<ThreadLocalDirectByteBuf>() { // from class: io.netty.buffer.ByteBufUtil.ThreadLocalDirectByteBuf.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public ThreadLocalDirectByteBuf newObject(ObjectPool.Handle<ThreadLocalDirectByteBuf> handle) {
                return new ThreadLocalDirectByteBuf(handle);
            }
        });
        private final Recycler.EnhancedHandle<ThreadLocalDirectByteBuf> handle;

        static ThreadLocalDirectByteBuf newInstance() {
            ThreadLocalDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private ThreadLocalDirectByteBuf(ObjectPool.Handle<ThreadLocalDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = (Recycler.EnhancedHandle) handle;
        }

        @Override // io.netty.buffer.UnpooledDirectByteBuf, io.netty.buffer.AbstractReferenceCountedByteBuf
        protected void deallocate() {
            if (capacity() > ByteBufUtil.THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                this.handle.unguardedRecycle(this);
            }
        }
    }

    public static boolean isText(ByteBuf buf, Charset charset) {
        return isText(buf, buf.readerIndex(), buf.readableBytes(), charset);
    }

    public static boolean isText(ByteBuf buf, int index, int length, Charset charset) {
        ObjectUtil.checkNotNull(buf, "buf");
        ObjectUtil.checkNotNull(charset, "charset");
        int maxIndex = buf.readerIndex() + buf.readableBytes();
        if (index < 0 || length < 0 || index > maxIndex - length) {
            throw new IndexOutOfBoundsException("index: " + index + " length: " + length);
        }
        if (charset.equals(CharsetUtil.UTF_8)) {
            return isUtf8(buf, index, length);
        }
        if (charset.equals(CharsetUtil.US_ASCII)) {
            return isAscii(buf, index, length);
        }
        CharsetDecoder decoder = CharsetUtil.decoder(charset, CodingErrorAction.REPORT, CodingErrorAction.REPORT);
        try {
            if (buf.nioBufferCount() == 1) {
                decoder.decode(buf.nioBuffer(index, length));
            } else {
                ByteBuf heapBuffer = buf.alloc().heapBuffer(length);
                try {
                    heapBuffer.writeBytes(buf, index, length);
                    decoder.decode(heapBuffer.internalNioBuffer(heapBuffer.readerIndex(), length));
                } finally {
                    heapBuffer.release();
                }
            }
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    private static boolean isAscii(ByteBuf buf, int index, int length) {
        return buf.forEachByte(index, length, FIND_NON_ASCII) == -1;
    }

    private static boolean isUtf8(ByteBuf buf, int index, int length) {
        int endIndex = index + length;
        while (index < endIndex) {
            int index2 = index + 1;
            int b1 = buf.getByte(index);
            if ((b1 & 128) == 0) {
                index = index2;
            } else if ((b1 & bl.cW) == 192) {
                if (index2 >= endIndex) {
                    return false;
                }
                int index3 = index2 + 1;
                if ((buf.getByte(index2) & MessagePack.Code.NIL) != 128 || (b1 & 255) < 194) {
                    return false;
                }
                index = index3;
            } else if ((b1 & bl.db) == 224) {
                if (index2 > endIndex - 2) {
                    return false;
                }
                int index4 = index2 + 1;
                byte b2 = buf.getByte(index2);
                int index5 = index4 + 1;
                byte b3 = buf.getByte(index4);
                if ((b2 & MessagePack.Code.NIL) != 128 || (b3 & MessagePack.Code.NIL) != 128) {
                    return false;
                }
                if ((b1 & 15) == 0 && (b2 & 255) < 160) {
                    return false;
                }
                if ((b1 & 15) == 13 && (b2 & 255) > 159) {
                    return false;
                }
                index = index5;
            } else {
                if ((b1 & bl.dj) != 240 || index2 > endIndex - 3) {
                    return false;
                }
                int index6 = index2 + 1;
                byte b22 = buf.getByte(index2);
                int index7 = index6 + 1;
                byte b32 = buf.getByte(index6);
                int index8 = index7 + 1;
                byte b4 = buf.getByte(index7);
                if ((b22 & MessagePack.Code.NIL) != 128 || (b32 & MessagePack.Code.NIL) != 128 || (b4 & MessagePack.Code.NIL) != 128 || (b1 & 255) > 244 || (((b1 & 255) == 240 && (b22 & 255) < 144) || ((b1 & 255) == 244 && (b22 & 255) > 143))) {
                    return false;
                }
                index = index8;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void readBytes(ByteBufAllocator allocator, ByteBuffer buffer, int position, int length, OutputStream out) throws IOException {
        if (buffer.hasArray()) {
            out.write(buffer.array(), buffer.arrayOffset() + position, length);
            return;
        }
        int chunkLen = Math.min(length, 8192);
        buffer.clear().position(position);
        if (length <= 1024 || !allocator.isDirectBufferPooled()) {
            getBytes(buffer, threadLocalTempArray(chunkLen), 0, chunkLen, out, length);
            return;
        }
        ByteBuf tmpBuf = allocator.heapBuffer(chunkLen);
        try {
            byte[] tmp = tmpBuf.array();
            int offset = tmpBuf.arrayOffset();
            getBytes(buffer, tmp, offset, chunkLen, out, length);
        } finally {
            tmpBuf.release();
        }
    }

    private static void getBytes(ByteBuffer inBuffer, byte[] in, int inOffset, int inLen, OutputStream out, int outLen) throws IOException {
        do {
            int len = Math.min(inLen, outLen);
            inBuffer.get(in, inOffset, len);
            out.write(in, inOffset, len);
            outLen -= len;
        } while (outLen > 0);
    }

    public static void setLeakListener(ResourceLeakDetector.LeakListener leakListener) {
        AbstractByteBuf.leakDetector.setLeakListener(leakListener);
    }

    private ByteBufUtil() {
    }
}
