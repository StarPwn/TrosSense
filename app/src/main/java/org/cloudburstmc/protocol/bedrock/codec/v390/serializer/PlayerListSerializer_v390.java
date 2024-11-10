package org.cloudburstmc.protocol.bedrock.codec.v390.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PlayerListSerializer_v390 implements BedrockPacketSerializer<PlayerListPacket> {
    public static final PlayerListSerializer_v390 INSTANCE = new PlayerListSerializer_v390();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerListPacket packet) {
        buffer.writeByte(packet.getAction().ordinal());
        VarInts.writeUnsignedInt(buffer, packet.getEntries().size());
        if (packet.getAction() == PlayerListPacket.Action.ADD) {
            for (PlayerListPacket.Entry entry : packet.getEntries()) {
                writeEntryBase(buffer, helper, entry);
            }
            for (PlayerListPacket.Entry entry2 : packet.getEntries()) {
                buffer.writeBoolean(entry2.isTrustedSkin());
            }
            return;
        }
        for (PlayerListPacket.Entry entry3 : packet.getEntries()) {
            helper.writeUuid(buffer, entry3.getUuid());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerListPacket packet) {
        PlayerListPacket.Action action = PlayerListPacket.Action.values()[buffer.readUnsignedByte()];
        packet.setAction(action);
        int length = VarInts.readUnsignedInt(buffer);
        if (action == PlayerListPacket.Action.ADD) {
            for (int i = 0; i < length; i++) {
                packet.getEntries().add(readEntryBase(buffer, helper));
            }
            for (int i2 = 0; i2 < length && buffer.isReadable(); i2++) {
                packet.getEntries().get(i2).setTrustedSkin(buffer.readBoolean());
            }
            return;
        }
        for (int i3 = 0; i3 < length; i3++) {
            packet.getEntries().add(new PlayerListPacket.Entry(helper.readUuid(buffer)));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEntryBase(ByteBuf buffer, BedrockCodecHelper helper, PlayerListPacket.Entry entry) {
        helper.writeUuid(buffer, entry.getUuid());
        VarInts.writeLong(buffer, entry.getEntityId());
        helper.writeString(buffer, entry.getName());
        helper.writeString(buffer, entry.getXuid());
        helper.writeString(buffer, entry.getPlatformChatId());
        buffer.writeIntLE(entry.getBuildPlatform());
        helper.writeSkin(buffer, entry.getSkin());
        buffer.writeBoolean(entry.isTeacher());
        buffer.writeBoolean(entry.isHost());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PlayerListPacket.Entry readEntryBase(ByteBuf buffer, BedrockCodecHelper helper) {
        PlayerListPacket.Entry entry = new PlayerListPacket.Entry(helper.readUuid(buffer));
        entry.setEntityId(VarInts.readLong(buffer));
        entry.setName(helper.readString(buffer));
        entry.setXuid(helper.readString(buffer));
        entry.setPlatformChatId(helper.readString(buffer));
        entry.setBuildPlatform(buffer.readIntLE());
        entry.setSkin(helper.readSkin(buffer));
        entry.setTeacher(buffer.readBoolean());
        entry.setHost(buffer.readBoolean());
        return entry;
    }
}
