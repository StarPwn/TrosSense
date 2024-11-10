package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class SocksInitRequestDecoder extends ReplayingDecoder<State> {

    /* loaded from: classes4.dex */
    public enum State {
        CHECK_PROTOCOL_VERSION,
        READ_AUTH_SCHEMES
    }

    public SocksInitRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        List<SocksAuthScheme> authSchemes;
        switch (state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
                    ctx.pipeline().remove(this);
                    return;
                }
                checkpoint(State.READ_AUTH_SCHEMES);
            case READ_AUTH_SCHEMES:
                byte authSchemeNum = byteBuf.readByte();
                if (authSchemeNum > 0) {
                    authSchemes = new ArrayList<>(authSchemeNum);
                    for (int i = 0; i < authSchemeNum; i++) {
                        authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte()));
                    }
                } else {
                    authSchemes = Collections.emptyList();
                }
                out.add(new SocksInitRequest(authSchemes));
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
