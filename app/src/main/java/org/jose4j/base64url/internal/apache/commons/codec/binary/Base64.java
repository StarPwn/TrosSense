package org.jose4j.base64url.internal.apache.commons.codec.binary;

import com.google.common.base.Ascii;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import java.math.BigInteger;
import okio.Utf8;
import org.jose4j.base64url.internal.apache.commons.codec.binary.BaseNCodec;
import org.jose4j.lang.StringUtil;

/* loaded from: classes5.dex */
public class Base64 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int MASK_6BITS = 63;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;
    static final byte[] CHUNK_SEPARATOR = {13, 10};
    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, Utf8.REPLACEMENT_BYTE, 52, 53, 54, 55, 56, 57, HttpConstants.COLON, HttpConstants.SEMICOLON, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, Utf8.REPLACEMENT_BYTE, -1, 26, Ascii.ESC, 28, 29, 30, Ascii.US, 32, BinaryMemcacheOpcodes.SASL_AUTH, 34, BinaryMemcacheOpcodes.GATK, BinaryMemcacheOpcodes.GATKQ, 37, 38, 39, 40, 41, 42, 43, HttpConstants.COMMA, 45, 46, 47, 48, 49, 50, 51};

    public Base64() {
        this(0);
    }

    public Base64(boolean urlSafe) {
        this(76, CHUNK_SEPARATOR, urlSafe);
    }

    public Base64(int lineLength) {
        this(lineLength, CHUNK_SEPARATOR);
    }

    public Base64(int lineLength, byte[] lineSeparator) {
        this(lineLength, lineSeparator, false);
    }

    public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
        super(3, 4, lineLength, lineSeparator == null ? 0 : lineSeparator.length);
        this.decodeTable = DECODE_TABLE;
        if (lineSeparator != null) {
            if (containsAlphabetOrPad(lineSeparator)) {
                String sep = StringUtil.newStringUtf8(lineSeparator);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
            }
            if (lineLength > 0) {
                this.encodeSize = lineSeparator.length + 4;
                this.lineSeparator = new byte[lineSeparator.length];
                System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
            } else {
                this.encodeSize = 4;
                this.lineSeparator = null;
            }
        } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }

    @Override // org.jose4j.base64url.internal.apache.commons.codec.binary.BaseNCodec
    void encode(byte[] bArr, int inPos, int inAvail, BaseNCodec.Context context) {
        if (context.eof) {
            return;
        }
        if (inAvail < 0) {
            context.eof = true;
            if (context.modulus == 0 && this.lineLength == 0) {
                return;
            }
            byte[] buffer = ensureBufferSize(this.encodeSize, context);
            int savedPos = context.pos;
            switch (context.modulus) {
                case 0:
                    break;
                case 1:
                    int i = context.pos;
                    context.pos = i + 1;
                    buffer[i] = this.encodeTable[(context.ibitWorkArea >> 2) & 63];
                    int i2 = context.pos;
                    context.pos = i2 + 1;
                    buffer[i2] = this.encodeTable[(context.ibitWorkArea << 4) & 63];
                    if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        int i3 = context.pos;
                        context.pos = i3 + 1;
                        buffer[i3] = 61;
                        int i4 = context.pos;
                        context.pos = i4 + 1;
                        buffer[i4] = 61;
                        break;
                    }
                    break;
                case 2:
                    int i5 = context.pos;
                    context.pos = i5 + 1;
                    buffer[i5] = this.encodeTable[(context.ibitWorkArea >> 10) & 63];
                    int i6 = context.pos;
                    context.pos = i6 + 1;
                    buffer[i6] = this.encodeTable[(context.ibitWorkArea >> 4) & 63];
                    int i7 = context.pos;
                    context.pos = i7 + 1;
                    buffer[i7] = this.encodeTable[(context.ibitWorkArea << 2) & 63];
                    if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        int i8 = context.pos;
                        context.pos = i8 + 1;
                        buffer[i8] = 61;
                        break;
                    }
                    break;
                default:
                    throw new IllegalStateException("Impossible modulus " + context.modulus);
            }
            context.currentLinePos += context.pos - savedPos;
            if (this.lineLength > 0 && context.currentLinePos > 0) {
                System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
                context.pos += this.lineSeparator.length;
                return;
            }
            return;
        }
        int i9 = 0;
        while (i9 < inAvail) {
            byte[] buffer2 = ensureBufferSize(this.encodeSize, context);
            context.modulus = (context.modulus + 1) % 3;
            int inPos2 = inPos + 1;
            int i10 = bArr[inPos];
            if (i10 < 0) {
                i10 += 256;
            }
            context.ibitWorkArea = (context.ibitWorkArea << 8) + i10;
            if (context.modulus == 0) {
                int i11 = context.pos;
                context.pos = i11 + 1;
                buffer2[i11] = this.encodeTable[(context.ibitWorkArea >> 18) & 63];
                int i12 = context.pos;
                context.pos = i12 + 1;
                buffer2[i12] = this.encodeTable[(context.ibitWorkArea >> 12) & 63];
                int i13 = context.pos;
                context.pos = i13 + 1;
                buffer2[i13] = this.encodeTable[(context.ibitWorkArea >> 6) & 63];
                int i14 = context.pos;
                context.pos = i14 + 1;
                buffer2[i14] = this.encodeTable[context.ibitWorkArea & 63];
                context.currentLinePos += 4;
                if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
                    System.arraycopy(this.lineSeparator, 0, buffer2, context.pos, this.lineSeparator.length);
                    context.pos += this.lineSeparator.length;
                    context.currentLinePos = 0;
                }
            }
            i9++;
            inPos = inPos2;
        }
    }

    @Override // org.jose4j.base64url.internal.apache.commons.codec.binary.BaseNCodec
    void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
        int result;
        if (context.eof) {
            return;
        }
        if (inAvail < 0) {
            context.eof = true;
        }
        int i = 0;
        while (true) {
            if (i >= inAvail) {
                break;
            }
            byte[] buffer = ensureBufferSize(this.decodeSize, context);
            int inPos2 = inPos + 1;
            byte b = in[inPos];
            if (b == 61) {
                context.eof = true;
                break;
            }
            if (b >= 0 && b < DECODE_TABLE.length && (result = DECODE_TABLE[b]) >= 0) {
                context.modulus = (context.modulus + 1) % 4;
                context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
                if (context.modulus == 0) {
                    int i2 = context.pos;
                    context.pos = i2 + 1;
                    buffer[i2] = (byte) ((context.ibitWorkArea >> 16) & 255);
                    int i3 = context.pos;
                    context.pos = i3 + 1;
                    buffer[i3] = (byte) ((context.ibitWorkArea >> 8) & 255);
                    int i4 = context.pos;
                    context.pos = i4 + 1;
                    buffer[i4] = (byte) (context.ibitWorkArea & 255);
                }
            }
            i++;
            inPos = inPos2;
        }
        if (context.eof && context.modulus != 0) {
            byte[] buffer2 = ensureBufferSize(this.decodeSize, context);
            switch (context.modulus) {
                case 1:
                    return;
                case 2:
                    context.ibitWorkArea >>= 4;
                    int i5 = context.pos;
                    context.pos = i5 + 1;
                    buffer2[i5] = (byte) (context.ibitWorkArea & 255);
                    return;
                case 3:
                    context.ibitWorkArea >>= 2;
                    int i6 = context.pos;
                    context.pos = i6 + 1;
                    buffer2[i6] = (byte) ((context.ibitWorkArea >> 8) & 255);
                    int i7 = context.pos;
                    context.pos = i7 + 1;
                    buffer2[i7] = (byte) (context.ibitWorkArea & 255);
                    return;
                default:
                    throw new IllegalStateException("Impossible modulus " + context.modulus);
            }
        }
    }

    @Deprecated
    public static boolean isArrayByteBase64(byte[] arrayOctet) {
        return isBase64(arrayOctet);
    }

    public static boolean isBase64(byte octet) {
        return octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
    }

    public static boolean isBase64(String base64) {
        return isBase64(StringUtil.getBytesUtf8(base64));
    }

    public static boolean isBase64(byte[] arrayOctet) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static String encodeBase64String(byte[] binaryData) {
        return StringUtil.newStringUtf8(encodeBase64(binaryData, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] binaryData) {
        return encodeBase64(binaryData, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] binaryData) {
        return StringUtil.newStringUtf8(encodeBase64(binaryData, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        return encodeBase64(binaryData, isChunked, false);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
        return encodeBase64(binaryData, isChunked, urlSafe, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
        if (binaryData == null || binaryData.length == 0) {
            return binaryData;
        }
        Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
        long len = b64.getEncodedLength(binaryData);
        if (len > maxResultSize) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
        }
        return b64.encode(binaryData);
    }

    public static byte[] decodeBase64(String base64String) {
        return new Base64().decode(base64String);
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        return new Base64().decode(base64Data);
    }

    public static BigInteger decodeInteger(byte[] pArray) {
        return new BigInteger(1, decodeBase64(pArray));
    }

    public static byte[] encodeInteger(BigInteger bigInt) {
        if (bigInt == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        }
        return encodeBase64(toIntegerBytes(bigInt), false);
    }

    static byte[] toIntegerBytes(BigInteger bigInt) {
        int bitlen = ((bigInt.bitLength() + 7) >> 3) << 3;
        byte[] bigBytes = bigInt.toByteArray();
        if (bigInt.bitLength() % 8 != 0 && (bigInt.bitLength() / 8) + 1 == bitlen / 8) {
            return bigBytes;
        }
        int startSrc = 0;
        int len = bigBytes.length;
        if (bigInt.bitLength() % 8 == 0) {
            startSrc = 1;
            len--;
        }
        int startDst = (bitlen / 8) - len;
        byte[] resizedBytes = new byte[bitlen / 8];
        System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
        return resizedBytes;
    }

    @Override // org.jose4j.base64url.internal.apache.commons.codec.binary.BaseNCodec
    protected boolean isInAlphabet(byte octet) {
        return octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1;
    }
}
