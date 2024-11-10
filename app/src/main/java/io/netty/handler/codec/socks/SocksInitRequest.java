package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class SocksInitRequest extends SocksRequest {
    private final List<SocksAuthScheme> authSchemes;

    public SocksInitRequest(List<SocksAuthScheme> authSchemes) {
        super(SocksRequestType.INIT);
        this.authSchemes = (List) ObjectUtil.checkNotNull(authSchemes, "authSchemes");
    }

    public List<SocksAuthScheme> authSchemes() {
        return Collections.unmodifiableList(this.authSchemes);
    }

    @Override // io.netty.handler.codec.socks.SocksMessage
    public void encodeAsByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(protocolVersion().byteValue());
        byteBuf.writeByte(this.authSchemes.size());
        for (SocksAuthScheme authScheme : this.authSchemes) {
            byteBuf.writeByte(authScheme.byteValue());
        }
    }
}
