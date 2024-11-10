package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RequestPermissionsPacket implements BedrockPacket {
    private int customPermissions;
    private PlayerPermission permissions;
    private long uniqueEntityId;

    public void setCustomPermissions(int customPermissions) {
        this.customPermissions = customPermissions;
    }

    public void setPermissions(PlayerPermission permissions) {
        this.permissions = permissions;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestPermissionsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RequestPermissionsPacket)) {
            return false;
        }
        RequestPermissionsPacket other = (RequestPermissionsPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.customPermissions != other.customPermissions) {
            return false;
        }
        Object this$permissions = this.permissions;
        Object other$permissions = other.permissions;
        return this$permissions != null ? this$permissions.equals(other$permissions) : other$permissions == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        int result2 = (result * 59) + this.customPermissions;
        Object $permissions = this.permissions;
        return (result2 * 59) + ($permissions == null ? 43 : $permissions.hashCode());
    }

    public String toString() {
        return "RequestPermissionsPacket(uniqueEntityId=" + this.uniqueEntityId + ", permissions=" + this.permissions + ", customPermissions=" + this.customPermissions + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public PlayerPermission getPermissions() {
        return this.permissions;
    }

    public int getCustomPermissions() {
        return this.customPermissions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REQUEST_PERMISSIONS;
    }
}
