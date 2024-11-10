package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.function.Consumer;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientCacheMissResponsePacket extends AbstractReferenceCounted implements BedrockPacket {
    private final Long2ObjectMap<ByteBuf> blobs = new Long2ObjectLinkedOpenHashMap();

    protected boolean canEqual(Object other) {
        return other instanceof ClientCacheMissResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientCacheMissResponsePacket)) {
            return false;
        }
        ClientCacheMissResponsePacket other = (ClientCacheMissResponsePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$blobs = this.blobs;
        Object other$blobs = other.blobs;
        return this$blobs != null ? this$blobs.equals(other$blobs) : other$blobs == null;
    }

    public int hashCode() {
        Object $blobs = this.blobs;
        int result = (1 * 59) + ($blobs == null ? 43 : $blobs.hashCode());
        return result;
    }

    public String toString() {
        return "ClientCacheMissResponsePacket(blobs=" + this.blobs + ")";
    }

    public Long2ObjectMap<ByteBuf> getBlobs() {
        return this.blobs;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENT_CACHE_MISS_RESPONSE;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        this.blobs.values().forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.packet.ClientCacheMissResponsePacket$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ByteBuf) obj).release();
            }
        });
    }

    @Override // io.netty.util.ReferenceCounted
    public ClientCacheMissResponsePacket touch(final Object hint) {
        this.blobs.values().forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.packet.ClientCacheMissResponsePacket$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ByteBuf) obj).touch(hint);
            }
        });
        return this;
    }
}
