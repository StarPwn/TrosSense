package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.packet.EducationSettingsPacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class EducationSettingsSerializer_v465 extends EducationSettingsSerializer_v407 {
    public static final EducationSettingsSerializer_v465 INSTANCE = new EducationSettingsSerializer_v465();

    protected EducationSettingsSerializer_v465() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, EducationSettingsPacket packet) {
        helper.writeString(buffer, packet.getCodeBuilderUri());
        helper.writeString(buffer, packet.getCodeBuilderTitle());
        buffer.writeBoolean(packet.isCanResizeCodeBuilder());
        buffer.writeBoolean(packet.isDisableLegacyTitle());
        helper.writeString(buffer, packet.getPostProcessFilter());
        helper.writeString(buffer, packet.getScreenshotBorderPath());
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), packet.getEntityCapabilities(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
        helper.writeOptional(buffer, new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isPresent;
                isPresent = ((Optional) obj).isPresent();
                return isPresent;
            }
        }, packet.getOverrideUri(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeString((ByteBuf) obj, (String) ((Optional) obj2).get());
            }
        });
        buffer.writeBoolean(packet.isQuizAttached());
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), packet.getExternalLinkSettings(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.EducationSettingsSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(final ByteBuf buffer, final BedrockCodecHelper helper, EducationSettingsPacket packet) {
        packet.setCodeBuilderUri(helper.readString(buffer));
        packet.setCodeBuilderTitle(helper.readString(buffer));
        packet.setCanResizeCodeBuilder(buffer.readBoolean());
        packet.setDisableLegacyTitle(buffer.readBoolean());
        packet.setPostProcessFilter(helper.readString(buffer));
        packet.setScreenshotBorderPath(helper.readString(buffer));
        packet.setEntityCapabilities((OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(ByteBuf.this.readBoolean());
                return of;
            }
        }));
        packet.setOverrideUri((Optional) helper.readOptional(buffer, Optional.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Optional of;
                of = Optional.of(BedrockCodecHelper.this.readString((ByteBuf) obj));
                return of;
            }
        }));
        packet.setQuizAttached(buffer.readBoolean());
        packet.setExternalLinkSettings((OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.EducationSettingsSerializer_v465$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(ByteBuf.this.readBoolean());
                return of;
            }
        }));
    }
}
