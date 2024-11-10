package org.cloudburstmc.protocol.bedrock.netty;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

/* loaded from: classes5.dex */
public class BedrockPacketWrapper extends AbstractReferenceCounted {
    private int headerLength;
    private BedrockPacket packet;
    private ByteBuf packetBuffer;
    private int packetId;
    private int senderSubClientId;
    private int targetSubClientId;

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public void setPacket(BedrockPacket packet) {
        this.packet = packet;
    }

    public void setPacketBuffer(ByteBuf packetBuffer) {
        this.packetBuffer = packetBuffer;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public void setSenderSubClientId(int senderSubClientId) {
        this.senderSubClientId = senderSubClientId;
    }

    public void setTargetSubClientId(int targetSubClientId) {
        this.targetSubClientId = targetSubClientId;
    }

    public String toString() {
        return "BedrockPacketWrapper(packetId=" + getPacketId() + ", senderSubClientId=" + getSenderSubClientId() + ", targetSubClientId=" + getTargetSubClientId() + ", headerLength=" + getHeaderLength() + ", packet=" + getPacket() + ", packetBuffer=" + getPacketBuffer() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof BedrockPacketWrapper;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BedrockPacketWrapper)) {
            return false;
        }
        BedrockPacketWrapper other = (BedrockPacketWrapper) o;
        if (!other.canEqual(this) || getPacketId() != other.getPacketId() || getSenderSubClientId() != other.getSenderSubClientId() || getTargetSubClientId() != other.getTargetSubClientId() || getHeaderLength() != other.getHeaderLength()) {
            return false;
        }
        Object this$packet = getPacket();
        Object other$packet = other.getPacket();
        if (this$packet != null ? !this$packet.equals(other$packet) : other$packet != null) {
            return false;
        }
        Object this$packetBuffer = getPacketBuffer();
        Object other$packetBuffer = other.getPacketBuffer();
        return this$packetBuffer != null ? this$packetBuffer.equals(other$packetBuffer) : other$packetBuffer == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getPacketId();
        int result2 = (((((result * 59) + getSenderSubClientId()) * 59) + getTargetSubClientId()) * 59) + getHeaderLength();
        Object $packet = getPacket();
        int result3 = (result2 * 59) + ($packet == null ? 43 : $packet.hashCode());
        Object $packetBuffer = getPacketBuffer();
        return (result3 * 59) + ($packetBuffer != null ? $packetBuffer.hashCode() : 43);
    }

    public BedrockPacketWrapper(int packetId, int senderSubClientId, int targetSubClientId, int headerLength, BedrockPacket packet, ByteBuf packetBuffer) {
        this.packetId = packetId;
        this.senderSubClientId = senderSubClientId;
        this.targetSubClientId = targetSubClientId;
        this.headerLength = headerLength;
        this.packet = packet;
        this.packetBuffer = packetBuffer;
    }

    public BedrockPacketWrapper() {
    }

    public int getPacketId() {
        return this.packetId;
    }

    public int getSenderSubClientId() {
        return this.senderSubClientId;
    }

    public int getTargetSubClientId() {
        return this.targetSubClientId;
    }

    public int getHeaderLength() {
        return this.headerLength;
    }

    public BedrockPacket getPacket() {
        return this.packet;
    }

    public ByteBuf getPacketBuffer() {
        return this.packetBuffer;
    }

    public BedrockPacketWrapper(int packetId, int senderSubClientId, int targetSubClientId, BedrockPacket packet, ByteBuf packetBuffer) {
        this.packetId = packetId;
        this.senderSubClientId = senderSubClientId;
        this.targetSubClientId = targetSubClientId;
        this.packet = packet;
        this.packetBuffer = packetBuffer;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        ReferenceCountUtil.safeRelease(this.packet);
        ReferenceCountUtil.safeRelease(this.packetBuffer);
    }

    @Override // io.netty.util.ReferenceCounted
    public BedrockPacketWrapper touch(Object hint) {
        ReferenceCountUtil.touch(this.packet);
        ReferenceCountUtil.touch(this.packetBuffer);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BedrockPacketWrapper retain() {
        return (BedrockPacketWrapper) super.retain();
    }
}
