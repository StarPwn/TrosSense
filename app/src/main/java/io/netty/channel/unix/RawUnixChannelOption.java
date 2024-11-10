package io.netty.channel.unix;

import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;

/* loaded from: classes4.dex */
public final class RawUnixChannelOption extends GenericUnixChannelOption<ByteBuffer> {
    private final int length;

    public RawUnixChannelOption(String name, int level, int optname, int length) {
        super(name, level, optname);
        this.length = ObjectUtil.checkPositive(length, "length");
    }

    public int length() {
        return this.length;
    }

    @Override // io.netty.channel.ChannelOption
    public void validate(ByteBuffer value) {
        super.validate((RawUnixChannelOption) value);
        if (value.remaining() != this.length) {
            throw new IllegalArgumentException("Length of value does not match. Expected " + this.length + ", but got " + value.remaining());
        }
    }
}
