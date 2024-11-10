package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Arrays;
import org.cloudburstmc.protocol.bedrock.data.PhotoType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PhotoTransferPacket implements BedrockPacket {
    private String bookId;
    private byte[] data;
    private String name;
    private String newPhotoName;
    private long ownerId;
    private PhotoType photoType;
    private PhotoType sourceType;

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNewPhotoName(String newPhotoName) {
        this.newPhotoName = newPhotoName;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public void setSourceType(PhotoType sourceType) {
        this.sourceType = sourceType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PhotoTransferPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PhotoTransferPacket)) {
            return false;
        }
        PhotoTransferPacket other = (PhotoTransferPacket) o;
        if (!other.canEqual(this) || this.ownerId != other.ownerId) {
            return false;
        }
        Object this$name = this.name;
        Object other$name = other.name;
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        Object this$bookId = this.bookId;
        Object other$bookId = other.bookId;
        if (this$bookId != null ? !this$bookId.equals(other$bookId) : other$bookId != null) {
            return false;
        }
        Object this$photoType = this.photoType;
        Object other$photoType = other.photoType;
        if (this$photoType != null ? !this$photoType.equals(other$photoType) : other$photoType != null) {
            return false;
        }
        Object this$sourceType = this.sourceType;
        Object other$sourceType = other.sourceType;
        if (this$sourceType != null ? !this$sourceType.equals(other$sourceType) : other$sourceType != null) {
            return false;
        }
        Object this$newPhotoName = this.newPhotoName;
        Object other$newPhotoName = other.newPhotoName;
        return this$newPhotoName != null ? this$newPhotoName.equals(other$newPhotoName) : other$newPhotoName == null;
    }

    public int hashCode() {
        long $ownerId = this.ownerId;
        int result = (1 * 59) + ((int) (($ownerId >>> 32) ^ $ownerId));
        Object $name = this.name;
        int result2 = (((result * 59) + ($name == null ? 43 : $name.hashCode())) * 59) + Arrays.hashCode(this.data);
        Object $bookId = this.bookId;
        int result3 = (result2 * 59) + ($bookId == null ? 43 : $bookId.hashCode());
        Object $photoType = this.photoType;
        int result4 = (result3 * 59) + ($photoType == null ? 43 : $photoType.hashCode());
        Object $sourceType = this.sourceType;
        int result5 = (result4 * 59) + ($sourceType == null ? 43 : $sourceType.hashCode());
        Object $newPhotoName = this.newPhotoName;
        return (result5 * 59) + ($newPhotoName != null ? $newPhotoName.hashCode() : 43);
    }

    public String toString() {
        return "PhotoTransferPacket(name=" + this.name + ", data=" + Arrays.toString(this.data) + ", bookId=" + this.bookId + ", photoType=" + this.photoType + ", sourceType=" + this.sourceType + ", ownerId=" + this.ownerId + ", newPhotoName=" + this.newPhotoName + ")";
    }

    public String getName() {
        return this.name;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getBookId() {
        return this.bookId;
    }

    public PhotoType getPhotoType() {
        return this.photoType;
    }

    public PhotoType getSourceType() {
        return this.sourceType;
    }

    public long getOwnerId() {
        return this.ownerId;
    }

    public String getNewPhotoName() {
        return this.newPhotoName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PHOTO_TRANSFER;
    }
}
