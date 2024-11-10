package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MapInfoRequestSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.map.MapPixel;
import org.cloudburstmc.protocol.bedrock.packet.MapInfoRequestPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class MapInfoRequestSerializer_v544 extends MapInfoRequestSerializer_v291 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MapInfoRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeArray(buffer, packet.getPixels(), new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.MapInfoRequestSerializer_v544$$ExternalSyntheticLambda0
            @Override // java.util.function.ObjIntConsumer
            public final void accept(Object obj, int i) {
                ((ByteBuf) obj).writeIntLE(i);
            }
        }, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.MapInfoRequestSerializer_v544$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                MapInfoRequestSerializer_v544.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (MapPixel) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buf, BedrockCodecHelper aHelper, MapPixel pixel) {
        buf.writeIntLE(pixel.getPixel());
        buf.writeShortLE(pixel.getIndex());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MapInfoRequestSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MapInfoRequestPacket packet) {
        super.deserialize(buffer, helper, packet);
        helper.readArray(buffer, packet.getPixels(), new ToLongFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.MapInfoRequestSerializer_v544$$ExternalSyntheticLambda2
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                long readUnsignedIntLE;
                readUnsignedIntLE = ((ByteBuf) obj).readUnsignedIntLE();
                return readUnsignedIntLE;
            }
        }, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.MapInfoRequestSerializer_v544$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return MapInfoRequestSerializer_v544.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ MapPixel lambda$deserialize$1(ByteBuf buf, BedrockCodecHelper aHelper) {
        int pixel = buf.readIntLE();
        int index = buf.readUnsignedShortLE();
        return new MapPixel(pixel, index);
    }
}
