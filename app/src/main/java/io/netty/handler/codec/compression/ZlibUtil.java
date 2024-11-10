package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

/* loaded from: classes4.dex */
final class ZlibUtil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void fail(Inflater z, String message, int resultCode) {
        throw inflaterException(z, message, resultCode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void fail(Deflater z, String message, int resultCode) {
        throw deflaterException(z, message, resultCode);
    }

    static DecompressionException inflaterException(Inflater z, String message, int resultCode) {
        return new DecompressionException(message + " (" + resultCode + ')' + (z.msg != null ? ": " + z.msg : ""));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CompressionException deflaterException(Deflater z, String message, int resultCode) {
        return new CompressionException(message + " (" + resultCode + ')' + (z.msg != null ? ": " + z.msg : ""));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JZlib.WrapperType convertWrapperType(ZlibWrapper wrapper) {
        switch (wrapper) {
            case NONE:
                JZlib.WrapperType convertedWrapperType = JZlib.W_NONE;
                return convertedWrapperType;
            case ZLIB:
                JZlib.WrapperType convertedWrapperType2 = JZlib.W_ZLIB;
                return convertedWrapperType2;
            case GZIP:
                JZlib.WrapperType convertedWrapperType3 = JZlib.W_GZIP;
                return convertedWrapperType3;
            case ZLIB_OR_NONE:
                JZlib.WrapperType convertedWrapperType4 = JZlib.W_ANY;
                return convertedWrapperType4;
            default:
                throw new Error();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int wrapperOverhead(ZlibWrapper wrapper) {
        switch (wrapper) {
            case NONE:
                return 0;
            case ZLIB:
            case ZLIB_OR_NONE:
                return 2;
            case GZIP:
                return 10;
            default:
                throw new Error();
        }
    }

    private ZlibUtil() {
    }
}
