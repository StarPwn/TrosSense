package io.netty.handler.codec.compression;

import com.google.common.base.Ascii;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import io.netty.handler.codec.memcache.binary.DefaultBinaryMemcacheResponse;
import okio.Utf8;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
final class Bzip2MoveToFrontTable {
    private final byte[] mtf = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, Ascii.ESC, 28, 29, 30, Ascii.US, 32, BinaryMemcacheOpcodes.SASL_AUTH, 34, BinaryMemcacheOpcodes.GATK, BinaryMemcacheOpcodes.GATKQ, 37, 38, 39, 40, 41, 42, 43, HttpConstants.COMMA, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, HttpConstants.COLON, HttpConstants.SEMICOLON, 60, 61, 62, Utf8.REPLACEMENT_BYTE, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, Byte.MAX_VALUE, Byte.MIN_VALUE, DefaultBinaryMemcacheResponse.RESPONSE_MAGIC_BYTE, -126, -125, -124, -123, -122, -121, -120, -119, -118, -117, -116, -115, -114, -113, MessagePack.Code.FIXARRAY_PREFIX, -111, -110, -109, -108, -107, -106, -105, -104, -103, -102, -101, -100, -99, -98, -97, MessagePack.Code.FIXSTR_PREFIX, -95, -94, -93, -92, -91, -90, -89, -88, -87, -86, -85, -84, -83, -82, -81, -80, -79, -78, -77, -76, -75, -74, -73, -72, -71, -70, -69, -68, -67, -66, -65, MessagePack.Code.NIL, MessagePack.Code.NEVER_USED, MessagePack.Code.FALSE, MessagePack.Code.TRUE, MessagePack.Code.BIN8, MessagePack.Code.BIN16, MessagePack.Code.BIN32, MessagePack.Code.EXT8, MessagePack.Code.EXT16, MessagePack.Code.EXT32, MessagePack.Code.FLOAT32, MessagePack.Code.FLOAT64, MessagePack.Code.UINT8, MessagePack.Code.UINT16, MessagePack.Code.UINT32, MessagePack.Code.UINT64, MessagePack.Code.INT8, MessagePack.Code.INT16, MessagePack.Code.INT32, MessagePack.Code.INT64, MessagePack.Code.FIXEXT1, MessagePack.Code.FIXEXT2, MessagePack.Code.FIXEXT4, MessagePack.Code.FIXEXT8, MessagePack.Code.FIXEXT16, MessagePack.Code.STR8, MessagePack.Code.STR16, MessagePack.Code.STR32, MessagePack.Code.ARRAY16, MessagePack.Code.ARRAY32, MessagePack.Code.MAP16, MessagePack.Code.MAP32, MessagePack.Code.NEGFIXINT_PREFIX, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1};

    /* JADX INFO: Access modifiers changed from: package-private */
    public int valueToFront(byte value) {
        int index = 0;
        byte temp = this.mtf[0];
        if (value != temp) {
            this.mtf[0] = value;
            while (value != temp) {
                index++;
                byte temp2 = temp;
                temp = this.mtf[index];
                this.mtf[index] = temp2;
            }
        }
        return index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte indexToFront(int index) {
        byte value = this.mtf[index];
        System.arraycopy(this.mtf, 0, this.mtf, 1, index);
        this.mtf[0] = value;
        return value;
    }
}
