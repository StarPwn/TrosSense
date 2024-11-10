package org.cloudburstmc.protocol.bedrock.packet;

import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerSkinPacket implements BedrockPacket {
    private String newSkinName;
    private String oldSkinName;
    private SerializedSkin skin;
    private boolean trustedSkin;
    private UUID uuid;

    public void setNewSkinName(String newSkinName) {
        this.newSkinName = newSkinName;
    }

    public void setOldSkinName(String oldSkinName) {
        this.oldSkinName = oldSkinName;
    }

    public void setSkin(SerializedSkin skin) {
        this.skin = skin;
    }

    public void setTrustedSkin(boolean trustedSkin) {
        this.trustedSkin = trustedSkin;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerSkinPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerSkinPacket)) {
            return false;
        }
        PlayerSkinPacket other = (PlayerSkinPacket) o;
        if (!other.canEqual(this) || this.trustedSkin != other.trustedSkin) {
            return false;
        }
        Object this$uuid = this.uuid;
        Object other$uuid = other.uuid;
        if (this$uuid != null ? !this$uuid.equals(other$uuid) : other$uuid != null) {
            return false;
        }
        Object this$skin = this.skin;
        Object other$skin = other.skin;
        if (this$skin != null ? !this$skin.equals(other$skin) : other$skin != null) {
            return false;
        }
        Object this$newSkinName = this.newSkinName;
        Object other$newSkinName = other.newSkinName;
        if (this$newSkinName != null ? !this$newSkinName.equals(other$newSkinName) : other$newSkinName != null) {
            return false;
        }
        Object this$oldSkinName = this.oldSkinName;
        Object other$oldSkinName = other.oldSkinName;
        return this$oldSkinName != null ? this$oldSkinName.equals(other$oldSkinName) : other$oldSkinName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.trustedSkin ? 79 : 97);
        Object $uuid = this.uuid;
        int result2 = (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $skin = this.skin;
        int result3 = (result2 * 59) + ($skin == null ? 43 : $skin.hashCode());
        Object $newSkinName = this.newSkinName;
        int result4 = (result3 * 59) + ($newSkinName == null ? 43 : $newSkinName.hashCode());
        Object $oldSkinName = this.oldSkinName;
        return (result4 * 59) + ($oldSkinName != null ? $oldSkinName.hashCode() : 43);
    }

    public String toString() {
        return "PlayerSkinPacket(uuid=" + this.uuid + ", skin=" + this.skin + ", newSkinName=" + this.newSkinName + ", oldSkinName=" + this.oldSkinName + ", trustedSkin=" + this.trustedSkin + ")";
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public SerializedSkin getSkin() {
        return this.skin;
    }

    public String getNewSkinName() {
        return this.newSkinName;
    }

    public String getOldSkinName() {
        return this.oldSkinName;
    }

    public boolean isTrustedSkin() {
        return this.trustedSkin;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_SKIN;
    }
}
