package org.cloudburstmc.protocol.bedrock.codec.v649.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.HudElement;
import org.cloudburstmc.protocol.bedrock.data.HudVisibility;
import org.cloudburstmc.protocol.bedrock.packet.SetHudPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetHudSerializer_v649 implements BedrockPacketSerializer<SetHudPacket> {
    public static final SetHudSerializer_v649 INSTANCE = new SetHudSerializer_v649();
    private static final HudElement[] VALUES = HudElement.values();
    private static final HudVisibility[] VISIBILITIES = HudVisibility.values();

    protected SetHudSerializer_v649() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SetHudPacket packet) {
        helper.writeArray(buffer, packet.getElements(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v649.serializer.SetHudSerializer_v649$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                VarInts.writeUnsignedInt((ByteBuf) obj, ((HudElement) obj2).ordinal());
            }
        });
        buffer.writeByte(packet.getVisibility().ordinal());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HudElement lambda$deserialize$1(ByteBuf buf) {
        return VALUES[VarInts.readUnsignedInt(buf)];
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SetHudPacket packet) {
        helper.readArray(buffer, packet.getElements(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v649.serializer.SetHudSerializer_v649$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SetHudSerializer_v649.lambda$deserialize$1((ByteBuf) obj);
            }
        });
        packet.setVisibility(VISIBILITIES[buffer.readUnsignedByte()]);
    }
}
