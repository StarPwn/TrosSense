package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultSpdyPingFrame implements SpdyPingFrame {
    private int id;

    public DefaultSpdyPingFrame(int id) {
        setId(id);
    }

    @Override // io.netty.handler.codec.spdy.SpdyPingFrame
    public int id() {
        return this.id;
    }

    @Override // io.netty.handler.codec.spdy.SpdyPingFrame
    public SpdyPingFrame setId(int id) {
        this.id = id;
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> ID = " + id();
    }
}
