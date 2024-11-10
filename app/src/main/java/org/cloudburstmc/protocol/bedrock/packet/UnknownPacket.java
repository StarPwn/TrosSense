package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCounted;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public final class UnknownPacket implements BedrockPacket, BedrockPacketSerializer<UnknownPacket>, ReferenceCounted {
    private int packetId;
    private ByteBuf payload;

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public void setPayload(ByteBuf payload) {
        this.payload = payload;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnknownPacket)) {
            return false;
        }
        UnknownPacket other = (UnknownPacket) o;
        if (this.packetId != other.packetId) {
            return false;
        }
        Object this$payload = this.payload;
        Object other$payload = other.payload;
        return this$payload != null ? this$payload.equals(other$payload) : other$payload == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.packetId;
        Object $payload = this.payload;
        return (result * 59) + ($payload == null ? 43 : $payload.hashCode());
    }

    public int getPacketId() {
        return this.packetId;
    }

    public ByteBuf getPayload() {
        return this.payload;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UnknownPacket packet) {
        buffer.writeBytes(packet.payload, packet.payload.readerIndex(), packet.payload.readableBytes());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UnknownPacket packet) {
        packet.payload = buffer.readRetainedSlice(buffer.readableBytes());
    }

    public String toString() {
        return "UNKNOWN - " + getPacketId() + " - Hex: " + ((this.payload == null || this.payload.refCnt() == 0) ? "null" : ByteBufUtil.hexDump(this.payload));
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return PacketSignal.UNHANDLED;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UNKNOWN;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        if (this.payload == null) {
            return 0;
        }
        return this.payload.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public UnknownPacket retain() {
        if (this.payload != null) {
            this.payload.retain();
        }
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public UnknownPacket retain(int increment) {
        if (this.payload != null) {
            this.payload.retain(increment);
        }
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public UnknownPacket touch() {
        if (this.payload != null) {
            this.payload.touch();
        }
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public UnknownPacket touch(Object hint) {
        if (this.payload != null) {
            this.payload.touch(hint);
        }
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.payload == null || this.payload.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.payload == null || this.payload.release(decrement);
    }
}
