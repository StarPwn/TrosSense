package org.cloudburstmc.protocol.bedrock.codec.v534.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AddEntitySerializer_v534 extends AddEntitySerializer_v313 {
    public static final AddEntitySerializer_v534 INSTANCE = new AddEntitySerializer_v534();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddEntityPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeString(buffer, packet.getIdentifier());
        helper.writeVector3f(buffer, packet.getPosition());
        helper.writeVector3f(buffer, packet.getMotion());
        helper.writeVector2f(buffer, packet.getRotation());
        buffer.writeFloatLE(packet.getHeadRotation());
        buffer.writeFloatLE(packet.getBodyRotation());
        helper.writeArray(buffer, packet.getAttributes(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddEntitySerializer_v534$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                AddEntitySerializer_v534.this.writeAttribute((ByteBuf) obj, (BedrockCodecHelper) obj2, (AttributeData) obj3);
            }
        });
        helper.writeEntityData(buffer, packet.getMetadata());
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.writeArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda1(helper));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v313.serializer.AddEntitySerializer_v313, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddEntityPacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setIdentifier(helper.readString(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setMotion(helper.readVector3f(buffer));
        packet.setRotation(helper.readVector2f(buffer));
        packet.setHeadRotation(buffer.readFloatLE());
        packet.setBodyRotation(buffer.readFloatLE());
        helper.readArray(buffer, packet.getAttributes(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddEntitySerializer_v534$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AddEntitySerializer_v534.this.readAttribute((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readEntityData(buffer, packet.getMetadata());
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.readArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda3(helper));
    }
}
