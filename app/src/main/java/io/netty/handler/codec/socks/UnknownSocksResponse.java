package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public final class UnknownSocksResponse extends SocksResponse {
    public UnknownSocksResponse() {
        super(SocksResponseType.UNKNOWN);
    }

    @Override // io.netty.handler.codec.socks.SocksMessage
    public void encodeAsByteBuf(ByteBuf byteBuf) {
    }
}
