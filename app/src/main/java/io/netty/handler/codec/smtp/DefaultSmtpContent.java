package io.netty.handler.codec.smtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

/* loaded from: classes4.dex */
public class DefaultSmtpContent extends DefaultByteBufHolder implements SmtpContent {
    public DefaultSmtpContent(ByteBuf data) {
        super(data);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public SmtpContent copy() {
        return (SmtpContent) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public SmtpContent duplicate() {
        return (SmtpContent) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public SmtpContent retainedDuplicate() {
        return (SmtpContent) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public SmtpContent replace(ByteBuf content) {
        return new DefaultSmtpContent(content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public SmtpContent retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public SmtpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public SmtpContent touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public SmtpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
