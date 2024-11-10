package org.cloudburstmc.protocol.bedrock.codec.v503.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.SpawnParticleEffectSerializer_v332;
import org.cloudburstmc.protocol.bedrock.packet.SpawnParticleEffectPacket;

/* loaded from: classes5.dex */
public class SpawnParticleEffectSerializer_v503 extends SpawnParticleEffectSerializer_v332 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.SpawnParticleEffectSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, SpawnParticleEffectPacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeOptional(buffer, new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.serializer.SpawnParticleEffectSerializer_v503$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isPresent;
                isPresent = ((Optional) obj).isPresent();
                return isPresent;
            }
        }, packet.getMolangVariablesJson(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.serializer.SpawnParticleEffectSerializer_v503$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeString((ByteBuf) obj, (String) ((Optional) obj2).get());
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.SpawnParticleEffectSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, SpawnParticleEffectPacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setMolangVariablesJson((Optional) helper.readOptional(buffer, Optional.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v503.serializer.SpawnParticleEffectSerializer_v503$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Optional of;
                of = Optional.of(BedrockCodecHelper.this.readString((ByteBuf) obj));
                return of;
            }
        }));
    }
}
