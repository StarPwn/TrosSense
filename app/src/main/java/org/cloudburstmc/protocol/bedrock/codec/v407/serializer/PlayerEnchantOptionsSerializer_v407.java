package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.EnchantData;
import org.cloudburstmc.protocol.bedrock.data.inventory.EnchantOptionData;
import org.cloudburstmc.protocol.bedrock.packet.PlayerEnchantOptionsPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PlayerEnchantOptionsSerializer_v407 implements BedrockPacketSerializer<PlayerEnchantOptionsPacket> {
    public static final PlayerEnchantOptionsSerializer_v407 INSTANCE = new PlayerEnchantOptionsSerializer_v407();

    protected PlayerEnchantOptionsSerializer_v407() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerEnchantOptionsPacket packet) {
        helper.writeArray(buffer, packet.getOptions(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                PlayerEnchantOptionsSerializer_v407.this.writeOption((ByteBuf) obj, (BedrockCodecHelper) obj2, (EnchantOptionData) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerEnchantOptionsPacket packet) {
        helper.readArray(buffer, packet.getOptions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return PlayerEnchantOptionsSerializer_v407.this.readOption((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeOption(ByteBuf buffer, BedrockCodecHelper helper, EnchantOptionData option) {
        VarInts.writeUnsignedInt(buffer, option.getCost());
        buffer.writeIntLE(option.getPrimarySlot());
        helper.writeArray(buffer, option.getEnchants0(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PlayerEnchantOptionsSerializer_v407.this.serializeEnchant((ByteBuf) obj, (EnchantData) obj2);
            }
        });
        helper.writeArray(buffer, option.getEnchants1(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PlayerEnchantOptionsSerializer_v407.this.serializeEnchant((ByteBuf) obj, (EnchantData) obj2);
            }
        });
        helper.writeArray(buffer, option.getEnchants2(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PlayerEnchantOptionsSerializer_v407.this.serializeEnchant((ByteBuf) obj, (EnchantData) obj2);
            }
        });
        helper.writeString(buffer, option.getEnchantName());
        VarInts.writeUnsignedInt(buffer, option.getEnchantNetId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EnchantOptionData readOption(ByteBuf buffer, BedrockCodecHelper helper) {
        int cost = VarInts.readUnsignedInt(buffer);
        int primarySlot = buffer.readIntLE();
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PlayerEnchantOptionsSerializer_v407.this.deserializeEnchant((ByteBuf) obj);
            }
        });
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList2, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PlayerEnchantOptionsSerializer_v407.this.deserializeEnchant((ByteBuf) obj);
            }
        });
        ObjectArrayList objectArrayList3 = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList3, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.PlayerEnchantOptionsSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PlayerEnchantOptionsSerializer_v407.this.deserializeEnchant((ByteBuf) obj);
            }
        });
        String enchantName = helper.readString(buffer);
        int enchantNetId = VarInts.readUnsignedInt(buffer);
        return new EnchantOptionData(cost, primarySlot, objectArrayList, objectArrayList2, objectArrayList3, enchantName, enchantNetId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void serializeEnchant(ByteBuf buffer, EnchantData enchant) {
        buffer.writeByte(enchant.getType());
        buffer.writeByte(enchant.getLevel());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EnchantData deserializeEnchant(ByteBuf buffer) {
        int type = buffer.readUnsignedByte();
        int level = buffer.readUnsignedByte();
        return new EnchantData(type, level);
    }
}
