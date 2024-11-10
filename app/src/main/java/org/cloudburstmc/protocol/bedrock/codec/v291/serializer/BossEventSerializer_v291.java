package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.BossEventPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BossEventSerializer_v291 implements BedrockPacketSerializer<BossEventPacket> {
    public static final BossEventSerializer_v291 INSTANCE = new BossEventSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, BossEventPacket packet) {
        VarInts.writeLong(buffer, packet.getBossUniqueEntityId());
        VarInts.writeUnsignedInt(buffer, packet.getAction().ordinal());
        serializeAction(buffer, helper, packet);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, BossEventPacket packet) {
        packet.setBossUniqueEntityId(VarInts.readLong(buffer));
        packet.setAction(BossEventPacket.Action.values()[VarInts.readUnsignedInt(buffer)]);
        deserializeAction(buffer, helper, packet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000c. Please report as an issue. */
    public void serializeAction(ByteBuf buffer, BedrockCodecHelper helper, BossEventPacket packet) {
        switch (packet.getAction()) {
            case REGISTER_PLAYER:
            case UNREGISTER_PLAYER:
                VarInts.writeLong(buffer, packet.getPlayerUniqueEntityId());
                return;
            case CREATE:
                helper.writeString(buffer, packet.getTitle());
                buffer.writeFloatLE(packet.getHealthPercentage());
            case UPDATE_PROPERTIES:
                buffer.writeShortLE(packet.getDarkenSky());
            case UPDATE_STYLE:
                VarInts.writeUnsignedInt(buffer, packet.getColor());
                VarInts.writeUnsignedInt(buffer, packet.getOverlay());
                return;
            case UPDATE_PERCENTAGE:
                buffer.writeFloatLE(packet.getHealthPercentage());
                return;
            case UPDATE_NAME:
                helper.writeString(buffer, packet.getTitle());
                return;
            case REMOVE:
                return;
            default:
                throw new RuntimeException("BossEvent transactionType was unknown!");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000c. Please report as an issue. */
    public void deserializeAction(ByteBuf buffer, BedrockCodecHelper helper, BossEventPacket packet) {
        switch (packet.getAction()) {
            case REGISTER_PLAYER:
            case UNREGISTER_PLAYER:
                packet.setPlayerUniqueEntityId(VarInts.readLong(buffer));
                return;
            case CREATE:
                packet.setTitle(helper.readString(buffer));
                packet.setHealthPercentage(buffer.readFloatLE());
            case UPDATE_PROPERTIES:
                packet.setDarkenSky(buffer.readUnsignedShortLE());
            case UPDATE_STYLE:
                packet.setColor(VarInts.readUnsignedInt(buffer));
                packet.setOverlay(VarInts.readUnsignedInt(buffer));
                return;
            case UPDATE_PERCENTAGE:
                packet.setHealthPercentage(buffer.readFloatLE());
                return;
            case UPDATE_NAME:
                packet.setTitle(helper.readString(buffer));
                return;
            default:
                return;
        }
    }
}
