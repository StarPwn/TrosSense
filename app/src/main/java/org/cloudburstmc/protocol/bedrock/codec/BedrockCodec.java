package org.cloudburstmc.protocol.bedrock.codec;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UnknownPacket;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public final class BedrockCodec {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BedrockCodec.class);
    private final Supplier<BedrockCodecHelper> helperFactory;
    private final String minecraftVersion;
    private final Map<Class<? extends BedrockPacket>, BedrockPacketDefinition<? extends BedrockPacket>> packetsByClass;
    private final BedrockPacketDefinition<? extends BedrockPacket>[] packetsById;
    private final int protocolVersion;
    private final int raknetProtocolVersion;

    public static Builder builder() {
        return new Builder();
    }

    public BedrockPacket tryDecode(BedrockCodecHelper bedrockCodecHelper, ByteBuf byteBuf, int i) throws PacketSerializeException {
        BedrockPacket bedrockPacket;
        BedrockPacketSerializer<? extends BedrockPacket> bedrockPacketSerializer;
        BedrockPacketDefinition<? extends BedrockPacket> packetDefinition = getPacketDefinition(i);
        if (packetDefinition == null) {
            UnknownPacket unknownPacket = new UnknownPacket();
            unknownPacket.setPacketId(i);
            bedrockPacket = unknownPacket;
            bedrockPacketSerializer = unknownPacket;
        } else {
            bedrockPacket = packetDefinition.getFactory().get();
            bedrockPacketSerializer = packetDefinition.getSerializer();
        }
        int readerIndex = byteBuf.readerIndex();
        boolean z = false;
        try {
            bedrockPacketSerializer.deserialize(byteBuf, bedrockCodecHelper, bedrockPacket);
        } catch (Exception e) {
            log.error("Error whilst deserializing " + bedrockPacket, (Throwable) e);
            z = true;
        }
        if (!z && byteBuf.isReadable()) {
            z = true;
            if (log.isDebugEnabled()) {
                log.debug(bedrockPacket.getClass().getSimpleName() + " still has " + byteBuf.readableBytes() + " bytes to read!");
            }
        }
        if (z) {
            byteBuf.readerIndex(readerIndex);
            UnknownPacket unknownPacket2 = new UnknownPacket();
            unknownPacket2.setPacketId(i);
            unknownPacket2.deserialize(byteBuf, bedrockCodecHelper, unknownPacket2);
            return unknownPacket2;
        }
        return bedrockPacket;
    }

    public <T extends BedrockPacket> void tryEncode(BedrockCodecHelper helper, ByteBuf buf, T packet) throws PacketSerializeException {
        BedrockPacketSerializer<T> serializer;
        try {
            if (packet instanceof UnknownPacket) {
                serializer = (BedrockPacketSerializer) packet;
            } else {
                BedrockPacketDefinition<T> definition = getPacketDefinition(packet.getClass());
                serializer = definition.getSerializer();
            }
            serializer.serialize(buf, helper, packet);
        } catch (Exception e) {
            throw new PacketSerializeException("Error whilst serializing " + packet, e);
        }
    }

    public <T extends BedrockPacket> BedrockPacketDefinition<T> getPacketDefinition(Class<T> packet) {
        Preconditions.checkNotNull(packet, "packet");
        return (BedrockPacketDefinition) this.packetsByClass.get(packet);
    }

    public BedrockPacketDefinition<? extends BedrockPacket> getPacketDefinition(int id) {
        if (id < 0 || id >= this.packetsById.length) {
            return null;
        }
        return this.packetsById[id];
    }

    public BedrockCodecHelper createHelper() {
        return this.helperFactory.get();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.packets.putAll(this.packetsByClass);
        builder.protocolVersion = this.protocolVersion;
        builder.raknetProtocolVersion = this.raknetProtocolVersion;
        builder.minecraftVersion = this.minecraftVersion;
        builder.helperFactory = this.helperFactory;
        return builder;
    }

    /* loaded from: classes5.dex */
    public static class Builder {
        private Supplier<BedrockCodecHelper> helperFactory;
        private String minecraftVersion;
        private final Map<Class<? extends BedrockPacket>, BedrockPacketDefinition<? extends BedrockPacket>> packets;
        private int protocolVersion;
        private int raknetProtocolVersion;

        /* JADX WARN: Multi-variable type inference failed */
        public <T extends BedrockPacket> Builder registerPacket(Supplier<T> factory, BedrockPacketSerializer<T> serializer, int id) {
            Class<?> cls = factory.get().getClass();
            Preconditions.checkArgument(id >= 0, "id cannot be negative");
            Preconditions.checkArgument(true ^ this.packets.containsKey(cls), "Packet class already registered");
            BedrockPacketDefinition<T> info = new BedrockPacketDefinition<>(id, factory, serializer);
            this.packets.put(cls, info);
            return this;
        }

        public <T extends BedrockPacket> Builder updateSerializer(Class<T> packetClass, BedrockPacketSerializer<T> serializer) {
            BedrockPacketDefinition<? extends BedrockPacket> bedrockPacketDefinition = this.packets.get(packetClass);
            Preconditions.checkArgument(bedrockPacketDefinition != null, "Packet does not exist");
            this.packets.replace(packetClass, bedrockPacketDefinition, new BedrockPacketDefinition<>(bedrockPacketDefinition.getId(), bedrockPacketDefinition.getFactory(), serializer));
            return this;
        }

        public Builder retainPackets(Class<? extends BedrockPacket>... packets) {
            this.packets.keySet().retainAll(Arrays.asList(packets));
            return this;
        }

        public Builder deregisterPacket(Class<? extends BedrockPacket> packetClass) {
            Preconditions.checkNotNull(packetClass, "packetClass");
            this.packets.remove(packetClass);
            return this;
        }

        public Builder protocolVersion(int protocolVersion) {
            Preconditions.checkArgument(protocolVersion >= 0, "protocolVersion cannot be negative");
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder raknetProtocolVersion(int version) {
            Preconditions.checkArgument(version >= 0, "raknetProtocolVersion cannot be negative");
            this.raknetProtocolVersion = version;
            return this;
        }

        public Builder minecraftVersion(String minecraftVersion) {
            Preconditions.checkNotNull(minecraftVersion, "minecraftVersion");
            Preconditions.checkArgument(!minecraftVersion.isEmpty() && minecraftVersion.split("\\.").length > 2, "Invalid minecraftVersion");
            this.minecraftVersion = minecraftVersion;
            return this;
        }

        public Builder helper(Supplier<BedrockCodecHelper> helperFactory) {
            Preconditions.checkNotNull(helperFactory, "helperFactory");
            this.helperFactory = helperFactory;
            return this;
        }

        public BedrockCodec build() {
            Preconditions.checkArgument(this.protocolVersion >= 0, "No protocol version defined");
            Preconditions.checkNotNull(this.minecraftVersion, "No Minecraft version defined");
            Preconditions.checkNotNull(this.helperFactory, "helperFactory cannot be null");
            int largestId = -1;
            for (BedrockPacketDefinition<? extends BedrockPacket> info : this.packets.values()) {
                if (info.getId() > largestId) {
                    largestId = info.getId();
                }
            }
            Preconditions.checkArgument(largestId > -1, "Must have at least one packet registered");
            BedrockPacketDefinition<? extends BedrockPacket>[] packetsById = new BedrockPacketDefinition[largestId + 1];
            for (BedrockPacketDefinition<? extends BedrockPacket> info2 : this.packets.values()) {
                packetsById[info2.getId()] = info2;
            }
            return new BedrockCodec(this.protocolVersion, this.minecraftVersion, packetsById, this.packets, this.helperFactory, this.raknetProtocolVersion);
        }

        private Builder() {
            this.packets = new IdentityHashMap();
            this.protocolVersion = -1;
            this.raknetProtocolVersion = 10;
            this.minecraftVersion = null;
        }
    }

    private BedrockCodec(int protocolVersion, String minecraftVersion, BedrockPacketDefinition<? extends BedrockPacket>[] packetsById, Map<Class<? extends BedrockPacket>, BedrockPacketDefinition<? extends BedrockPacket>> packetsByClass, Supplier<BedrockCodecHelper> helperFactory, int raknetProtocolVersion) {
        this.protocolVersion = protocolVersion;
        this.minecraftVersion = minecraftVersion;
        this.packetsById = packetsById;
        this.packetsByClass = packetsByClass;
        this.helperFactory = helperFactory;
        this.raknetProtocolVersion = raknetProtocolVersion;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    public int getRaknetProtocolVersion() {
        return this.raknetProtocolVersion;
    }
}
