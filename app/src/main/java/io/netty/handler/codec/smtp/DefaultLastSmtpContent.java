package io.netty.handler.codec.smtp;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public final class DefaultLastSmtpContent extends DefaultSmtpContent implements LastSmtpContent {
    public DefaultLastSmtpContent(ByteBuf data) {
        super(data);
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastSmtpContent copy() {
        return (LastSmtpContent) super.copy();
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastSmtpContent duplicate() {
        return (LastSmtpContent) super.duplicate();
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastSmtpContent retainedDuplicate() {
        return (LastSmtpContent) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastSmtpContent replace(ByteBuf content) {
        return new DefaultLastSmtpContent(content);
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultLastSmtpContent retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultLastSmtpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultLastSmtpContent touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.smtp.DefaultSmtpContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public DefaultLastSmtpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
