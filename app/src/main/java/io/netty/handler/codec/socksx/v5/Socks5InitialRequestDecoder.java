package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socksx.SocksVersion;
import java.util.List;

/* loaded from: classes4.dex */
public class Socks5InitialRequestDecoder extends ReplayingDecoder<State> {

    /* loaded from: classes4.dex */
    public enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5InitialRequestDecoder() {
        super(State.INIT);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            switch (state()) {
                case INIT:
                    byte version = in.readByte();
                    if (version != SocksVersion.SOCKS5.byteValue()) {
                        throw new DecoderException("unsupported version: " + ((int) version) + " (expected: " + ((int) SocksVersion.SOCKS5.byteValue()) + ')');
                    }
                    int authMethodCnt = in.readUnsignedByte();
                    Socks5AuthMethod[] authMethods = new Socks5AuthMethod[authMethodCnt];
                    for (int i = 0; i < authMethodCnt; i++) {
                        authMethods[i] = Socks5AuthMethod.valueOf(in.readByte());
                    }
                    out.add(new DefaultSocks5InitialRequest(authMethods));
                    checkpoint(State.SUCCESS);
                    break;
                case SUCCESS:
                    break;
                case FAILURE:
                    in.skipBytes(actualReadableBytes());
                    return;
                default:
                    return;
            }
            int readableBytes = actualReadableBytes();
            if (readableBytes > 0) {
                out.add(in.readRetainedSlice(readableBytes));
            }
        } catch (Exception e) {
            fail(out, e);
        }
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        checkpoint(State.FAILURE);
        Socks5Message m = new DefaultSocks5InitialRequest(Socks5AuthMethod.NO_AUTH);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
