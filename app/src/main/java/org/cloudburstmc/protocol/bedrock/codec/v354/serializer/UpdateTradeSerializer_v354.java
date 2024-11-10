package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.packet.UpdateTradePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateTradeSerializer_v354 implements BedrockPacketSerializer<UpdateTradePacket> {
    public static final UpdateTradeSerializer_v354 INSTANCE = new UpdateTradeSerializer_v354();

    protected UpdateTradeSerializer_v354() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateTradePacket packet) {
        buffer.writeByte(packet.getContainerId());
        buffer.writeByte(packet.getContainerType().getId());
        VarInts.writeInt(buffer, packet.getSize());
        VarInts.writeInt(buffer, packet.getTradeTier());
        VarInts.writeLong(buffer, packet.getTraderUniqueEntityId());
        VarInts.writeLong(buffer, packet.getPlayerUniqueEntityId());
        helper.writeString(buffer, packet.getDisplayName());
        buffer.writeBoolean(packet.isNewTradingUi());
        buffer.writeBoolean(packet.isUsingEconomyTrade());
        helper.writeTag(buffer, packet.getOffers());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateTradePacket packet) {
        packet.setContainerId(buffer.readByte());
        packet.setContainerType(ContainerType.from(buffer.readByte()));
        packet.setSize(VarInts.readInt(buffer));
        packet.setTradeTier(VarInts.readInt(buffer));
        packet.setTraderUniqueEntityId(VarInts.readLong(buffer));
        packet.setPlayerUniqueEntityId(VarInts.readLong(buffer));
        packet.setDisplayName(helper.readString(buffer));
        packet.setNewTradingUi(buffer.readBoolean());
        packet.setUsingEconomyTrade(buffer.readBoolean());
        packet.setOffers((NbtMap) helper.readTag(buffer, NbtMap.class));
    }
}
