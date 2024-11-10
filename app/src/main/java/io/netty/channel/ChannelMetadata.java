package io.netty.channel;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class ChannelMetadata {
    private final int defaultMaxMessagesPerRead;
    private final boolean hasDisconnect;

    public ChannelMetadata(boolean hasDisconnect) {
        this(hasDisconnect, 16);
    }

    public ChannelMetadata(boolean hasDisconnect, int defaultMaxMessagesPerRead) {
        ObjectUtil.checkPositive(defaultMaxMessagesPerRead, "defaultMaxMessagesPerRead");
        this.hasDisconnect = hasDisconnect;
        this.defaultMaxMessagesPerRead = defaultMaxMessagesPerRead;
    }

    public boolean hasDisconnect() {
        return this.hasDisconnect;
    }

    public int defaultMaxMessagesPerRead() {
        return this.defaultMaxMessagesPerRead;
    }
}
