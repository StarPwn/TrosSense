package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class SocksInitResponseDecoder extends ReplayingDecoder<State> {

    /* loaded from: classes4.dex */
    public enum State {
        CHECK_PROTOCOL_VERSION,
        READ_PREFERRED_AUTH_TYPE
    }

    public SocksInitResponseDecoder() {
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
                checkpoint(State.READ_PREFERRED_AUTH_TYPE);
            case READ_PREFERRED_AUTH_TYPE:
                SocksAuthScheme authScheme = SocksAuthScheme.valueOf(byteBuf.readByte());
                out.add(new SocksInitResponse(authScheme));
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
