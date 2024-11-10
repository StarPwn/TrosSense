package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class FilterTextPacket implements BedrockPacket {
    private boolean fromServer;
    private String text;

    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FilterTextPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FilterTextPacket)) {
            return false;
        }
        FilterTextPacket other = (FilterTextPacket) o;
        if (!other.canEqual(this) || this.fromServer != other.fromServer) {
            return false;
        }
        Object this$text = this.text;
        Object other$text = other.text;
        return this$text != null ? this$text.equals(other$text) : other$text == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.fromServer ? 79 : 97);
        Object $text = this.text;
        return (result * 59) + ($text == null ? 43 : $text.hashCode());
    }

    public String toString() {
        return "FilterTextPacket(text=" + this.text + ", fromServer=" + this.fromServer + ")";
    }

    public String getText() {
        return this.text;
    }

    public boolean isFromServer() {
        return this.fromServer;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.FILTER_TEXT;
    }
}
