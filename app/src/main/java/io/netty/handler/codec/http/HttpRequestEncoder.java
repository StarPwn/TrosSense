package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;

/* loaded from: classes4.dex */
public class HttpRequestEncoder extends HttpObjectEncoder<HttpRequest> {
    private static final char QUESTION_MARK = '?';
    private static final char SLASH = '/';
    private static final int SLASH_AND_SPACE_SHORT = 12064;
    private static final int SPACE_SLASH_AND_SPACE_MEDIUM = 2109216;

    @Override // io.netty.handler.codec.http.HttpObjectEncoder, io.netty.handler.codec.MessageToMessageEncoder
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpResponse);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.HttpObjectEncoder
    public void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception {
        ByteBufUtil.copy(request.method().asciiName(), buf);
        String uri = request.uri();
        if (uri.isEmpty()) {
            ByteBufUtil.writeMediumBE(buf, SPACE_SLASH_AND_SPACE_MEDIUM);
        } else {
            CharSequence uriCharSequence = uri;
            boolean needSlash = false;
            int start = uri.indexOf("://");
            if (start != -1 && uri.charAt(0) != '/') {
                int start2 = start + 3;
                int index = uri.indexOf(63, start2);
                if (index == -1) {
                    if (uri.lastIndexOf(47) < start2) {
                        needSlash = true;
                    }
                } else if (uri.lastIndexOf(47, index) < start2) {
                    uriCharSequence = new StringBuilder(uri).insert(index, SLASH);
                }
            }
            buf.writeByte(32).writeCharSequence(uriCharSequence, CharsetUtil.UTF_8);
            if (needSlash) {
                ByteBufUtil.writeShortBE(buf, SLASH_AND_SPACE_SHORT);
            } else {
                buf.writeByte(32);
            }
        }
        request.protocolVersion().encode(buf);
        ByteBufUtil.writeShortBE(buf, 3338);
    }
}
