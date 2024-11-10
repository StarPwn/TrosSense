package org.cloudburstmc.protocol.bedrock.codec.compat;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.compat.serializer.DisconnectSerializerCompat;
import org.cloudburstmc.protocol.bedrock.codec.compat.serializer.LoginSerializerCompat;
import org.cloudburstmc.protocol.bedrock.codec.compat.serializer.PlayStatusSerializerCompat;
import org.cloudburstmc.protocol.bedrock.codec.compat.serializer.RequestNetworkSettingsSerializerCompat;
import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;

/* loaded from: classes5.dex */
public class BedrockCompat {
    public static BedrockCodec CODEC = BedrockCodec.builder().helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            BedrockCodecHelper bedrockCodecHelper;
            bedrockCodecHelper = NoopBedrockCodecHelper.INSTANCE;
            return bedrockCodecHelper;
        }
    }).registerPacket(new BedrockCompat$$ExternalSyntheticLambda1(), LoginSerializerCompat.INSTANCE, 1).registerPacket(new BedrockCompat$$ExternalSyntheticLambda2(), PlayStatusSerializerCompat.INSTANCE, 2).registerPacket(new BedrockCompat$$ExternalSyntheticLambda3(), new DisconnectSerializerCompat(true), 5).registerPacket(new BedrockCompat$$ExternalSyntheticLambda4(), RequestNetworkSettingsSerializerCompat.INSTANCE, bl.cr).protocolVersion(0).minecraftVersion("0.0.0").build();
    public static BedrockCodec CODEC_LEGACY = CODEC.toBuilder().updateSerializer(DisconnectPacket.class, new DisconnectSerializerCompat(false)).build();
}
