package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.NetUtil;
import java.util.List;

/* loaded from: classes4.dex */
public class SocksCmdResponseDecoder extends ReplayingDecoder<State> {
    private SocksAddressType addressType;
    private SocksCmdStatus cmdStatus;

    /* loaded from: classes4.dex */
    public enum State {
        CHECK_PROTOCOL_VERSION,
        READ_CMD_HEADER,
        READ_CMD_ADDRESS
    }

    public SocksCmdResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        switch (state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                    ctx.pipeline().remove(this);
                    return;
                }
                checkpoint(State.READ_CMD_HEADER);
            case READ_CMD_HEADER:
                this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
                byteBuf.skipBytes(1);
                this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
                checkpoint(State.READ_CMD_ADDRESS);
            case READ_CMD_ADDRESS:
                switch (this.addressType) {
                    case IPv4:
                        int fieldLength = ByteBufUtil.readIntBE(byteBuf);
                        String host = NetUtil.intToIpAddress(fieldLength);
                        int port = ByteBufUtil.readUnsignedShortBE(byteBuf);
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
                        break;
                    case DOMAIN:
                        int fieldLength2 = byteBuf.readByte();
                        String host2 = SocksCommonUtils.readUsAscii(byteBuf, fieldLength2);
                        int port2 = ByteBufUtil.readUnsignedShortBE(byteBuf);
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host2, port2));
                        break;
                    case IPv6:
                        byte[] bytes = new byte[16];
                        byteBuf.readBytes(bytes);
                        String host3 = SocksCommonUtils.ipv6toStr(bytes);
                        int port3 = ByteBufUtil.readUnsignedShortBE(byteBuf);
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host3, port3));
                        break;
                    case UNKNOWN:
                        out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                        break;
                    default:
                        throw new Error();
                }
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
