package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetEntityLinkPacket implements BedrockPacket {
    private EntityLinkData entityLink;

    public void setEntityLink(EntityLinkData entityLink) {
        this.entityLink = entityLink;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetEntityLinkPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetEntityLinkPacket)) {
            return false;
        }
        SetEntityLinkPacket other = (SetEntityLinkPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$entityLink = this.entityLink;
        Object other$entityLink = other.entityLink;
        return this$entityLink != null ? this$entityLink.equals(other$entityLink) : other$entityLink == null;
    }

    public int hashCode() {
        Object $entityLink = this.entityLink;
        int result = (1 * 59) + ($entityLink == null ? 43 : $entityLink.hashCode());
        return result;
    }

    public String toString() {
        return "SetEntityLinkPacket(entityLink=" + this.entityLink + ")";
    }

    public EntityLinkData getEntityLink() {
        return this.entityLink;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_ENTITY_LINK;
    }
}
