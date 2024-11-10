package org.cloudburstmc.netty.channel.raknet.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import org.cloudburstmc.netty.channel.raknet.RakPriority;
import org.cloudburstmc.netty.channel.raknet.RakReliability;

/* loaded from: classes5.dex */
public final class RakMessage extends DefaultByteBufHolder {
    private final int channel;
    private final RakPriority priority;
    private final RakReliability reliability;

    public RakMessage(ByteBuf payloadBuffer) {
        this(payloadBuffer, RakReliability.RELIABLE_ORDERED, RakPriority.NORMAL, 0);
    }

    public RakMessage(ByteBuf payloadBuffer, RakReliability reliability) {
        this(payloadBuffer, reliability, RakPriority.NORMAL, 0);
    }

    public RakMessage(ByteBuf payloadBuffer, RakReliability reliability, RakPriority priority) {
        this(payloadBuffer, reliability, priority, 0);
    }

    public RakMessage(ByteBuf payloadBuffer, RakReliability reliability, RakPriority priority, int channel) {
        super(payloadBuffer);
        this.reliability = reliability;
        this.priority = priority;
        this.channel = channel;
    }

    public RakReliability reliability() {
        return this.reliability;
    }

    public RakPriority priority() {
        return this.priority;
    }

    public int channel() {
        return this.channel;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RakMessage message = (RakMessage) o;
        if (this.reliability != message.reliability || this.priority != message.priority || this.channel != message.channel) {
            return false;
        }
        return content().equals(message.content());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public int hashCode() {
        int result = this.reliability.hashCode();
        return (((((result * 31) + this.priority.hashCode()) * 31) + this.channel) * 31) + content().hashCode();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public RakMessage copy() {
        return (RakMessage) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public RakMessage duplicate() {
        return (RakMessage) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public RakMessage retainedDuplicate() {
        return (RakMessage) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public RakMessage replace(ByteBuf content) {
        return new RakMessage(content, this.reliability, this.priority, this.channel);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public RakMessage retain() {
        return (RakMessage) super.retain();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public RakMessage touch() {
        return (RakMessage) super.touch();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return "RakMessage{reliability=" + this.reliability + ", priority=" + this.priority + ", channel=" + this.channel + "}";
    }
}
