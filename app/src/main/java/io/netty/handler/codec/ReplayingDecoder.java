package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;
import io.netty.util.internal.StringUtil;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder {
    static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class, "REPLAY");
    private int checkpoint;
    private final ReplayingDecoderByteBuf replayable;
    private S state;

    /* JADX INFO: Access modifiers changed from: protected */
    public ReplayingDecoder() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ReplayingDecoder(S initialState) {
        this.replayable = new ReplayingDecoderByteBuf();
        this.checkpoint = -1;
        this.state = initialState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkpoint() {
        this.checkpoint = internalBuffer().readerIndex();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkpoint(S state) {
        checkpoint();
        state(state);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public S state() {
        return this.state;
    }

    protected S state(S newState) {
        S oldState = this.state;
        this.state = newState;
        return oldState;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    final void channelInputClosed(ChannelHandlerContext ctx, List<Object> out) throws Exception {
        try {
            this.replayable.terminate();
            if (this.cumulation != null) {
                callDecode(ctx, internalBuffer(), out);
            } else {
                this.replayable.setCumulation(Unpooled.EMPTY_BUFFER);
            }
            decodeLast(ctx, this.replayable, out);
        } catch (Signal replay) {
            replay.expect(REPLAY);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int checkpoint;
        this.replayable.setCumulation(in);
        while (in.isReadable()) {
            try {
                int oldReaderIndex = in.readerIndex();
                this.checkpoint = oldReaderIndex;
                int outSize = out.size();
                if (outSize > 0) {
                    fireChannelRead(ctx, out, outSize);
                    out.clear();
                    if (!ctx.isRemoved()) {
                        outSize = 0;
                    } else {
                        return;
                    }
                }
                S oldState = this.state;
                int oldInputLength = in.readableBytes();
                try {
                    decodeRemovalReentryProtection(ctx, this.replayable, out);
                    if (!ctx.isRemoved()) {
                        if (outSize == out.size()) {
                            if (oldInputLength == in.readableBytes() && oldState == this.state) {
                                throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() must consume the inbound data or change its state if it did not decode anything.");
                            }
                        } else {
                            if (oldReaderIndex == in.readerIndex() && oldState == this.state) {
                                throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() method must consume the inbound data or change its state if it decoded something.");
                            }
                            if (isSingleDecode()) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                } catch (Signal replay) {
                    replay.expect(REPLAY);
                    if (!ctx.isRemoved() && (checkpoint = this.checkpoint) >= 0) {
                        in.readerIndex(checkpoint);
                        return;
                    }
                    return;
                }
            } catch (DecoderException e) {
                throw e;
            } catch (Exception cause) {
                throw new DecoderException(cause);
            }
        }
    }
}
