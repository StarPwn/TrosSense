package io.netty.handler.pcap;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.io.OutputStream;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
final class PcapHeaders {
    private static final byte[] GLOBAL_HEADER = {-95, -78, MessagePack.Code.TRUE, MessagePack.Code.FIXEXT1, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 1};

    private PcapHeaders() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeGlobalHeader(OutputStream outputStream) throws IOException {
        outputStream.write(GLOBAL_HEADER);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writePacketHeader(ByteBuf byteBuf, int ts_sec, int ts_usec, int incl_len, int orig_len) {
        byteBuf.writeInt(ts_sec);
        byteBuf.writeInt(ts_usec);
        byteBuf.writeInt(incl_len);
        byteBuf.writeInt(orig_len);
    }
}
