package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.EduSharedUriResource;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EduUriResourcePacket implements BedrockPacket {
    private EduSharedUriResource eduSharedUriResource;

    public void setEduSharedUriResource(EduSharedUriResource eduSharedUriResource) {
        this.eduSharedUriResource = eduSharedUriResource;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EduUriResourcePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EduUriResourcePacket)) {
            return false;
        }
        EduUriResourcePacket other = (EduUriResourcePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$eduSharedUriResource = this.eduSharedUriResource;
        Object other$eduSharedUriResource = other.eduSharedUriResource;
        return this$eduSharedUriResource != null ? this$eduSharedUriResource.equals(other$eduSharedUriResource) : other$eduSharedUriResource == null;
    }

    public int hashCode() {
        Object $eduSharedUriResource = this.eduSharedUriResource;
        int result = (1 * 59) + ($eduSharedUriResource == null ? 43 : $eduSharedUriResource.hashCode());
        return result;
    }

    public String toString() {
        return "EduUriResourcePacket(eduSharedUriResource=" + this.eduSharedUriResource + ")";
    }

    public EduSharedUriResource getEduSharedUriResource() {
        return this.eduSharedUriResource;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EDU_URI_RESOURCE;
    }
}
