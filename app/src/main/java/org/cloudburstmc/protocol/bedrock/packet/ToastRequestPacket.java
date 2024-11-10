package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ToastRequestPacket implements BedrockPacket {
    private String content;
    private String title;

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ToastRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ToastRequestPacket)) {
            return false;
        }
        ToastRequestPacket other = (ToastRequestPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$title = this.title;
        Object other$title = other.title;
        if (this$title != null ? !this$title.equals(other$title) : other$title != null) {
            return false;
        }
        Object this$content = this.content;
        Object other$content = other.content;
        return this$content != null ? this$content.equals(other$content) : other$content == null;
    }

    public int hashCode() {
        Object $title = this.title;
        int result = (1 * 59) + ($title == null ? 43 : $title.hashCode());
        Object $content = this.content;
        return (result * 59) + ($content != null ? $content.hashCode() : 43);
    }

    public String toString() {
        return "ToastRequestPacket(title=" + this.title + ", content=" + this.content + ")";
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TOAST_REQUEST;
    }
}
