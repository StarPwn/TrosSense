package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

/* loaded from: classes4.dex */
public interface Socks5AddressEncoder {
    public static final Socks5AddressEncoder DEFAULT = new Socks5AddressEncoder() { // from class: io.netty.handler.codec.socksx.v5.Socks5AddressEncoder.1
        @Override // io.netty.handler.codec.socksx.v5.Socks5AddressEncoder
        public void encodeAddress(Socks5AddressType addrType, String addrValue, ByteBuf out) throws Exception {
            byte typeVal = addrType.byteValue();
            if (typeVal == Socks5AddressType.IPv4.byteValue()) {
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                    return;
                } else {
                    out.writeInt(0);
                    return;
                }
            }
            if (typeVal == Socks5AddressType.DOMAIN.byteValue()) {
                if (addrValue != null) {
                    out.writeByte(addrValue.length());
                    out.writeCharSequence(addrValue, CharsetUtil.US_ASCII);
                    return;
                } else {
                    out.writeByte(0);
                    return;
                }
            }
            if (typeVal == Socks5AddressType.IPv6.byteValue()) {
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                    return;
                } else {
                    out.writeLong(0L);
                    out.writeLong(0L);
                    return;
                }
            }
            throw new EncoderException("unsupported addrType: " + (addrType.byteValue() & 255));
        }
    };

    void encodeAddress(Socks5AddressType socks5AddressType, String str, ByteBuf byteBuf) throws Exception;
}
