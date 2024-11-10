package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class SocksAuthResponseDecoder extends ReplayingDecoder<State> {

    /* loaded from: classes4.dex */
    public enum State {
        CHECK_PROTOCOL_VERSION,
        READ_AUTH_RESPONSE
    }

    public SocksAuthResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        switch (state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksSubnegotiationVersion.AUTH_PASSWORD.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                    channelHandlerContext.pipeline().remove(this);
                    return;
                }
                checkpoint(State.READ_AUTH_RESPONSE);
            case READ_AUTH_RESPONSE:
                SocksAuthStatus authStatus = SocksAuthStatus.valueOf(byteBuf.readByte());
                out.add(new SocksAuthResponse(authStatus));
                channelHandlerContext.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
