package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultStompFrame extends DefaultStompHeadersSubframe implements StompFrame {
    private final ByteBuf content;

    public DefaultStompFrame(StompCommand command) {
        this(command, Unpooled.buffer(0));
    }

    public DefaultStompFrame(StompCommand command, ByteBuf content) {
        this(command, content, null);
    }

    DefaultStompFrame(StompCommand command, ByteBuf content, DefaultStompHeaders headers) {
        super(command, headers);
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.content;
    }

    @Override // io.netty.handler.codec.stomp.StompContentSubframe, io.netty.buffer.ByteBufHolder
    public StompFrame copy() {
        return replace(this.content.copy());
    }

    @Override // io.netty.handler.codec.stomp.StompContentSubframe, io.netty.buffer.ByteBufHolder
    public StompFrame duplicate() {
        return replace(this.content.duplicate());
    }

    @Override // io.netty.handler.codec.stomp.StompContentSubframe, io.netty.buffer.ByteBufHolder
    public StompFrame retainedDuplicate() {
        return replace(this.content.retainedDuplicate());
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [io.netty.handler.codec.stomp.DefaultStompHeaders] */
    @Override // io.netty.handler.codec.stomp.StompContentSubframe, io.netty.buffer.ByteBufHolder
    public StompFrame replace(ByteBuf content) {
        return new DefaultStompFrame(this.command, content, this.headers.copy());
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.content.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public StompFrame retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public StompFrame retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public StompFrame touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public StompFrame touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.content.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.content.release(decrement);
    }

    @Override // io.netty.handler.codec.stomp.DefaultStompHeadersSubframe
    public String toString() {
        return "DefaultStompFrame{command=" + this.command + ", headers=" + this.headers + ", content=" + this.content.toString(CharsetUtil.UTF_8) + '}';
    }
}
