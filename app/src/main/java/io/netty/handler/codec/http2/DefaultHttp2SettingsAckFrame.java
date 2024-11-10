package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
final class DefaultHttp2SettingsAckFrame implements Http2SettingsAckFrame {
    @Override // io.netty.handler.codec.http2.Http2SettingsAckFrame, io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "SETTINGS(ACK)";
    }

    public String toString() {
        return StringUtil.simpleClassName(this);
    }
}
