package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CodeBuilderPacket implements BedrockPacket {
    private boolean opening;
    private String url;

    public void setOpening(boolean opening) {
        this.opening = opening;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CodeBuilderPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeBuilderPacket)) {
            return false;
        }
        CodeBuilderPacket other = (CodeBuilderPacket) o;
        if (!other.canEqual(this) || this.opening != other.opening) {
            return false;
        }
        Object this$url = this.url;
        Object other$url = other.url;
        return this$url != null ? this$url.equals(other$url) : other$url == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.opening ? 79 : 97);
        Object $url = this.url;
        return (result * 59) + ($url == null ? 43 : $url.hashCode());
    }

    public String toString() {
        return "CodeBuilderPacket(url=" + this.url + ", opening=" + this.opening + ")";
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isOpening() {
        return this.opening;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CODE_BUILDER;
    }
}
