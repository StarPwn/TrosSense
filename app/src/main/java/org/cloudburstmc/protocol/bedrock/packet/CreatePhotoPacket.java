package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CreatePhotoPacket implements BedrockPacket {
    private long id;
    private String photoItemName;
    private String photoName;

    public void setId(long id) {
        this.id = id;
    }

    public void setPhotoItemName(String photoItemName) {
        this.photoItemName = photoItemName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CreatePhotoPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreatePhotoPacket)) {
            return false;
        }
        CreatePhotoPacket other = (CreatePhotoPacket) o;
        if (!other.canEqual(this) || this.id != other.id) {
            return false;
        }
        Object this$photoName = this.photoName;
        Object other$photoName = other.photoName;
        if (this$photoName != null ? !this$photoName.equals(other$photoName) : other$photoName != null) {
            return false;
        }
        Object this$photoItemName = this.photoItemName;
        Object other$photoItemName = other.photoItemName;
        return this$photoItemName != null ? this$photoItemName.equals(other$photoItemName) : other$photoItemName == null;
    }

    public int hashCode() {
        long $id = this.id;
        int result = (1 * 59) + ((int) (($id >>> 32) ^ $id));
        Object $photoName = this.photoName;
        int result2 = (result * 59) + ($photoName == null ? 43 : $photoName.hashCode());
        Object $photoItemName = this.photoItemName;
        return (result2 * 59) + ($photoItemName != null ? $photoItemName.hashCode() : 43);
    }

    public String toString() {
        return "CreatePhotoPacket(id=" + this.id + ", photoName=" + this.photoName + ", photoItemName=" + this.photoItemName + ")";
    }

    public long getId() {
        return this.id;
    }

    public String getPhotoName() {
        return this.photoName;
    }

    public String getPhotoItemName() {
        return this.photoItemName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CREATE_PHOTO;
    }
}
