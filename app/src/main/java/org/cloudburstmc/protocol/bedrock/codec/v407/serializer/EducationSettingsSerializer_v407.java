package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.EducationSettingsPacket;

/* loaded from: classes5.dex */
public class EducationSettingsSerializer_v407 implements BedrockPacketSerializer<EducationSettingsPacket> {
    public static final EducationSettingsSerializer_v407 INSTANCE = new EducationSettingsSerializer_v407();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, EducationSettingsPacket packet) {
        helper.writeString(buffer, packet.getCodeBuilderUri());
        helper.writeString(buffer, packet.getCodeBuilderTitle());
        buffer.writeBoolean(packet.isCanResizeCodeBuilder());
        helper.writeOptional(buffer, new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isPresent;
                isPresent = ((Optional) obj).isPresent();
                return isPresent;
            }
        }, packet.getOverrideUri(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeString((ByteBuf) obj, (String) ((Optional) obj2).get());
            }
        });
        buffer.writeBoolean(packet.isQuizAttached());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, EducationSettingsPacket packet) {
        packet.setCodeBuilderUri(helper.readString(buffer));
        packet.setCodeBuilderTitle(helper.readString(buffer));
        packet.setCanResizeCodeBuilder(buffer.readBoolean());
        packet.setOverrideUri((Optional) helper.readOptional(buffer, Optional.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Optional of;
                of = Optional.of(BedrockCodecHelper.this.readString((ByteBuf) obj));
                return of;
            }
        }));
        packet.setQuizAttached(buffer.readBoolean());
    }
}
