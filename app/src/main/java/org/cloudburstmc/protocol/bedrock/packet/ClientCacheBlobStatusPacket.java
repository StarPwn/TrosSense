package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientCacheBlobStatusPacket implements BedrockPacket {
    private final LongList acks = new LongArrayList();
    private final LongList naks = new LongArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof ClientCacheBlobStatusPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientCacheBlobStatusPacket)) {
            return false;
        }
        ClientCacheBlobStatusPacket other = (ClientCacheBlobStatusPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$acks = this.acks;
        Object other$acks = other.acks;
        if (this$acks != null ? !this$acks.equals(other$acks) : other$acks != null) {
            return false;
        }
        Object this$naks = this.naks;
        Object other$naks = other.naks;
        return this$naks != null ? this$naks.equals(other$naks) : other$naks == null;
    }

    public int hashCode() {
        Object $acks = this.acks;
        int result = (1 * 59) + ($acks == null ? 43 : $acks.hashCode());
        Object $naks = this.naks;
        return (result * 59) + ($naks != null ? $naks.hashCode() : 43);
    }

    public String toString() {
        return "ClientCacheBlobStatusPacket(acks=" + this.acks + ", naks=" + this.naks + ")";
    }

    public LongList getAcks() {
        return this.acks;
    }

    public LongList getNaks() {
        return this.naks;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENT_CACHE_BLOB_STATUS;
    }
}
