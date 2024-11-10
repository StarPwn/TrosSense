package io.netty.handler.codec.smtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes4.dex */
public final class SmtpRequestEncoder extends MessageToMessageEncoder<Object> {
    private static final int CRLF_SHORT = 3338;
    private static final ByteBuf DOT_CRLF_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(3).writeByte(46).writeByte(13).writeByte(10)).asReadOnly();
    private static final byte SP = 32;
    private boolean contentExpected;

    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return (msg instanceof SmtpRequest) || (msg instanceof SmtpContent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof SmtpRequest) {
            SmtpRequest req = (SmtpRequest) msg;
            if (this.contentExpected) {
                if (req.command().equals(SmtpCommand.RSET)) {
                    this.contentExpected = false;
                } else {
                    throw new IllegalStateException("SmtpContent expected");
                }
            }
            boolean release = true;
            ByteBuf buffer = ctx.alloc().buffer();
            try {
                req.command().encode(buffer);
                boolean notEmpty = req.command() != SmtpCommand.EMPTY;
                writeParameters(req.parameters(), buffer, notEmpty);
                ByteBufUtil.writeShortBE(buffer, CRLF_SHORT);
                out.add(buffer);
                release = false;
                if (req.command().isContentExpected()) {
                    this.contentExpected = true;
                }
            } finally {
                if (release) {
                    buffer.release();
                }
            }
        }
        if (msg instanceof SmtpContent) {
            if (!this.contentExpected) {
                throw new IllegalStateException("No SmtpContent expected");
            }
            ByteBuf content = ((SmtpContent) msg).content();
            out.add(content.retain());
            if (msg instanceof LastSmtpContent) {
                out.add(DOT_CRLF_BUFFER.retainedDuplicate());
                this.contentExpected = false;
            }
        }
    }

    private static void writeParameters(List<CharSequence> parameters, ByteBuf out, boolean commandNotEmpty) {
        if (parameters.isEmpty()) {
            return;
        }
        if (commandNotEmpty) {
            out.writeByte(32);
        }
        if (parameters instanceof RandomAccess) {
            int sizeMinusOne = parameters.size() - 1;
            for (int i = 0; i < sizeMinusOne; i++) {
                ByteBufUtil.writeAscii(out, parameters.get(i));
                out.writeByte(32);
            }
            ByteBufUtil.writeAscii(out, parameters.get(sizeMinusOne));
            return;
        }
        Iterator<CharSequence> params = parameters.iterator();
        while (true) {
            ByteBufUtil.writeAscii(out, params.next());
            if (params.hasNext()) {
                out.writeByte(32);
            } else {
                return;
            }
        }
    }
}
