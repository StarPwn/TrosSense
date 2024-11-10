package org.cloudburstmc.protocol.bedrock.codec.v618.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;

/* loaded from: classes5.dex */
public class ResourcePacksInfoSerializer_v618 extends ResourcePacksInfoSerializer_v448 {
    public static final ResourcePacksInfoSerializer_v618 INSTANCE = new ResourcePacksInfoSerializer_v618();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448, org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ResourcePacksInfoSerializer_v422, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        super.serialize(buffer, helper, packet);
        writeCDNEntries(buffer, packet, helper);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448, org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ResourcePacksInfoSerializer_v422, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        super.deserialize(buffer, helper, packet);
        readCDNEntries(buffer, packet, helper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCDNEntries(ByteBuf buffer, ResourcePacksInfoPacket packet, final BedrockCodecHelper helper) {
        helper.writeArray(buffer, packet.getCDNEntries(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.ResourcePacksInfoSerializer_v618$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ResourcePacksInfoSerializer_v618.lambda$writeCDNEntries$0(BedrockCodecHelper.this, (ByteBuf) obj, (ResourcePacksInfoPacket.CDNEntry) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeCDNEntries$0(BedrockCodecHelper helper, ByteBuf buf, ResourcePacksInfoPacket.CDNEntry entry) {
        helper.writeString(buf, entry.getPackId());
        helper.writeString(buf, entry.getRemoteUrl());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readCDNEntries(ByteBuf buffer, ResourcePacksInfoPacket packet, final BedrockCodecHelper helper) {
        helper.readArray(buffer, packet.getCDNEntries(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.ResourcePacksInfoSerializer_v618$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ResourcePacksInfoSerializer_v618.lambda$readCDNEntries$1(BedrockCodecHelper.this, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ResourcePacksInfoPacket.CDNEntry lambda$readCDNEntries$1(BedrockCodecHelper helper, ByteBuf buf) {
        return new ResourcePacksInfoPacket.CDNEntry(helper.readString(buf), helper.readString(buf));
    }
}
