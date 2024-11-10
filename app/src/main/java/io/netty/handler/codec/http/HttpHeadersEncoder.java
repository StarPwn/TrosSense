package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class HttpHeadersEncoder {
    private static final int COLON_AND_SPACE_SHORT = 14880;

    private HttpHeadersEncoder() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void encoderHeader(CharSequence name, CharSequence value, ByteBuf buf) {
        int nameLen = name.length();
        int valueLen = value.length();
        int entryLen = nameLen + valueLen + 4;
        buf.ensureWritable(entryLen);
        int offset = buf.writerIndex();
        writeAscii(buf, offset, name);
        int offset2 = offset + nameLen;
        ByteBufUtil.setShortBE(buf, offset2, COLON_AND_SPACE_SHORT);
        int offset3 = offset2 + 2;
        writeAscii(buf, offset3, value);
        int offset4 = offset3 + valueLen;
        ByteBufUtil.setShortBE(buf, offset4, 3338);
        buf.writerIndex(offset4 + 2);
    }

    private static void writeAscii(ByteBuf buf, int offset, CharSequence value) {
        if (value instanceof AsciiString) {
            ByteBufUtil.copy((AsciiString) value, 0, buf, offset, value.length());
        } else {
            buf.setCharSequence(offset, value, CharsetUtil.US_ASCII);
        }
    }
}
