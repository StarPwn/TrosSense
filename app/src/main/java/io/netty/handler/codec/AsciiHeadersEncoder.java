package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.Map;

/* loaded from: classes4.dex */
public final class AsciiHeadersEncoder {
    private final ByteBuf buf;
    private final NewlineType newlineType;
    private final SeparatorType separatorType;

    /* loaded from: classes4.dex */
    public enum NewlineType {
        LF,
        CRLF
    }

    /* loaded from: classes4.dex */
    public enum SeparatorType {
        COLON,
        COLON_SPACE
    }

    public AsciiHeadersEncoder(ByteBuf buf) {
        this(buf, SeparatorType.COLON_SPACE, NewlineType.CRLF);
    }

    public AsciiHeadersEncoder(ByteBuf buf, SeparatorType separatorType, NewlineType newlineType) {
        this.buf = (ByteBuf) ObjectUtil.checkNotNull(buf, "buf");
        this.separatorType = (SeparatorType) ObjectUtil.checkNotNull(separatorType, "separatorType");
        this.newlineType = (NewlineType) ObjectUtil.checkNotNull(newlineType, "newlineType");
    }

    public void encode(Map.Entry<CharSequence, CharSequence> entry) {
        int offset;
        int offset2;
        CharSequence name = entry.getKey();
        CharSequence value = entry.getValue();
        ByteBuf buf = this.buf;
        int nameLen = name.length();
        int valueLen = value.length();
        int entryLen = nameLen + valueLen + 4;
        int offset3 = buf.writerIndex();
        buf.ensureWritable(entryLen);
        writeAscii(buf, offset3, name);
        int offset4 = offset3 + nameLen;
        switch (this.separatorType) {
            case COLON:
                buf.setByte(offset4, 58);
                offset = offset4 + 1;
                break;
            case COLON_SPACE:
                int offset5 = offset4 + 1;
                buf.setByte(offset4, 58);
                offset = offset5 + 1;
                buf.setByte(offset5, 32);
                break;
            default:
                throw new Error();
        }
        writeAscii(buf, offset, value);
        int offset6 = offset + valueLen;
        switch (this.newlineType) {
            case LF:
                buf.setByte(offset6, 10);
                offset2 = offset6 + 1;
                break;
            case CRLF:
                int offset7 = offset6 + 1;
                buf.setByte(offset6, 13);
                offset2 = offset7 + 1;
                buf.setByte(offset7, 10);
                break;
            default:
                throw new Error();
        }
        buf.writerIndex(offset2);
    }

    private static void writeAscii(ByteBuf buf, int offset, CharSequence value) {
        if (value instanceof AsciiString) {
            ByteBufUtil.copy((AsciiString) value, 0, buf, offset, value.length());
        } else {
            buf.setCharSequence(offset, value, CharsetUtil.US_ASCII);
        }
    }
}
