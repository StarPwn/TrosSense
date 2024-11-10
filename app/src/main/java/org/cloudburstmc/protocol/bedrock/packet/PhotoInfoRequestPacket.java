package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PhotoInfoRequestPacket implements BedrockPacket {
    private long photoId;

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PhotoInfoRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PhotoInfoRequestPacket)) {
            return false;
        }
        PhotoInfoRequestPacket other = (PhotoInfoRequestPacket) o;
        return other.canEqual(this) && this.photoId == other.photoId;
    }

    public int hashCode() {
        long $photoId = this.photoId;
        int result = (1 * 59) + ((int) (($photoId >>> 32) ^ $photoId));
        return result;
    }

    public String toString() {
        return "PhotoInfoRequestPacket(photoId=" + this.photoId + ")";
    }

    public long getPhotoId() {
        return this.photoId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PHOTO_INFO_REQUEST;
    }
}
