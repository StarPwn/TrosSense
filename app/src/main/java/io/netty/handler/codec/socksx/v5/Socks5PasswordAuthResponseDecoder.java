package io.netty.handler.codec.socksx.v5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class Socks5PasswordAuthResponseDecoder extends ReplayingDecoder<State> {

    /* loaded from: classes4.dex */
    public enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5PasswordAuthResponseDecoder() {
        super(State.INIT);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            switch (state()) {
                case INIT:
                    byte version = in.readByte();
                    if (version != 1) {
                        throw new DecoderException("unsupported subnegotiation version: " + ((int) version) + " (expected: 1)");
                    }
                    out.add(new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.valueOf(in.readByte())));
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
        Socks5Message m = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}
