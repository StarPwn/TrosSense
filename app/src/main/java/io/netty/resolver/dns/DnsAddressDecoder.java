package io.netty.resolver.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes4.dex */
final class DnsAddressDecoder {
    private static final int INADDRSZ4 = 4;
    private static final int INADDRSZ6 = 16;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InetAddress decodeAddress(DnsRecord record, String name, boolean decodeIdn) {
        String unicode;
        if (!(record instanceof DnsRawRecord)) {
            return null;
        }
        ByteBuf content = ((ByteBufHolder) record).content();
        int contentLen = content.readableBytes();
        if (contentLen != 4 && contentLen != 16) {
            return null;
        }
        byte[] addrBytes = new byte[contentLen];
        content.getBytes(content.readerIndex(), addrBytes);
        if (!decodeIdn) {
            unicode = name;
        } else {
            try {
                unicode = IDN.toUnicode(name);
            } catch (UnknownHostException e) {
                throw new Error(e);
            }
        }
        return InetAddress.getByAddress(unicode, addrBytes);
    }

    private DnsAddressDecoder() {
    }
}
