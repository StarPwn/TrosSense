package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.ExplodePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ExplodeSerializer_v291 implements BedrockPacketSerializer<ExplodePacket> {
    public static final ExplodeSerializer_v291 INSTANCE = new ExplodeSerializer_v291();

    protected ExplodeSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, ExplodePacket packet) {
        helper.writeVector3f(buffer, packet.getPosition());
        VarInts.writeInt(buffer, (int) (packet.getRadius() * 32.0f));
        List<Vector3i> records = packet.getRecords();
        helper.getClass();
        helper.writeArray(buffer, records, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ExplodeSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeVector3i((ByteBuf) obj, (Vector3i) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, ExplodePacket packet) {
        packet.setPosition(helper.readVector3f(buffer));
        packet.setRadius(VarInts.readInt(buffer) / 32.0f);
        List<Vector3i> records = packet.getRecords();
        helper.getClass();
        helper.readArray(buffer, records, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ExplodeSerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readVector3i((ByteBuf) obj);
            }
        });
    }
}
